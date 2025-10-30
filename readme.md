# CampusConnect

A desktop application that enables college students, event-organizers, and admins to manage and participate in events efficiently.

---

## 🚀 Features

- User Authentication (Login / Register)
- Role-Based Access Control for Admin, Organizer, and Student
- Event Management: Create, View, Edit/Delete events
- Event Registration system for students
- Feedback Submission by participants
- Notification Reminders for upcoming events
- Event Archive: View past events and outcomes

---

## 🛠️ Tech Stack

- **Language:** Java 17+
- **UI Framework:** Swing (MVC pattern)
- **Database:** SQLite (via JDBC)
- **Build Tool:** Maven
- **Testing:** JUnit 5

---


## 🎬 Getting Started

### Prerequisites
- JDK 17 or above
- Maven 3.x
- Optional: SQLite Browser for DB inspection

### Setup & Run
1. Clone the repository
   ```bash
   git clone https://github.com/Nekhal-james/CampusConnect.git
   cd CampusConnect
   ```
2. Build the project
   ```bash
   mvn clean install
   ```
3. Run the application
   ```bash
   mvn exec:java -Dexec.mainClass="com.yourorg.campusconnect.App"
   ```

### Running Tests
```bash
mvn test
```

---

## 📌 Usage / Workflow

1. Launch the application and login or register.
2. Depending on your role:
   - Admin: manage users, organizers, events.
   - Organizer: create events, view registrations, collect feedback.
   - Student: browse events, register, submit feedback.
3. Use the dashboard for events, registrations, and notifications.
4. Archived section displays past events and feedback.

---

## 🔧 Configuration

- Database: Local SQLite (campusconnect.db), configurable in `config.properties`
- UI: Swing look-and-feel customizable in setup code.

---

## ✅ Contributing

1. Fork the repository
2. Create a branch (`git checkout -b feature/YourFeature`)
3. Make your changes
4. Add tests where applicable
5. Commit (`git commit -m "Add some feature"`)
6. Push (`git push origin feature/YourFeature`)
7. Open a Pull Request

Ensure consistent style and passing tests.

---

## 📄 License

MIT License (see LICENSE file)

---

## 👤 Author

**Nekhal-James**  
GitHub: [@Nekhal-James](https://github.com/Nekhal-james)  
Contact: Add email or preferred method

---

## 🧭 Acknowledgements

- SQLite
- Swing tutorials
- JUnit
- Maven ecosystem

---

*Happy coding and have fun building!*

