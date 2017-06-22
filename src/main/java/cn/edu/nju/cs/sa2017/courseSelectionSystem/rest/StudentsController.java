package cn.edu.nju.cs.sa2017.courseSelectionSystem.rest;

import cn.edu.nju.cs.sa2017.courseSelectionSystem.exceptions.BussinessException;
import cn.edu.nju.cs.sa2017.courseSelectionSystem.models.Student;
import cn.edu.nju.cs.sa2017.courseSelectionSystem.repositories.StudentRepository;
import cn.edu.nju.cs.sa2017.courseSelectionSystem.utils.U;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/students")
public class StudentsController {

    private Logger logger = LoggerFactory.getLogger(StudentsController.class);

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job importStudentsFromExcelJob;

    @Autowired
    StudentRepository studentRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Student>> getStudents() {
        Iterator<Student> iter = studentRepository.findAll().iterator();
        Map<String, Student> r = new HashMap<>();
        Student s;

        while (iter.hasNext()) {
            s = iter.next();
            r.put(s.getId(), s);
        }

        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Student> postStudent(@RequestBody @Valid Student student) throws BussinessException {
        if (studentRepository.exists(student.getId())) {
            throw new BussinessException(HttpStatus.CONFLICT);
        }

        studentRepository.save(student);

        return new ResponseEntity<>(student, HttpStatus.CREATED);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, params = { "upload" })
    public ResponseEntity<Map<String, Student>> uploadFile(
            @RequestParam("upload") String upload,
            @RequestParam("file") MultipartFile file) throws Exception {
        Map<String, Student> ret = new HashMap<>();

        if (file.isEmpty()) {
            return new ResponseEntity<>(ret, HttpStatus.CREATED);
        }

        // create a temp file
        String path = U.createTempFile(file);

        if (path == null) {
            return new ResponseEntity<>(ret, HttpStatus.CREATED);
        }

        JobParameters parameters = new JobParametersBuilder().addString("path-to-file", path).toJobParameters();
        JobExecution jobExecution = jobLauncher.run(importStudentsFromExcelJob, parameters);

        return new ResponseEntity<>(ret, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<Student> patchStudent(@PathVariable("id") String id, @RequestBody Map<String, Object> body)
            throws Exception {
        Student student = studentRepository.findOne(id);

        if (student == null) {
            throw new BussinessException(HttpStatus.NOT_FOUND);
        }

        Map<String, Object> so = U.pick(body, new HashSet<>(
                Arrays.asList("name", "prof", "commonGrade", "experimentGrade", "examGrade")));

        for (Map.Entry<String, Object> entry : so.entrySet()) {
            if (entry.getKey().equals("name")) {
                student.setName((String) entry.getValue());
            } else if (entry.getKey().equals("prof")) {
                student.setProf((String) entry.getValue());
            } else if (entry.getKey().equals("commonGrade")) {
                try {
                    student.setCommonGrade((double) entry.getValue());
                } catch (ClassCastException e) {
                    student.setCommonGrade((double)(int) entry.getValue());
                }
            } else if (entry.getKey().equals("experimentGrade")) {
                try {
                    student.setExperimentGrade((double) entry.getValue());
                } catch (ClassCastException e) {
                    student.setExperimentGrade((double)(int) entry.getValue());
                }
            } else if (entry.getKey().equals("examGrade")) {
                try {
                    student.setExamGrade((double) entry.getValue());
                } catch (ClassCastException e) {
                    student.setExamGrade((double)(int) entry.getValue());
                }
            }
        }

        student = studentRepository.save(student);

        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteStudent(@PathVariable("id") String id) {
        studentRepository.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
