<div align="center">

# VEIMS

**Vidupiyasa Educational Institute Management System**

A JavaFX desktop app for running a tuition/education institute — students, attendance, fees, grades, subjects, and revenue, all in one place.

![Java](https://img.shields.io/badge/Java-24-orange?style=for-the-badge&logo=openjdk)
![JavaFX](https://img.shields.io/badge/JavaFX-17-blue?style=for-the-badge)
![MySQL](https://img.shields.io/badge/MySQL-database-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Maven](https://img.shields.io/badge/Build-Maven-C71A36?style=for-the-badge&logo=apachemaven)

</div>

---

## 📖 About

VEIMS is a desktop management system built for **Vidupiyasa Educational Institute**, covering the day-to-day admin work of running a tuition class or small institute: registering students, tracking attendance, collecting and recording fees, managing grades/subjects, and generating revenue reports for both the institute and individual teachers.

---

## ✨ Features

- **Student registration** — auto-generated student IDs, profile management, subject registration per grade
- **Attendance tracking** — webcam-based check-in with QR code generation/scanning for student IDs
- **Fee management** — daily and monthly fee records, "mark as paid" workflow, unpaid-student tracking, receipt generation
- **Grades & subjects** — per-grade subject setup, auto-registration of subjects for new grades, subject removal/cleanup
- **Revenue reporting** — institute-wide revenue breakdown by year/month/day, and per-teacher revenue/bill generation with printable output
- **Login system** — local login plus optional Firebase-backed authentication, with login/logout event logging
- **Configurable database connection** — point the app at a local or remote MySQL server via IP/port/credentials in-app

---

## 🧱 Tech Stack

- **Java 24** + **JavaFX 17** (Controls, FXML, Web, Swing, Media)
- **MySQL** via HikariCP connection pooling (`mysql-connector-j`)
- **SQLite** (`sqlite-jdbc`) for local/offline data
- **ControlsFX**, **FormsFX**, **ValidatorFX**, **BootstrapFX**, **TilesFX**, **Ikonli** — JavaFX UI/UX libraries
- **ZXing** — QR code generation & scanning
- **Webcam Capture** — attendance camera integration
- **Jackson** / **org.json** / **json-simple** — JSON handling
- **SLF4J** — logging
- **Firebase Realtime Database** — optional remote authentication

---

## 🧩 Requirements

- **JDK 24**
- **Maven**
- A running **MySQL server** (local or network-accessible) — the app will need a database, user, and password configured
- A webcam, if you want to use the attendance/QR features

---

## 🚀 Getting Started

1. **Clone the repo:**

   ```bash
   git clone https://github.com/titan7k-zc/VEIMS.git
   cd VEIMS
   ```

2. **Set up your database.**
   Create a MySQL database and user, then configure the connection (IP, port, username, password, database name) through the app's login/config screen — do **not** hardcode or commit real credentials into the repo.

3. **Run it with Maven:**

   ```bash
   ./mvnw clean javafx:run
   ```

   (or `mvnw.cmd clean javafx:run` on Windows)

---

## 🗂️ Project Structure

```
VEIMS/
├── pom.xml                  # Maven build config (Java 24, JavaFX plugin)
├── src/main/java/voidbreaker/prolog/
│   ├── Main.java             # app entry point
│   ├── Login.java            # login screen logic
│   ├── Home.java             # main dashboard — attendance, fees, grades, revenue, etc.
│   ├── DatabaseHandler.java  # all MySQL/database operations
│   ├── FirebaseAuth.java     # optional Firebase-based login
│   ├── HostUpdator.java      # local IP / config sync helper
│   └── *Record.java          # data models (Student, Fee, Attendance, Grade, Subject, Revenue, ...)
└── src/main/resources/
    ├── login.fxml
    ├── home.fxml
    └── style.css
```

---

## ⚠️ Notes

- This project was built for a specific institute's internal workflow, so some naming/branding (grades, fee structures, receipts) is tailored to that use case rather than being fully generic.
- Database credentials should be supplied at runtime/config, never committed to source control.

## 📄 License

No license has been added yet. Add a `LICENSE` file if you want to define how others can use or contribute to this code.
