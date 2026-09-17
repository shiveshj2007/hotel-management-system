# Project Statement & Scope Document

## Project Title
# HOTEL MANAGEMENT SYSTEM

**Submitted for VITyarthi "Build Your Own Project" Evaluation**

---

# 1. Problem Statement

Modern hotel management and front-desk operations require seamless, error-free management of guest profiles, room inventories, real-time availability checks, date validations, itemized service billing, and financial reporting. Traditional manual record-keeping or unvalidated spreadsheet applications introduce critical operational errors, including:

1. **Double-Booking Overlaps**: Assigning the same hotel room to multiple guests for overlapping date intervals.
2. **Inaccurate Invoicing & Tax Miscalculations**: Manual errors in calculating stay durations, missing itemized additional service charges (e.g., room service, laundry, spa), or incorrect tax calculations.
3. **Data Loss & Corruption**: Lack of structured, persistent storage leading to data loss upon system shutdown.
4. **Inefficient Operational Workflow**: Inability to quickly search guest records, perform rapid check-in/check-out transitions, or view instant occupancy metrics.

To overcome these operational challenges, the **Hotel Management System** provides a desktop GUI-based, modular Java application. It automates guest management, room inventory control, reservation overlap validation, automated itemized invoicing with 12% GST tax calculations, and real-time analytical reporting.

---

# 2. Scope of the System

### What the System Covers:
* **Module 1 — Guest Management**: Complete CRUD operations (Add, View, Search, Update, Delete) for hotel guests with strict phone (10–12 digits) and email regex validation.
* **Module 2 — Room & Reservation Management**: Room classification (Single, Double, Deluxe, Suite), pricing, capacity, and operational status tracking (Available, Reserved, Occupied, Maintenance). Includes booking creation with double-booking overlap prevention, check-in, check-out, and cancellation state transitions.
* **Module 3 — Billing & Reporting**: Automated invoice generation upon checkout combining room charges (`Nights * Rate`) and itemized service charges (Food, Laundry, Spa), applying 12% GST tax (`Subtotal * 0.12`), recording payment settlement (Cash, Credit Card, UPI), and providing a real-time analytics reporting dashboard.
* **Data Persistence**: Binary file serialization storing all records persistently in the `data/` directory with automatic initial sample data seeding.

### What the System Does Not Cover (Out of Scope):
* Remote cloud database servers (MySQL/PostgreSQL) — designed intentionally as a standalone offline desktop application requiring zero external database setup.
* Online payment gateway integration (e.g., Stripe/Razorpay) — payment modes are recorded locally.
* Multi-hotel enterprise chain synchronization — focused on single-property hotel operations.

---

# 3. Target Users

1. **Receptionist / Front-Desk Staff**: Handles guest registrations, room searches, booking check-ins, itemized service charge additions, and check-out processing.
2. **Hotel Manager / Administrator**: Oversees room inventory settings, inspects financial billing records, and analyzes revenue/occupancy statistical reports.

---

# 4. High-Level Major Modules

### Module 1: Guest Management
- Auto-generated unique IDs (`GST-1001`, `GST-1002`).
- Mandatory fields & regex format validation for phone and email.
- Real-time search across guest IDs, names, phone numbers, and email addresses.
- Confirmation dialogs before destructive operations (Delete Guest).

### Module 2: Room & Reservation Management
- **Room Management**: Room inventory grid, room type configuration (Single, Double, Deluxe, Suite), price per night, capacity, and status updates (`AVAILABLE`, `RESERVED`, `OCCUPIED`, `MAINTENANCE`).
- **Reservation Management**: Links Guest -> Room -> Check-In Date -> Check-Out Date. Date range validation (`Check-Out > Check-In`), overlap check prohibiting double-booking of active reservations, Check-In state transition (Room -> `OCCUPIED`), and Check-Out transition (Room -> `AVAILABLE`).

### Module 3: Billing & Reporting
- **Billing**: Itemized invoice generation upon checkout, room charge calculations, itemized service charge additions, subtotaling, 12% GST tax computation (`Bill.DEFAULT_TAX_RATE = 0.12`), payment settlement recording, and invoice history viewing.
- **Reporting**: Analytical dashboard presenting total rooms, available rooms, occupied rooms, occupancy rate percentage, total reservations, collected revenue, and pending balances.
