package voidbreaker.prolog;

import javafx.beans.property.SimpleStringProperty;

public class SubjectRegistrationRecord {
    private final SimpleStringProperty subjectId;
    private final SimpleStringProperty subject;
    private final SimpleStringProperty registrationDate;

    public SubjectRegistrationRecord(String subjectId, String subject, String registrationDate) {
        this.subjectId = new SimpleStringProperty(subjectId);
        this.subject = new SimpleStringProperty(subject);
        this.registrationDate = new SimpleStringProperty(registrationDate);
    }

    public String getSubjectId() {
        return subjectId.get();
    }

    public SimpleStringProperty subjectIdProperty() {
        return subjectId;
    }

    public String getSubject() {
        return subject.get();
    }

    public SimpleStringProperty subjectProperty() {
        return subject;
    }

    public String getRegistrationDate() {
        return registrationDate.get();
    }

    public SimpleStringProperty registrationDateProperty() {
        return registrationDate;
    }
}
