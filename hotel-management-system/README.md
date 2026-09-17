# HOTEL MANAGEMENT SYSTEM



This HOTEL MANAGEMENT SYSTEM is a complete, functional desktop application implemented in Java and designed according to the VITyarthi "Build Your Own Project" evaluation criteria.



A custom Swing user interface, 4-layer architecture, file-based binary storage, automated itemized billing with 12% tax calculation, double-booking prevention logic and operational analytics dashboard reporting are among the key features.



## 📋 Table of Contents

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

- [Screenshots](#-screenshots)

- [Future Enhancements](#-future-enhancements)

- [Authors](#-authors)

---



## 📌 Project Overview

The Hotel Management System is designed to automate front-desk administrative processes and managerial accounting operations in the hospitality industry. It handles guest registrations, room inventory management, check-in/check-out procedures, itemized service-based billing, payments settlements and financial reporting.

---



## 🎯 Problem Being Solved

Many hotel owners still maintain paper records or use spreadsheet programs for their daily operations which introduces several problems:

1. Double-booking errors when assigning rooms to multiple guests staying on the same dates

2. Financial inconsistencies due to miscalculations of stay duration or omitted charges

3. Record loss risk due to improper digital data storage

4. Lengthy manual data lookup procedures



This system addresses these issues by providing a desktop application that:



- Prevents double-booking through automatic date validation

- Enforces input validation through regular expressions

- Tracks itemized invoices with automated 12% tax calculation

- Provides file-based data storage

- Contains operational analytics dashboard

---

## 🏁 Objectives

1. Design and implement a 4-tier Java desktop application that follows object-oriented design principles

2. Automate front-desk registration, room booking and billing processes

3. Prevent double-booking of hotel rooms through mathematical date validation

4. Provide reliable data storage using file-based binary storage

5. Create an executable command-line for testing purposes

---

## 🚀 Major Functional Modules



### 1. Guest Management

- Add/edit/delete functionality with automatic ID generation (GST-1001)

- Input field validation using regular expressions

- Searching mechanism that filters results in real-time

- Dynamic updates to the database



### 2. Room & Reservation Management

- Room inventory maintenance with category selection

- Reservation booking with availability check

- Double-booking prevention using date math

- Check-in/check-out operations

- Room status tracking

### 3. Billing & Reporting

- Itemized billing with food, laundry and other charges

- Tax calculation with configurable percentage

- Payment method tracking and invoice settlement

- Operational analytics dashboard with:

- Room occupancy percentage

- Revenue statistics

- Active reservations count

---

## ✨ Features

- Customized graphical interface using Java Swing

- Automatic ID generation with predefined patterns

- Date validation to prevent double-booking

- Itemized service charge billing with tax calculation

- Visual analytics dashboard

- File-based binary database

- Search and filtering capabilities

- Cross-platform compatibility

- Lightweight with no external dependencies

---

## 🛠️ Technologies Used

| Technology | Version |

|----------|---------|

| Programming Language | Java SE 11 |

| GUI Framework | Java Swing |

| Architecture | 4-tier |

| Database | File-based binary storage |

| Testing | Built-in test suite |

---

## 💻 System Requirements

| Requirement | Specification |

|-------------|----------------|

| OS | Windows 10/11, macOS or Linux |

| RAM | 2 GB (4 GB recommended) |

| Storage | Minimum 50 MB |

| Java | JDK 11+ |

---



## 📁 Project Structure

```

hotel-management-system/

├── src/

│ └── hotelmanagement/

│ ├── Main.java # Main entry point

│ ├── model/ # Data models

│ │ ├── Guest.java

│ │ ├── Room.java

│ │ ├── RoomType.java

│ │ ├── RoomStatus.java

│ │ ├── Reservation.java

│ │ ├── ReservationStatus.java

│ │ ├── ServiceCharge.java

│ │ ├── Bill.java

│ │ └── PaymentStatus.java

│ ├── manager/ # Business logic

│ │ ├── GuestManager.java

│ │ ├── RoomManager.java

│ │ ├── ReservationManager.java

│ │ ├── BillingManager.java

│ │ └── ReportGenerator.java

│ ├── gui/ # GUI components

│ │ ├── MainFrame.java

│ │ ├── DashboardPanel.java

│ │ ├── GuestPanel.java

│ │ ├── RoomReservationPanel.java

│ │ ├── BillingPanel.java

│ │ ├── ReportPanel.java

│ │ └── UITheme.java

│ ├── storage/

│ │ └── FileManager.java

│ └── util/

│ ├── ValidationUtil.java

│ └── DateUtil.java

├── data/ # Database

│ ├── guests.dat

│ ├── rooms.dat

│ ├── reservations.dat

│ └── bills.dat

├── tests/ # Test suite

│ ├── BusinessLogicTest.java

│ └── TestRunner.java

├── diagrams/ # Design documents

│ ├── architecture.md

│ ├── workflow.md

│ ├── use-case.md

│ └── class-diagram.md

├── report/

│ └── PROJECT_REPORT.md

├── README.md

└── statement.md

```

---

## 🏗️ Architecture

```

GUI Layer

|

▼

Business Logic Layer

|

▼

Domain Model Layer

|

▼

Persistence Layer

```

---

## ⚙️ Installation & Setup

1. Clone the repository to your local machine:

```

git clone https://github.com/username/hotel-management-system.git

cd hotel-management-system

```

2. Verify that Java is installed:

```

java -version

javac -version

```

---

## 🔨 Compilation

To compile all Java classes to the `out/` directory:

Windows (CMD):

```

javac -d out src\hotelmanagement\Main.java src\hotelmanagement\model\.java src\hotelmanagement\manager\.java src\hotelmanagement\gui\.java src\hotelmanagement\storage\.java src\hotelmanagement\util\.java tests\.java

```

Windows (PowerShell):

```

powershell -Command "$files = Get-ChildItem -Path src,tests -Filter .java -Recurse | Select-Object -ExpandProperty FullName; javac -d out $files"

```

---

## 🚀 Execution

To start the application:

```

java -cp out hotelmanagement.Main

```

---

## 🧪 Testing

To run the test suite:

```

java -cp out tests.TestRunner

```

The test suite contains:

1. Regex validation tests

2. Guest registration tests

3. Room inventory tests

4. Date range validation tests

5. Double-booking prevention tests

6. Check-in/check-out process tests

7. Tax calculation tests

8. Dashboard generation tests

---

## 📖 Usage Guide

1. Dashboard Panel - View operational statistics

2. Guest Management

- Fill out the form and click Add Guest

- Use the search bar to find specific entries

- Select a row to update or delete information

3. Room & Reservation Management

- Room Tab: Maintain room inventory

- Reservation Tab: Make reservations by selecting:

- Guest

- Room

- Check-in date (YYYY-MM-DD)

- Check-out date (YYYY-MM-DD)

- Click Check Availability & Calculate

- Then click Book Reservation

- Select a reservation and click Check-In Guest

- When leaving, click Check-Out & Bill

4. Billing & Reporting

- Billing Tab: Select an invoice from check-out

- Click + Add Additional Service Charge

- Then click Record Payment & Settle Invoice

- Reporting Tab: View reports and click Refresh Real-Time Report

---

## 💾 Data Storage

The system uses file-based storage and stores information in the `data/` directory:

| File | Description |

|------|-------------|

| `guests.dat` | Guest information |

| `rooms.dat` | Room inventory information |

| `reservations.dat` | Reservation information |

| `bills.dat` | Invoice information |

If the `data/` directory is deleted, the system will automatically recreate it with sample data.

---

## 🛡️ Validation and Error Handling

- Phone number validation: 10-12 digits

- Email validation: RFC standard format

- Date validation: Check-out date must be after check-in

- Double-booking validation: Prevents overlapping reservations

- All error messages shown to the user are friendly and informative

---

## 📸 Screenshots

The following screenshots show the different panels of the application:

| Dashboard View | Guest Management |

| :---: | :---: |

| ![Dashboard](dashboard.png) | ![Guest Management](guests.png) |

| Room & Reservation Management | Billing & Invoicing |

| :---: | :---: |

| ![Room & Reservation Management](rooms.png) | ![Billing & Invoicing](billing.png) |

---

## 🔮 Future Enhancements

- Adding PDF export support for invoices

- Chart reporting using JFreeChart

- Connecting to a MySQL database using JDBC

---

## 👤 Authors

This project was created as part of the VITyarthi "Build Your Own Project" course by SHivesh Jha.
