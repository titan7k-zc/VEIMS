package voidbreaker.prolog;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AttendanceRecord {
    private final StringProperty subject;
    private final StringProperty[] days = new StringProperty[31];
    private final StringProperty totalPresent;

    public AttendanceRecord(String subject) {
        this.subject = new SimpleStringProperty(subject);
        this.totalPresent = new SimpleStringProperty("0");
        for (int i = 0; i < 31; i++) {
            this.days[i] = new SimpleStringProperty(""); // Default to empty
        }
    }

    public StringProperty subjectProperty() { return subject; }
    public StringProperty dayProperty(int dayIndex) { return days[dayIndex]; } // dayIndex 0-30
    public StringProperty totalPresentProperty() { return totalPresent; }

    public void markDay(int day) { // day 1-31
        if (day >= 1 && day <= 31) {
            this.days[day - 1].set("✅"); // display
            updateTotal();
        }
    }

    private void updateTotal() {
        int count = 0;
        for (StringProperty day : days) {
            if ("✅".equals(day.get())) { // TP counter
                count++;
            }
        }
        totalPresent.set(String.valueOf(count));
    }
}