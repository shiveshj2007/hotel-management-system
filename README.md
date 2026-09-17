# HOTEL MANAGEMENT SYSTEM

Hotel Management System, a complete functional GUI based Java desktop application suitable for the VITyarthi "Build Your Own Project" evaluation.

The system features a custom built swing graphical interface, 4 layer architecture, file based binary data persistence, auto itemized billing with 12% GST
tax calculation, double-booking overlap validation, and real time operational analytics reporting dashboard.
---

## Contents
- [Project Overview](#-project-overview)
- [Problem Being Solved](#-problem-being-solved)
- [Objectives](#-objectives)
- [Major Functional Modules](#-major-functional-modules)
- [1. Guest Management](#1-guest-management)
- [2. Room & Reservation Management](#2-room--reservation-management)
- [3. Billing & Reporting](#3-billing--reporting)
- [Features](#-features)
- [Technologies Used](#-technologies-used)
- [System Requirements](#-system-requirements)
- [Project Structure](#-project-structure)
- [Architecture](#-architecture)
- [Installation & Setup](#-installation--setup)
- [Compilation](#-compilation)
- [Execution](#-execution)
- [Testing](#-testing)
- [Usage Guide](#-usage-guide)
- [Data Storage](#-data-storage)
- [Validation and Error Handling](#-validation-and-error-handling)

- [Future Enhancements](#-future-enhancements)
- [Authors](#-authors)
---

## 📌 Project Overview

The Hotel Management System is intended to reduce front-desk administrative burden and managerial overhead for hotel establishment by automation of guest registrations, room inventory, booking life-cycle management, itemized service invoicing and payment settlements.
---

## 🎯 Problem Being Solved
Manual hotel administration requires vulnerable paper ledgers or loose spreadsheets resulting in:

1. Double-Booking Overlaps
2. Financial Inaccuracies
3. Data loss
4. Slow front-desk queries...
The system provides a desktop application addressing these issues through automatic date validation, regex input checks, itemized billing, local file persistence, and analytics dashboard generation.
---

## 🏁 Objectives
- Design a modular 4 layer Java desktop application complying trictly with objectT oriented software engineering principles.
- Automate front-desk guest registration, room booking, check-in, check-out and invoicing.
- Eliminate double-booking errors through mathematical date-range overlap validation algorithms.
- Provide reliable local data using binary file serialization (`data/.dat`) without external database.
- Provide executable command-line build and testing environment accessible through standard JDK tools.
---
## 🚀 Major Functional Modules

### 1. Guest Management
Guest Registration: Add guest with auto generated unique ID (`GST-1001`
Regex Input Validation: Validate phone numbers (10-12 digits) and email formatti
Search & Filter: Real time multi-field search across guest IDs, names, phones, and emails.
Profile Controls: 
Update guest details and delete profiles with confirmation dialog.

### 2. Room & Reservation Management
Room Inventory: Manage room numbers, types (Single, Double, Deluxe, Suite), pricing, capacity, and operational status (Available, Reserved, Occupied, Maintenance).
Reservation Booking: Connect Guest -> Room -> Check-In Date -> Check-Out Date.
Double-Booking Prevention: Date interval overlap validation for active reservations.
Lifecycle State Machine: Transition reservation and room statuses through Check-In, Check-Out, and Cancellation workflows.
### 3. Billing & Reporting
Itemized Invoicing: Auto-generate invoice combining room charges (`Nights Price/Night` and itemized additional service charges (Food, Laundry, Spa).
Tax Calculation: Configure 12% GST tax rate application (`Subtotal 0.12`) and calculate Grand Total (`Subtotal + Tax`).
Payment Settlement: Record payment mode (Cash, Credit Card, UPI) and update invoice status to `PAID`.
Analytical Dashboard: Visual dashboard cards for total rooms, occupancy percentage, active reservations, total revenue collected, and pending receivables.
---
## ✨ Features
- Modern Swing GUI: Custom dark slate header, color coded status badges, styled forms and interactive `JTable` data grids.
- Auto ID Generation: Auto generation of structured IDs (`GST-1001` `RES-1001`, `INV-1001`).
- Date Interval Overlap Checker: Algorithmic check (`start1< end2 && start2  Manager -> Model -> Storage)
- Data Storage: Local Binary File Serialization (data/.dat)
-
 Testing: Standalone Executable Java Test Suite (`tests/TestRunner.java)
---
## 💻 System Requirements
- Operating System: Windows 10/11, macOS, or Linux
- Java Development Kit (JDK): JDK 11 or higher
- RAM: 2 GB minimum (4 GB recommended)
- Disk Space: 50 MB available space
---

## 📁 Project Structure
```
hotel-management-system/
├── src/
│ └── hotelmanageent/
│  ├── Main.java      # Entry point, launches MainFrame GUI
│  ├── model/       # Encapsulated Data Models
│  │ ├── Guest.java
│  │ ├── Room.java
│  │ ├── RoomType.java (Enum)
│  │ ├── RoomStatus.java (Enum)
│  │ ├── Reservation.java
│  │ ├── ReservationStatus.java (Enum)
│  │ ├── ServiceCharge.java
│  │ ├── Bill.java
│  │ └── PaymentStatus.java (Enum)
│  ├── manager/       # Business Logic Layer
│  │ ├── GuestManager.java
│  │ ├── RoomManager.java
│  │ ├── ReservationManager.java
│  │ ├── BillingManager.java
│  │ └── ReportGenerator.java
│  ├── gui/        # Swing GUI Layer
│  │ ├── MainFrame.java    # Main Window with navigation bar
│  │ ├── DashboardPanel.java   # Metric overview panel
│  │ ├── GuestPanel.java    # Module 1 GUI
│  │ ├── RoomReservationPanel.java # Module 2 GUI
│  │ ├── BillingPanel.java   # Module 3A GUI
│  │ ├── ReportPanel.java    # Module 3B GUI
│  │ └── UITheme.java     # Theme palette & styling constants
│  ├── storage/
│  │ └── FileManager.java    # Persistent binary I/O handler
│  └── util/
│   ├── ValidationUtil.java   # Regex input validation
│   └── DateUtil.java    # Date parsing & overlap logic
├── data/         # Local File Persistence (Auto-seeded)
│ ├── guests.dat
│ ├── rooms.dat
│ ├── reservations.dat
│ └── bills.dat
├── tests/         # Standalone Test Suite
│ ├── BusinessLogicTest.java    # Unit test cases
│ └── TestRunner.java      # Executable CLI test runner
├── diagrams/        # Architecture & System Diagrams(Mermaid)
│ ├── architecture.md
│ ├── workflow.md
│ ├── use-case.md
│ ├── class-diagram.md
│ └── sequence.md
├── report/
│ └── PROJECT_REPORT.md     # 15-Section Academic Report
├── README.md        # Project Documentation
└── statement.md        # Problem Statement & Scope
```
---
## 🏗️ Architecture
```
GUI Layer (Swing Panels & Components)
│
▼
Business Logic Layer (GuestManager, RoomManager, ReservationManager,BillingManager,ReportGenerator)
│
▼
Domain Model Layer (Guest, Room, Reservation, Bill, ServiceCharge, Enums)
│
▼
Persistence Layer (FileManager -> Local binary files in data/.dat)
```
---
## ⚙️ Installation & Setup
1. Clone/Download the Repository:
```cmd
git clone https://github.com/username/hotel-management-system.git
cd hotel-management-system
```
2. Verify Java Installation:
Ensure `javac` and `java` are available in your system path:
```cmd
java -version
javac -version
```
---
## 🔨 Compilation
To compile all Java source files into the `out/` directory:
### Windows (PowerShell):
```powershell
powershell -Command "$files = Get-ChildItem -Path src,tests -Filter .java -Recurse | Select-Object -ExpandProperty FullName; javac -d out $files"
```
### Windows (CMD):
```cmd
javac -d out src\hotelmanagement\Main.java src\hotelmanagement\model\.java src\hotelmanagement\manager\.java src\hotelmanagement\gui\.java src\hotelmanagement\storage\.java src\hotelmanagement\util\.java tests\.java
```
---
## 🚀 Execution
To launch the GUI desktop application:
```cmd
java -cp out hotelmanagement.Main
```
---
## 🧪 Testing
Run the automated test runner directly from the terminal:
```cmd
java -cp out tests.TestRunner
```
The test runner validates:
1. Regex validation for emails and phone numbers.
2. Guest registration and duplicate rejection.
3. Room inventory creation and capacity assignment.
4. Date range validation (`Check-Out > Check-In`)
5. Overlapping reservation rejectionprohibiting double booking.
6. Check-In and Check-Out state machine transitions.
7. Subtotal12% GST tax, and grand total accuracy.
8. Operational statistics aggregation.
---
## 📖 Usage Guide
1. Dashboard Panel: Shows high level statistics (Total Rooms, available, occupied, T0TAL Revenue).
2. Guest Management:
- Fill out the guest details form and click Add Guest.
- Use the search bar to filter guests by name, ID, or phone.
- Select a row in the table to populate form fields for updates or deletion.

3. Room & Reservation Management:
- Room Tab: View room list, update room status, or add new rooms.
- Reservation Tab: Select guest & room, enter Check-In/Out dates (`YYYY-MM-DD`), click Check Availability & Calculate, then click Book Reservation.
- Select a reservation from the table and click Check-In Guest when the guest arrives, or Check-Out & Bill when departing.



4. Billing & Reporting:
- Billing Tab: Select an invoice generated at check-out, click + Add Additional Service Charge to add itemized charges, and click Record Payment & Settle Invoice to complete payment.
- Reporting Tab: View detailed hotel metrics and click Refresh Real-Time Report.
---
## 💾 Data Storage
All data is storedin binary serialized format insidehe `data/` folder:
- `guests.da`: Stores guest profiles.
- `rooms.dat: Stores room inventory and statuses.
- `reservations.dat`: Stores active and completed reservations
- `bills.dat: Stores generated invoices and payment records.
If the `data/` folder is deleted or missing, the system auto creates it and populates realistic fictional sample data on startup.
---
## 🛡️ Validation and Error Handling
- Phone Number Validation: Accepts 10 to 12 digits (`ValidationUtil.isValidPhone`).
- Email Validation: Enforces standard RFC email regex (`ValidationUtil.isValidEmail`).
- Date Check: Rejects check-out dates on or before checkn dates.
- DoublBooking Validation: Checks date range overlaps before confirming bookings.
- User Alerts: Friendly dialog popups (`JOptionPane.showMessageDialog`) presents error messages clearly without application crashing.
---

---
## 🔮 Future Enhancements
- Exporting generated invoices to downloadable PD format.
- JFreeChart integration for visual monthly revenue bar charts.
- JDBCdatabaseconnector for enterprise MySQL server integration
---
## 👤 Authors
- Developer: Student Project Submission
- Course: VITyarthi "Build Your Own Project" Evaluation
