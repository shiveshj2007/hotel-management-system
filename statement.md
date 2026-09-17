# Project Statement & Scope Document



## Project Title

# HOTEL MANAGEMENT SYSTEM





Submitted for VITyarthi "Build Your Own Project" Evaluation

---



# 1. Problem Statement


Modern hotel management and front-desk operations require effective management of guest profiles, room inventories, availability checks, date validations and itemized service billing and financial reporting. Traditional manual record-keeping or unvalidated spreadsheet applications introduce critical errors such as



1. Double-Booking overlaps (Same hotel room assigned to multiple guests for overlapping date intervals)

2. Inaccurate invoicing and tax miscalculations (Manual errors in calculating stay durations, missing itemized additional service charges or incorrect tax calculations)

3. Data loss and corruption (Lack of structured, persistent storage)

4. Inefficient operational workflow (Inabilityto search guest records, perform rapid check-in/check-out transitions or view instant occupancy metrics)



To overcome these errors the Hotel Management System provides a desktop GUI-based modular Java application that automates guest management, room inventory control, reservation overlap validation, itemized invoicing (12% GST tax calculations) and real-time analytical reports.

---



# 2. Scope of the System



### What the system covers:

Module 1 — Guest Management: Complete CRUD (Add, View, Search,Update, Delete) of hotel guests with phone (10–12 digits) and email regex validation.

Module 2 — Room & Reservation Management: Classification of room (Single, Double, Deluxe, Suite) pricing, capacity and operational status (Available, Reserved, Occupied, Maintenance). Includes booking creation (double-booking overlap prevention), check-in, check-out and cancellation state transitions.

Module 3 — Billing & Reporting: Automated invoice generation upon checkou combined room charges (Nights Rate) and itemized service charges (Food, Laundry, Spa) with 12% GST tax (Subtotal 0.12) recording payment settlement (Cash, Credit Card, UPI) and a real-time analytics reporting dashboard.

Data Persistence: Binary file serialization storing all records persistently in the data/ directory with automatic initial sample data seeding.



### What the system does not cove Out Of Scope):

Remote cloud database servers (MySQL/PostgreSQL) — designed as a standalone offline desktop application requiring no external database setup.

Online payment gateway integration (e.g. Stripe/Razorpay) — payment modes are recorded locally.

Multi-hotel enterprise chain synchronization — focused on single-property hoteloperations.

---



# 3. Target Users



1. Receptionist / Front-Desk Staff: Handles guest registrations,room searches, booking checkout, itemized service charge additions and check-out processing.

2. Hotel Manager /Administrator: Oversees room inventory settings, inspects financial billing records and analyzes revenue/occupancy statistical reports.

---



# 4. High-level Major Modules



### Module 1: Guest Management

- Auto-generated unique IDs (GST-1001,GST-1002)

- Mandatory fields and regex format validation for phone and email.

- Real-time search across guest IDs, names, phone numbers and email addresses.

- Confirmation dialogs before destructive operations (Delete Guest).



### Module 2: Room & Reservation Management

- Room Management: Room inventory grid, room type configuration Single, Double, Deluxe, Suite), price per night, capacity and status updates (AVAILABLE, RESERVED, OCCUPIED, MAINTENANC).

- Reservation Management: Links Guest -> Room -> Check-In Date -> Check-Out Date. Date range validation (Check-Out > Check-In), overlap check prohibiting double-booking of active reservations, Check-In state transition (Room -> OCCUPIED), and Check-Out transition (Room -> AVAILABLE).



### Module 3: Billing & Reporting

- Billing: Itemized invoice generation upon checkout, room charge calculations, itemized service charge additions, subtotaling, 12% GST tax computation (Bill.DEFAULTTAXRATE = 0.12), payment settlement recording and invoice history viewing.

- Reporting: Analytical dashboard presenting total rooms, available rooms, occupied rooms, occupancy rate percentage, total reservations, collected revenue and pending balances.