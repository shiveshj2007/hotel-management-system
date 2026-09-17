# ACADEMIC PROJECT REPORT

# HOTEL MANAGEMENT SYSTEM

A Desktop GUI-Based Java ApplicationSubmitted for the VITyarthi "Build Your Own Project" Evaluation.
---

## 1. Cover Page

Project Title: Hotel Management System
Course/Program: B.Tech Computer Science / Software Engineering (VITyarthiProject Evaluation)
Domain: Software Engineering & GUI Application Development
Implementation Language: Java (JDK 11+)
GUI Framework: Java Swing
Architecture: 4-Layer Modular Architecture with Binary File Serialization Storage
Evaluation Date: Sept 2026.
---



## 2. Introduction

I am presenting my solution to an hotel management system, which provides an end-to-end automation for daily administrative and front-desk hotel processes. Built using Object Oriented Programming (OOP) paradigms using Java language and Java Swing Graphical Toolkit. Front desk workers are rovided an intuitive software interface for managing registrations, room inventory process, resrvation booking with overlap validation, itmized invoice with tax calculations andpayment tracking.
---

## 3. Problem Statement

Hotels face issues due to manual administration using paperlogbooks or spreadsheet files, such as:
1. Double-booking of same rooms for overlapping intervals when manual date comparisons are made within that.
2. Billing and taxation errors while calculating stays, missing additional temized charges
3. Data corruption or lost records when saving in fragmented ways across systems
4. Inefficient front-des operations due to lack of instant filtering/search facilities

These issues are addressed by the Hotel Management System through the use of automated field validations, itemized billing, file storage system and analytical reporting.
---

## 4. Functional Requirements

The application enforces strict set of 3 majormodules that have beenimplemented:

### Module 1: Guest Management
Guest Registration
Regex Input validation
Search / Filter feature
Edit / Delete capabilities.

### Module 2: Room and Reservation Management

Room Inventory: Number, type, price, occupants, operational status
Booking Reservations: Connecting Guest -> Room -> Check-in and Check-out dates. Restriction to double-booking using date-interval algorithm to reject overlapping reservation in it.
Room Lifecycle Management: Transitioning between states like   Available', 'Occupied', 'Reserved' through check-in, check-out and cancellation process.
### Module 3: Billing and Reporting
Itemized Invoicing: Automatically generating invoices with room charges (Nights Price/Night) and itemized additional charges in it as well (FoodLaundry,Spa), applying 12% tax and generating a grand total
Payment Settlement: Tracking payment details for each invoice, such as Cash, Credit Card or UPI. Marking invoice status as PAID.
Operational Analytics Dashboard: Showing cards for number of rooms, occupancy percentage, active reservations, collected receivables and pending invoices.
---
## 5. Non-Functional Requirements
1. Usability
Consistent layout for desktop Swing GUI, with intuitive side pane navigation, form field validations, confirmation prompts for destructive operations.
2. Reliability
Proper exception handling to not crash the application when invalid user inputs or file read/write failures occur.
3. Maintainability
Clear 4-layer architecture (`gui`, `manager`, `model`, `storage`, `util`) following principle of separation of concerns.
4. Performance
Responsive performance for local file queries and mathematical rule evaluations (~< 50ms).
5. Resource Constraints
No external heavy framework dependencies, uses light-weight JDK libraries.
6. Error Handling
Friendly error dialogues informing the user of invalid operations.....
---
## 6. System Architecture
The Hotel Management application follows a 4 layer architecure as depicted in this diagram:
```
┌─────────────────────────────────────────────────────────────┐

│         GUI Layer (Java Swing)           │
│ (MainFrame, DashboardPanel, GuestPanel, RoomReservationPanel│
│       BillingPanel, ReportPanel, UITheme)       │
└──────────────────────────────┬──────────────────────────────┘
│
▼
┌─────────────────────────────────────────────────────────────┐
│       Business Logic / Manager Layer         │
│ (GuestManager, RoomManager, ReservationManager,       │
│      BillingManager, ReportGenerator)         │
└──────────────────────────────┬──────────────────────────────┘
│
▼
┌─────────────────────────────────────────────────────────────┐
│          Domain Model Layer            │
│  (Guest, Room, Reservation, Bill, ServiceCharge, Enums)  │
└──────────────────────────────┬──────────────────────────────┘
│
▼
┌─────────────────────────────────────────────────────────────┐
│         Storage / Persistence Layer        │
│  (FileManager -> Binary File Serialization in data/.dat) │
└─────────────────────────────────────────────────────────────┘
```
---

# 7. Design Diagrams:
(Diagram source files available in diagrams/ directory)
Use Case Diagram (`diagrams/use-case.md`):
- Receptionist: Register Guest, Search Guest, Check Availability, Create Reservations, Process Check-In, Process Check-Out, Add Service Charges, Record Payment
- Administrative: Manage Room Inventory, View Analytics & Revenue Reports
Process Workflow Diagram (`diagrams/workflow.md):
- Staff opens app -> Dashboard -> Select Module -> Input Validation -> Process Business Logic -> Update Storage -> Display Output
Sequence Diagram (`diagrams/sequence.md`):
- Details checkout sequence: RoomReservationPanel -> ReservationManager -> BillingManager -> FileManager -> bills.dat -> Invoice Summary view
Class Diagram (`diagrams/class-diagram.md`):
- Show relationships between Guest, Room, Reservation, Bill, ServiceCharge and state enums.
---
## 8. Design Decisions
Selected Java Swing to provide GUI desktop experience without requiring additional setup of web servers or browser runtime configurations.
Opted for binary file serialization instead of a database server so that evaluators can also duplicate the repository and run the application without installing any additional software like MySQL.
Built a Pure Java Executable Test Suite (`TestRunner.java`) that can be tested directly on any standard JDK installation without external JUnit JAR classpath dependencies.
---

## 9. Implementation Details
Language: Java (JDK 11+)
GUI Toolkit: Java Swing (`JFrame`, `JTabbedPane`, `JTable`, `CardLayout`, `GridBagLayout`, `JOptionPane`)
Algorithmic Rules
Date Overlap Check: start1 < end2 && start2 < end1
Tax Calculation: Tax = Subtotal 0.12
Grand Total: GrandTotal = Subtotal + Tax.
---
## 10. Screenshots & Results
1. Dashboard Panel: Operational cards for Rooms (Total,Available, Occupied), Registered Guests, Revenue. Collected.
2. Guest Management Forms: Panels for registering guest, searching multi-field queries, updating and deleting guest profiles
3. Room and Reservation Management: Tabs showing Room Inventory and a frm for reservation booking process with date availability checks
4. Billing & Invoicing: Detailed panel for itemized room and service charges, 12% tax calculation and payment recording
5. Reporting and Analytics Table: Details around operational summry of total rooms, occupancy percentage, active bookings, revenue tracking
---


## 11. Testing Approach
A standalone test suite (`tests/TestRunner.java`) performs autopmatic assertions for:
1. Regex validation of Email field and Phone fiel
2. Guest ID auto-generation rule and duplicate ID rejections
3. Room creation process and inventory status management
4. Reservation check-out date >check-in date validation
5. Double-booking overlap detection,
6. Check-in and check-out process state machine transitions
7. Subtotal, 12% tax and Grand Total validation rules,
8. Operational statistics aggregation tests.
---
## 12. Challenges Faced
Challenge: Double-booking reservations for the same room when multiple reservations are created.
Solution: Implemented mathematical interval overlap detection in `ReservationManager.isRoomAvailable()`.
Challenge: Ensuring that persistent storage mechanism works out-of-the-box for clean installations
Solution: Designed `FileManager` to automatically create `data/` directory and seed realistic data on first execution.
---
## 13. Learnings and Takeaways
Practical application of Object Oriented Programming (Encapsulation, Modularization, Sepaation of Concerns)
Implementation of Swing UI layout managers for eg `CardLayout`, `GridBagLayout`, `BorderLayout`
Design of robust business logic layers decoupled from presentation graphics.
---

## 14. Future Enhancements
Export itemized invoices to PDF for client download
Implement JFreeChart for displayiiing bar charts around monthly revenue trends
Implement JDBC for connecting to enterprise-grade MySQL database for producction use....
--
## 15. References
1. Oracle Java 11 SE Documentation and was Swing API Specification.!
2. VITyarthi Prject Instruction & Evaluation Guidellines....

## 16. Details:


Name: Shivesh Jha
Course: Programming in java
Faculty: Sharmila Joseph Ma'am
College: VIT Bhopal
