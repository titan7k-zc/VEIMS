
package voidbreaker.prolog;

import com.github.sarxos.webcam.Webcam;
import com.google.zxing.*;
import com.google.zxing.Reader;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.UnaryOperator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javafx.scene.control.TableColumn;
import javafx.scene.control.DatePicker;
import javafx.util.Duration;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.time.format.TextStyle;

import javafx.scene.control.TableCell;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;





import javafx.scene.control.SelectionMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;



import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
;

public class Home {

    //<editor-fold desc="FXML Declarations">
    // --- Common UI Controls ---
    @FXML
    private Button closewin;
    @FXML
    private Button minwin;
    @FXML
    private AnchorPane titlebar;
    @FXML
    private Button logoutwin;
    @FXML
    private Button refreshbtn;

    // --- Student Registration Tab ---
    @FXML
    private TextField stu_name_reg;
    @FXML
    private TextField stu_moib_reg;
    @FXML
    private ChoiceBox<String> stu_grade_reg;
    @FXML
    private RadioButton stu_gender1_reg;
    @FXML
    private ListView<String> stu_subjects_reg;
    @FXML
    private Button reg_stu_btn;
    @FXML
    private Label reg_status;
    @FXML
    private Label name_output;
    @FXML
    private Label grade_output;
    @FXML
    private Label gender_output;
    @FXML
    private Label id_output;
    @FXML
    private Text subjects_output;
    @FXML
    private ImageView qr_output;

    // --- Dashboard Tab ---
    @FXML
    private TableView<StudentRecord> dashboardTable;
    @FXML
    private TableColumn<StudentRecord, String> colId;
    @FXML
    private TableColumn<StudentRecord, String> colName;
    @FXML
    private TableColumn<StudentRecord, String> colGrade;
    @FXML
    private TableColumn<StudentRecord, String> colSubjects;
    @FXML
    private TextField dashboardtable_scarch;
    @FXML
    private TableView<SubjectInfoRecord> s_info_dash;
    @FXML
    private TableColumn<SubjectInfoRecord, String> si_grade;
    @FXML
    private TableColumn<SubjectInfoRecord, String> si_id;
    @FXML
    private TableColumn<SubjectInfoRecord, String> si_sub;
    @FXML
    private TableColumn<SubjectInfoRecord, String> si_tea;
    @FXML
    private TableColumn<SubjectInfoRecord, Number> si_fee;
    @FXML
    private Label tit_day;
    @FXML
    private Label tit_month;
    @FXML
    private Label tit_year;
    @FXML
    private Label iit_day;
    @FXML
    private Label iit_month;
    @FXML
    private Label iit_year;



    // unpaid students
    @FXML private TableView<UnpaidStudentRecord> unpaidTable;
    @FXML private TableColumn<UnpaidStudentRecord, String> ofr_id;
    @FXML private TableColumn<UnpaidStudentRecord, String> ofr_name;
    @FXML private TableColumn<UnpaidStudentRecord, String> ofr_grade;
    @FXML private TableColumn<UnpaidStudentRecord, String> ofr_lpd;
    @FXML private TableColumn<UnpaidStudentRecord, Number> ofr_um;
    @FXML private TableColumn<UnpaidStudentRecord, String> ofr_no;
    @FXML private TableColumn<UnpaidStudentRecord, String> ofr_lr;
    @FXML private TableColumn<UnpaidStudentRecord, String> ofr_rem;

    @FXML private Text od_id;
    @FXML private Text od_name;
    @FXML private Text od_grade;
    @FXML private Text od_con;
    @FXML private TextArea od_remark;
    @FXML private DatePicker od_dp;
    @FXML private Button db_unpaid_details_save_btn;

    @FXML private Label od_out;

    // --- Revenue Section ---

    // teacher revenue table
    @FXML
    private DatePicker dash_dp_tr;
    @FXML
    private TableView<RevenueRecord> tro_table;
    @FXML
    private TableColumn<RevenueRecord, String> tro_grade;
    @FXML
    private TableColumn<RevenueRecord, String> tro_sub;
    @FXML
    private TableColumn<RevenueRecord, String> tro_teacher;
    @FXML
    private TableColumn<RevenueRecord, String> tro_year;
    @FXML
    private TableColumn<RevenueRecord, String> tro_month;
    @FXML
    private TableColumn<RevenueRecord, String> tro_day;

    // institute revenue table
    @FXML
    private DatePicker dash_dp_in;
    @FXML
    private TableView<RevenueRecord> iro_table;
    @FXML
    private TableColumn<RevenueRecord, String> iro_grade;
    @FXML
    private TableColumn<RevenueRecord, String> iro_sub;
    @FXML
    private TableColumn<RevenueRecord, String> iro_teacher;
    @FXML
    private TableColumn<RevenueRecord, String> iro_year;
    @FXML
    private TableColumn<RevenueRecord, String> iro_month;
    @FXML
    private TableColumn<RevenueRecord, String> iro_day;

    private final ObservableList<RevenueRecord> teachersRevenueData = FXCollections.observableArrayList();
    private final ObservableList<RevenueRecord> instituteRevenueData = FXCollections.observableArrayList();
    private final ObservableList<SubjectInfoRecord> subjectInfoData = FXCollections.observableArrayList();

    // --- Student Profile Tab ---
    @FXML
    private TextField scarch_id_inp_sp;
    @FXML
    private RadioButton enable_qr;
    @FXML
    private ImageView qr_cam_field_sp;
    @FXML
    private Label name_output_sp;
    @FXML
    private Label grade_output_sp;
    @FXML
    private Label gender_output_sp;
    @FXML
    private Label mobnum_output_sp;
    @FXML
    private Label rdate_output_sp;
    @FXML
    private Label sp_main_result_out;
    @FXML
    private TextField fee_total_out;
    @FXML
    private DatePicker df_dp_inp_sp;

    @FXML
    private Label vCode_output_sp;
    @FXML
    private TextField vcode_inp_sp;
    @FXML
    private Button reg_rem_ver_btn;
    @FXML
    private Label ver_out_sp;

    // --- Tute Section ---
    @FXML
    private Button mark_tute_btn;


    // --- Fee Section ---
    @FXML
    private ListView<String> sub_select_fee;
    @FXML
    private TextField year_fee;
    @FXML
    private ChoiceBox<String> month_fee;
    @FXML
    private Button mark_fee_btn;
    @FXML
    private TableView<FeeRecord> fee_datatable;
    @FXML
    private TextField inp_year_fee;
    @FXML
    private RadioButton mk_m_fee_ind;
    @FXML
    private RadioButton mk_d_fee_ind;
    @FXML
    private RadioButton manual_fee_rb;

    // --- Attendance Section ---
    @FXML
    private RadioButton auto_att_switch;
    @FXML
    private ChoiceBox<String> grade_att;
    @FXML
    private ChoiceBox<String> sublist_att;
    @FXML
    private Button Att_mark_btn;
    @FXML
    private TableView<AttendanceRecord> attendance_datatable;
    @FXML
    private TextField inp_year_att;
    @FXML
    private ChoiceBox<String> month_att_rec;

    // -- Subject registration section --
    @FXML
    private ChoiceBox<String> grade_new_reg;
    @FXML
    private ChoiceBox<String> sublist_new_reg;
    @FXML
    private Button reg_new_sub_stu;
    @FXML
    private Label sp_sub_reg_result_out;

    // -- Subject unregistration section --
    @FXML
    private ChoiceBox<String> grade_new_unreg;
    @FXML
    private ChoiceBox<String> sublist_new_unreg;
    @FXML
    private Button unreg_new_sub_stu;
    @FXML
    private Label sp_sub_unreg_result_out;

    @FXML
    private Label admin_sp_out;

    // -- Subject registration info table --
    @FXML
    private TableView<SubjectRegistrationRecord> sub_reg_info;
    @FXML
    private TableColumn<SubjectRegistrationRecord, String> sub_reg_info_subject;
    @FXML
    private TableColumn<SubjectRegistrationRecord, String> sub_reg_info_date;
    @FXML
    private TableColumn<SubjectRegistrationRecord, String> sub_reg_info_subject_id;

    // -- student profile erase from db --
    @FXML
    private Button erase_stu_btn_sp;
    @FXML
    private Label erase_out_sp;

    // Add these FXML annotations to the class fields
    @FXML
    private TableView<DailyFeeRecord> daily_fee_table;
    @FXML
    private TextField inp_year_day_fee;
    @FXML
    private ChoiceBox<String> month_day_fee;

    // --- Configuration Tab ---
    @FXML
    private Label result_out_conf;
    @FXML
    private TextField new_grade_db_inp_conf;
    @FXML
    private TextField db_ip_out_conf;

    @FXML
    private ChoiceBox<String> grade_list_for_subject_inp_con;
    @FXML
    private TextField new_subject_db_inp_conf;
    @FXML
    private TextField new_subject_tn_inp_conf;

    @FXML
    private TextField new_subject_fee_inp_conf;

    @FXML
    private ChoiceBox<String> sub_fee_update_grade_conf;
    @FXML
    private ChoiceBox<String> sub_fee_update_sub_conf;
    @FXML
    private DatePicker sub_fee_update_date_conf;
    @FXML
    private TextField sub_fee_update_fee_conf;

    @FXML
    private Button apply_btn_conf;

    @FXML
    private ChoiceBox<String> conf_utn_grade;
    @FXML
    private ChoiceBox<String> conf_utn_sub;
    @FXML
    private TextField conf_utn_name;
    @FXML
    private Button apply_btn_conf2;
    @FXML
    private Label conf_out_2;

    @FXML
    private ChoiceBox<String> conf_rg_grade;
    @FXML
    private Button apply_btn_conf3;

    @FXML
    private ChoiceBox<String> conf_rs_grade;
    @FXML
    private ChoiceBox<String> conf_rs_sub;
    @FXML
    private Button apply_btn_conf4;

    @FXML
    private Label admin_conf_out;

    @FXML private Label cpu_usg;
    @FXML private Label ram_usg;
    @FXML private Label cpu_bal;
    @FXML private Label ram_bal;

    // grade subject students count table
    @FXML
    private TableView<GradeSubjectRecord> gradeAndSubjectStudentsDetails;
    @FXML
    private TableColumn<GradeSubjectRecord, String> gsr_subname;
    @FXML
    private TableColumn<GradeSubjectRecord, String> gsr_subid;
    @FXML
    private TableColumn<GradeSubjectRecord, String> gsr_grade;
    @FXML
    private TableColumn<GradeSubjectRecord, Number> gsr_subsc;

    @FXML
    private ChoiceBox<String> dash_gsr_grade_inp;
    @FXML
    private ChoiceBox<String> dash_gsr_sub_inp;
    @FXML
    private Button gsrAppltBtn;
    @FXML
    private TableView<StudentRecord> sub_stu_list_table;
    @FXML
    private TableColumn<StudentRecord, String> gsr_stuID;
    @FXML
    private TableColumn<StudentRecord, String> gsr_stuName;


    // clock
    @FXML
    private Label main_clock;

    //</editor-fold>

    // --- Class Variables ---
    private double xOffset = 0;
    private double yOffset = 0;
    private final DatabaseHandler dbHandler = new DatabaseHandler();
    private final ObservableList<StudentRecord> studentData = FXCollections.observableArrayList(); // dashboard table
    private String currentProfileStudentId = null; // To keep track of the currently viewed student
    private String initialGrade;
    private LocalDate registerDate;
    private String loggedInUser = null;
    private String loginTime = null;
    private String verificationCode;
    private boolean verified = false;


    // --- Grade Subject students Data ---
    private final ObservableList<GradeSubjectRecord> gradeSubjectData = FXCollections.observableArrayList();

    // --- Webcam QR Scanner ---
    private Webcam webcam = null;
    private boolean isCameraActive = false;
    private Task<Void> cameraTask;

    // -- indicators --
    boolean student_loaded = false;

    private boolean admin;

    public void setAdmin(boolean isAdmin) {
        this.admin = isAdmin;
        setAccess();
    }


    /**
     * Sets the logged-in user and records login time.
     */
    public void setLoggedInUser(String username) {
        this.loggedInUser = username;
        this.loginTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        logLoginEvent();
    }


    /**
     * Logs the login event to JSON file.
     */
    private void logLoginEvent() {
        File logDir = new File("C:\\ProgramData\\prolog\\logs");
        if (!logDir.exists()) {
            logDir.mkdirs();
        }
        String logFilePath = logDir.getPath() + "\\logdatas.json";

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        List<Map<String, String>> logEntries;
        try {
            if (Files.exists(Paths.get(logFilePath))) {
                logEntries = mapper.readValue(new File(logFilePath), mapper.getTypeFactory().constructCollectionType(List.class, Map.class));
            } else {
                logEntries = new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Map<String, String> newEntry = new HashMap<>();
        newEntry.put("username", loggedInUser);
        newEntry.put("loginTime", loginTime);
        newEntry.put("logoutTime", null);
        logEntries.add(newEntry);

        try {
            mapper.writeValue(new File(logFilePath), logEntries);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Logs the logout event by updating the logout time in the JSON file.
     */
    private void logLogoutEvent() {
        if (loggedInUser == null || loginTime == null) return;

        String logoutTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        File logFile = new File("C:\\ProgramData\\prolog\\logs\\logdatas.json");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        List<Map<String, String>> logEntries;
        try {
            if (Files.exists(Paths.get(logFile.getPath()))) {
                logEntries = mapper.readValue(logFile, mapper.getTypeFactory().constructCollectionType(List.class, Map.class));
            } else {
                logEntries = new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Find the latest entry for the current user and login time
        for (int i = logEntries.size() - 1; i >= 0; i--) {
            Map<String, String> entry = logEntries.get(i);
            if (entry.get("username").equals(loggedInUser) &&
                    entry.get("loginTime").equals(loginTime) &&
                    entry.get("logoutTime") == null) {
                entry.put("logoutTime", logoutTime);
                break;
            }
        }

        try {
            mapper.writeValue(logFile, logEntries);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    public void initialize() {

        //setAccess();
        setupWindowDraggable();
        studentGradeUpdate();
        deleteOldFeeRecords();
        setupDashboard();
        setupRegistrationTab();
        setupProfileTab();
        setupConfigurationTab();
        setupTeacherNameUpdate();
        setupSubjectRemoval();
        setupGradeRemoval();

        loadStudentsFromDB(); // Initial data load for dashboard

        setupSubjectInfoTable();
        loadSubjectInfoTable();

        setupUnpaidStudentsTable();
        loadUnpaidStudents();


        clock();

        // Add revenue table setup
        setupTeacherRevenueTable();
        dash_dp_tr.setValue(LocalDate.now());
        loadTeacherRevenueTable();

        // Add listener for DatePicker changes
        dash_dp_tr.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadTeacherRevenueTable();
            }
        });


        // Add institute revenue table setup
        setupInstituteRevenueTable();
        dash_dp_in.setValue(LocalDate.now());
        loadInstituteRevenueTable();

        // Add listener for DatePicker changes
        dash_dp_in.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadInstituteRevenueTable();
            }
        });


        // Setup logout button
        logoutwin.setOnAction(event -> logout());

        // setup refresh button
        refreshbtn.setOnAction(event -> refreshAll());


        //refressh datas every 5 seconds
        refreshDashboardTables();

        // Schedule every 30 seconds
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(5), e -> refreshDashboardTables())
        );
        timeline.setCycleCount(Timeline.INDEFINITE); // repeat forever
        timeline.play();


        // grade subject students count table setup
        setupGradeSubjectTable();
        loadGradeSubjectTable();
        gsr_stuID.setCellValueFactory(new PropertyValueFactory<>("id"));
        gsr_stuName.setCellValueFactory(new PropertyValueFactory<>("name"));
        sub_stu_list_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // Auto-adjust columns to fit width
        sub_stu_list_table.setSelectionModel(null); // Disable row selection if not needed
        // Load grades into the grade ChoiceBox
        loadGradesIntoChoiceBox(dash_gsr_grade_inp);

        // Listener to populate subjects when a grade is selected
        dash_gsr_grade_inp.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                dash_gsr_sub_inp.getItems().setAll(dbHandler.getSubjectsForGrade(newVal));
            } else {
                dash_gsr_sub_inp.getItems().clear();
            }
        });

        // Button action to load students into the table
        gsrAppltBtn.setOnAction(event -> {
            String selectedGrade = dash_gsr_grade_inp.getValue();
            String selectedSubject = dash_gsr_sub_inp.getValue();
            if (selectedGrade != null && selectedSubject != null) {
                List<StudentRecord> students = dbHandler.getStudentsForSubject(selectedGrade, selectedSubject);
                sub_stu_list_table.setItems(FXCollections.observableArrayList(students));
            } else {
                // Optionally clear the table or show an alert if inputs are missing
                sub_stu_list_table.getItems().clear();
            }
        });


        // save unpaid student details button action
        db_unpaid_details_save_btn.setOnAction(event -> {
            od_out.setText(""); // Reset label at the start
            String sid = od_id.getText();
            if (sid == null || sid.trim().isEmpty()) {
                od_out.setText("No student selected. Please select a student from the table.");
                od_out.setStyle("-fx-text-fill: red;");
                return;
            }
            String remark = od_remark.getText();
            LocalDate dpValue = od_dp.getValue();
            String lrd = (dpValue != null) ? dpValue.toString() : null;

            try {
                int rowsAffected = dbHandler.updateStudentRemarks(sid, remark, lrd);
                if (rowsAffected > 0) {
                    loadUnpaidStudents(); // Refresh the table after save
                    od_out.setText("Details saved successfully.");
                    od_out.setStyle("-fx-text-fill: green;");
                } else {
                    od_out.setText("No changes were made.");
                    od_out.setStyle("-fx-text-fill: orange;"); // Optional: different style for no change
                }
            } catch (Exception e) {
                e.printStackTrace();
                od_out.setText("Error saving details: " + e.getMessage());
                od_out.setStyle("-fx-text-fill: red;");
            }
        });

        // cpu ram usage
        Timeline resourceTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateResources()));
        resourceTimeline.setCycleCount(Timeline.INDEFINITE);
        resourceTimeline.play();


    }

    // =================================================================================
    // Input Validation Methods
    // =================================================================================

    /**
     * Validates and filters input for grade fields (only 2-digit integers allowed)
     */
    private void setupGradeInputValidation(TextField textField) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,2}")) {
                if (newText.length() == 2) {
                    try {
                        int value = Integer.parseInt(newText);
                        if (value >= 1 && value <= 99) {
                            return change;
                        }
                        System.out.println("Invalid grade: Must be between 01 and 99. Input: " + newText);
                        return null; // Reject values outside 01-99
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid grade: Must be a valid number. Input: " + newText);
                        return null; // Reject non-numeric input
                    }
                }
                return change; // Allow partial input (0 or 1 digit) for typing
            }
            System.out.println("Invalid input for grade: Only 2-digit integers are allowed. Input: " + newText);
            return null; // Reject invalid input
        };
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        textField.setTextFormatter(textFormatter);
    }

    /**
     * Validates and filters input for fee amount fields (only doubles allowed)
     */
    private void setupFeeInputValidation(TextField textField) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*\\.?\\d*")) {
                return change;
            }
            System.out.println("Invalid input for fee amount: Only decimal numbers are allowed. Input: " + newText);
            return null; // Reject invalid input
        };
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        textField.setTextFormatter(textFormatter);
    }

    /**
     * Validates and filters input for mobile number (only integers, 0-10 digits)
     */
    private void setupMobileInputValidation(TextField textField) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,10}")) {
                return change; // Allow 0-12 digits
            }
            System.out.println("Invalid input for mobile: Only digits are allowed. Input: " + newText);
            return null; // Reject invalid input
        };
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        textField.setTextFormatter(textFormatter);
    }

    /**
     * Validates and filters input for year fields (only 4-digit integers)
     */
    private void setupYearInputValidation(TextField textField) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,4}")) {
                if (newText.length() == 4 && !newText.matches("19\\d{2}|20\\d{2}")) {
                    System.out.println("Invalid year: Must be a valid 4-digit year (1900-2099). Input: " + newText);
                    return null; // Reject invalid year
                }
                return change;
            }
            System.out.println("Invalid input for year: Only 4 digits are allowed. Input: " + newText);
            return null; // Reject invalid input
        };
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        textField.setTextFormatter(textFormatter);
    }

    /**
     * Validates and filters input for name field (only letters and spaces)
     */
    private void setupNameInputValidation(TextField textField) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[a-zA-Z\\s]*")) {
                return change;
            }
            System.out.println("Invalid input for name: Only letters and spaces are allowed. Input: " + newText);
            return null; // Reject invalid input
        };
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        textField.setTextFormatter(textFormatter);
    }

    // =================================================================================
    // Initial Setup Methods
    // =================================================================================

    private void setupWindowDraggable() {
        titlebar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        titlebar.setOnMouseDragged(event -> {
            Stage stage = (Stage) titlebar.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    private void setupDashboard() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colGrade.setCellValueFactory(new PropertyValueFactory<>("grade"));

        // Create a FilteredList to wrap the studentData
        FilteredList<StudentRecord> filteredData = new FilteredList<>(studentData, p -> true);

        // Add listener to the search TextField
        dashboardtable_scarch.textProperty().addListener((obs, oldValue, newValue) -> {
            filteredData.setPredicate(student -> {
                // If search field is empty, show all students
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }

                // Convert search text to lowercase for case-insensitive search
                String lowerCaseFilter = newValue.toLowerCase();

                // Check if student ID or name contains the search text
                if (student.getId().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                if (student.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false; // Does not match
            });
        });

        // Set the filtered data to the TableView
        dashboardTable.setItems(filteredData);
        dashboardTable.setSelectionModel(null); // Disable selection

    }

    // registration tab setup
    private void setupRegistrationTab() {
        stu_subjects_reg.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        loadGradesIntoChoiceBox(stu_grade_reg);

        // Setup input validation for registration fields
        setupNameInputValidation(stu_name_reg);
        setupMobileInputValidation(stu_moib_reg);

        // When a grade is selected, update the available subjects
        stu_grade_reg.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                stu_subjects_reg.getItems().setAll(dbHandler.getSubjectsForGrade(newVal));
            }
        });
        reg_stu_btn.setOnAction(event -> registerStudent());
    }

    /**
     * Handles the student registration process.
     */
    private void registerStudent() {
        String name = stu_name_reg.getText().trim();
        String mobile = stu_moib_reg.getText().trim();
        String grade = stu_grade_reg.getValue();
        String gender = stu_gender1_reg.isSelected() ? "Male" : "Female";
        ObservableList<String> selectedSubjects = stu_subjects_reg.getSelectionModel().getSelectedItems();

        if (name.isEmpty() || mobile.isEmpty() || grade == null || selectedSubjects.isEmpty()) {
            setStatusLabel(reg_status, "Please fill out all fields.", "red");
            clearRegistrationOutputs();
            return;
        }
        if (mobile.length() != 9 && mobile.length() != 10) {
            setStatusLabel(reg_status, "Please enter a valid mobile number.", "red");
            clearRegistrationOutputs();
            return;
        }

        String studentId = generateNextStudentId();
        String registerDate = LocalDate.now().toString();

        List<String> subjectList = new ArrayList<>(selectedSubjects);
        dbHandler.insertStudent(studentId, name, grade, gender, mobile, registerDate, subjectList);

        loadStudentsFromDB(); // Refresh dashboard
        loadUnpaidStudents();


        setStatusLabel(reg_status, "Registration Successful.", "#129a00");
        name_output.setText(name);
        grade_output.setText(grade);
        gender_output.setText(gender);
        subjects_output.setText(String.join(", ", subjectList));
        id_output.setText(studentId);

        generateAndDisplayQRCode(studentId);

        // Create the TXT file with student details
        File qrDir = new File("C:\\Prolog\\Students\\");
        String txtPath = qrDir.getPath() + "\\" + studentId + ".txt";
        try (PrintWriter writer = new PrintWriter(new File(txtPath))) {
            writer.println("Student ID - " + studentId);
            writer.println("Student Name - " + name);
            writer.println("Gender - " + gender);
            writer.println("Contact Number - " + mobile);
            writer.println("Registered Date - " + registerDate);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            setStatusLabel(reg_status, "Failed to create student details file.", "red");
            return; // Optional: you can continue even if TXT fails, but here we stop for safety
        }

        // Zip the PNG and TXT into a single ZIP file
        String pngPath = qrDir.getPath() + "\\" + studentId + ".png";
        String zipPath = qrDir.getPath() + "\\" + studentId + ".zip";
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath))) {
            // Add PNG to ZIP
            File pngFile = new File(pngPath);
            ZipEntry pngEntry = new ZipEntry(studentId + ".png");
            zos.putNextEntry(pngEntry);
            Files.copy(pngFile.toPath(), zos);
            zos.closeEntry();

            // Add TXT to ZIP
            File txtFile = new File(txtPath);
            ZipEntry txtEntry = new ZipEntry(studentId + ".txt");
            zos.putNextEntry(txtEntry);
            Files.copy(txtFile.toPath(), zos);
            zos.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
            setStatusLabel(reg_status, "Failed to create ZIP file.", "red");
            return; // Optional: continue if zipping fails
        }

        // Delete the separate PNG and TXT files after zipping
        new File(pngPath).delete();
        new File(txtPath).delete();

        clearRegistrationInputs();
    }

    // student profile tab setup
    private void setupProfileTab() {
        // --- Common ---
        loadGradesIntoChoiceBox(grade_att);
        grade_att.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                sublist_att.getItems().setAll(dbHandler.getSubjectsForGrade(newVal));
            }
        });

        // switch focus to subject list after selecting grade
        grade_att.setOnAction(e -> {
            sublist_att.requestFocus();
        });

        // --- Student Search ---
        scarch_id_inp_sp.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                // Always convert to uppercase
                String upperCaseVal = newVal.toUpperCase();

                if (!upperCaseVal.equals(newVal)) {
                    scarch_id_inp_sp.setText(upperCaseVal);
                    return; // Prevent infinite loop
                }
                if (upperCaseVal.length() == 8) {
                    loadStudentProfile(scarch_id_inp_sp.getText().trim());
                    if (auto_att_switch.isSelected()) {
                        markAttendance(upperCaseVal, true);
                    }
                } else if (upperCaseVal.length() > 8) {
                    // Keep only characters after the first 8
                    String remaining = upperCaseVal.substring(8);
                    scarch_id_inp_sp.setText(remaining);
                }

            }
        });

        // --- QR Scanner ---
        enable_qr.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if (isNowSelected) {
                startCamera();
            } else {
                stopCamera();
            }
        });


        // --- Fee marking Section ---
        sub_select_fee.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        sub_select_fee.getSelectionModel().getSelectedItems().addListener((ListChangeListener<String>) change -> {
            updateFeeTotal();
        });

        fee_total_out.setDisable(true);
        year_fee.setDisable(false);
        month_fee.setDisable(false);
        df_dp_inp_sp.setDisable(true);

        // Setup radio button toggle group for mk_m_fee_ind and mk_d_fee_ind
        ToggleGroup feeModeGroup = new ToggleGroup();
        mk_m_fee_ind.setToggleGroup(feeModeGroup);
        mk_d_fee_ind.setToggleGroup(feeModeGroup);

        // Listener for feeModeGroup (mk_m_fee_ind and mk_d_fee_ind)
        feeModeGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == mk_d_fee_ind) {
                sub_select_fee.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                // Clear multiple selections if any
                if (sub_select_fee.getSelectionModel().getSelectedItems().size() > 1) {
                    String firstSelected = sub_select_fee.getSelectionModel().getSelectedItems().get(0);
                    sub_select_fee.getSelectionModel().clearSelection();
                    sub_select_fee.getSelectionModel().select(firstSelected);
                }
                year_fee.setDisable(true);
                month_fee.setDisable(true);
                mark_tute_btn.setDisable(true);
                df_dp_inp_sp.setDisable(false);
            } else if (newToggle == mk_m_fee_ind) {
                sub_select_fee.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                year_fee.setDisable(false);
                month_fee.setDisable(false);
                df_dp_inp_sp.setDisable(true);
                mark_tute_btn.setDisable(false);
            }

            // If manual_fee_rb is selected, override to single selection
            if (manual_fee_rb.isSelected()) {
                sub_select_fee.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                // Clear multiple selections if any
                if (sub_select_fee.getSelectionModel().getSelectedItems().size() > 1) {
                    String firstSelected = sub_select_fee.getSelectionModel().getSelectedItems().get(0);
                    sub_select_fee.getSelectionModel().clearSelection();
                    sub_select_fee.getSelectionModel().select(firstSelected);
                }
            }

            updateFeeTotal();
        });

        // Listener for manual_fee_rb
        manual_fee_rb.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                sub_select_fee.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                fee_total_out.setDisable(false);
                // Clear multiple selections if any
                if (sub_select_fee.getSelectionModel().getSelectedItems().size() > 1) {
                    String firstSelected = sub_select_fee.getSelectionModel().getSelectedItems().get(0);
                    sub_select_fee.getSelectionModel().clearSelection();
                    sub_select_fee.getSelectionModel().select(firstSelected);
                }
            } else {
                // Revert to the selection mode dictated by feeModeGroup
                if (feeModeGroup.getSelectedToggle() == mk_d_fee_ind) {
                    sub_select_fee.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                } else {
                    sub_select_fee.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                }
                fee_total_out.setDisable(true);
            }

            updateFeeTotal();
        });

        ObservableList<String> months = FXCollections.observableArrayList(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December");

        month_fee.getItems().setAll(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        );

        // Setup input validation for fee and year fields
        setupYearInputValidation(year_fee);
        setupYearInputValidation(inp_year_fee);
        setupYearInputValidation(inp_year_att);
        setupFeeInputValidation(new_subject_fee_inp_conf);
        setupFeeInputValidation(sub_fee_update_fee_conf);
        setupFeeInputValidation(fee_total_out);

        year_fee.textProperty().addListener((obs, oldVal, newVal) -> {
            if (student_loaded && currentProfileStudentId != null) {
                updateSubSelectFee(newVal);
                updateFeeTotal();
            }
        });

        // Modified button action to call different methods based on radio button states
        mark_fee_btn.setOnAction(event -> {
            if ((mk_m_fee_ind.isSelected() || mk_d_fee_ind.isSelected()) && !manual_fee_rb.isSelected()) {
                markFees();
            } else if ((mk_m_fee_ind.isSelected() || mk_d_fee_ind.isSelected()) && manual_fee_rb.isSelected()) {
                markFees();

            }
        });

        // mark tute marking button action
        mark_tute_btn.setOnAction(event -> {
            marktute();
        });


        // -- fee table section ---
        inp_year_fee.setText(String.valueOf(LocalDate.now().getYear()));
        inp_year_fee.setOnAction(e -> {
            if (currentProfileStudentId != null) {
                loadFeeTable(currentProfileStudentId, Integer.parseInt(inp_year_fee.getText()));
            }
        });
        setupFeeTable();


        // --- Daily Fee Section ---
        month_day_fee.setItems(months);
        month_day_fee.getSelectionModel().select(LocalDate.now().getMonthValue() - 1); // Set default to current month
        inp_year_day_fee.setText(String.valueOf(LocalDate.now().getYear())); // Set default to current year
        setupDailyFeeTable();
        // Setup input validation for year field (mirrors inp_year_att)
        setupYearInputValidation(inp_year_day_fee);

        // Trigger table load on Enter key for year
        inp_year_day_fee.setOnAction(e -> {
            if (currentProfileStudentId != null && student_loaded) {
                try {
                    int year = Integer.parseInt(inp_year_day_fee.getText());
                    int month = month_day_fee.getSelectionModel().getSelectedIndex() + 1;
                    loadDailyFeeTable(currentProfileStudentId, year, month);
                } catch (NumberFormatException ex) {
                    setStatusLabel(sp_main_result_out, "Invalid year format.", "red");
                }
            }
        });

        // Trigger table load on month selection
        month_day_fee.setOnAction(e -> {
            if (currentProfileStudentId != null && student_loaded) {
                try {
                    int year = Integer.parseInt(inp_year_day_fee.getText());
                    int month = month_day_fee.getSelectionModel().getSelectedIndex() + 1;
                    loadDailyFeeTable(currentProfileStudentId, year, month);
                } catch (NumberFormatException ex) {
                    setStatusLabel(sp_main_result_out, "Invalid year format.", "red");
                }
            }
        });


        // --- Attendance Section ---
        month_att_rec.setItems(months);
        month_att_rec.getSelectionModel().select(LocalDate.now().getMonthValue() - 1);
        inp_year_att.setText(String.valueOf(LocalDate.now().getYear()));
        setupAttendanceTable();

        inp_year_att.setOnAction(e -> {
            if (currentProfileStudentId != null) {
                int year = Integer.parseInt(inp_year_att.getText());
                int month = month_att_rec.getSelectionModel().getSelectedIndex() + 1;
                loadAttendanceTable(currentProfileStudentId, year, month);
            }
        }); // Trigger search on Enter key

        month_att_rec.setOnAction(e -> {
            if (currentProfileStudentId != null) {
                int year = Integer.parseInt(inp_year_att.getText());
                int month = month_att_rec.getSelectionModel().getSelectedIndex() + 1;
                loadAttendanceTable(currentProfileStudentId, year, month);
            }
        }); // Trigger search on Enter key

        Att_mark_btn.setOnAction(e -> markAttendance(currentProfileStudentId, false));

        // --- Subject Registration Section ---
        loadGradesIntoChoiceBox(grade_new_reg);

        grade_new_reg.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && currentProfileStudentId != null && student_loaded) {
                List<String> allSubjects = dbHandler.getSubjectsForGrade(newVal);
                List<String> registeredSubjects = dbHandler.getStudentSubjectsForGrade(currentProfileStudentId, newVal);
                allSubjects.removeAll(registeredSubjects);
                sublist_new_reg.getItems().setAll(allSubjects);
            } else {
                sublist_new_reg.getItems().clear();
            }
        });

        // new subject registration button action
        reg_new_sub_stu.setOnAction(e -> {
            if (!student_loaded) {
                setStatusLabel(sp_sub_reg_result_out, "No student profile loaded.", "red");
                return;
            } else {
                String reg_grade = grade_new_reg.getValue();
                String reg_subject = sublist_new_reg.getValue();

                if (reg_grade == null || reg_subject == null) {
                    setStatusLabel(sp_sub_reg_result_out, "Please select both grade and subject!", "red");
                    return;
                }

                boolean status = dbHandler.insertStudentToSubject(
                        reg_grade.trim(),
                        reg_subject.trim(),
                        currentProfileStudentId,
                        LocalDate.now().toString()
                );
                if (status) {
                    setStatusLabel(sp_sub_reg_result_out, "Subject added successfully.", "green");
                    // Refresh profile
                    clearProfileOutputs();
                    loadStudentProfile(currentProfileStudentId);
                } else {
                    setStatusLabel(sp_sub_reg_result_out, "Failed to add subject (may already be registered).", "red");
                }
            }
        });

        // subject section
        setupSubjectRegistrationTable();

        // erase student
        erase_stu_btn_sp.setOnAction(e -> eraseStudent());

        // --- Subject Unregistration Section ---
        loadGradesIntoChoiceBox(grade_new_unreg);

        grade_new_unreg.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && currentProfileStudentId != null && student_loaded) {
                List<String> registeredSubjects = dbHandler.getStudentSubjectsForGrade(currentProfileStudentId, newVal);
                sublist_new_unreg.getItems().setAll(registeredSubjects);
            } else {
                sublist_new_unreg.getItems().clear();
            }
        });

        unreg_new_sub_stu.setOnAction(event -> {
            if (currentProfileStudentId == null || !student_loaded) {
                setStatusLabel(sp_sub_unreg_result_out, "No student profile loaded.", "red");
                return;
            }
            String grade = grade_new_unreg.getValue();
            String subject = sublist_new_unreg.getValue();
            if (grade == null || subject == null) {
                setStatusLabel(sp_sub_unreg_result_out, "Please select grade and subject.", "red");
                return;
            }
            boolean success = dbHandler.unregisterSubject(currentProfileStudentId, grade, subject);
            if (success) {
                setStatusLabel(sp_sub_unreg_result_out, "Subject unregistered successfully.", "green");
                // Refresh sub_reg_info table
                loadSubjectRegistrationTable(currentProfileStudentId);
                // Refresh sublist_new_unreg to reflect the unregistration
                String selectedGrade = grade_new_unreg.getValue();
                if (selectedGrade != null) {
                    List<String> updatedRegisteredSubjects = dbHandler.getStudentSubjectsForGrade(currentProfileStudentId, selectedGrade);
                    sublist_new_unreg.getItems().setAll(updatedRegisteredSubjects);
                }
                // Refresh fee and attendance tables
                int year = inp_year_fee.getText().isEmpty() ? LocalDate.now().getYear() : Integer.parseInt(inp_year_fee.getText());
                int month = month_att_rec.getValue() != null
                        ? Month.valueOf(month_att_rec.getValue().toUpperCase()).getValue()
                        : LocalDate.now().getMonthValue();
                loadFeeTable(currentProfileStudentId, year);
                loadAttendanceTable(currentProfileStudentId, year, month);
                loadDailyFeeTable(currentProfileStudentId, year, month);
                // Also refresh sub_select_fee if needed (for current year)
                updateSubSelectFee(year_fee.getText());

                // Refresh profile to reflect changes
                clearProfileOutputs();
                loadStudentProfile(currentProfileStudentId);
            } else {
                setStatusLabel(sp_sub_unreg_result_out, "Failed to unregister subject.", "red");
            }
        });

        // disable unreg and erase section initially
        eraseAndUnregsection(false);
    }

    // configuration tab setup
    private void setupConfigurationTab() {
        showDatabaseIP();
        loadGradesIntoChoiceBox(grade_list_for_subject_inp_con);
        loadGradesIntoChoiceBox(sub_fee_update_grade_conf);

        // Setup input validation for configuration fields
        setupGradeInputValidation(new_grade_db_inp_conf);

        // Add listener to load subjects when grade is selected
        sub_fee_update_grade_conf.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                sub_fee_update_sub_conf.getItems().setAll(dbHandler.getSubjectsForGrade(newValue));
            } else {
                sub_fee_update_sub_conf.getItems().clear();
            }
        });

        apply_btn_conf.setOnAction(e -> applyConfigurationChanges());
    }

    private void setupTeacherNameUpdate() {
        // Load grades into the grade ChoiceBox
        loadGradesIntoChoiceBox(conf_utn_grade);

        // Listener to populate subjects when a grade is selected
        conf_utn_grade.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                conf_utn_sub.getItems().setAll(dbHandler.getSubjectsForGrade(newVal));
                conf_utn_sub.getSelectionModel().clearSelection(); // Clear subject selection on grade change
            } else {
                conf_utn_sub.getItems().clear();
            }
        });

        // Setup input validation for teacher name (letters and spaces only, similar to name validation)
        setupNameInputValidation(conf_utn_name);

        // Button action to apply changes
        apply_btn_conf2.setOnAction(event -> updateTeacherName());
    }

    private void updateTeacherName() {
        String grade = conf_utn_grade.getValue();
        String subject = conf_utn_sub.getValue();
        String newTeacherName = conf_utn_name.getText().trim();

        // Input validation
        if (grade == null || subject == null || newTeacherName.isEmpty()) {
            setStatusLabel(conf_out_2, "Please select grade, subject, and enter a new teacher name.", "red");
            return;
        }

        // Call DB update
        boolean success = dbHandler.updateTeacherName(grade, subject, newTeacherName);
        if (success) {
            setStatusLabel(conf_out_2, "Teacher name updated successfully for " + subject + " in " + grade + ".", "#129a00"); // Green
            // Optionally refresh related tables that display teacher names (e.g., revenue, subject info)
            loadTeacherRevenueTable();
            loadInstituteRevenueTable();
            loadUnpaidStudents();
            loadSubjectInfoTable();
            // Clear inputs for next use
            conf_utn_grade.getSelectionModel().clearSelection();
            conf_utn_sub.getItems().clear();
            conf_utn_name.clear();
        } else {
            setStatusLabel(conf_out_2, "Failed to update teacher name. Check if subject exists or try again.", "red");
        }
    }

    /**
     * Sets up the teacher revenue table (tro_table) with column bindings.
     */
    private void setupTeacherRevenueTable() {
        tro_grade.setCellValueFactory(new PropertyValueFactory<>("grade"));
        tro_sub.setCellValueFactory(new PropertyValueFactory<>("subject"));
        tro_teacher.setCellValueFactory(new PropertyValueFactory<>("teacher"));
        tro_year.setCellValueFactory(new PropertyValueFactory<>("yearIncome"));
        tro_month.setCellValueFactory(new PropertyValueFactory<>("monthIncome"));
        tro_day.setCellValueFactory(new PropertyValueFactory<>("dayIncome"));

        tro_table.setItems(teachersRevenueData);
        tro_table.setSelectionModel(null); // Disable selection
        tro_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void setupInstituteRevenueTable() {
        iro_grade.setCellValueFactory(new PropertyValueFactory<>("grade"));
        iro_sub.setCellValueFactory(new PropertyValueFactory<>("subject"));
        iro_teacher.setCellValueFactory(new PropertyValueFactory<>("teacher"));
        iro_year.setCellValueFactory(new PropertyValueFactory<>("yearIncome"));
        iro_month.setCellValueFactory(new PropertyValueFactory<>("monthIncome"));
        iro_day.setCellValueFactory(new PropertyValueFactory<>("dayIncome"));

        iro_table.setItems(instituteRevenueData);
        iro_table.setSelectionModel(null); // Disable selection
        iro_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    // =================================================================================
    // UI Actions and Event Handlers
    // =================================================================================

    @FXML
    private void closeApp() {
        stopCamera();
        Stage stage = (Stage) closewin.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void minimizeApp() {
        Stage stage = (Stage) minwin.getScene().getWindow();
        stage.setIconified(true);
    }

    /**
     * Handles the logout action.
     */
    private void logout() {
        logLogoutEvent();
        Main.changeScene("/login.fxml", 900, 600);
    }

    public void studentGradeUpdate() {
        DatabaseHandler db = new DatabaseHandler();
        db.updateStudentGradesByRegistrationDate();
    }

    private void loadStudentsFromDB() {
        studentData.setAll(dbHandler.getAllStudents());
    }

    private void loadStudentProfile(String studentId) {
        student_loaded = false;

        if (studentId == null || studentId.isEmpty()) {
            setStatusLabel(sp_main_result_out, "Please enter a Student ID.", "red");
            return;
        }

        Map<String, String> studentDetails = dbHandler.getStudentById(studentId);
        if (studentDetails.isEmpty()) {
            setStatusLabel(sp_main_result_out, "Student ID not found.", "red");
            clearProfileOutputs();
            currentProfileStudentId = null;
            return;
        }
        student_loaded = true;

        currentProfileStudentId = studentId;
        scarch_id_inp_sp.setText(studentId);
        name_output_sp.setText(studentDetails.get("name"));
        grade_output_sp.setText(studentDetails.get("grade"));
        gender_output_sp.setText(studentDetails.get("gender"));
        mobnum_output_sp.setText(studentDetails.get("mobile"));
        rdate_output_sp.setText(studentDetails.get("register_date"));

        // Store initial grade and register date for grade calculations
        initialGrade = studentDetails.get("initial_grade");
        registerDate = LocalDate.parse(studentDetails.get("register_date"));

        // Load related data
        String currentGrade = studentDetails.get("grade");
        List<String> studentSubjects = dbHandler.getStudentSubjectsForGrade(studentId, currentGrade); // Filtered for current grade
        sub_select_fee.getItems().setAll(studentSubjects);
        updateFeeTotal();

        // Load tables with current year/month data
        int currentYear = LocalDate.now().getYear();
        int currentMonth = LocalDate.now().getMonthValue();
        int year = Integer.parseInt(inp_year_fee.getText());
        loadFeeTable(studentId, year);
        loadAttendanceTable(studentId, currentYear, currentMonth);
        loadDailyFeeTable(studentId, year, currentMonth); // For daily fee table

        year_fee.setText(String.valueOf(currentYear));
        df_dp_inp_sp.setValue(LocalDate.now());
        month_fee.setValue(Month.of(currentMonth).name());


        // same year synchronization between year_fee and df_dp_inp_sp
        year_fee.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    int newYear = Integer.parseInt(newValue);
                    LocalDate currentDate = df_dp_inp_sp.getValue();
                    int currentYearInDate = (currentDate != null) ? currentDate.getYear() : -1;

                    if (newYear != currentYearInDate) {
                        if (currentDate != null) {
                            df_dp_inp_sp.setValue(currentDate.withYear(newYear));
                        } else {
                            // If no date selected, default to January 1 of the new year
                            df_dp_inp_sp.setValue(LocalDate.of(newYear, 1, 1));
                        }
                    }
                } catch (NumberFormatException e) {
                    // Ignore invalid input; do not update the date picker
                } catch (Exception e) {
                    // Handle any other exceptions, e.g., invalid year range; optionally revert text
                    year_fee.setText(oldValue);
                }
            }
        });

        df_dp_inp_sp.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                String currentText = year_fee.getText();
                int textYear = -1;
                try {
                    textYear = Integer.parseInt(currentText);
                } catch (NumberFormatException e) {
                    // Ignore invalid text
                }

                if (newValue != null) {
                    // update year
                    int newYear = newValue.getYear();
                    if (newYear != textYear) {
                        year_fee.setText(String.valueOf(newYear));
                    }

                    // update month
                    int newMonth = newValue.getMonthValue(); // 1 - 12
                    month_fee.setValue(Month.of(newMonth).name());
                    // 👉 This will give FULL UPPERCASE month (e.g., "JANUARY").
                    // If you want "January", use: newValue.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH)

                } else {
                    // If date is cleared
                    year_fee.setText("");
                    month_fee.setValue(null); // or "" depending on control type
                }
            }
        });

        month_fee.valueProperty().addListener((obs, oldMonth, newMonth) -> {
            if (newMonth != null && df_dp_inp_sp.getValue() != null) {
                LocalDate currentDate = df_dp_inp_sp.getValue();

                // Convert string month name back to Month enum
                Month selectedMonth;
                try {
                    selectedMonth = Month.valueOf(newMonth.toUpperCase()); // works if items are "JANUARY", "FEBRUARY" ...
                } catch (IllegalArgumentException e) {
                    // if you are using "January", "February", ... use this instead:
                    selectedMonth = Month.valueOf(newMonth.toUpperCase(Locale.ENGLISH));
                }

                // Keep same year & day, just change month (adjust day if invalid, e.g. 31-Feb → 28-Feb)
                int dayOfMonth = Math.min(currentDate.getDayOfMonth(), selectedMonth.length(currentDate.isLeapYear()));
                LocalDate updatedDate = LocalDate.of(currentDate.getYear(), selectedMonth, dayOfMonth);

                df_dp_inp_sp.setValue(updatedDate);
            }
        });





        setStatusLabel(sp_main_result_out, "Student profile loaded.", "green");

        // Load subject registration info
        loadSubjectRegistrationTable(studentId);

        // Clear subject registration/unregistration selections and messages
        clearLablesForProfile();


        // verification part
        verified = false;
        eraseAndUnregsection(false);
        verificationCode = vCode();
        vCode_output_sp.setText(verificationCode);

        reg_rem_ver_btn.setOnAction(e -> {
            verified = verifyuser();
            if (verified) {
                eraseAndUnregsection(true);
            } else {
                eraseAndUnregsection(false);
            }
        });

    }

    private void updateSubSelectFee(String yearStr) {
        if (initialGrade == null || registerDate == null || currentProfileStudentId == null) {
            setStatusLabel(sp_main_result_out, "Missing student registration data.", "red");
            sub_select_fee.getItems().clear();
            return;
        }

        int year;
        try {
            year = Integer.parseInt(yearStr);
        } catch (NumberFormatException e) {
            sub_select_fee.getItems().clear();
            return;
        }

        int regYear = registerDate.getYear();
        int gradeNum;
        try {
            gradeNum = Integer.parseInt(initialGrade) + (year - regYear);
        } catch (NumberFormatException e) {
            setStatusLabel(sp_main_result_out, "Invalid initial grade format.", "red");
            sub_select_fee.getItems().clear();
            return;
        }

        String gradeName;
        if (gradeNum > 13) {
            gradeName = "completed";
        } else if (gradeNum < 1) {
            gradeName = "1";
        } else {
            gradeName = String.valueOf(gradeNum);
        }

        List<String> studentSubjects = dbHandler.getStudentSubjectsForGrade(currentProfileStudentId, gradeName);
        sub_select_fee.getItems().setAll(studentSubjects);
    }

    /**
     * Handles marking fees for the selected subjects and month.
     */
    private void markFees() {
        if (currentProfileStudentId == null || !student_loaded) {
            setStatusLabel(sp_main_result_out, "No student profile loaded.", "red");
            return;
        }

        ObservableList<String> selectedSubjects = sub_select_fee.getSelectionModel().getSelectedItems();
        if (selectedSubjects.isEmpty()) {
            setStatusLabel(sp_main_result_out, "Please select at least one subject.", "red");
            return;
        }

        String yearStr = year_fee.getText();
        if (yearStr.isEmpty()) {
            setStatusLabel(sp_main_result_out, "Please enter a year.", "red");
            return;
        }

        int year;
        try {
            year = Integer.parseInt(yearStr);
        } catch (NumberFormatException e) {
            setStatusLabel(sp_main_result_out, "Invalid year format.", "red");
            return;
        }

        String monthStr = month_fee.getValue();
        if (monthStr == null) {
            setStatusLabel(sp_main_result_out, "Please select a month.", "red");
            return;
        }

        int month = Month.valueOf(monthStr.toUpperCase()).getValue();
        int regYear = registerDate.getYear();
        int gradeNum;
        try {
            gradeNum = Integer.parseInt(initialGrade) + (year - regYear);
        } catch (NumberFormatException e) {
            setStatusLabel(sp_main_result_out, "Invalid initial grade format.", "red");
            return;
        }

        // for daily fee
        LocalDate selectedDate = df_dp_inp_sp.getValue();
        int yeard = selectedDate.getYear();
        int monthd = selectedDate.getMonthValue();
        int dayd = selectedDate.getDayOfMonth();

        String studentGrade;
        if (gradeNum > 13) {
            studentGrade = "completed";
        } else if (gradeNum < 1) {
            studentGrade = "1";
        } else {
            studentGrade = String.valueOf(gradeNum);
        }

        boolean allSuccess = true;
        List<String> successfulSubjects = new ArrayList<>();
        for (String subjectName : selectedSubjects) {
            double fee;

            if (manual_fee_rb.isSelected()) {
                fee = fee_total_out.getText().isEmpty() ? 0.0 : Double.parseDouble(fee_total_out.getText());

            } else {
                fee = 0.0;
            }

            String result;
            if (mk_m_fee_ind.isSelected()) {
                result = dbHandler.markFee(currentProfileStudentId, subjectName, studentGrade, year, month, fee);

            } else {
                result = dbHandler.markDailyFee(currentProfileStudentId, subjectName, studentGrade, yeard, monthd, dayd, fee);
            }

            if (result.startsWith("Fee marked")) {
                successfulSubjects.add(subjectName);
            } else {
                allSuccess = false;
            }
        }

        if (allSuccess) {
            setStatusLabel(sp_main_result_out, "Fees marked successfully for selected subjects.", "green");
        } else {
            setStatusLabel(sp_main_result_out, "Some fees could not be marked.", "red");
        }

        if (!successfulSubjects.isEmpty()) {
            String studentName = dbHandler.getStudentById(currentProfileStudentId).get("name");
            generateReceipt(currentProfileStudentId, year, month, successfulSubjects, studentName, studentGrade);
        }




        feeAndTuteRefresh(year, month);

    }

    private void marktute(){
        if (currentProfileStudentId == null) {
            setStatusLabel(sp_main_result_out, "No student profile loaded.", "red");
            return;
        }

        ObservableList<String> selectedSubjects = sub_select_fee.getSelectionModel().getSelectedItems();
        if (selectedSubjects.isEmpty()) {
            setStatusLabel(sp_main_result_out, "No subjects selected.", "red");
            return;
        }

        String yearStr = year_fee.getText().trim();
        String monthStr = month_fee.getValue();
        if (yearStr.isEmpty() || monthStr == null) {
            setStatusLabel(sp_main_result_out, "Year and month required.", "red");
            return;
        }

        int year;
        try {
            year = Integer.parseInt(yearStr);
        } catch (NumberFormatException e) {
            setStatusLabel(sp_main_result_out, "Invalid year.", "red");
            return;
        }

        int month = Month.valueOf(monthStr.toUpperCase()).getValue();

        int regYear = registerDate.getYear();
        int gradeNum;
        try {
            gradeNum = Integer.parseInt(initialGrade) + (year - regYear);
        } catch (NumberFormatException e) {
            setStatusLabel(sp_main_result_out, "Invalid initial grade format.", "red");
            return;
        }


            String studentGrade;
        if (gradeNum > 13) {
            studentGrade = "completed";
        } else if (gradeNum < 1) {
            studentGrade = "1";
        } else {
            studentGrade = String.valueOf(gradeNum);
        }
        StringBuilder result = new StringBuilder();
        for (String subject : selectedSubjects) {
            String status = dbHandler.markTute(currentProfileStudentId, subject, studentGrade, year, month);
            result.append(status).append("\n");
        }

        setStatusLabel(sp_main_result_out, result.toString(), "green");

        feeAndTuteRefresh(year, month);
    }

    /**
     * Handles marking attendance, either manually or via auto-switch.
     *
     * @param studentId The ID of the student.
     * @param isAuto    A flag to indicate if this is from the auto-scanner.
     */
    private void markAttendance(String studentId, boolean isAuto) {
        String grade = grade_att.getValue();
        String subject = sublist_att.getValue();

        if (studentId == null) {
            setStatusLabel(sp_main_result_out, "No student selected.", "red");
            return;
        }

        if (grade == null || subject == null) {
            setStatusLabel(sp_main_result_out, "Please select a grade and subject for attendance.", "red");
            return;
        }

        String result = dbHandler.markAttendance(studentId, subject, grade);
        String color = result.startsWith("Failed") || result.startsWith("Error") ? "red" : "green";
        setStatusLabel(sp_main_result_out, result, color);

        // If the marked attendance is for the currently viewed student, refresh their table
        if (studentId.equals(currentProfileStudentId)) {
            int year = Integer.parseInt(inp_year_att.getText());
            int month = month_att_rec.getSelectionModel().getSelectedIndex() + 1;
            loadAttendanceTable(studentId, year, month);
        }
    }

    /**
     * Handles the erase student button click to delete all student data from the database.
     */
    @FXML
    private void eraseStudent() {
        if (currentProfileStudentId == null) {
            setStatusLabel(erase_out_sp, "No student profile loaded.", "red");
            return;
        }

        boolean success = dbHandler.deleteStudentData(currentProfileStudentId);
        if (success) {
            setStatusLabel(erase_out_sp, "Student data deleted successfully.", "green");
            // Clear the profile and refresh the dashboard
            clearProfileOutputs();
            loadStudentsFromDB();
            loadUnpaidStudents();

            currentProfileStudentId = null;
            scarch_id_inp_sp.clear();
        } else {
            setStatusLabel(erase_out_sp, "Failed to delete student data.", "red");
        }
    }

    /**
     * Applies changes from the configuration tab to the database.
     */
    private void applyConfigurationChanges() {
        // Get all input values
        String newGrade = new_grade_db_inp_conf.getText().trim();
        String newSubject = new_subject_db_inp_conf.getText().trim().toUpperCase();
        String selectedGrade = grade_list_for_subject_inp_con.getValue();
        String feeAmount = new_subject_fee_inp_conf.getText().trim();
        String teacher = new_subject_tn_inp_conf.getText().trim().toUpperCase();
        String updateGrade = sub_fee_update_grade_conf.getValue();
        String updateSubject = sub_fee_update_sub_conf.getValue();
        LocalDate updateDate = sub_fee_update_date_conf.getValue();
        String updateFee = sub_fee_update_fee_conf.getText().trim();

        // Determine which options have fields filled
        boolean option1Filled = !newGrade.isEmpty();
        boolean option2AnyFilled = !newSubject.isEmpty() || !feeAmount.isEmpty() || selectedGrade != null || !teacher.isEmpty();
        boolean option3AnyFilled = updateGrade != null || updateSubject != null || updateDate != null || !updateFee.isEmpty();

        // Count active options
        int activeOptions = 0;
        if (option1Filled) activeOptions++;
        if (option2AnyFilled) activeOptions++;
        if (option3AnyFilled) activeOptions++;

        // Check if more than one option is attempted
        if (activeOptions > 1) {
            setStatusLabel(result_out_conf, "Please complete only one configuration option at a time.", "red");
            clearConfigrationInputs();
            return;
        }

        // Check if no option is selected
        if (activeOptions == 0) {
            setStatusLabel(result_out_conf, "No configuration option selected.", "red");
            return;
        }

        boolean gradeAdded = false;
        boolean subjectAdded = false;
        boolean feeUpdated = false;

        // Option 1: Add new grade
        if (option1Filled) {
            if (dbHandler.insertGrade(newGrade)) {
                setStatusLabel(result_out_conf, "Grade '" + newGrade + "' added successfully.", "green");
                gradeAdded = true;
            } else {
                setStatusLabel(result_out_conf, "Grade '" + newGrade + "' already exists.", "red");
            }
            clearConfigrationInputs();
        }
        // Option 2: Add new subject
        else if (option2AnyFilled) {
            if (!newSubject.isEmpty() && !feeAmount.isEmpty() && selectedGrade != null && !teacher.isEmpty()) {
                if (dbHandler.insertSubject(newSubject, selectedGrade, feeAmount, teacher)) {
                    setStatusLabel(result_out_conf,
                            "Subject '" + newSubject + "' added to " + selectedGrade + ".", "green");
                    subjectAdded = true;
                } else {
                    setStatusLabel(result_out_conf,
                            "Subject already exists in this grade.", "red");
                }
            } else {
                setStatusLabel(result_out_conf,
                        "Please enter Subject, Teacher, Fee Amount, and select a Grade.", "red");
            }

            clearConfigrationInputs();
        }
        // Option 3: Update subject fee
        else if (option3AnyFilled) {
            if (updateGrade == null || updateSubject == null || updateDate == null || updateFee.isEmpty()) {
                setStatusLabel(result_out_conf,
                        "Please enter Fee Amount and select a Grade, Subject, Effective Date.", "red");
            } else {
                String effectiveDate = updateDate.toString();
                if (dbHandler.insertSubjectFee(updateSubject, updateGrade, effectiveDate, updateFee)) {
                    setStatusLabel(result_out_conf,
                            "Fee for '" + updateSubject + "' in " + updateGrade + " updated to " + updateFee + " effective from " + effectiveDate + ".", "green");
                    feeUpdated = true;
                } else {
                    setStatusLabel(result_out_conf,
                            "Failed to update fee for '" + updateSubject + "'. Check if the effective date is unique or inputs are valid.", "red");
                }
            }
            clearConfigrationInputs();
        }

        // Refresh UI if changes were made
        if (gradeAdded || subjectAdded) {
            loadSubjectInfoTable();
            if (gradeAdded) {
                refreshChoiceBoxes();
            }
            if (subjectAdded) {
                refreshAll();
                refreshDashboardTables();
                String currentUpdateGrade = sub_fee_update_grade_conf.getValue();
                if (currentUpdateGrade != null && currentUpdateGrade.equals(selectedGrade)) {
                    sub_fee_update_sub_conf.getItems().setAll(dbHandler.getSubjectsForGrade(currentUpdateGrade));
                }
            }
        }
    }

    /**
     * Loads teacher revenue data into the tro_table based on the selected date.
     */
    private void loadTeacherRevenueTable() {
        LocalDate selectedDate = dash_dp_tr.getValue();
        if (selectedDate == null) {
            return; // No date selected, skip loading
        }

        int year = selectedDate.getYear();
        int month = selectedDate.getMonthValue();
        int day = selectedDate.getDayOfMonth();

        List<RevenueRecord> records = dbHandler.getTeacherRevenue(year, month, day);
        teachersRevenueData.setAll(records);

        // Calculate totals
        double totalYear = 0.0;
        double totalMonth = 0.0;
        double totalDay = 0.0;

        for (RevenueRecord record : teachersRevenueData) {
            totalYear += record.getYearIncome();
            totalMonth += record.getMonthIncome();
            totalDay += record.getDayIncome();
        }

        // Update labels with formatted totals
        tit_year.setText(String.format("%.2f", totalYear));
        tit_month.setText(String.format("%.2f", totalMonth));
        tit_day.setText(String.format("%.2f", totalDay));
    }

    // Updated loadInstituteRevenueTable()
    private void loadInstituteRevenueTable() {
        LocalDate selectedDate = dash_dp_in.getValue();
        if (selectedDate == null) {
            return; // No date selected, skip loading
        }

        int year = selectedDate.getYear();
        int month = selectedDate.getMonthValue();
        int day = selectedDate.getDayOfMonth();

        List<RevenueRecord> records = dbHandler.getInstituteRevenue(year, month, day);
        instituteRevenueData.setAll(records);

        // Calculate totals
        double totalYear = 0.0;
        double totalMonth = 0.0;
        double totalDay = 0.0;

        for (RevenueRecord record : instituteRevenueData) {
            totalYear += record.getYearIncome();
            totalMonth += record.getMonthIncome();
            totalDay += record.getDayIncome();
        }

        // Update labels with formatted totals
        iit_year.setText(String.format("%.2f", totalYear));
        iit_month.setText(String.format("%.2f", totalMonth));
        iit_day.setText(String.format("%.2f", totalDay));
    }

    private void deleteOldFeeRecords() {
        DatabaseHandler db = new DatabaseHandler();
        db.deleteOldDayFeeRecords();
        db.deleteOldMonthFeeRecords();
    }

    // =================================================================================
    // Table Loading and Setup
    // =================================================================================

    private void setupFeeTable() {
        // Create columns dynamically
        fee_datatable.setSelectionModel(null); // Disable selection
        //fee_datatable.getSelectionModel().setSelectionMode(null); // This line is for debugging purpose.
        TableColumn<FeeRecord, String> subjectCol = new TableColumn<>("Subject");
        subjectCol.setCellValueFactory(new PropertyValueFactory<>("subject"));
        fee_datatable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // // RESIZE_POLICY "total width = table width." auto adjust
        subjectCol.setMinWidth(130);// prevents expansion
        subjectCol.setMaxWidth(130);// prevents shrinking
        subjectCol.setSortable(false);
        //subjectCol.setPrefWidth(139);

        fee_datatable.getColumns().add(subjectCol);

        for (int i = 0; i < 12; i++) {
            final int monthIndex = i;
            TableColumn<FeeRecord, String> monthCol = new TableColumn<>(Month.of(i + 1).name().substring(0, 3));
            monthCol.setCellValueFactory(cellData -> cellData.getValue().monthProperty(monthIndex));
            monthCol.setMinWidth(73);
            monthCol.setMaxWidth(73);
            monthCol.setSortable(false);
            fee_datatable.getColumns().add(monthCol);
            //fee_datatable.addEventFilter(ScrollEvent.ANY, Event::consume);
            //fee_datatable.setSelectionModel(null);
        }
    }

    private void loadFeeTable(String studentId, int year) {
        if (initialGrade == null || registerDate == null) {
            setStatusLabel(sp_main_result_out, "Missing student registration data.", "red");
            fee_datatable.getItems().clear();
            return;
        }

        int regYear = registerDate.getYear();
        int gradeNum;
        try {
            gradeNum = Integer.parseInt(initialGrade) + (year - regYear);
        } catch (NumberFormatException e) {
            setStatusLabel(sp_main_result_out, "Invalid initial grade format.", "red");
            fee_datatable.getItems().clear();
            return;
        }

        String gradeName;
        if (gradeNum > 13) {
            gradeName = "completed";
        } else if (gradeNum < 1) { // Assuming minimum grade is 1; adjustable if needed
            gradeName = "1";
        } else {
            gradeName = String.valueOf(gradeNum);
        }

        // Get filtered subjects for the computed grade
        List<String> studentSubjects = dbHandler.getStudentSubjectsForGrade(studentId, gradeName);

        ObservableList<FeeRecord> feeData = FXCollections.observableArrayList();
        Map<String, Map<Integer, String>> records = dbHandler.getFeeRecords(studentId, year);
        Map<String, Set<Integer>> tutes = dbHandler.getTuteRecords(studentId, year);

        for (String subject : studentSubjects) {
            FeeRecord row = new FeeRecord(subject);
            Map<Integer, String> payments = records.getOrDefault(subject, new HashMap<>());
            Set<Integer> tuteMonths = tutes.getOrDefault(subject, new HashSet<>());

            for (int month = 1; month <= 12; month++) {
                String cellValue = "";
                String paymentDate = payments.get(month);
                boolean hasTute = tuteMonths.contains(month);

                if (paymentDate != null) {
                    String formattedDate = paymentDate.substring(5, 10).replace('-', '.');
                    if (hasTute) {
                        cellValue = "Ⓣ" + formattedDate;
                    } else {
                        cellValue = formattedDate;
                    }
                } else if (hasTute) {
                    cellValue = "Ⓣ";
                }

                row.setMonthPayment(month, cellValue);
            }
            feeData.add(row);
        }
        fee_datatable.setItems(feeData);
    }

    private void setupAttendanceTable() {
        attendance_datatable.setSelectionModel(null); // Disable selection
        //attendance_datatable.getSelectionModel().setSelectionMode(null); // This line is for debugging purpose.
        TableColumn<AttendanceRecord, String> subjectCol = new TableColumn<>("Subject");
        subjectCol.setCellValueFactory(new PropertyValueFactory<>("subject"));
        attendance_datatable.getColumns().add(subjectCol);
        //attendance_datatable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // RESIZE_POLICY "total width = table width." auto adjust
        subjectCol.setMinWidth(130);// prevents expansion
        subjectCol.setMaxWidth(130);// prevents shrinking
        subjectCol.setSortable(false);

        for (int i = 0; i < 31; i++) {
            final int dayIndex = i;
            TableColumn<AttendanceRecord, String> dayCol = new TableColumn<>(String.format("%02d", i + 1));
            dayCol.setCellValueFactory(cellData -> cellData.getValue().dayProperty(dayIndex));
            //dayCol.setPrefWidth(26);
            dayCol.setMaxWidth(27);  // prevents expansion
            dayCol.setMinWidth(27);  // prevents shrinking
            dayCol.setSortable(false);

            attendance_datatable.getColumns().add(dayCol);
        }

        TableColumn<AttendanceRecord, String> totalCol = new TableColumn<>("TP");
        totalCol.setCellValueFactory(new PropertyValueFactory<>("totalPresent"));
        totalCol.setMinWidth(39);
        totalCol.setMaxWidth(39);
        totalCol.setSortable(false);
        attendance_datatable.getColumns().add(totalCol);
    }

    private void loadAttendanceTable(String studentId, int year, int month) {
        if (initialGrade == null || registerDate == null) {
            setStatusLabel(sp_main_result_out, "Missing student registration data.", "red");
            attendance_datatable.getItems().clear();
            return;
        }

        int regYear = registerDate.getYear();
        int gradeNum;
        try {
            gradeNum = Integer.parseInt(initialGrade) + (year - regYear);
        } catch (NumberFormatException e) {
            setStatusLabel(sp_main_result_out, "Invalid initial grade format.", "red");
            attendance_datatable.getItems().clear();
            return;
        }

        String gradeName;
        if (gradeNum > 13) {
            gradeName = "completed";
        } else if (gradeNum < 1) { // Assuming minimum grade is 1; adjust if needed
            gradeName = "1";
        } else {
            gradeName = String.valueOf(gradeNum);
        }

        // Get filtered subjects for the computed grade
        List<String> studentSubjects = dbHandler.getStudentSubjectsForGrade(studentId, gradeName);

        ObservableList<AttendanceRecord> attendanceData = FXCollections.observableArrayList();
        Map<String, List<Integer>> records = dbHandler.getAttendanceRecords(studentId, year, month);

        for (String subject : studentSubjects) {
            AttendanceRecord row = new AttendanceRecord(subject);
            if (records.containsKey(subject)) {
                for (Integer day : records.get(subject)) {
                    row.markDay(day);
                }
            }
            attendanceData.add(row);
        }
        attendance_datatable.setItems(attendanceData);
    }

    private void setupSubjectRegistrationTable() {
        sub_reg_info.setSelectionModel(null); // Disable selection
        //sub_reg_info.getSelectionModel().setSelectionMode(null); // This line is for debugging purpose.
        // Set up the columns
        sub_reg_info_subject.setCellValueFactory(new PropertyValueFactory<>("subject"));
        sub_reg_info_date.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
    }

    private void loadSubjectRegistrationTable(String studentId) {
        List<SubjectRegistrationRecord> records = dbHandler.getStudentSubjectsWithDate(studentId);
        ObservableList<SubjectRegistrationRecord> data = FXCollections.observableArrayList(records);

        sub_reg_info_subject_id.setCellValueFactory(cellData -> cellData.getValue().subjectIdProperty());
        sub_reg_info_subject.setCellValueFactory(cellData -> cellData.getValue().subjectProperty());
        sub_reg_info_date.setCellValueFactory(cellData -> cellData.getValue().registrationDateProperty());
        sub_reg_info.setItems(data);
    }

    private void updateFeeTotal() {
        fee_total_out.setText("0.00"); // Default to 0.00

        String yearStr = year_fee.getText();
        if (yearStr.isEmpty()) return;

        int year;
        try {
            year = Integer.parseInt(yearStr);
        } catch (NumberFormatException e) {
            return;
        }

        if (initialGrade == null || registerDate == null || currentProfileStudentId == null) {
            return;
        }

        int regYear = registerDate.getYear();
        int gradeNum;
        try {
            gradeNum = Integer.parseInt(initialGrade) + (year - regYear);
        } catch (NumberFormatException e) {
            return;
        }

        String gradeName;
        if (gradeNum > 13) {
            gradeName = "completed";
        } else if (gradeNum < 1) {
            gradeName = "1";
        } else {
            gradeName = String.valueOf(gradeNum);
        }

        ObservableList<String> selectedSubjects = sub_select_fee.getSelectionModel().getSelectedItems();
        if (selectedSubjects.isEmpty()) {
            return; // Keeps 0.00
        }

        double total = 0.0;
        for (String subject : selectedSubjects) {
            int subjectId = dbHandler.getSubjectId(subject, gradeName);
            if (subjectId != -1) {
                double fee = dbHandler.getCurrentFee(subjectId);
                total += fee;
            }
        }

        fee_total_out.setText(String.format("%.2f", total));


    }

    private void setupSubjectInfoTable() {
        s_info_dash.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);  // Auto-adjust columns to fit width
        si_grade.setCellValueFactory(cellData -> cellData.getValue().gradeProperty());
        si_id.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        si_sub.setCellValueFactory(cellData -> cellData.getValue().subjectProperty());
        si_tea.setCellValueFactory(cellData -> cellData.getValue().teacherProperty());
        si_fee.setCellValueFactory(cellData -> cellData.getValue().feeProperty());

        // Custom cell factory for fee column to format as "0.00" (or whole number if no decimals)
        si_fee.setCellFactory(column -> new TableCell<SubjectInfoRecord, Number>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    double value = item.doubleValue();
                    if (value == (long) value) {
                        setText(String.format("%.0f", value));  // Whole number
                    } else {
                        setText(String.format("%.2f", value));  // With two decimals
                    }
                }
            }
        });

        s_info_dash.setItems(subjectInfoData);
        s_info_dash.setSelectionModel(null); // Disable selection
    }

    private void loadSubjectInfoTable() {
        subjectInfoData.setAll(dbHandler.getAllSubjectsInfo());
    }

    private void setupDailyFeeTable() {
        daily_fee_table.setSelectionModel(null); // Disable selection
        TableColumn<DailyFeeRecord, String> subjectCol = new TableColumn<>("Subject");
        subjectCol.setCellValueFactory(new PropertyValueFactory<>("subject"));
        daily_fee_table.getColumns().add(subjectCol);
        subjectCol.setMinWidth(130); // prevents expansion
        subjectCol.setMaxWidth(130); // prevents shrinking
        subjectCol.setSortable(false);

        for (int i = 0; i < 31; i++) {
            final int dayIndex = i;
            TableColumn<DailyFeeRecord, String> dayCol = new TableColumn<>(String.format("%02d", i + 1));
            dayCol.setCellValueFactory(cellData -> cellData.getValue().dayProperty(dayIndex));
            dayCol.setMaxWidth(27);  // prevents expansion
            dayCol.setMinWidth(27);  // prevents shrinking
            dayCol.setSortable(false);
            daily_fee_table.getColumns().add(dayCol);
        }

        TableColumn<DailyFeeRecord, String> totalCol = new TableColumn<>("TP");
        totalCol.setCellValueFactory(new PropertyValueFactory<>("totalPaid"));
        totalCol.setMinWidth(39);
        totalCol.setMaxWidth(39);
        totalCol.setSortable(false);
        daily_fee_table.getColumns().add(totalCol);
    }

    private void loadDailyFeeTable(String studentId, int year, int month) {
        if (initialGrade == null || registerDate == null) {
            setStatusLabel(sp_main_result_out, "Missing student registration data.", "red");
            daily_fee_table.getItems().clear();
            return;
        }

        int regYear = registerDate.getYear();
        int gradeNum;
        try {
            gradeNum = Integer.parseInt(initialGrade) + (year - regYear);
        } catch (NumberFormatException e) {
            setStatusLabel(sp_main_result_out, "Invalid initial grade format.", "red");
            daily_fee_table.getItems().clear();
            return;
        }

        String gradeName;
        if (gradeNum > 13) {
            gradeName = "completed";
        } else if (gradeNum < 1) { // Assuming minimum grade is 1; adjustable if needed
            gradeName = "1";
        } else {
            gradeName = String.valueOf(gradeNum);
        }

        // Get filtered subjects for the computed grade
        List<String> studentSubjects = dbHandler.getStudentSubjectsForGrade(studentId, gradeName);

        ObservableList<DailyFeeRecord> dailyFeeData = FXCollections.observableArrayList();
        Map<String, List<Integer>> records = dbHandler.getDailyFeeRecords(studentId, year, month);

        for (String subject : studentSubjects) {
            DailyFeeRecord row = new DailyFeeRecord(subject);
            if (records.containsKey(subject)) {
                for (Integer day : records.get(subject)) {
                    row.markDay(day);
                }
            }
            dailyFeeData.add(row);
        }
        daily_fee_table.setItems(dailyFeeData);
    }

    private void setupGradeSubjectTable() {
        gradeAndSubjectStudentsDetails.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);  // Auto-adjust columns to fit width
        gsr_grade.setCellValueFactory(cellData -> cellData.getValue().gradeProperty());
        gsr_subid.setCellValueFactory(cellData -> cellData.getValue().subjectIdProperty());
        gsr_subname.setCellValueFactory(cellData -> cellData.getValue().subjectNameProperty());
        gsr_subsc.setCellValueFactory(cellData -> cellData.getValue().studentCountProperty());
        gradeAndSubjectStudentsDetails.setItems(gradeSubjectData);
        gradeAndSubjectStudentsDetails.setSelectionModel(null); // Disable selection if not needed
    }

    private void loadGradeSubjectTable() {
        gradeSubjectData.setAll(dbHandler.getAllSubjectsStudentCounts());
    }

    private void setupUnpaidStudentsTable() {
        ofr_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        ofr_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        ofr_grade.setCellValueFactory(new PropertyValueFactory<>("grade"));
        ofr_lpd.setCellValueFactory(new PropertyValueFactory<>("lastPaymentDate"));
        ofr_um.setCellValueFactory(new PropertyValueFactory<>("unpaidMonths"));
        ofr_no.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        ofr_lr.setCellValueFactory(new PropertyValueFactory<>("lastReminder"));
        ofr_rem.setCellValueFactory(new PropertyValueFactory<>("remark"));
        unpaidTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        unpaidTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        unpaidTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                od_id.setText(newVal.getId());
                od_name.setText(newVal.getName());
                od_grade.setText(newVal.getGrade());
                od_con.setText(newVal.getMobile());
                od_remark.setText(newVal.getRemark());
                String lr = newVal.getLastReminder();
                od_dp.setValue((lr != null && !lr.isEmpty()) ? LocalDate.parse(lr) : null);
                od_out.setText(""); // Reset label when a new student is selected
            } else {
                od_id.setText("");
                od_name.setText("");
                od_grade.setText("");
                od_con.setText("");
                od_remark.setText("");
                od_dp.setValue(null);
                od_out.setText(""); // Reset label when no student is selected
            }
        });
    }

    private void loadUnpaidStudents() {
        List<UnpaidStudentRecord> list = new ArrayList<>();
        LocalDate now = LocalDate.now();
        int cy = now.getYear();
        int cm = now.getMonthValue();

        for (StudentRecord sr : dbHandler.getAllStudents()) {
            String sid = sr.getId();
            Map<String, String> det = dbHandler.getStudentById(sid);
            if (det.isEmpty()) continue;

            String regDateStr = det.get("register_date");
            if (regDateStr == null) continue;

            LocalDate regDate = LocalDate.parse(regDateStr);
            int ry = regDate.getYear();
            int rm = regDate.getMonthValue();

            String sql = "SELECT MAX(year * 12 + month) AS max_paid_months, MAX(payment_date) AS max_payment_date " +
                    "FROM fee_payments WHERE student_id = ?";
            int ly = ry;
            int lm = rm - 1;
            String lpd = "None";
            try (Connection conn = dbHandler.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, sid);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    int maxPaidMonths = rs.getInt("max_paid_months");
                    if (!rs.wasNull()) {
                        ly = maxPaidMonths / 12;
                        lm = maxPaidMonths % 12;
                        lpd = rs.getString("max_payment_date");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (lm <= 0) {
                lm += 12;
                ly--;
            }

            int unpaid = (cy - ly) * 12 + cm - lm;
            if (unpaid <= 0) continue;

            Map<String, String> remarks = dbHandler.getStudentRemarks(sid);
            String remark = remarks.getOrDefault("remark", "");
            String lr = remarks.getOrDefault("last_reminder_date", "");

            UnpaidStudentRecord usr = new UnpaidStudentRecord(sid, det.get("name"), det.get("grade"), lpd, unpaid, det.get("mobile"), lr, remark);
            list.add(usr);
        }

        list.sort(Comparator.comparingInt(UnpaidStudentRecord::getUnpaidMonths).reversed());
        unpaidTable.setItems(FXCollections.observableArrayList(list));
    }

    // =================================================================================
    // Helper and Utility Methods
    // =================================================================================

    private void clearRegistrationInputs() {
        stu_name_reg.clear();
        stu_moib_reg.clear();
        stu_grade_reg.getSelectionModel().clearSelection();
        stu_gender1_reg.setSelected(true);
        stu_subjects_reg.getItems().clear();
    }

    private void clearRegistrationOutputs() {
        id_output.setText("");
        grade_output.setText("");
        name_output.setText("");
        subjects_output.setText("");
        gender_output.setText("");
        qr_output.setImage(null);
    }

    private void clearConfigrationInputs() {
        // Clear TextFields
        new_grade_db_inp_conf.setText("");
        new_subject_db_inp_conf.setText("");
        new_subject_fee_inp_conf.setText("");
        new_subject_tn_inp_conf.setText("");
        sub_fee_update_fee_conf.setText("");

        // Clear ComboBoxes/ChoiceBoxes (set to null or empty selection)
        grade_list_for_subject_inp_con.setValue(null);
        sub_fee_update_grade_conf.setValue(null);
        sub_fee_update_sub_conf.setValue(null);

        // Clear DatePicker
        sub_fee_update_date_conf.setValue(null);
    }

    private void clearProfileOutputs() {
        name_output_sp.setText("");
        grade_output_sp.setText("");
        gender_output_sp.setText("");
        mobnum_output_sp.setText("");
        rdate_output_sp.setText("");
        sub_select_fee.getItems().clear();
        fee_datatable.getItems().clear();
        attendance_datatable.getItems().clear();
        sub_reg_info.getItems().clear();
        sublist_new_reg.getItems().clear();
        grade_new_reg.getSelectionModel().clearSelection();
        sublist_new_unreg.getSelectionModel().clearSelection();
        grade_new_unreg.getSelectionModel().clearSelection();
        daily_fee_table.getItems().clear();
        vcode_inp_sp.clear();
        ver_out_sp.setText("");
    }

    private void clearLablesForProfile() {
        sp_sub_reg_result_out.setText(""); // subject registration result label
        sp_sub_unreg_result_out.setText(""); // subject unregistration result label
        erase_out_sp.setText(""); // erase student result label
    }

    private void setStatusLabel(Label label, String text, String color) {
        Platform.runLater(() -> {
            label.setText(text);
            label.setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold;");
        });
    }

    private String generateNextStudentId() {
        String lastId = dbHandler.getLastStudentId();
        int batch = 1, number = 0;

        if (lastId != null && lastId.matches("B\\d{7}")) {
            batch = Integer.parseInt(lastId.substring(1, 4));   // first 3 digits
            number = Integer.parseInt(lastId.substring(4));     // last 4 digits
        }

        number++;
        if (number > 9999) {
            batch++;
            number = 1;
        }

        return String.format("B%03d%04d", batch, number);
    }

    private void generateAndDisplayQRCode(String studentId) {
        File qrDir = new File("C:\\Prolog\\Students\\");
        if (!qrDir.exists()) {
            qrDir.mkdirs();
        }
        String filePath = qrDir.getPath() + "\\" + studentId + ".png";
        int width = 300;
        int height = 300;

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(studentId, BarcodeFormat.QR_CODE, width, height);
            Path path = FileSystems.getDefault().getPath(filePath);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
            Image qrImage = new Image("file:" + filePath);
            qr_output.setImage(qrImage);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }

    private void loadGradesIntoChoiceBox(ChoiceBox<String> choiceBox) {
        List<String> grades = dbHandler.getAllGrades();
        choiceBox.getItems().setAll(grades);
    }

    private void eraseAndUnregsection(boolean x) {
        if (x) {
            unreg_new_sub_stu.setDisable(false);
            erase_stu_btn_sp.setDisable(false);
            grade_new_unreg.setDisable(false);
            sublist_new_unreg.setDisable(false);

        } else {
            unreg_new_sub_stu.setDisable(true);
            erase_stu_btn_sp.setDisable(true);
            grade_new_unreg.setDisable(true);
            sublist_new_unreg.setDisable(true);
        }
    }

    private void setupGradeRemoval() {
        loadGradesIntoChoiceBox(conf_rg_grade);

        // Set button action
        apply_btn_conf3.setOnAction(event -> {
            String selectedGrade = conf_rg_grade.getValue();
            if (selectedGrade == null || selectedGrade.trim().isEmpty()) {
                setStatusLabel(conf_out_2, "Please select a grade to remove.", "red");
                return;
            }

            // Perform deletion without confirmation
            boolean success = dbHandler.deleteGrade(selectedGrade);
            if (success) {
                setStatusLabel(conf_out_2, "Grade '" + selectedGrade + "' deleted successfully.", "green");
                // Refresh relevant UI elements (e.g., reload grades in all ChoiceBoxes, refresh tables)
                refreshDashboardTables();
                refreshChoiceBoxes();
            } else {
                setStatusLabel(conf_out_2, "Failed to delete grade '" + selectedGrade, "red");
            }

        });
    }

    private void setupSubjectRemoval() {
        loadGradesIntoChoiceBox(conf_rs_grade);

        // Listener to populate subjects when a grade is selected
        conf_rs_grade.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                conf_rs_sub.getItems().setAll(dbHandler.getSubjectsForGrade(newVal));
            } else {
                conf_rs_sub.getItems().clear();
            }
        });

        // Button action to remove the selected subject
        apply_btn_conf4.setOnAction(event -> {
            String selectedGrade = conf_rs_grade.getValue();
            String selectedSubject = conf_rs_sub.getValue();
            if (selectedGrade == null || selectedGrade.trim().isEmpty() ||
                    selectedSubject == null || selectedSubject.trim().isEmpty()) {
                setStatusLabel(conf_out_2, "Please select a grade and subject to remove.", "red");
                return;
            }

            boolean success = dbHandler.deleteSubject(selectedGrade, selectedSubject);
            if (success) {
                setStatusLabel(conf_out_2, "Subject '" + selectedSubject + "' in grade '" + selectedGrade + "' deleted successfully.", "green");
                // Refresh relevant UI elements
                conf_rs_sub.getItems().clear(); // Clear current subjects
                if (selectedGrade != null) {
                    conf_rs_sub.getItems().setAll(dbHandler.getSubjectsForGrade(selectedGrade)); // Reload subjects for current grade
                }
                refreshDashboardTables();
                refreshChoiceBoxes();
                // Optionally refresh other ChoiceBoxes that list subjects
            } else {
                setStatusLabel(conf_out_2, "Failed to delete subject '" + selectedSubject + "' in grade '" + selectedGrade + "'.", "red");
            }
        });
    }


    // =================================================================================
    // QR Code Camera Scanner Methods
    // =================================================================================

    private void startCamera() {
        if (isCameraActive) return;

        webcam = Webcam.getDefault();
        if (webcam == null) {
            setStatusLabel(sp_main_result_out, "No webcam found.", "red");
            return;
        }
        webcam.open();
        isCameraActive = true;

        cameraTask = new Task<>() {
            @Override
            protected Void call() {
                final Reader reader = new MultiFormatReader();
                while (isCameraActive && !isCancelled()) {
                    try {
                        BufferedImage image = webcam.getImage();
                        if (image == null) continue;

                        // Update the camera view in the UI
                        Platform.runLater(() -> qr_cam_field_sp.setImage(SwingFXUtils.toFXImage(image, null)));

                        // Decode QR code
                        LuminanceSource source = new BufferedImageLuminanceSource(image);
                        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                        Result result = reader.decode(bitmap);

                        if (result != null) {
                            String studentId = result.getText();
                            Platform.runLater(() -> {
                                scarch_id_inp_sp.setText(studentId);
                                loadStudentProfile(studentId);
                            });
                            // Pause after finding a QR code to prevent rapid re-scans
                            Thread.sleep(2000);
                        }
                    } catch (NotFoundException e) {
                        // no QR code was found in the frame
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        };

        Thread cameraThread = new Thread(cameraTask);
        cameraThread.setDaemon(true);
        cameraThread.start();
    }

    private void stopCamera() {
        if (!isCameraActive || webcam == null) return;

        isCameraActive = false;
        if (cameraTask != null) {
            cameraTask.cancel();
        }
        webcam.close();
        qr_cam_field_sp.setImage(null);
    }

    // =================================================================================
    // Other Methods
    // =================================================================================


    // A real-time clock that updates every second
    private void clock() {
        // Create a Timeline that runs every second
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            // Get current date and time
            String dateTime = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            // Update the Label text on the JavaFX Application Thread
            main_clock.setText(dateTime);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE); // Run indefinitely
        timeline.play(); // Start the timeline
    }

    private void generateReceipt(String studentId, int year, int month, List<String> subjects, String studentName, String grade) {
        File billDir = new File("C:\\prolog\\bills\\");
        if (!billDir.exists()) {
            billDir.mkdirs();
        }

        LocalDateTime now = LocalDateTime.now();

        // File name: HHmmss.txt
        String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String billPath = billDir.getPath() + "\\" + fileName + ".txt";
        String date = now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String time = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String monthName = Month.of(month).getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        String period = monthName + " " + year;

        Map<String, Double> subjectFees = new HashMap<>();
        double total = 0.0;

        boolean isManual = manual_fee_rb.isSelected();

        if (!isManual) {
            for (String subject : subjects) {
                int subjectId = dbHandler.getSubjectId(subject, grade);
                if (subjectId != -1) {
                    double fee = dbHandler.getCurrentFee(subjectId);
                    subjectFees.put(subject, fee);
                    total += fee;
                }
            }
        } else {
            String manualTotalStr = fee_total_out.getText();
            try {
                total = Double.parseDouble(manualTotalStr);
            } catch (NumberFormatException e) {
                setStatusLabel(sp_main_result_out, "Invalid manual fee amount.", "red");
                return;
            }
            String combinedSubjects = subjects.isEmpty() ? "Fees" : String.join(", ", subjects);
            subjectFees.put(combinedSubjects, total);
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(billPath, true))) {
            writer.println("--------------------------------------");
            writer.println("  Vidupiyasa Educational Institute");
            writer.println("Boga Asala, Rathnapura Road, Baduraliya");
            writer.println("          Tel: 070 7365753");
            writer.println("");
            writer.println("         FEE PAYMENT RECEIPT");
            writer.println("--------------------------------------");
            writer.println("Date: " + date + "          Time: " + time);
            writer.println("");
            writer.println("Student ID : " + studentId);
            writer.println("Student    : " + studentName);
            writer.println("Grade      : " + grade);
            writer.println("");
            writer.println("Subjects:");
            for (Map.Entry<String, Double> entry : subjectFees.entrySet()) {
                writer.println("   • " + entry.getKey() + " (" + period + ")   Rs. " + formatAmount(entry.getValue()));
            }
            writer.println("");
            writer.println("Total Paid: Rs. " + formatAmount(total));
            writer.println("");
            writer.println("--------------------------------------");
            writer.println("   Thank you for your payment!");
            writer.println("--------------------------------------");
            writer.println(""); // Extra line for separation between receipts
        } catch (IOException e) {
            e.printStackTrace();
            setStatusLabel(sp_main_result_out, "Failed to generate receipt file.", "red");
        }
    }

    private String formatAmount(double amount) {
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        if (amount == (long) amount) {
            return nf.format((long) amount);
        } else {
            NumberFormat df = NumberFormat.getInstance(Locale.US);
            df.setMinimumFractionDigits(2);
            df.setMaximumFractionDigits(2);
            return df.format(amount);
        }
    }

    private void showDatabaseIP() {
        String ip = DatabaseHandler.getIp();
        db_ip_out_conf.setText(ip);
    }

    public String vCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "abcdefghijklmnopqrstuvwxyz"
                + "0123456789"
                + "!#%@*()";

        StringBuilder sb = new StringBuilder();
        java.util.Random rand = new java.util.Random();

        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(rand.nextInt(chars.length())));
        }

        return sb.toString();
    }

    private boolean verifyuser() {
        String enteredCode = vcode_inp_sp.getText().trim();
        if (enteredCode.isEmpty()) {
            setStatusLabel(ver_out_sp, "Please enter the verification code.", "red");
            return false;
        }
        if (enteredCode.equals(verificationCode)) {
            setStatusLabel(ver_out_sp, "Verification successful.", "green");
            return true;

        } else {
            setStatusLabel(ver_out_sp, "Incorrect verification code.", "red");
            return false;
        }
    }

    private void refreshDashboardTables() {
        loadTeacherRevenueTable();
        loadInstituteRevenueTable();
        loadSubjectInfoTable();
        loadStudentsFromDB();
        loadGradeSubjectTable();
        //loadUnpaidStudents();
    }

    private void refreshChoiceBoxes() {
        loadGradesIntoChoiceBox(stu_grade_reg);
        stu_grade_reg.getSelectionModel().clearSelection();
        loadGradesIntoChoiceBox(grade_new_reg);
        grade_new_reg.getSelectionModel().clearSelection();
        loadGradesIntoChoiceBox(grade_new_unreg);
        grade_new_unreg.getSelectionModel().clearSelection();
        loadGradesIntoChoiceBox(sub_fee_update_grade_conf);
        sub_fee_update_grade_conf.getSelectionModel().clearSelection();
        loadGradesIntoChoiceBox(grade_list_for_subject_inp_con);
        grade_list_for_subject_inp_con.getSelectionModel().clearSelection();
        loadGradesIntoChoiceBox(conf_rg_grade);
        conf_rg_grade.getSelectionModel().clearSelection();
        loadGradesIntoChoiceBox(conf_rs_grade);
        conf_rs_grade.getSelectionModel().clearSelection();
        loadGradesIntoChoiceBox(grade_att);
        grade_att.getSelectionModel().clearSelection();
        loadGradesIntoChoiceBox(conf_utn_grade);
        conf_utn_grade.getSelectionModel().clearSelection();
    }

    private void refreshAll() {
        refreshDashboardTables();
        refreshChoiceBoxes();
    }

    private void feeAndTuteRefresh(int year , int month) {
        inp_year_fee.setText(String.valueOf(year));
        loadFeeTable(currentProfileStudentId, year);
        inp_year_day_fee.setText(String.valueOf(year));
        month_day_fee.setValue(Month.of(month).name());
        loadDailyFeeTable(currentProfileStudentId, year, month);
        inp_year_att.setText(String.valueOf(year));
        month_att_rec.setValue(Month.of(month).name());
        loadAttendanceTable(currentProfileStudentId, year, month);
        loadInstituteRevenueTable();
        loadTeacherRevenueTable();
        loadUnpaidStudents();
        sub_select_fee.getSelectionModel();//.clearSelection();   clear removed for tute marking
        updateFeeTotal();

    }

    private void setAccess(){
        if (admin) {
            conf_rg_grade.setDisable(false);
            apply_btn_conf3.setDisable(false);
            conf_rs_grade.setDisable(false);
            conf_rs_sub.setDisable(false);
            apply_btn_conf4.setDisable(false);
            vcode_inp_sp.setDisable(false);
            reg_rem_ver_btn.setDisable(false);
            vCode_output_sp.setVisible(true);
        } else {
            conf_rg_grade.setDisable(true);
            apply_btn_conf3.setDisable(true);
            conf_rs_grade.setDisable(true);
            conf_rs_sub.setDisable(true);
            apply_btn_conf4.setDisable(true);
            vcode_inp_sp.setDisable(true);
            reg_rem_ver_btn.setDisable(true);
            vCode_output_sp.setVisible(false);
            setStatusLabel(admin_conf_out, "Admin access required.", "red");
            setStatusLabel(admin_sp_out, "Admin access required.", "red");
        }
    }

    private void updateResources() {
        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        // App CPU usage (process CPU load %)
        double processCpuLoad = osBean.getProcessCpuLoad() * 100;
        cpu_usg.setText(String.format("%.2f%%", processCpuLoad));

        // App RAM usage (JVM heap used)
        long processRamUsed = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        ram_usg.setText(formatBytes(processRamUsed));

        // Available CPU (100% - system CPU load)
        double systemCpuLoad = osBean.getSystemCpuLoad() * 100;
        cpu_bal.setText(String.format("%.2f%%", 100 - systemCpuLoad));

        // Available RAM (system free physical memory)
        long freeRam = osBean.getFreePhysicalMemorySize();
        ram_bal.setText(formatBytes(freeRam));
    }

    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }




}


