HOTEL MANAGEMENT SYSTEM
A complete, functional, GUI-based Java desktop application built for the VITyarthi "Build Your Own Project" evaluation.

The system features a custom Swing graphical interface, 4-layer architecture, file-based binary data persistence, automated itemized billing with 12% GST tax calculation, double-booking overlap validation, and a real-time operational analytics reporting dashboard.

📋 Table of Contents
Project Overview
Problem Being Solved
Objectives
Major Functional Modules
1. Guest Management
2. Room & Reservation Management
3. Billing & Reporting
Features
Technologies Used
System Requirements
Project Structure
Architecture
Installation & Setup
Compilation
Execution
Testing
Usage Guide
Data Storage
Validation and Error Handling
Screenshots
Future Enhancements
Authors
📌 Project Overview
The Hotel Management System simplifies front-desk administrative duties and managerial operations for hotel establishments. It streamlines guest registrations, room inventory management, booking lifecycle transitions, itemized service invoicing, payment settlement, and revenue tracking.

🎯 Problem Being Solved
Manual hotel administration relies on vulnerable paper ledgers or loose spreadsheets, causing:

Double-Booking Overlaps: Multiple guests assigned to the same room during overlapping date ranges.
Financial Inaccuracies: Errors in stay length calculations, omitted room service charges, or tax miscalculations.
Data Instability: Record loss due to unorganized data storage.
Slow Front-Desk Workflow: Inability to quickly query guest profiles or monitor real-time occupancy metrics.
This system provides a desktop application addressing these issues with automated date validation, strict input regex checks, itemized billing, local file persistence, and real-time analytical reports.

🏁 Objectives
Design a modular, 4-layer Java desktop application complying strictly with object-oriented software engineering principles.
Automate front-desk guest registration, room booking, check-in, check-out, and invoicing.
Eliminate double-booking errors via mathematical date-range overlap validation algorithms.
Provide reliable local data persistence using binary file serialization (data/*.dat) without external database overhead.
Deliver an executable command-line build and testing environment accessible via standard JDK tools.
🚀 Major Functional Modules
1. Guest Management
Guest Registration: Add guest with auto-generated unique ID (GST-1001).
Regex Input Validation: Validates phone numbers (10–12 digits) and email formatting.
Search & Filter: Real-time multi-field search across guest IDs, names, phones, and emails.
Profile Controls: Update guest details and delete profiles with confirmation dialogs.
2. Room & Reservation Management
Room Inventory: Manage room numbers, types (Single, Double, Deluxe, Suite), pricing, capacity, and operational status (Available, Reserved, Occupied, Maintenance).
Reservation Booking: Connect Guest -> Room -> Check-In Date -> Check-Out Date.
Double-Booking Prevention: Date interval overlap validation prohibiting double-booking of active reservations.
Lifecycle State Machine: Transition reservation and room statuses through Check-In, Check-Out, and Cancellation workflows.
3. Billing & Reporting
Itemized Invoicing: Automated bill generation upon checkout combining room charges (Nights * Price/Night) and itemized additional service charges (Food, Laundry, Spa).
Tax Calculation: Configurable 12% GST tax rate application (Subtotal * 0.12) and Grand Total calculation (Subtotal + Tax).
Payment Settlement: Record payment mode (Cash, Credit Card, UPI) and update invoice status to PAID.
Analytical Dashboard: Visual dashboard cards for total rooms, occupancy percentage, active reservations, total revenue collected, and pending receivables.
✨ Features
Modern Swing GUI: Custom dark slate header, color-coded status badges, styled forms, and interactive JTable data grids.
Auto ID Generation: Automatic generation of structured IDs (GST-1001, RES-1001, INV-1001).
Date Interval Overlap Checker: Algorithmic check (start1 < end2 && start2 < end1) rejecting overlapping bookings.
Itemized Service Charges: Capability to add extra hotel service charges (Room Service, Laundry, Spa) to any active invoice.
Auto Data Persistence: Auto-seeds sample data on clean installation and saves all mutations to binary data files.
Zero Third-Party Dependencies: Runs natively on standard JDK 11+ without external library requirements.
🛠️ Technologies Used
Programming Language: Java (JDK 11 or higher)
GUI Framework: Java Swing (JFrame, JTabbedPane, JTable, CardLayout, GridBagLayout)
Architecture: 4-Layer Architecture (GUI -> Manager -> Model -> Storage)
Data Storage: Local Binary File Serialization (data/*.dat)
Testing: Standalone Executable Java Test Suite (tests/TestRunner.java)
💻 System Requirements
Operating System: Windows 10/11, macOS, or Linux
Java Development Kit (JDK): JDK 11 or higher
RAM: 2 GB minimum (4 GB recommended)
Disk Space: 50 MB available space
📁 Project Structure
hotel-management-system/
├── src/
│   └── hotelmanagement/
│       ├── Main.java                        # Entry point, launches MainFrame GUI
│       ├── model/                           # Encapsulated Data Models
│       │   ├── Guest.java
│       │   ├── Room.java
│       │   ├── RoomType.java (Enum)
│       │   ├── RoomStatus.java (Enum)
│       │   ├── Reservation.java
│       │   ├── ReservationStatus.java (Enum)
│       │   ├── ServiceCharge.java
│       │   ├── Bill.java
│       │   └── PaymentStatus.java (Enum)
│       ├── manager/                         # Business Logic Layer
│       │   ├── GuestManager.java
│       │   ├── RoomManager.java
│       │   ├── ReservationManager.java
│       │   ├── BillingManager.java
│       │   └── ReportGenerator.java
│       ├── gui/                             # Swing GUI Layer
│       │   ├── MainFrame.java               # Main Window with navigation bar
│       │   ├── DashboardPanel.java          # Metric overview panel
│       │   ├── GuestPanel.java              # Module 1 GUI
│       │   ├── RoomReservationPanel.java    # Module 2 GUI
│       │   ├── BillingPanel.java            # Module 3A GUI
│       │   ├── ReportPanel.java             # Module 3B GUI
│       │   └── UITheme.java                 # Theme palette & styling constants
│       ├── storage/
│       │   └── FileManager.java             # Persistent binary I/O handler
│       └── util/
│           ├── ValidationUtil.java          # Regex input validation
│           └── DateUtil.java                # Date parsing & overlap logic
├── data/                                    # Local File Persistence (Auto-seeded)
│   ├── guests.dat
│   ├── rooms.dat
│   ├── reservations.dat
│   └── bills.dat
├── tests/                                   # Standalone Test Suite
│   ├── BusinessLogicTest.java               # Unit test cases
│   └── TestRunner.java                      # Executable CLI test runner
├── diagrams/                                # Architecture & System Diagrams (Mermaid)
│   ├── architecture.md
│   ├── workflow.md
│   ├── use-case.md
│   ├── class-diagram.md
│   └── sequence.md
├── report/
│   └── PROJECT_REPORT.md                    # 15-Section Academic Report
├── README.md                                # Project Documentation
└── statement.md                             # Problem Statement & Scope
🏗️ Architecture
GUI Layer (Swing Panels & Components)
           │
           ▼
Business Logic Layer (GuestManager, RoomManager, ReservationManager, BillingManager, ReportGenerator)
           │
           ▼
Domain Model Layer (Guest, Room, Reservation, Bill, ServiceCharge, Enums)
           │
           ▼
Persistence Layer (FileManager -> Local binary files in data/*.dat)
⚙️ Installation & Setup
Clone/Download the Repository:

git clone https://github.com/username/hotel-management-system.git
cd hotel-management-system
Verify Java Installation: Ensure javac and java are available in your system path:

java -version
javac -version
🔨 Compilation
To compile all Java source files into the out/ directory:

Windows (PowerShell):
powershell -Command "$files = Get-ChildItem -Path src,tests -Filter *.java -Recurse | Select-Object -ExpandProperty FullName; javac -d out $files"
Windows (CMD):
javac -d out src\hotelmanagement\Main.java src\hotelmanagement\model\*.java src\hotelmanagement\manager\*.java src\hotelmanagement\gui\*.java src\hotelmanagement\storage\*.java src\hotelmanagement\util\*.java tests\*.java
🚀 Execution
To launch the GUI desktop application:

java -cp out hotelmanagement.Main
🧪 Testing
Run the automated test runner directly from the terminal:

java -cp out tests.TestRunner
The test runner validates:

Regex validation for emails and phone numbers.
Guest registration and duplicate rejection.
Room inventory creation and capacity assignment.
Date range validation (Check-Out > Check-In).
Overlapping reservation rejection prohibiting double-booking.
Check-In and Check-Out state machine transitions.
Subtotal, 12% GST tax, and grand total accuracy.
Operational statistics aggregation.
📖 Usage Guide
Dashboard Panel: Shows high-level statistics (Total Rooms, Available, Occupied, Total Revenue).
Guest Management:
Fill out the guest details form and click Add Guest.
Use the search bar to filter guests by name, ID, or phone.
Select a row in the table to populate form fields for updates or deletion.
Room & Reservation Management:
Room Tab: View room list, update room status, or add new rooms.
Reservation Tab: Select guest & room, enter Check-In/Out dates (YYYY-MM-DD), click Check Availability & Calculate, then click Book Reservation.
Select a reservation from the table and click Check-In Guest when the guest arrives, or Check-Out & Bill when departing.
Billing & Reporting:
Billing Tab: Select an invoice generated at check-out, click + Add Additional Service Charge to add itemized charges, and click Record Payment & Settle Invoice to complete payment.
Reporting Tab: View detailed hotel metrics and click Refresh Real-Time Report.
💾 Data Storage
All data is stored in binary serialized format inside the data/ folder:

guests.dat: Stores guest profiles.
rooms.dat: Stores room inventory and statuses.
reservations.dat: Stores active and completed reservations.
bills.dat: Stores generated invoices and payment records.
If the data/ folder is deleted or missing, the system automatically creates it and populates realistic fictional sample data on startup.

🛡️ Validation and Error Handling
Phone Number Validation: Accepts 10 to 12 digits (ValidationUtil.isValidPhone).
Email Validation: Enforces standard RFC email regex (ValidationUtil.isValidEmail).
Date Check: Rejects check-out dates on or before check-in dates.
Double-Booking Validation: Checks date range overlaps before confirming bookings.
User Alerts: Friendly dialog popups (JOptionPane.showMessageDialog) present error messages clearly without application crashing.
📸 Screenshots
(Add screenshots of your running GUI application here for submission)

Dashboard View	Guest Management
(Insert Dashboard Screenshot)	(Insert Guest Panel Screenshot)
Room & Reservation Management	Billing & Invoicing
(Insert Reservation Screenshot)	(Insert Billing Screenshot)
🔮 Future Enhancements
Exporting generated invoices to downloadable PDF format.
JFreeChart integration for visual monthly revenue bar charts.
JDBC database connector for enterprise MySQL server integration.
👤 Authors
Developer: Student Project Submission
Course: VITyarthi "Build Your Own Project" Evaluation
