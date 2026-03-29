package voidbreaker.prolog;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

// Add this static class inside Home.java, perhaps after the other record classes like StudentRecord, RevenueRecord, etc.
public class UnpaidStudentRecord {
    private final StringProperty id = new SimpleStringProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty grade = new SimpleStringProperty();
    private final StringProperty lastPaymentDate = new SimpleStringProperty();
    private final IntegerProperty unpaidMonths = new SimpleIntegerProperty();
    private final StringProperty mobile = new SimpleStringProperty();
    private final StringProperty lastReminder = new SimpleStringProperty();
    private final StringProperty remark = new SimpleStringProperty();

    public UnpaidStudentRecord(String id, String name, String grade, String lastPaymentDate, int unpaidMonths, String mobile, String lastReminder, String remark) {
        this.id.set(id);
        this.name.set(name);
        this.grade.set(grade);
        this.lastPaymentDate.set(lastPaymentDate);
        this.unpaidMonths.set(unpaidMonths);
        this.mobile.set(mobile);
        this.lastReminder.set(lastReminder);
        this.remark.set(remark);
    }

    public String getId() { return id.get(); }
    public StringProperty idProperty() { return id; }
    public String getName() { return name.get(); }
    public StringProperty nameProperty() { return name; }
    public String getGrade() { return grade.get(); }
    public StringProperty gradeProperty() { return grade; }
    public String getLastPaymentDate() { return lastPaymentDate.get(); }
    public StringProperty lastPaymentDateProperty() { return lastPaymentDate; }
    public int getUnpaidMonths() { return unpaidMonths.get(); }
    public IntegerProperty unpaidMonthsProperty() { return unpaidMonths; }
    public String getMobile() { return mobile.get(); }
    public StringProperty mobileProperty() { return mobile; }
    public String getLastReminder() { return lastReminder.get(); }
    public StringProperty lastReminderProperty() { return lastReminder; }
    public String getRemark() { return remark.get(); }
    public StringProperty remarkProperty() { return remark; }
}