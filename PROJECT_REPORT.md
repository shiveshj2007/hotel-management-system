# ACADEMIC PROJECT REPORT

# HOTEL MANAGEMENT SYSTEM

**A Desktop GUI-Based Java Application Submitted for the VITyarthi "Build Your Own Project" Evaluation**

---

## 1. COVER PAGE

* **Project Title**: Hotel Management System
* **Course/Program**: B.Tech Computer Science / Software Engineering (VITyarthi Project Evaluation)
* **Domain**: Software Engineering & GUI Application Development
* **Implementation Language**: Java (JDK 11+)
* **GUI Framework**: Java Swing
* **Architecture**: 4-Layer Modular Architecture (GUI, Manager, Model, Storage)
* **Persistence Strategy**: Binary File Serialization (`data/*.dat`)
* **Evaluation Date**: September 2026

---

## 2. INTRODUCTION

The **Hotel Management System** is a complete, modular desktop application designed to automate day-to-day administrative and front-desk hotel operations. Developed using Object-Oriented Programming (OOP) principles in Java and utilizing the Java Swing graphical toolkit, the system equips front-desk staff with an interactive interface for guest registrations, room inventory control, reservation booking with double-booking overlap validation, itemized invoice generation with 12% GST tax computation, payment settlement recording, and real-time operational reporting.

---

## 3. PROBLEM STATEMENT

Manual hotel administration relying on paper logbooks or loose spreadsheet files suffers from severe operational vulnerabilities:
1. **Double-Booking Errors**: Assigning the same room to multiple guests for overlapping date ranges due to lack of automated date interval checking.
2. **Billing & Tax Miscalculations**: Errors in calculating stay durations, missing itemized additional service charges (e.g., room service, laundry, spa), or incorrect tax application.
3. **Data Inconsistency & Record Loss**: Fragmented record-keeping leading to data corruption or accidental loss upon system restart.
4. **Slow Front-Desk Workflow**: Inability to quickly query guest records, perform rapid check-in/check-out state transitions, or monitor real-time hotel occupancy statistics.

The **Hotel Management System** addresses these problems through automated date validation, strict input regex checks, itemized billing, local file persistence, and real-time analytical reporting.

---

## 4. FUNCTIONAL REQUIREMENTS

The application strictly implements three major functional modules:

### Module 1: Guest Management
* **Guest Registration**: Add guest with auto-generated unique ID (`GST-1001`).
* **Regex Input Validation**: Validates phone numbers (10–12 digits) and email formatting.
* **Search & Filter**: Real-time multi-field search across guest IDs, names, phones, and emails.
* **Profile Controls**: Update guest details and delete profiles with confirmation dialogs.

### Module 2: Room & Reservation Management
* **Room Inventory**: Manage room numbers, types (Single, Double, Deluxe, Suite), pricing, capacity, and operational status (`AVAILABLE`, `RESERVED`, `OCCUPIED`, `MAINTENANCE`).
* **Reservation Booking**: Connect Guest -> Room -> Check-In Date -> Check-Out Date.
* **Double-Booking Prevention**: Date interval overlap validation prohibiting double-booking of active reservations.
* **Lifecycle State Machine**: Transition reservation and room statuses through Check-In, Check-Out, and Cancellation workflows.

### Module 3: Billing & Reporting
* **Itemized Invoicing**: Automated bill generation upon checkout combining room charges (`Nights * Price/Night`) and itemized additional service charges (Food, Laundry, Spa).
* **Tax Calculation**: Configurable 12% GST tax rate application (`Subtotal * 0.12`) and Grand Total calculation (`Subtotal + Tax`).
* **Payment Settlement**: Record payment mode (Cash, Credit Card, UPI) and update invoice status to `PAID`.
* **Analytical Dashboard**: Visual dashboard cards for total rooms, occupancy percentage, active reservations, total revenue collected, and pending receivables.

---

## 5. NON-FUNCTIONAL REQUIREMENTS

1. **Usability**: Consistent desktop Swing GUI layout with intuitive side menu navigation, form validation feedback, and confirmation dialogs for destructive actions.
2. **Reliability**: Fault-tolerant exception handling preventing system crashes during invalid user input or storage file read/write operations.
3. **Maintainability**: Clean 4-layer architecture (`gui`, `manager`, `model`, `storage`, `util`) enforcing clear separation of concerns.
4. **Performance**: Instantaneous response time (< 50 ms) for local file queries and mathematical rule evaluations.
5. **Resource Efficiency**: Zero external heavy framework dependencies; operates lightweight using standard JDK libraries.
6. **Error Handling**: Friendly error dialogs (`JOptionPane`) informing users of specific validation errors.

---

## 6. SYSTEM ARCHITECTURE

The application uses a 4-layer architecture:

```
┌─────────────────────────────────────────────────────────────┐
│                 GUI Layer (Java Swing)                      │
│ (MainFrame, DashboardPanel, GuestPanel, RoomReservationPanel│
│             BillingPanel, ReportPanel, UITheme)             │
└──────────────────────────────┬──────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────┐
│             Business Logic / Manager Layer                  │
│ (GuestManager, RoomManager, ReservationManager,             │
│            BillingManager, ReportGenerator)                 │
└──────────────────────────────┬──────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────┐
│                   Domain Model Layer                        │
│   (Guest, Room, Reservation, Bill, ServiceCharge, Enums)   │
└──────────────────────────────┬──────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────┐
│                  Storage / Persistence Layer                │
│    (FileManager -> Binary File Serialization in data/*.dat) │
└─────────────────────────────────────────────────────────────┘
```

---

## 7. DESIGN DIAGRAMS

*(Diagram source files available in `diagrams/` directory)*

### Use Case Diagram (`diagrams/use-case.md`)
- Receptionist / Staff: Register Guest, Search Guest, Check Availability, Create Reservation, Process Check-In, Process Check-Out, Add Service Charges, Record Payment.
- Administrator: Manage Room Inventory, View Analytics & Revenue Reports.

### Process Workflow Diagram (`diagrams/workflow.md`)
- Staff opens app -> Dashboard -> Select Module -> Input Validation -> Process Business Logic -> Update Storage -> Display Output.

### Sequence Diagram (`diagrams/sequence.md`)
- Details the checkout sequence: `RoomReservationPanel` -> `ReservationManager` -> `BillingManager` -> `FileManager` -> `bills.dat` -> Invoice Summary view.

### Class Diagram (`diagrams/class-diagram.md`)
- Demarcates relationships between `Guest`, `Room`, `Reservation`, `Bill`, `ServiceCharge`, and state enums.

---

## 8. DESIGN DECISIONS & RATIONALE

1. **Java Swing over Web Framework**: Selected Java Swing to satisfy desktop GUI requirements without requiring external web servers or browser runtime configurations.
2. **Binary File Serialization over Database Server**: Chosen local file serialization (`data/*.dat`) so evaluators can clone and run the repository instantly without installing or configuring external database servers like MySQL.
3. **Pure Java Executable Test Suite**: Built `TestRunner.java` to allow command-line test execution on any standard JDK installation without requiring external JUnit JAR classpath dependencies.

---

## 9. IMPLEMENTATION DETAILS

* **Language**: Java (JDK 11+)
* **GUI Toolkit**: Java Swing (`JFrame`, `JTabbedPane`, `JTable`, `CardLayout`, `GridBagLayout`, `JOptionPane`).
* **Key Algorithmic Rules**:
  * Date Overlap Check: `start1 < end2 && start2 < end1`
  * Tax Calculation: `Tax = Subtotal * 0.12`
  * Grand Total: `GrandTotal = Subtotal + Tax`

---

## 10. SCREENSHOTS & RESULTS

1. **Dashboard Panel**: Displays real-time hotel operational overview cards (Total Rooms, Available, Occupied, Registered Guests, Collected Revenue).
2. **Guest Management**: Forms for registering guests, searching by multi-field queries, updating details, and deleting profiles.
3. **Room & Reservation Management**: Tabbed interface containing room inventory grid and reservation booking form with date availability checking.
4. **Billing & Invoicing**: Detail panel rendering itemized room and service charges, 12% GST tax computation, and payment recording.
5. **Reporting & Analytics**: Operational summary table detailing total rooms, occupancy percentage, active bookings, and financial metrics.

---

## 11. TESTING APPROACH

A standalone test suite (`tests/TestRunner.java`) executes automated assertions covering:
1. Regex validation for emails and phone numbers.
2. Guest ID auto-generation and duplicate rejection.
3. Room creation and inventory status updates.
4. Reservation check-out date > check-in date enforcement.
5. Double-booking prevention during overlapping date intervals.
6. Check-In and Check-Out state machine transitions.
7. Subtotal, 12% GST tax, and grand total accuracy.
8. Operational statistics aggregation.

---

## 12. CHALLENGES FACED & SOLUTIONS

* **Challenge**: Preventing double-booking when multiple reservations are created for the same room.
  * **Solution**: Implemented mathematical interval overlap checking in `ReservationManager.isRoomAvailable()`.
* **Challenge**: Ensuring persistent storage works out-of-the-box on clean installations.
  * **Solution**: Designed `FileManager` to automatically create the `data/` directory and seed realistic sample data on first execution.

---

## 13. LEARNINGS & KEY TAKEAWAYS

* Practical application of Object-Oriented Programming (Encapsulation, Modularization, Separation of Concerns).
* Implementation of Swing UI layout managers (`CardLayout`, `GridBagLayout`, `BorderLayout`).
* Designing robust business logic layers decoupled from presentation graphics.

---

## 14. FUTURE ENHANCEMENTS

* Exporting generated invoices to downloadable PDF format.
* JFreeChart integration for visual monthly revenue bar charts.
* JDBC database connector for enterprise MySQL server integration.

---

## 15. REFERENCES

1. Oracle Java 11 SE Documentation & Swing API Specification.
2. VITyarthi Project Instruction & Evaluation Guidelines.
