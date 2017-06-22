package cn.edu.nju.cs.sa2017.courseSelectionSystem.utils;

import cn.edu.nju.cs.sa2017.courseSelectionSystem.models.Student;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public final class Map {

    public static Map<String, Object> pick(Map<String, Object> o, Set<String> whiteKey) {
        Map<String, Object> r = new HashMap<>();
        Set<String> keys = o.keySet();

        // for accelerate
        whiteKey = new HashSet<>(whiteKey);

        for (String k : keys) {
            if (whiteKey.contains(k)) {
                r.put(k, o.get(k));
            }
        }

        return r;
    }

    public static Map<String, Object> hide(Map<String, Object> o, Set<String> blackKey) {
        Map<String, Object> r = new HashMap<>();
        Set<String> keys = o.keySet();

        // for accelerate
        blackKey = new HashSet<>(blackKey);

        for (String k : keys) {
            if (!blackKey.contains(k)) {
                r.put(k, o.get(k));
            }
        }

        return r;
    }

    public static String createTempFile(MultipartFile file) {
        try {
            File f = File.createTempFile(getUniqueSignature(file.getName()), ".xlsx");
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(file.getBytes());
            fos.close();
            return f.getAbsolutePath();
        } catch (Exception e) {
            return null;
        }
    }

    public static String getUniqueSignature(String t) {
        return t + '-' + UUID.randomUUID().toString() + '-' + new Date().getTime();
    }

}
