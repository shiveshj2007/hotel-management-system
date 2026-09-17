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
