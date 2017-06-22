package cn.eud.nju.cs.sa2017.courseSelectionSystem.batch;

import cn.edu.nju.cs.sa2017.courseSelectionSystem.models.Student;
import cn.edu.nju.cs.sa2017.courseSelectionSystem.repositories.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.excel.mapping.PassThroughRowMapper;
import org.springframework.batch.item.excel.poi.PoiItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import java.util.List;


@Configuration
@EnableBatchProcessing
public class ImportStudentsFromExcel {

    private Logger logger = LoggerFactory.getLogger(ImportStudentsFromExcel.class);

    private static final int LINES_TO_SKIP = 4;

    private static final int CHUNK_SIZE = 16;

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Bean
    @StepScope
    public PoiItemReader<String[]> importStudentsFromExcelReader(
            @Value(value = "#{jobParameters['path-to-file']}") String path) throws Exception {
        PoiItemReader<String[]> reader = new PoiItemReader<String[]>();
        reader.setLinesToSkip(LINES_TO_SKIP);
        reader.setResource(new UrlResource("file://" + path));
        reader.setRowMapper(new PassThroughRowMapper());
        return reader;
    }

    @Bean
    public ItemProcessor<String[], Student> importStudentsFromExcelProcessor() {
        return new ImportStudentsFromExcelItemProcessor();
    }

    @Bean
    public ItemWriter<Student> importStudentsFromExcelWriter() {
        return new ImportStudentsFromExcelItemWriter();
    }

    @Bean
    public Step importStudentsFromExcelStep(ItemReader<String[]> importStudentsFromExcelReader) throws Exception {
        return stepBuilderFactory.get("importStudentsFromExcelStep")
                .<String[], Student> chunk(CHUNK_SIZE)
                .reader(importStudentsFromExcelReader)
                .processor(importStudentsFromExcelProcessor())
                .writer(importStudentsFromExcelWriter())
                .build();
    }

    @Bean
    public Job importStudentsFromExcelJob(ImportStudentsFromExcelJobExecutionListener listener,
                                          Step importStudentsFromExcelStep) throws Exception {
        return jobBuilderFactory.get("importStudentsFromExcelJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(importStudentsFromExcelStep)
                .end()
                .build();
    }

    public static class ImportStudentsFromExcelItemProcessor implements ItemProcessor<String[], Student> {

        private Logger logger = LoggerFactory.getLogger(ImportStudentsFromExcelItemProcessor.class);

        @Override
        public Student process(String[] tokens) throws Exception {
            Student student = new Student();

            // PoiItemReader will transform id to scientific-based number: 1.41221102E8, we transform it
            student.setId(transformId(tokens[0]));
            student.setName(transformName(tokens[1]));
            student.setProf(transformProf(tokens[3]));

            return student;
        }

        private String transformId(String originId) {
            String[] tokens = originId.split("E");
            String id = tokens[0].replace(".", "");
            if (id.length() < 9) {
                for (int i = id.length(); i < 9; i ++) {
                    id = id + "0";
                }
            }
            return id;
        }

        private String transformName(String originName) {
            return originName;
        }

        private String transformProf(String originProf) {
            return originProf;
        }

    }

    public static class ImportStudentsFromExcelItemWriter implements ItemWriter<Student> {

        @Autowired
        StudentRepository studentRepository;

        @Override
        public void write(List<? extends Student> list) throws Exception {
            // save into repository
            studentRepository.save(list);
        }

    }

    @Component
    public static class ImportStudentsFromExcelJobExecutionListener implements JobExecutionListener {

        private Logger logger = LoggerFactory.getLogger(ImportStudentsFromExcelJobExecutionListener.class);

        @Override
        public void beforeJob(JobExecution jobExecution) { logger.info(">>>>>> before"); }

        @Override
        public void afterJob(JobExecution jobExecution) {
            logger.info(">>>>>> after");
        }

    }

}
