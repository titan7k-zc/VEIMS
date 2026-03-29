package voidbreaker.prolog;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class DatabaseHandler {
    private static String ip = "";
    private static String User = "";
    private static String Pass = "";
    private static String db = "";
    private static String port = "";
    private static final String CONFIG_PATH = "C:\\ProgramData\\prolog\\configs\\config.json";

    static {
        File configFile = new File(CONFIG_PATH);

        // Check if file exists, create it with empty values if it doesn't
        if (!configFile.exists()) {
            try {
                // Create parent directories if they don't exist
                configFile.getParentFile().mkdirs();

                // Create JSON object with empty values
                JSONObject config = new JSONObject();
                config.put("ip", "");
                config.put("username", "");
                config.put("password", "");
                config.put("database", "");
                config.put("port", "");

                // Create JSON array and add the config object
                JSONArray configArray = new JSONArray();
                configArray.add(config);

                // Write to file
                FileWriter writer = new FileWriter(configFile);
                writer.write(configArray.toJSONString());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                System.err.println("Error creating config file: " + e.getMessage());
            }
        }

        // Load configuration from file
        try {
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader(CONFIG_PATH);
            JSONArray configArray = (JSONArray) parser.parse(reader);

            // Get the first object from the array
            JSONObject config = (JSONObject) configArray.get(0);

            // Assign values to static fields
            ip = (String) config.get("ip");
            User = (String) config.get("username");
            Pass = (String) config.get("password");
            db = (String) config.get("database");
            port = (String) config.get("port"); // 3306

            reader.close();
        } catch (IOException e) {
            System.err.println("Error reading config file: " + e.getMessage());
        } catch (ParseException e) {
            System.err.println("Error parsing config file: " + e.getMessage());
        }
    }

    // Getter methods for accessing the configuration
    public static String getIp() {
        return ip;
    }
    public static String getUser() {
        return User;
    }
    public static String getPass() {
        return Pass;
    }
    public static String getDb() {
        return db;
    }
    public static String getPort() {
        return port;
    }


    private static String Url = "jdbc:mysql://"+ip+":"+port+"/"+db+"?useSSL=false&serverTimezone=UTC";

    private static HikariDataSource dataSource;

    static {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            HikariConfig config = new HikariConfig();
            // XAMPP default MySQL settings
            config.setJdbcUrl(Url);
            config.setUsername(User);
            config.setPassword(Pass);
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setConnectionTimeout(30000); // 30 seconds
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            dataSource = new HikariDataSource(config);
        } catch (ClassNotFoundException e) {
            System.err.println("FATAL: MySQL JDBC driver not found. Ensure MySQL Connector/J is included in the project.");
            e.printStackTrace();
            throw new RuntimeException("Database initialization failed.", e);
        }
    }

    /**
     * Constructor for DatabaseHandler.
     * It creates the necessary tables in the MySQL database.
     */
    public DatabaseHandler() {
        createTables();
    }

    /**
     * Establishes a connection to the MySQL database using HikariCP.
     * @return A Connection object to the database.
     * @throws SQLException if a database access error occurs.
     */
    Connection connect() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Creates all necessary tables in the database if they don't already exist.
     * This includes tables for students, grades, subjects, fees, and attendance.
     */
    private void createTables() {
        String createStudentsTable = "CREATE TABLE IF NOT EXISTS students (" +
                "id VARCHAR(255) PRIMARY KEY, " +
                "name VARCHAR(255) NOT NULL, " +
                "grade VARCHAR(50), " +
                "gender VARCHAR(10), " +
                "mobile VARCHAR(20), " +
                "register_date VARCHAR(10)," +
                "initial_grade VARCHAR(50)" +
                ")";

        String createGradesTable = "CREATE TABLE IF NOT EXISTS grades (" +
                "id INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                "name VARCHAR(50) UNIQUE NOT NULL" +
                ")";

        String createSubjectsTable = "CREATE TABLE IF NOT EXISTS subjects (" +
                "id INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                "name VARCHAR(255) NOT NULL, " +
                "grade_id INTEGER, " +
                "teacher VARCHAR(255), " +
                "FOREIGN KEY(grade_id) REFERENCES grades(id), " +
                "UNIQUE(name, grade_id)" +
                ")";

        String createSubjectFeeTable = "CREATE TABLE IF NOT EXISTS subject_fee (" +
                "id INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                "subject_id INTEGER NOT NULL, " +
                "effective_date VARCHAR(10) NOT NULL, " +
                "fee_amount DOUBLE NOT NULL, " +
                "FOREIGN KEY(subject_id) REFERENCES subjects(id), " +
                "UNIQUE(subject_id, effective_date)" +
                ")";

        String createSubjectTuteTable ="CREATE TABLE IF NOT EXISTS subject_tute (" +
                "student_id VARCHAR(255), " +
                "subject_id INTEGER, " +
                "year INTEGER, " +
                "month INTEGER, " +
                "FOREIGN KEY(student_id) REFERENCES students(id), " +
                "FOREIGN KEY(subject_id) REFERENCES subjects(id), " +
                "UNIQUE(student_id, subject_id, year, month)" +
                ")";

        String createStudentSubjectsTable = "CREATE TABLE IF NOT EXISTS student_subjects (" +
                "student_id VARCHAR(255), " +
                "subject_id INTEGER, " +
                "subject_register_date VARCHAR(10), " +
                "FOREIGN KEY(student_id) REFERENCES students(id), " +
                "FOREIGN KEY(subject_id) REFERENCES subjects(id), " +
                "UNIQUE(student_id, subject_id)" +
                ")";

        String createFeePaymentsTable = "CREATE TABLE IF NOT EXISTS fee_payments (" +
                "student_id VARCHAR(255), " +
                "subject_id INTEGER, " +
                "year INTEGER, " +
                "month INTEGER, " +
                "payment_date VARCHAR(10), " +
                "FOREIGN KEY(student_id) REFERENCES students(id), " +
                "FOREIGN KEY(subject_id) REFERENCES subjects(id), " +
                "UNIQUE(student_id, subject_id, year, month)" +
                ")";

        String createDailyFeePaymentsTable = "CREATE TABLE IF NOT EXISTS daily_fee_payments (" +
                "student_id VARCHAR(255), " +
                "subject_id INTEGER, " +
                "year INTEGER, " +
                "month INTEGER, " +
                "day INTEGER, " +
                "payment_date VARCHAR(10), " +
                "FOREIGN KEY(student_id) REFERENCES students(id), " +
                "FOREIGN KEY(subject_id) REFERENCES subjects(id), " +
                "UNIQUE(student_id, subject_id, year, month,day)" +
                ")";

        String createAttendanceTable = "CREATE TABLE IF NOT EXISTS attendance (" +
                "student_id VARCHAR(255), " +
                "subject_id INTEGER, " +
                "attendance_date VARCHAR(10), " +
                "FOREIGN KEY(student_id) REFERENCES students(id), " +
                "FOREIGN KEY(subject_id) REFERENCES subjects(id), " +
                "UNIQUE(student_id, subject_id, attendance_date)" +
                ")";

        String createYearFeeTable = "CREATE TABLE IF NOT EXISTS year_fee (" +
                "id INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                "subject_id INTEGER NOT NULL, " +
                "year INTEGER NOT NULL, " +
                "total DOUBLE NOT NULL, " +
                "FOREIGN KEY(subject_id) REFERENCES subjects(id), " +
                "UNIQUE(subject_id, year)" +
                ")";

        String createMonthFeeTable = "CREATE TABLE IF NOT EXISTS month_fee (" +
                "id INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                "subject_id INTEGER NOT NULL, " +
                "year INTEGER NOT NULL, " +
                "month INTEGER NOT NULL, " +
                "total DOUBLE NOT NULL, " +
                "FOREIGN KEY(subject_id) REFERENCES subjects(id), " +
                "UNIQUE(subject_id, year, month)" +
                ")";

        String createDayFeeTable = "CREATE TABLE IF NOT EXISTS day_fee (" +
                "id INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                "subject_id INTEGER NOT NULL, " +
                "year INTEGER NOT NULL, " +
                "month INTEGER NOT NULL, " +
                "day INTEGER NOT NULL, " +
                "total DOUBLE NOT NULL, " +
                "FOREIGN KEY(subject_id) REFERENCES subjects(id), " +
                "UNIQUE(subject_id, year, month, day)" +
                ")";

        String createStudentRemarksTable = "CREATE TABLE IF NOT EXISTS student_remarks (" +
                "student_id VARCHAR(255) PRIMARY KEY, " +
                "remark TEXT, " +
                "last_reminder_date VARCHAR(10), " +
                "FOREIGN KEY(student_id) REFERENCES students(id)" +
                ")";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(createStudentsTable);
            stmt.execute(createGradesTable);
            stmt.execute(createSubjectsTable);
            stmt.execute(createSubjectFeeTable);
            stmt.execute(createStudentSubjectsTable);
            stmt.execute(createFeePaymentsTable);
            stmt.execute(createDailyFeePaymentsTable);
            stmt.execute(createAttendanceTable);
            stmt.execute(createYearFeeTable);
            stmt.execute(createMonthFeeTable);
            stmt.execute(createDayFeeTable);
            stmt.execute(createStudentRemarksTable);
            stmt.execute(createSubjectTuteTable);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts a new student record along with their selected subjects into the database.
     */
    public void insertStudent(String id, String name, String grade, String gender, String mobile, String registerDate, List<String> subjects) {
        String sqlStudent = "INSERT INTO students(id, name, grade, gender, mobile, register_date, initial_grade) VALUES(?,?,?,?,?,?,?)";
        String sqlGetSubjectId = "SELECT id FROM subjects WHERE name = ? AND grade_id = (SELECT id FROM grades WHERE name = ?)";
        String sqlStudentSubject = "INSERT INTO student_subjects(student_id, subject_id, subject_register_date) VALUES(?,?,?)";

        try (Connection conn = connect()) {
            conn.setAutoCommit(false); // Start transaction

            // Insert student details
            try (PreparedStatement pstmt = conn.prepareStatement(sqlStudent)) {
                pstmt.setString(1, id);
                pstmt.setString(2, name);
                pstmt.setString(3, grade);
                pstmt.setString(4, gender);
                pstmt.setString(5, mobile);
                pstmt.setString(6, registerDate);
                pstmt.setString(7, grade);
                pstmt.executeUpdate();
            }

            // Link student to subjects
            for (String subj : subjects) {
                int subjectId = -1;
                try (PreparedStatement pstmt = conn.prepareStatement(sqlGetSubjectId)) {
                    pstmt.setString(1, subj);
                    pstmt.setString(2, grade);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        subjectId = rs.getInt("id");
                    }
                }

                if (subjectId != -1) {
                    try (PreparedStatement pstmt = conn.prepareStatement(sqlStudentSubject)) {
                        pstmt.setString(1, id);
                        pstmt.setInt(2, subjectId);
                        pstmt.setString(3, registerDate);
                        pstmt.executeUpdate();
                    }
                }
            }
            conn.commit(); // Commit transaction
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Registers a student into a subject for a given grade and date.
     * @param gradeName The grade of the student (e.g., "Grade 10").
     * @param subjectName The subject name (e.g., "Mathematics").
     * @param studentId The ID of the student.
     * @param registerDate The date of registration (YYYY-MM-DD format).
     * @return A status message indicating success or failure.
     */
    public boolean insertStudentToSubject(String gradeName, String subjectName, String studentId, String registerDate) {
        try (Connection conn = connect()) {
            conn.setAutoCommit(false); // Start transaction
            int subjectId = -1;
            try (PreparedStatement pstmt = conn.prepareStatement("SELECT id FROM subjects WHERE name = ? AND grade_id = (SELECT id FROM grades WHERE name = ?)")) {
                pstmt.setString(1, subjectName);
                pstmt.setString(2, gradeName);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    subjectId = rs.getInt("id");
                }
            }
            if (subjectId != -1) {
                try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO student_subjects(student_id, subject_id, subject_register_date) VALUES(?,?,?)")) {
                    pstmt.setString(1, studentId);
                    pstmt.setInt(2, subjectId);
                    pstmt.setString(3, registerDate);
                    pstmt.executeUpdate();
                }
            }

            conn.commit(); // Commit transaction
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves the last (highest) student ID from the database to continue the sequence.
     * @return The last student ID as a String, or null if no students exist.
     */
    public String getLastStudentId() {
        String sql = "SELECT id FROM students ORDER BY id DESC LIMIT 1";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // No students in DB
    }

    /**
     * Fetches a list of all students with their concatenated subjects for the dashboard table.
     * @return A List of StudentRecord objects.
     */
    public List<StudentRecord> getAllStudents() {
        List<StudentRecord> students = new ArrayList<>();
        String sql = "SELECT s.id, s.name, s.grade, GROUP_CONCAT(sub.name, ', ') as subjects " +
                "FROM students s " +
                "LEFT JOIN student_subjects ss ON s.id = ss.student_id " +
                "LEFT JOIN subjects sub ON ss.subject_id = sub.id " +
                "GROUP BY s.id, s.name, s.grade";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                students.add(new StudentRecord(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("grade"),
                        rs.getString("subjects") != null ? rs.getString("subjects") : "No Subjects"
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    /**
     * Retrieves all details for a single student by their ID.
     * @return A Map containing the student's details.
     */
    public Map<String, String> getStudentById(String studentId) {
        String sql = "SELECT * FROM students WHERE id = ?";
        Map<String, String> studentDetails = new HashMap<>();
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                studentDetails.put("id", rs.getString("id"));
                studentDetails.put("name", rs.getString("name"));
                studentDetails.put("grade", rs.getString("grade"));
                studentDetails.put("gender", rs.getString("gender"));
                studentDetails.put("mobile", rs.getString("mobile"));
                studentDetails.put("register_date", rs.getString("register_date"));
                studentDetails.put("initial_grade", rs.getString("initial_grade"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return studentDetails;
    }

    /**
     * Inserts a new grade into the database.
     * @return true if insertion is successful, false otherwise (e.g., duplicate grade).
     */
    public boolean insertGrade(String gradeName) {
        String sql = "INSERT INTO grades(name) VALUES(?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, gradeName);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Inserts a new subject for a specific grade into the database.
     * @return true if insertion is successful, false otherwise.
     */
    public boolean insertSubject(String subjectName, String gradeName, String feeAmount, String teacherName) {
        String sqlSubject = "INSERT INTO subjects(name, grade_id, teacher) VALUES(?, (SELECT id FROM grades WHERE name = ?), ?)";
        String sqlFee = "INSERT INTO subject_fee(subject_id, effective_date, fee_amount) VALUES(?,?,?)";

        try (Connection conn = connect()) {
            conn.setAutoCommit(false); // Start transaction

            // Validate feeAmount
            double fee;
            try {
                fee = Double.parseDouble(feeAmount);
                if (fee < 0) {
                    conn.rollback();
                    return false; // Invalid fee amount
                }
            } catch (NumberFormatException e) {
                conn.rollback();
                return false; // Invalid fee format
            }

            // Insert subject
            int subjectId = -1;
            try (PreparedStatement pstmt = conn.prepareStatement(sqlSubject, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, subjectName);
                pstmt.setString(2, gradeName);
                pstmt.setString(3, teacherName);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected == 0) {
                    conn.rollback();
                    return false; // Subject insertion failed
                }
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    subjectId = rs.getInt(1);
                }
            }

            // Insert fee
            try (PreparedStatement pstmt = conn.prepareStatement(sqlFee)) {
                pstmt.setInt(1, subjectId);
                pstmt.setString(2, LocalDate.now().toString());
                pstmt.setDouble(3, fee);
                pstmt.executeUpdate();
            }

            conn.commit(); // Commit transaction
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves a list of all grade names from the database.
     */
    public List<String> getAllGrades() {
        List<String> grades = new ArrayList<>();
        String sql = "SELECT name FROM grades ORDER BY name";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                grades.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return grades;
    }

    /**
     * Retrieves a list of subjects for a given grade.
     */
    public List<String> getSubjectsForGrade(String gradeName) {
        List<String> subjects = new ArrayList<>();
        String sql = "SELECT name FROM subjects WHERE grade_id = (SELECT id FROM grades WHERE name = ?) ORDER BY name";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, gradeName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                subjects.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjects;
    }

    /**
     * Gets the ID of a subject based on its name and grade.
     * @return The subject ID, or -1 if not found.
     */
    public int getSubjectId(String subjectName, String gradeName) {
        String sql = "SELECT id FROM subjects WHERE name = ? AND grade_id = (SELECT id FROM grades WHERE name = ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, subjectName);
            pstmt.setString(2, gradeName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Gets all subjects a specific student is registered for.
     */
    public List<String> getStudentSubjects(String studentId) {
        List<String> subjects = new ArrayList<>();
        String sql = "SELECT sub.name FROM subjects sub " +
                "JOIN student_subjects ss ON sub.id = ss.subject_id " +
                "WHERE ss.student_id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                subjects.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjects;
    }

    /**
     * Inserts a new fee for a subject with the given effective date.
     * @param subjectName The name of the subject.
     * @param gradeName The name of the grade.
     * @param effectiveDate The effective date for the new fee (YYYY-MM-DD).
     * @param feeAmount The new fee amount as a string.
     * @return true if insertion is successful, false otherwise (e.g., invalid input or duplicate date).
     */
    public boolean insertSubjectFee(String subjectName, String gradeName, String effectiveDate, String feeAmount) {
        int subjectId = getSubjectId(subjectName, gradeName);
        if (subjectId == -1) {
            return false;
        }

        double fee;
        try {
            fee = Double.parseDouble(feeAmount);
            if (fee < 0) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        String sql = "INSERT INTO subject_fee(subject_id, effective_date, fee_amount) VALUES(?,?,?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, subjectId);
            pstmt.setString(2, effectiveDate);
            pstmt.setDouble(3, fee);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Marks a fee as paid for a student, subject, year, and month.
     * @return A status message.
     */
    public String markFee(String studentId, String subjectName, String studentGrade, int year, int month ,double feeInput) {
        int subjectId = getSubjectId(subjectName, studentGrade);
        if (subjectId == -1) {
            return "Subject not found.";
        }
        String sql = "INSERT INTO fee_payments(student_id, subject_id, year, month, payment_date) VALUES(?,?,?,?,?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            pstmt.setInt(2, subjectId);
            pstmt.setInt(3, year);
            pstmt.setInt(4, month);
            pstmt.setString(5, LocalDate.now().toString());
            pstmt.executeUpdate();

            // After marking fee, update fee records to year_fee, month_fee, day_fee tables
            double feeAmount;
            if (feeInput > 0) {
                feeAmount= feeInput;
            }else{
                feeAmount = getCurrentFee(subjectId);
            }

            deleteStudentRemark(studentId);
            insertOrUpdateFeeRecords(subjectId, feeAmount);

            return "Fee marked for " + subjectName;
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                return "Fee already paid for " + subjectName + " this month.";
            }
            e.printStackTrace();
            return "Error marking fee.";
        }
    }

    /**
     * Marks a fee as paid for a student, subject, year, month, and day.
     * @return A status message.
     */
    public String markDailyFee(String studentId, String subjectName, String studentGrade, int year, int month ,int day ,double feeInput) {
        int subjectId = getSubjectId(subjectName, studentGrade);
        if (subjectId == -1) {
            return "Subject not found.";
        }
        String sql = "INSERT INTO daily_fee_payments(student_id, subject_id, year, month, day, payment_date) VALUES(?,?,?,?,?,?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            pstmt.setInt(2, subjectId);
            pstmt.setInt(3, year);
            pstmt.setInt(4, month);
            pstmt.setInt(5, day);
            pstmt.setString(6, LocalDate.now().toString());
            pstmt.executeUpdate();

            // After marking fee, update fee records to year_fee, month_fee, day_fee tables
            double feeAmount;
            if (feeInput > 0) {
                feeAmount= feeInput;
            }else{
                feeAmount = getCurrentFee(subjectId);
            }


            insertOrUpdateFeeRecords(subjectId, feeAmount);

            return "Fee marked for " + subjectName;
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                return "Fee already paid for " + subjectName + " this month.";
            }
            e.printStackTrace();
            return "Error marking fee.";
        }
    }

    /**
     * Marks attendance for a student for a specific subject on the current date.
     * @return A status message.
     */
    public String markAttendance(String studentId, String subjectName, String gradeName) {
        // First, check if the student is actually registered for this grade and subject.
        String studentActualGrade = getStudentById(studentId).get("grade");
        if (!gradeName.equals(studentActualGrade)) {
            return "Failed: Student is in Grade " + studentActualGrade;
        }

        List<String> registeredSubjects = getStudentSubjects(studentId);
        if (!registeredSubjects.contains(subjectName)) {
            return "Failed: Student not registered for " + subjectName;
        }

        int subjectId = getSubjectId(subjectName, gradeName);
        if (subjectId == -1) {
            return "Error: Subject ID not found.";
        }

        String sql = "INSERT INTO attendance(student_id, subject_id, attendance_date) VALUES(?,?,?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            pstmt.setInt(2, subjectId);
            pstmt.setString(3, LocalDate.now().toString());
            pstmt.executeUpdate();
            return "Attendance marked for " + studentId;
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                return "Attendance already marked today for " + studentId;
            }
            e.printStackTrace();
            return "Error marking attendance.";
        }
    }

    /**
     * Retrieves fee payment records for a student for a given year.
     * The outer map's key is the subject name. The inner map's key is the month (1-12),
     * and the value is the payment date.
     */
    public Map<String, Map<Integer, String>> getFeeRecords(String studentId, int year) {
        Map<String, Map<Integer, String>> records = new HashMap<>();
        String sql = "SELECT s.name as subject_name, fp.month, fp.payment_date " +
                "FROM fee_payments fp " +
                "JOIN subjects s ON fp.subject_id = s.id " +
                "WHERE fp.student_id = ? AND fp.year = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            pstmt.setInt(2, year);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String subjectName = rs.getString("subject_name");
                int month = rs.getInt("month");
                String paymentDate = rs.getString("payment_date");
                records.putIfAbsent(subjectName, new HashMap<>());
                records.get(subjectName).put(month, paymentDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    /**
     * Retrieves attendance records for a student for a given year and month.
     * The map's key is the subject name, and the value is a list of days (1-31)
     * the student was present.
     */
    public Map<String, List<Integer>> getAttendanceRecords(String studentId, int year, int month) {
        Map<String, List<Integer>> records = new HashMap<>();
        String sql = "SELECT s.name as subject_name, DAY(a.attendance_date) as day " +
                "FROM attendance a " +
                "JOIN subjects s ON a.subject_id = s.id " +
                "WHERE a.student_id = ? AND YEAR(a.attendance_date) = ? " +
                "AND MONTH(a.attendance_date) = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            pstmt.setInt(2, year);
            pstmt.setInt(3, month);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String subjectName = rs.getString("subject_name");
                int day = rs.getInt("day");
                records.putIfAbsent(subjectName, new ArrayList<>());
                records.get(subjectName).add(day);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    /**
     * Updates all students' grades based on their initial grade and registration date.
     * If a student's grade is upgraded, automatically registers them for subjects in the new grade
     * that match the names of subjects they are already registered for, with an "AU" prefixed registration date.
     * Deletes student data if they have been marked as "completed" for more than one year.
     */
    public void updateStudentGradesByRegistrationDate() {
        String sql = "SELECT id, initial_grade, register_date, grade FROM students";
        try (Connection conn = connect()) {
            conn.setAutoCommit(false); // Start transaction for all operations
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {

                LocalDate today = LocalDate.now();
                String autoRegDate = "AU" + today.toString();

                while (rs.next()) {
                    String studentId = rs.getString("id");
                    String initialGradeStr = rs.getString("initial_grade");
                    String regDateStr = rs.getString("register_date");
                    String currentGrade = rs.getString("grade");

                    try {
                        int initialGrade = Integer.parseInt(initialGradeStr);
                        LocalDate regDate = LocalDate.parse(regDateStr);

                        // Years passed since registration
                        int yearsPassed = today.getYear() - regDate.getYear();

                        // Correct grade calculation
                        int correctGradeNum = initialGrade + yearsPassed;
                        String gradeToSet = correctGradeNum > 13 ? "completed" : String.valueOf(correctGradeNum);

                        if (gradeToSet.equals("completed")) {
                            // Calculate years since reaching grade 13
                            int yearsSinceGrade13 = yearsPassed - (13 - initialGrade);
                            if (yearsSinceGrade13 > 1) {
                                // Delete student data if more than one year has passed since completion
                                deleteStudentData(studentId);
                                continue; // Skip further processing for deleted student
                            }
                        }

                        if (!gradeToSet.equals(currentGrade)) {
                            // Update grade in DB
                            try (PreparedStatement updateStmt = conn.prepareStatement(
                                    "UPDATE students SET grade = ? WHERE id = ?")) {
                                updateStmt.setString(1, gradeToSet);
                                updateStmt.setString(2, studentId);
                                int updated = updateStmt.executeUpdate();
                                if (updated > 0 && !gradeToSet.equals("completed") && Integer.parseInt(gradeToSet) > Integer.parseInt(currentGrade)) {
                                    autoRegisterSubjectsForNewGrade(conn, studentId, gradeToSet, autoRegDate);
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid initial grade or current grade for student " + studentId + ": " + e.getMessage());
                    }
                }
                conn.commit(); // Commit all changes for all students
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Auto-registers a student for subjects in the new grade that match the names of subjects
     * they are already registered for, using an "AU" prefixed registration date.
     * Uses the provided connection to avoid multiple concurrent connections.
     * @param conn The database connection to use.
     * @param studentId The ID of the student.
     * @param newGrade The new grade to register subjects for.
     * @param autoRegDate The registration date with "AU" prefix (e.g., AU2025-09-16).
     */
    private void autoRegisterSubjectsForNewGrade(Connection conn, String studentId, String newGrade, String autoRegDate) {
        // Get the student's currently registered subject names
        Set<String> registeredSubjectNames = new HashSet<>();
        String sqlRegistered = "SELECT DISTINCT sub.name FROM subjects sub " +
                "JOIN student_subjects ss ON sub.id = ss.subject_id " +
                "WHERE ss.student_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlRegistered)) {
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                registeredSubjectNames.add(rs.getString("name").toLowerCase());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        // Get all subjects in the new grade with their IDs
        Map<String, Integer> newGradeSubjects = new HashMap<>();
        String sqlNewSubjects = "SELECT id, name FROM subjects WHERE grade_id = (SELECT id FROM grades WHERE name = ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlNewSubjects)) {
            pstmt.setString(1, newGrade);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                newGradeSubjects.put(rs.getString("name").toLowerCase(), rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        // For each subject in new grade, if name matches a registered one, insert
        String sqlInsert = "INSERT IGNORE INTO student_subjects (student_id, subject_id, subject_register_date) VALUES (?, ?, ?)";
        try (PreparedStatement insertStmt = conn.prepareStatement(sqlInsert)) {
            for (Map.Entry<String, Integer> entry : newGradeSubjects.entrySet()) {
                String subjectName = entry.getKey();
                int subjectId = entry.getValue();
                if (registeredSubjectNames.contains(subjectName)) {
                    insertStmt.setString(1, studentId);
                    insertStmt.setInt(2, subjectId);
                    insertStmt.setString(3, autoRegDate);
                    insertStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets all subjects a specific student is registered for, with the registration date and subject ID.
     */
    public List<SubjectRegistrationRecord> getStudentSubjectsWithDate(String studentId) {
        List<SubjectRegistrationRecord> subjectsWithDate = new ArrayList<>();
        String sql = "SELECT sub.id, sub.name, ss.subject_register_date FROM subjects sub " +
                "JOIN student_subjects ss ON sub.id = ss.subject_id " +
                "WHERE ss.student_id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                subjectsWithDate.add(new SubjectRegistrationRecord(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("subject_register_date")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjectsWithDate;
    }

    /**
     * Gets all subjects a specific student is registered for, filtered by grade.
     */
    public List<String> getStudentSubjectsForGrade(String studentId, String gradeName) {
        List<String> subjects = new ArrayList<>();
        String sql = "SELECT sub.name FROM subjects sub " +
                "JOIN student_subjects ss ON sub.id = ss.subject_id " +
                "JOIN grades g ON sub.grade_id = g.id " +
                "WHERE ss.student_id = ? AND g.name = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            pstmt.setString(2, gradeName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                subjects.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjects;
    }

    /**
     * Deletes all data related to a student from all relevant tables.
     * @param studentId The ID of the student to delete.
     * @return true if deletion is successful, false otherwise.
     */
    public boolean deleteStudentData(String studentId) {
        String deleteAttendance = "DELETE FROM attendance WHERE student_id = ?";
        String deleteFeePayments = "DELETE FROM fee_payments WHERE student_id = ?";
        String deleteDailyFeePayments = "DELETE FROM daily_fee_payments WHERE student_id = ?";
        String deleteStudentSubjects = "DELETE FROM student_subjects WHERE student_id = ?";
        String deleteStudent = "DELETE FROM students WHERE id = ?";
        String deleteStudentRemarks = "DELETE FROM student_remarks WHERE student_id = ?";
        String checkStudent = "SELECT COUNT(*) FROM students WHERE id = ?";

        Connection conn = null;
        try {
            conn = connect();
            if (conn == null) {
                System.err.println("Failed to establish database connection.");
                return false;
            }
            conn.setAutoCommit(false); // Start transaction

            // Verify student exists
            try (PreparedStatement pstmt = conn.prepareStatement(checkStudent)) {
                pstmt.setString(1, studentId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    System.err.println("Student ID " + studentId + " does not exist.");
                    return false;
                }
            }

            // Delete from dependent tables
            try (PreparedStatement pstmt = conn.prepareStatement(deleteAttendance)) {
                pstmt.setString(1, studentId);
                pstmt.executeUpdate();
            }

            try (PreparedStatement pstmt = conn.prepareStatement(deleteFeePayments)) {
                pstmt.setString(1, studentId);
                pstmt.executeUpdate();
            }

            try (PreparedStatement pstmt = conn.prepareStatement(deleteDailyFeePayments)) {
                pstmt.setString(1, studentId);
                pstmt.executeUpdate();
            }

            try (PreparedStatement pstmt = conn.prepareStatement(deleteStudentSubjects)) {
                pstmt.setString(1, studentId);
                pstmt.executeUpdate();
            }
            try (PreparedStatement pstmt = conn.prepareStatement(deleteStudentRemarks)) {
                pstmt.setString(1, studentId);
                pstmt.executeUpdate();
            }

            // Delete from students table
            try (PreparedStatement pstmt = conn.prepareStatement(deleteStudent)) {
                pstmt.setString(1, studentId);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected == 0) {
                    conn.rollback();
                    System.err.println("No rows deleted for student ID: " + studentId);
                    return false;
                }
            }

            conn.commit(); // Commit transaction
            return true;
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                    System.err.println("Transaction rolled back.");
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Rollback failed: " + rollbackEx.getMessage());
            }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException closeEx) {
                System.err.println("Failed to close connection: " + closeEx.getMessage());
            }
        }
    }

    /**
     * Unregisters a student from a specific subject in a given grade.
     * This involves deleting records from student_subjects, fee_payments, and attendance tables.
     * @param studentId The ID of the student.
     * @param gradeName The name of the grade (e.g., "Grade 10").
     * @param subjectName The name of the subject (e.g., "Mathematics").
     * @return true if unregistration is successful, false otherwise.
     */
    public boolean unregisterSubject(String studentId, String gradeName, String subjectName) {
        try (Connection conn = connect()) {
            conn.setAutoCommit(false); // Start transaction

            // Get subject_id
            String sqlGetSubjectId = "SELECT id FROM subjects WHERE name = ? AND grade_id = (SELECT id FROM grades WHERE name = ?)";
            int subjectId = -1;
            try (PreparedStatement pstmt = conn.prepareStatement(sqlGetSubjectId)) {
                pstmt.setString(1, subjectName);
                pstmt.setString(2, gradeName);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    subjectId = rs.getInt("id");
                } else {
                    conn.rollback();
                    return false; // Subject not found in grade
                }
            }

            // Delete from attendance
            String sqlDeleteAttendance = "DELETE FROM attendance WHERE student_id = ? AND subject_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteAttendance)) {
                pstmt.setString(1, studentId);
                pstmt.setInt(2, subjectId);
                pstmt.executeUpdate();
            }

            // Delete from fee_payments
            String sqlDeleteFees = "DELETE FROM fee_payments WHERE student_id = ? AND subject_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteFees)) {
                pstmt.setString(1, studentId);
                pstmt.setInt(2, subjectId);
                pstmt.executeUpdate();
            }

            // Delete from daily_fee_payments
            String sqlDeleteDailyFees = "DELETE FROM daily_fee_payments WHERE student_id = ? AND subject_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteDailyFees)) {
                pstmt.setString(1, studentId);
                pstmt.setInt(2, subjectId);
                pstmt.executeUpdate();
            }

            // Delete from student_subjects
            String sqlDeleteRegistration = "DELETE FROM student_subjects WHERE student_id = ? AND subject_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteRegistration)) {
                pstmt.setString(1, studentId);
                pstmt.setInt(2, subjectId);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected == 0) {
                    conn.rollback();
                    return false; // No registration found to delete
                }
            }

            conn.commit(); // Commit transaction
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves the current fee amount for a given subject based on the effective date.
     * Returns the fee_amount with the latest effective_date <= current date.
     * @param subjectId The ID of the subject.
     * @return The current fee amount, or 0.0 if no applicable fee is found.
     */
    public double getCurrentFee(int subjectId) {
        String currentDate = LocalDate.now().toString();
        String sql = "SELECT fee_amount FROM subject_fee " +
                "WHERE subject_id = ? AND effective_date <= ? " +
                "ORDER BY effective_date DESC LIMIT 1";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, subjectId);
            pstmt.setString(2, currentDate);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("fee_amount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0; // Default if no fee found
    }

    /**
     * Inserts or updates fee records for a subject in year_fee, month_fee, and day_fee tables.
     * If records already exist for the same subject and date, adds the fee amount to existing total.
     * If no records exist, creates new records with the provided fee amount.
     *
     * @param subjectId The ID of the subject.
     * @param feeAmount The fee amount to insert or add to existing records.
     * @return true if all operations are successful, false otherwise.
     */
    public boolean insertOrUpdateFeeRecords(int subjectId, double feeAmount) {
        if (feeAmount < 0) {
            return false; // Invalid fee amount
        }

        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        int currentMonth = currentDate.getMonthValue();
        int currentDay = currentDate.getDayOfMonth();

        String sqlYearFee = "INSERT INTO year_fee(subject_id, year, total) VALUES(?,?,?) " +
                "ON DUPLICATE KEY UPDATE total = total + ?";
        String sqlMonthFee = "INSERT INTO month_fee(subject_id, year, month, total) VALUES(?,?,?,?) " +
                "ON DUPLICATE KEY UPDATE total = total + ?";
        String sqlDayFee = "INSERT INTO day_fee(subject_id, year, month, day, total) VALUES(?,?,?,?,?) " +
                "ON DUPLICATE KEY UPDATE total = total + ?";

        try (Connection conn = connect()) {
            conn.setAutoCommit(false); // Start transaction

            // Insert/Update year_fee table
            try (PreparedStatement pstmt = conn.prepareStatement(sqlYearFee)) {
                pstmt.setInt(1, subjectId);
                pstmt.setInt(2, currentYear);
                pstmt.setDouble(3, feeAmount);
                pstmt.setDouble(4, feeAmount);
                pstmt.executeUpdate();
            }

            // Insert/Update month_fee table
            try (PreparedStatement pstmt = conn.prepareStatement(sqlMonthFee)) {
                pstmt.setInt(1, subjectId);
                pstmt.setInt(2, currentYear);
                pstmt.setInt(3, currentMonth);
                pstmt.setDouble(4, feeAmount);
                pstmt.setDouble(5, feeAmount);
                pstmt.executeUpdate();
            }

            // Insert/Update day_fee table
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDayFee)) {
                pstmt.setInt(1, subjectId);
                pstmt.setInt(2, currentYear);
                pstmt.setInt(3, currentMonth);
                pstmt.setInt(4, currentDay);
                pstmt.setDouble(5, feeAmount);
                pstmt.setDouble(6, feeAmount);
                pstmt.executeUpdate();
            }

            conn.commit(); // Commit all changes
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves revenue data for all subjects, including their grade, teacher, and fee totals
     * for a specific year, month, and day.
     * @param year The selected year for filtering year_fee.
     * @param month The selected month for filtering month_fee (1-12).
     * @param day The selected day for filtering day_fee (1-31).
     * @return A List of RevenueRecord objects containing grade, subject, teacher, and fee totals.
     */
    public List<RevenueRecord> getTeacherRevenue(int year, int month, int day) {
        List<RevenueRecord> revenueRecords = new ArrayList<>();
        String sql = "SELECT g.name AS grade_name, s.name AS subject_name, s.teacher, " +
                "yf.total AS year_total, mf.total AS month_total, df.total AS day_total " +
                "FROM subjects s " +
                "JOIN grades g ON s.grade_id = g.id " +
                "LEFT JOIN year_fee yf ON s.id = yf.subject_id AND yf.year = ? " +
                "LEFT JOIN month_fee mf ON s.id = mf.subject_id AND mf.year = ? AND mf.month = ? " +
                "LEFT JOIN day_fee df ON s.id = df.subject_id AND df.year = ? AND df.month = ? AND df.day = ?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, year);
            pstmt.setInt(2, year);
            pstmt.setInt(3, month);
            pstmt.setInt(4, year);
            pstmt.setInt(5, month);
            pstmt.setInt(6, day);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                revenueRecords.add(new RevenueRecord(
                        rs.getString("grade_name"),
                        rs.getString("subject_name"),
                        rs.getString("teacher"),
                        rs.getDouble("year_total") * 0.8,
                        rs.getDouble("month_total") * 0.8,
                        rs.getDouble("day_total") * 0.8
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return revenueRecords;
    }

    /**
     * Retrieves revenue data for the institute, including fee totals
     * for a specific year, month, and day.
     * @param year The selected year for filtering year_fee.
     * @param month The selected month for filtering month_fee (1-12).
     * @param day The selected day for filtering day_fee (1-31).
     * @return A List of RevenueRecord objects containing grade, subject, teacher, and fee totals.
     */
    public List<RevenueRecord> getInstituteRevenue(int year, int month, int day) {
        List<RevenueRecord> revenueRecords = new ArrayList<>();
        String sql = "SELECT g.name AS grade_name, s.name AS subject_name, s.teacher, " +
                "yf.total AS year_total, mf.total AS month_total, df.total AS day_total " +
                "FROM subjects s " +
                "JOIN grades g ON s.grade_id = g.id " +
                "LEFT JOIN year_fee yf ON s.id = yf.subject_id AND yf.year = ? " +
                "LEFT JOIN month_fee mf ON s.id = mf.subject_id AND mf.year = ? AND mf.month = ? " +
                "LEFT JOIN day_fee df ON s.id = df.subject_id AND df.year = ? AND df.month = ? AND df.day = ?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, year);
            pstmt.setInt(2, year);
            pstmt.setInt(3, month);
            pstmt.setInt(4, year);
            pstmt.setInt(5, month);
            pstmt.setInt(6, day);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                revenueRecords.add(new RevenueRecord(
                        rs.getString("grade_name"),
                        rs.getString("subject_name"),
                        rs.getString("teacher"),
                        rs.getDouble("year_total") * 0.2,
                        rs.getDouble("month_total") * 0.2,
                        rs.getDouble("day_total") * 0.2
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return revenueRecords;
    }

    /**
     * Deletes records from the day_fee table that are older than 30 days from the current date.
     * This ensures the table only retains data for today and the past 30 days (total 31 days).
     */
    public void deleteOldDayFeeRecords() {
        LocalDate now = LocalDate.now();
        LocalDate cutoff = now.minusDays(30);
        String cutoffStr = cutoff.toString(); // Format: YYYY-MM-DD

        String sql = "DELETE FROM day_fee WHERE CONCAT(LPAD(year, 4, '0'), '-', LPAD(month, 2, '0'), '-', LPAD(day, 2, '0')) < ?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cutoffStr);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes records from the month_fee table that are older than 11 months from the current date.
     * This ensures the table only retains data for the current month and the past 11 months (total 12 months).
     */
    public void deleteOldMonthFeeRecords() {
        LocalDate now = LocalDate.now();
        LocalDate cutoff = now.minusMonths(11).withDayOfMonth(1);
        String cutoffStr = cutoff.toString(); // Format: YYYY-MM-DD

        String sql = "DELETE FROM month_fee WHERE CONCAT(LPAD(year, 4, '0'), '-', LPAD(month, 2, '0'), '-01') < ?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cutoffStr);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves information for all subjects, including grade name, subject ID, subject name, teacher, and current fee.
     * The current fee is the latest fee_amount where effective_date <= current date.
     * If no fee is found, fee defaults to 0.0.
     * @return A List of SubjectInfoRecord objects.
     */
    public List<SubjectInfoRecord> getAllSubjectsInfo() {
        List<SubjectInfoRecord> records = new ArrayList<>();
        String currentDate = LocalDate.now().toString();
        String sql = "SELECT g.name AS grade_name, s.id AS subject_id, s.name AS subject_name, s.teacher, " +
                "(SELECT fee_amount FROM subject_fee WHERE subject_id = s.id AND effective_date <= ? " +
                "ORDER BY effective_date DESC LIMIT 1) AS current_fee " +
                "FROM subjects s " +
                "JOIN grades g ON s.grade_id = g.id " +
                "ORDER BY g.name, s.name";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, currentDate);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String grade = rs.getString("grade_name");
                String id = rs.getString("subject_id");
                String subject = rs.getString("subject_name");
                String teacher = rs.getString("teacher");
                double fee = rs.getDouble("current_fee");
                records.add(new SubjectInfoRecord(grade, id, subject, teacher, fee));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    /**
     * Retrieves daily fee payment records for a student for a given year and month.
     * The map's key is the subject name, and the value is a list of days (1-31)
     * the fee was paid.
     */
    public Map<String, List<Integer>> getDailyFeeRecords(String studentId, int year, int month) {
        Map<String, List<Integer>> records = new HashMap<>();
        String sql = "SELECT s.name as subject_name, dfp.day " +
                "FROM daily_fee_payments dfp " +
                "JOIN subjects s ON dfp.subject_id = s.id " +
                "WHERE dfp.student_id = ? AND dfp.year = ? " +
                "AND dfp.month = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            pstmt.setInt(2, year);
            pstmt.setInt(3, month);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String subjectName = rs.getString("subject_name");
                int day = rs.getInt("day");
                records.putIfAbsent(subjectName, new ArrayList<>());
                records.get(subjectName).add(day);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    /**
     * Retrieves information for all subjects, including grade name, subject ID, subject name, and total student count.
     * @return A List of GradeSubjectRecord objects.
     */
    public List<GradeSubjectRecord> getAllSubjectsStudentCounts() {
        List<GradeSubjectRecord> records = new ArrayList<>();
        String sql = "SELECT g.name AS grade_name, s.id AS subject_id, s.name AS subject_name, " +
                "COUNT(ss.student_id) AS student_count " +
                "FROM subjects s " +
                "JOIN grades g ON s.grade_id = g.id " +
                "LEFT JOIN student_subjects ss ON s.id = ss.subject_id " +
                "GROUP BY s.id, g.name, s.name " +
                "ORDER BY g.name, s.name";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                records.add(new GradeSubjectRecord(
                        rs.getString("grade_name"),
                        rs.getString("subject_id"),
                        rs.getString("subject_name"),
                        rs.getInt("student_count")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    /**
     * Retrieves a list of students registered for a specific subject in a given grade.
     * @param gradeName The name of the grade (e.g., "Grade 10").
     * @param subjectName The name of the subject (e.g., "Mathematics").
     * @return A List of StudentRecord objects containing student ID and name.
     */
    public List<StudentRecord> getStudentsForSubject(String gradeName, String subjectName) {
        List<StudentRecord> students = new ArrayList<>();
        String sql = "SELECT s.id, s.name " +
                "FROM students s " +
                "JOIN student_subjects ss ON s.id = ss.student_id " +
                "JOIN subjects sub ON ss.subject_id = sub.id " +
                "JOIN grades g ON sub.grade_id = g.id " +
                "WHERE g.name = ? AND sub.name = ? " +
                "ORDER BY s.id";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, gradeName);
            pstmt.setString(2, subjectName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                students.add(new StudentRecord(
                        rs.getString("id"),
                        rs.getString("name"),
                        null, // Grade not needed for this view
                        null  // Subjects not needed for this view
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    /**
     * Updates the teacher name for a specific subject in a given grade.
     * @param gradeName The name of the grade (e.g., "Grade 10").
     * @param subjectName The name of the subject (e.g., "Mathematics").
     * @param newTeacherName The new teacher name to set.
     * @return true if the update was successful (at least one row affected), false otherwise (e.g., subject not found or SQL error).
     */
    public boolean updateTeacherName(String gradeName, String subjectName, String newTeacherName) {
        if (gradeName == null || gradeName.trim().isEmpty() ||
                subjectName == null || subjectName.trim().isEmpty() ||
                newTeacherName == null || newTeacherName.trim().isEmpty()) {
            System.err.println("Invalid input: Grade, subject, or new teacher name cannot be empty.");
            return false;
        }

        int subjectId = getSubjectId(subjectName, gradeName);
        if (subjectId == -1) {
            System.err.println("Subject not found for grade: " + gradeName + ", subject: " + subjectName);
            return false;
        }

        String sql = "UPDATE subjects SET teacher = ? WHERE id = ?";
        try (Connection conn = connect()) {
            conn.setAutoCommit(false); // Start transaction for safety (though single update, it's good practice)
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, newTeacherName.trim());
                pstmt.setInt(2, subjectId);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    System.err.println("No rows updated for subject ID: " + subjectId);
                    return false;
                }
            } catch (SQLException e) {
                conn.rollback();
                System.err.println("SQL error during teacher name update: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Connection error during teacher name update: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a grade and all associated data from the database.
     * This includes deleting related subjects, fees, payments, attendance, and student-subject links.
     * The operation is performed in a transaction for atomicity.
     *
     * @param gradeName The name of the grade to delete (e.g., "Grade 10").
     * @return true if deletion is successful, false otherwise (e.g., grade not found, SQL error, or integrity violation).
     */
    public boolean deleteGrade(String gradeName) {
        if (gradeName == null || gradeName.trim().isEmpty()) {
            System.err.println("Invalid grade name: Cannot be null or empty.");
            return false;
        }

        Connection conn = null;
        try {
            conn = connect();
            if (conn == null) {
                System.err.println("Failed to establish database connection.");
                return false;
            }
            conn.setAutoCommit(false); // Start transaction

            // Step 1: Get grade_id
            String sqlGetGradeId = "SELECT id FROM grades WHERE name = ?";
            int gradeId = -1;
            try (PreparedStatement pstmt = conn.prepareStatement(sqlGetGradeId)) {
                pstmt.setString(1, gradeName);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    gradeId = rs.getInt("id");
                } else {
                    conn.rollback();
                    System.err.println("Grade not found: " + gradeName);
                    return false; // Grade does not exist
                }
            }

            // Step 2: Get all subject_ids for this grade
            List<Integer> subjectIds = new ArrayList<>();
            String sqlGetSubjects = "SELECT id FROM subjects WHERE grade_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlGetSubjects)) {
                pstmt.setInt(1, gradeId);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    subjectIds.add(rs.getInt("id"));
                }
            }

            if (subjectIds.isEmpty()) {
                // No subjects, proceed to delete grade directly
            } else {
                // Step 3: Delete dependent records for each subject_id
                for (int subjectId : subjectIds) {
                    // Delete from attendance
                    String sqlDeleteAttendance = "DELETE FROM attendance WHERE subject_id = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteAttendance)) {
                        pstmt.setInt(1, subjectId);
                        pstmt.executeUpdate();
                    }

                    // Delete from fee_payments
                    String sqlDeleteFeePayments = "DELETE FROM fee_payments WHERE subject_id = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteFeePayments)) {
                        pstmt.setInt(1, subjectId);
                        pstmt.executeUpdate();
                    }

                    // Delete from daily_fee_payments
                    String sqlDeleteDailyFeePayments = "DELETE FROM daily_fee_payments WHERE subject_id = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteDailyFeePayments)) {
                        pstmt.setInt(1, subjectId);
                        pstmt.executeUpdate();
                    }

                    // Delete from student_subjects
                    String sqlDeleteStudentSubjects = "DELETE FROM student_subjects WHERE subject_id = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteStudentSubjects)) {
                        pstmt.setInt(1, subjectId);
                        pstmt.executeUpdate();
                    }

                    // Delete from subject_fee
                    String sqlDeleteSubjectFee = "DELETE FROM subject_fee WHERE subject_id = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteSubjectFee)) {
                        pstmt.setInt(1, subjectId);
                        pstmt.executeUpdate();
                    }

                    // Delete from year_fee
                    String sqlDeleteYearFee = "DELETE FROM year_fee WHERE subject_id = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteYearFee)) {
                        pstmt.setInt(1, subjectId);
                        pstmt.executeUpdate();
                    }

                    // Delete from month_fee
                    String sqlDeleteMonthFee = "DELETE FROM month_fee WHERE subject_id = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteMonthFee)) {
                        pstmt.setInt(1, subjectId);
                        pstmt.executeUpdate();
                    }

                    // Delete from day_fee
                    String sqlDeleteDayFee = "DELETE FROM day_fee WHERE subject_id = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteDayFee)) {
                        pstmt.setInt(1, subjectId);
                        pstmt.executeUpdate();
                    }
                }

                // Step 4: Delete from subjects
                String sqlDeleteSubjects = "DELETE FROM subjects WHERE grade_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteSubjects)) {
                    pstmt.setInt(1, gradeId);
                    pstmt.executeUpdate();
                }
            }

            // Step 5: Delete from grades
            String sqlDeleteGrade = "DELETE FROM grades WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteGrade)) {
                pstmt.setInt(1, gradeId);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected == 0) {
                    conn.rollback();
                    System.err.println("No rows deleted for grade: " + gradeName);
                    return false;
                }
            }

            conn.commit(); // Commit transaction
            System.out.println("Grade '" + gradeName + "' and all associated data deleted successfully.");
            return true;
        } catch (SQLException e) {
            System.err.println("SQL Error during grade deletion: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                    System.err.println("Transaction rolled back due to error.");
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Rollback failed: " + rollbackEx.getMessage());
            }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException closeEx) {
                System.err.println("Failed to close connection: " + closeEx.getMessage());
            }
        }
    }

    /**
     * Deletes a subject and all associated data from the database.
     * This includes deleting related fees, payments, attendance, and student-subject links.
     * The operation is performed in a transaction for atomicity.
     *
     * @param gradeName The name of the grade the subject belongs to (e.g., "Grade 10").
     * @param subjectName The name of the subject to delete (e.g., "Mathematics").
     * @return true if deletion is successful, false otherwise (e.g., subject not found, SQL error, or integrity violation).
     */
    public boolean deleteSubject(String gradeName, String subjectName) {
        if (gradeName == null || gradeName.trim().isEmpty() || subjectName == null || subjectName.trim().isEmpty()) {
            System.err.println("Invalid input: Grade name or subject name cannot be null or empty.");
            return false;
        }

        Connection conn = null;
        try {
            conn = connect();
            if (conn == null) {
                System.err.println("Failed to establish database connection.");
                return false;
            }
            conn.setAutoCommit(false); // Start transaction

            // Step 1: Get subject_id using grade_name and subject_name
            String sqlGetSubjectId = "SELECT s.id FROM subjects s " +
                    "JOIN grades g ON s.grade_id = g.id " +
                    "WHERE g.name = ? AND s.name = ?";
            int subjectId = -1;
            try (PreparedStatement pstmt = conn.prepareStatement(sqlGetSubjectId)) {
                pstmt.setString(1, gradeName);
                pstmt.setString(2, subjectName);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    subjectId = rs.getInt("id");
                } else {
                    conn.rollback();
                    System.err.println("Subject not found: " + subjectName + " in grade " + gradeName);
                    return false; // Subject does not exist
                }
            }

            // Step 2: Delete from dependent tables
            // Delete from attendance
            String sqlDeleteAttendance = "DELETE FROM attendance WHERE subject_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteAttendance)) {
                pstmt.setInt(1, subjectId);
                pstmt.executeUpdate();
            }

            // Delete from fee_payments
            String sqlDeleteFeePayments = "DELETE FROM fee_payments WHERE subject_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteFeePayments)) {
                pstmt.setInt(1, subjectId);
                pstmt.executeUpdate();
            }

            // Delete from daily_fee_payments
            String sqlDeleteDailyFeePayments = "DELETE FROM daily_fee_payments WHERE subject_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteDailyFeePayments)) {
                pstmt.setInt(1, subjectId);
                pstmt.executeUpdate();
            }

            // Delete from student_subjects
            String sqlDeleteStudentSubjects = "DELETE FROM student_subjects WHERE subject_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteStudentSubjects)) {
                pstmt.setInt(1, subjectId);
                pstmt.executeUpdate();
            }

            // Delete from subject_fee
            String sqlDeleteSubjectFee = "DELETE FROM subject_fee WHERE subject_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteSubjectFee)) {
                pstmt.setInt(1, subjectId);
                pstmt.executeUpdate();
            }

            // Delete from year_fee
            String sqlDeleteYearFee = "DELETE FROM year_fee WHERE subject_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteYearFee)) {
                pstmt.setInt(1, subjectId);
                pstmt.executeUpdate();
            }

            // Delete from month_fee
            String sqlDeleteMonthFee = "DELETE FROM month_fee WHERE subject_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteMonthFee)) {
                pstmt.setInt(1, subjectId);
                pstmt.executeUpdate();
            }

            // Delete from day_fee
            String sqlDeleteDayFee = "DELETE FROM day_fee WHERE subject_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteDayFee)) {
                pstmt.setInt(1, subjectId);
                pstmt.executeUpdate();
            }

            // Step 3: Delete from subjects
            String sqlDeleteSubject = "DELETE FROM subjects WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteSubject)) {
                pstmt.setInt(1, subjectId);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected == 0) {
                    conn.rollback();
                    System.err.println("No rows deleted for subject: " + subjectName + " in grade " + gradeName);
                    return false;
                }
            }

            conn.commit(); // Commit transaction
            System.out.println("Subject '" + subjectName + "' in grade '" + gradeName + "' and all associated data deleted successfully.");
            return true;
        } catch (SQLException e) {
            System.err.println("SQL Error during subject deletion: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                    System.err.println("Transaction rolled back due to error.");
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Rollback failed: " + rollbackEx.getMessage());
            }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException closeEx) {
                System.err.println("Failed to close connection: " + closeEx.getMessage());
            }
        }
    }

    public Map<String, String> getStudentRemarks(String studentId) {
        Map<String, String> remarks = new HashMap<>();
        String sql = "SELECT remark, last_reminder_date FROM student_remarks WHERE student_id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                remarks.put("remark", rs.getString("remark"));
                remarks.put("last_reminder_date", rs.getString("last_reminder_date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return remarks;
    }

    public int updateStudentRemarks(String studentId, String remark, String lastReminderDate) {
        String sql = "INSERT INTO student_remarks(student_id, remark, last_reminder_date) VALUES(?,?,?) " +
                "ON DUPLICATE KEY UPDATE remark = ?, last_reminder_date = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            pstmt.setString(2, remark);
            pstmt.setString(3, lastReminderDate);
            pstmt.setString(4, remark);
            pstmt.setString(5, lastReminderDate);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Marks a tute for a student, subject, year, and month.
     * @return A status message.
     */
    public String markTute(String studentId, String subjectName, String studentGrade, int year, int month) {
        int subjectId = getSubjectId(subjectName, studentGrade);
        if (subjectId == -1) {
            return "Subject not found.";
        }
        String sql = "INSERT INTO subject_tute(student_id, subject_id, year, month) VALUES(?,?,?,?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            pstmt.setInt(2, subjectId);
            pstmt.setInt(3, year);
            pstmt.setInt(4, month);
            pstmt.executeUpdate();
            return "Tute marked for " + subjectName;
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                return "Tute already marked for " + subjectName + " this month.";
            }
            e.printStackTrace();
            return "Error marking tute.";
        }
    }

    public Map<String, Set<Integer>> getTuteRecords(String studentId, int year) {
        Map<String, Set<Integer>> tuteMap = new HashMap<>();
        String sql = "SELECT sub.name AS subject, st.month " +
                "FROM subject_tute st " +
                "JOIN subjects sub ON st.subject_id = sub.id " +
                "WHERE st.student_id = ? AND st.year = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            pstmt.setInt(2, year);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String subject = rs.getString("subject");
                int month = rs.getInt("month");
                tuteMap.computeIfAbsent(subject, k -> new HashSet<>()).add(month);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tuteMap;
    }

    /**
     * Deletes the remark record for a specific student from the student_remarks table.
     * @param studentId The ID of the student whose remark record should be deleted.
     * @return true if the deletion was successful (record existed and was deleted), false otherwise (e.g., no record found or SQL error).
     */
    public boolean deleteStudentRemark(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            System.err.println("Invalid student ID: Cannot be null or empty.");
            return false;
        }

        String sql = "DELETE FROM student_remarks WHERE student_id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting student remark: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}