package voidbreaker.prolog;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class GradeSubjectRecord {
    private final SimpleStringProperty grade;
    private final SimpleStringProperty subjectId;
    private final SimpleStringProperty subjectName;
    private final SimpleIntegerProperty studentCount;

    public GradeSubjectRecord(String grade, String subjectId, String subjectName, int studentCount) {
        this.grade = new SimpleStringProperty(grade);
        this.subjectId = new SimpleStringProperty(subjectId);
        this.subjectName = new SimpleStringProperty(subjectName);
        this.studentCount = new SimpleIntegerProperty(studentCount);
    }

    public SimpleStringProperty gradeProperty() {
        return grade;
    }

    public SimpleStringProperty subjectIdProperty() {
        return subjectId;
    }

    public SimpleStringProperty subjectNameProperty() {
        return subjectName;
    }

    public SimpleIntegerProperty studentCountProperty() {
        return studentCount;
    }
}