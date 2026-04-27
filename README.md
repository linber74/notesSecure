# Secure Notes

A console-based Java application for securely creating and managing personal notes with user authentication.

---

## Features

### User
- Register a new account
- Login with username and password
- Change password
- Create, view, edit and delete your own notes
- Logout

### Admin
- All user features
- View all notes in the system
- Delete any user's notes

---

## Technologies Used

| Technology | Version |
|---|---|
| Java | 21 |
| MySQL | 8.0 |
| BCrypt (jbcrypt) | 0.4 |
| MySQL Connector/J | 8.0.33 |
| dotenv-java | 3.2.0 |

---

## Security

- Passwords are hashed with **BCrypt** before being stored
- Login is limited to **3 attempts** before being blocked
- Password requirements: min 8 characters, 1 uppercase, 1 lowercase, 1 digit, 1 special character
- Sensitive credentials stored in `.env` file (not included in repository)
- Application errors are logged to `logs/app.log`

---

## Requirements

- Java 21 or higher
- MySQL 8.0 or higher
- Maven

---

## Setup

### 1. Database
Create the database and tables in MySQL. The schema requires:
- A `users` table with: `userId`, `username`, `password`, `role`
- A `notes` table with: `notesId`, `text`, `createdAt`, `updatedAt`, `userId`

### 2. Environment Variables
Create a `.env` file in the root of the project with the following:

```
db_url=jdbc:mysql://localhost:3306/secure_notes
db_user=your_mysql_username
db_password=your_mysql_password
```

### 3. Admin User
Insert an admin user directly into the database with the role `Admin`.

---

## How to Run

1. Clone the repository
2. Set up the database and `.env` file as described above
3. Open the project in IntelliJ IDEA
4. Run `Main.java`

---

## Project Structure

```
src/
└── main/
    └── java/
        ├── config/         # DatabaseConnection, LoggerConfig
        ├── model/          # User, Notes
        ├── repository/     # UserRepository, NotesRepository
        ├── service/        # AuthService, NotesService
        └── ui/             # ConsoleMenu
        Main.java
```

---

## Notes

- The `.env` file is excluded from version control for security reasons
- Logs are stored in the `logs/` directory (excluded from version control)
