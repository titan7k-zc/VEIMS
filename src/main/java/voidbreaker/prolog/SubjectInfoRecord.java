package voidbreaker.prolog;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class SubjectInfoRecord {
    private final SimpleStringProperty grade;
    private final SimpleStringProperty id;
    private final SimpleStringProperty subject;
    private final SimpleStringProperty teacher;
    private final SimpleDoubleProperty fee;

    public SubjectInfoRecord(String grade, String id, String subject, String teacher, double fee) {
        this.grade = new SimpleStringProperty(grade);
        this.id = new SimpleStringProperty(id);
        this.subject = new SimpleStringProperty(subject);
        this.teacher = new SimpleStringProperty(teacher);
        this.fee = new SimpleDoubleProperty(fee);
    }

    public SimpleStringProperty gradeProperty() {
        return grade;
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public SimpleStringProperty subjectProperty() {
        return subject;
    }

    public SimpleStringProperty teacherProperty() {
        return teacher;
    }

    public SimpleDoubleProperty feeProperty() {
        return fee;
    }
}