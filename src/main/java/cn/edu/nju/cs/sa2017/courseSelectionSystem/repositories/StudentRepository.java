package cn.eud.nju.cs.sa2017.courseSelectionSystem.repositories;

import cn.edu.nju.cs.sa2017.courseSelectionSystem.models.Student;
import org.springframework.data.repository.CrudRepository;

public interface StudentRepository
        extends CrudRepository<Student, String> {

    // do nothing here, all works are done by CrudRepository

}
