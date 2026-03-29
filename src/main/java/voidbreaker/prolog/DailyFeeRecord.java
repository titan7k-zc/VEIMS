package voidbreaker.prolog;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DailyFeeRecord {
    private final StringProperty subject = new SimpleStringProperty();
    private final ObservableList<StringProperty> days = FXCollections.observableArrayList();
    private final StringProperty totalPaid = new SimpleStringProperty("0");

    public DailyFeeRecord(String subject) {
        this.subject.set(subject);
        for (int i = 0; i < 31; i++) {
            days.add(new SimpleStringProperty(""));
        }
    }

    public void markDay(int day) {
        if (day >= 1 && day <= 31) {
            days.get(day - 1).set("✅");
        }
        updateTotal();
    }

    private void updateTotal() {
        long count = days.stream().filter(p -> !p.get().isEmpty()).count();
        totalPaid.set(String.valueOf(count));
    }

    public StringProperty subjectProperty() {
        return subject;
    }

    public StringProperty dayProperty(int index) {
        return days.get(index);
    }

    public StringProperty totalPaidProperty() {
        return totalPaid;
    }
}