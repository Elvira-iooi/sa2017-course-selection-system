package cn.eud.nju.cs.sa2017.courseSelectionSystem.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "students")
@JsonInclude(JsonInclude.Include.ALWAYS)
public class Student {

    @Id
    @NotNull
    private String id;

    @Column(name = "studentname")
    private String name = "";

    @Column(name = "department")
    private String department = "";

    @Column(name = "grade")
    private int grade = 89;

    @Column(name = "usual_grade")
    private int usualGrade = 60;

    @Column(name = "design_grade")
    private int designGrade = 70;

    @Column(name = "exam_grade")
    private int examGrade = 90;

    public String getId() {
        return id;
    }

    public void setId(@NotNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(@NotNull String department) {
        this.department = department;
    }

    public void getGrade() { return grade; }

    public void setGrade(int grade) { this.grade = grade; }

    public double getUsualGrade() {
        return usualGrade;
    }

    public void setUsualGrade(int usualGrade) {
        this.usualGrade = usualGrade;
    }

    public double getExamGrade() {
        return examGrade;
    }

    public void setExamGrade(int examGrade) {
        this.examGrade = examGrade;
    }

    public double getDesignGrade() {
        return designGrade;
    }

    public void setDesignGrade(int designGrade) {
        this.designGrade = designGrade;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Student)) {
            return false;
        }

        Student s = (Student) obj;

        return s.getId().equals(this.id) &&
                s.getName().equals(this.name) &&
                s.getDepartment().equals(this.department);
    }

    @Override
    public String toString() {
        return "{ id: " + this.id +
                ", name: " + this.name +
                ", department: " + this.department +
                ", grade: " + this.grade +
                ", usualGrade: " + this.usualGrade +
                ", designGrade: " + this.desognGrade +
                ", examGrade: " + this.examGrade +
                " }";
    }
}
