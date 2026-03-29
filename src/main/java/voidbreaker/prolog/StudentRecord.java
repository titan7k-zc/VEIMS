package voidbreaker.prolog;

public class StudentRecord {
    private String id;
    private String name;
    private String grade;
    private String subjects;

    public StudentRecord(String id, String name, String grade, String subjects) {
        this.id = id;
        this.name = name;
        this.grade = grade;
        this.subjects = subjects;
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getGrade() {
        return grade;
    }
    public String getSubjects() {
        return subjects;
    }
}
