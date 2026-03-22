# 🧑‍💼 UserVault — User Management System

A full-stack **Java Desktop Application** for managing users with a PostgreSQL database backend.

![Java](https://img.shields.io/badge/Java-17%2B-orange?style=flat-square&logo=java)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?style=flat-square&logo=postgresql)
![Swing](https://img.shields.io/badge/UI-Java%20Swing-green?style=flat-square)
![JDBC](https://img.shields.io/badge/DB-JDBC-yellow?style=flat-square)

---

## 📌 Project Overview

UserVault is a desktop-based User Management System built using **Core Java** and **Java Swing** for the UI, with **PostgreSQL** as the database. It provides full **CRUD** (Create, Read, Update, Delete) operations with a clean, modern dark-themed interface.

---

## ✨ Features

- ➕ **Add User** — Add new users with name, email, phone, department and role
- 🔍 **Search Users** — Real-time search by name or email
- ✎ **Edit User** — Update existing user details
- ⌫ **Delete User** — Remove users with confirmation dialog
- 📋 **List All Users** — View all users in a sortable table
- ✅ **Form Validation** — Client-side + database-level validation
- 🔒 **Unique Email Check** — Prevents duplicate email entries
- 📊 **Live User Count** — Total users displayed in header

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 17+ |
| UI Framework | Java Swing |
| Database | PostgreSQL 15 |
| DB Connectivity | JDBC (PostgreSQL Driver) |
| Architecture | DAO Pattern (MVC) |
| IDE | VS Code / IntelliJ IDEA |

---

## 📁 Project Structure

```
UserManagementSystem/
├── src/
│   └── com/usermgmt/
│       ├── db/
│       │   └── DBConnection.java       ← Database connection (Singleton)
│       ├── model/
│       │   └── User.java               ← User entity/model class
│       ├── dao/
│       │   └── UserDAO.java            ← All DB operations (CRUD)
│       └── ui/
│           ├── UserManagementApp.java  ← Main application window
│           └── UserFormDialog.java     ← Add/Edit user dialog
├── lib/
│   └── postgresql-42.6.0.jar           ← PostgreSQL JDBC driver
├── database/
│   └── setup.sql                       ← DB setup script
└── README.md
```

---

## ⚙️ Setup & Run

### Prerequisites
- Java JDK 17+
- PostgreSQL 15+
- VS Code or any Java IDE

### Step 1 — Database Setup

Run `database/setup.sql` in pgAdmin or psql:

```sql
CREATE DATABASE usermgmt;

CREATE TABLE IF NOT EXISTS users (
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    email      VARCHAR(150) NOT NULL UNIQUE,
    phone      VARCHAR(15)  NOT NULL,
    department VARCHAR(100),
    role       VARCHAR(100)
);
```

### Step 2 — Configure DB Password

Open `src/com/usermgmt/db/DBConnection.java` and update:

```java
private static final String PASSWORD = "YOUR_POSTGRESQL_PASSWORD";
```

### Step 3 — Add JDBC Driver

Download [postgresql-42.6.0.jar](https://jdbc.postgresql.org/download/) and place it in the `lib/` folder.

### Step 4 — Compile & Run

```bash
# Compile
javac -cp lib/postgresql-42.6.0.jar -d out src/com/usermgmt/**/*.java

# Run
java -cp "out;lib/postgresql-42.6.0.jar" com.usermgmt.ui.UserManagementApp
```

---

## 🗄️ Database Schema

```sql
CREATE TABLE users (
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    email      VARCHAR(150) NOT NULL UNIQUE,
    phone      VARCHAR(15)  NOT NULL,
    department VARCHAR(100),
    role       VARCHAR(100)
);
```

---

## 🔌 API / DAO Methods

| Method | Description |
|--------|-------------|
| `addUser(User)` | Insert new user into DB |
| `getAllUsers()` | Fetch all users |
| `searchUsers(String)` | Search by name or email |
| `getUserById(int)` | Fetch single user |
| `updateUser(User)` | Update existing user |
| `deleteUser(int)` | Delete user by ID |
| `emailExists(String, int)` | Check duplicate email |
| `getTotalUsers()` | Count total users |

---

## 👨‍💻 Author

**[Your Name]**
- 🌐 GitHub: [@yourusername](https://github.com/yourusername)
- 📧 Email: your@email.com

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).
