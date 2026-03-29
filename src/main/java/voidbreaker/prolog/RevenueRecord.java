package voidbreaker.prolog;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RevenueRecord {
    private final String grade;
    private final String subject;
    private final String teacher;
    private final double yearIncome;
    private final double monthIncome;
    private final double dayIncome;

    public RevenueRecord(String grade, String subject, String teacher, double yearIncome, double monthIncome, double dayIncome) {
        this.grade = grade;
        this.subject = subject;
        this.teacher = teacher;
        this.yearIncome = yearIncome;
        this.monthIncome = monthIncome;
        this.dayIncome = dayIncome;
    }

    // Getters
    public String getGrade() { return grade; }
    public String getSubject() { return subject; }
    public String getTeacher() { return teacher; }
    public double getYearIncome() { return yearIncome; }
    public double getMonthIncome() { return monthIncome; }
    public double getDayIncome() { return dayIncome; }

    // Property getters for JavaFX
    public StringProperty gradeProperty() { return new SimpleStringProperty(grade); }
    public StringProperty subjectProperty() { return new SimpleStringProperty(subject); }
    public StringProperty teacherProperty() { return new SimpleStringProperty(teacher); }
    public StringProperty yearIncomeProperty() { return new SimpleStringProperty(String.format("%.2f", yearIncome)); }
    public StringProperty monthIncomeProperty() { return new SimpleStringProperty(String.format("%.2f", monthIncome)); }
    public StringProperty dayIncomeProperty() { return new SimpleStringProperty(String.format("%.2f", dayIncome)); }
}