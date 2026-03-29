package voidbreaker.prolog;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FeeRecord {
    private final StringProperty subject;
    private final StringProperty[] months = new StringProperty[12];

    public FeeRecord(String subject) {
        this.subject = new SimpleStringProperty(subject);
        for (int i = 0; i < 12; i++) {
            this.months[i] = new SimpleStringProperty(""); // Default to empty
        }
    }

    public String getSubject() { return subject.get(); }
    public StringProperty subjectProperty() { return subject; }

    public StringProperty monthProperty(int monthIndex) { // monthIndex 0-11
        return months[monthIndex];
    }

    public void setMonthPayment(int month, String paymentDate) { // month 1-12
        if (month >= 1 && month <= 12) {
            this.months[month - 1].set(paymentDate);
        }
    }
}