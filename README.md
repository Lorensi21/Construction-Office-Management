# Construction Office Management System

## 📖 Overview

This repository contains the **Construction Office Management System**, an advanced Java-based desktop application developed as a comprehensive university project.

**The Goal**

The primary objective of this application is to provide a robust platform to support managers in efficiently overseeing the complex operations of an architecture firm. Specifically, the system centralizes the management of regional bureaus, client projects, customer records, personnel, and physical construction zones.

Rather than implementing a simple CRUD application, the goal of this project was to tackle advanced domain modeling and architectural challenges:

*   **Complex Human Resources:** The system successfully handles dynamic and overlapping employee roles, allowing staff members to change roles or hold multiple titles simultaneously (e.g., acting as a Senior Manager and an Electrical Engineer at the same time) without data conflicts.
*   **Lifecycle Management:** It implements a strict state-machine architecture to control project phases (Drafting, Awaiting Approval, Execution), ensuring that business logic and managerial approval protocols cannot be bypassed.
*   **Data Integrity:** The system enforces strict temporal validation rules, ensuring an employee is never assigned to an exact same role within an overlapping time interval.

## 🚀 Tech Stack

*   **Language:** Java 17+
*   **Framework:** Spring Framework
*   **Persistence:** Hibernate (JPA)
*   **Database:** H2 (In-Memory)
*   **GUI:** Java Swing
*   **Boilerplate Reduction:** Lombok

## 🧠 Architectural Highlights & Design Decisions

*   **Flattened Hierarchy Strategy:** Bypassed Java's lack of multiple inheritance by implementing a dynamic role-tracking system using `EnumSet<EmployeeType>`, allowing employees to simultaneously hold overlapping roles (e.g., Manager, Engineer).
*   **State Machine for Project Lifecycle:** Delegated project state transitions (Drafting -> Approval -> Execution) to strict Service Layer methods, ensuring security rules are enforced and state lifecycles cannot be bypassed.
*   **Disjoint & Complete Inheritance:** Utilized JPA `@Inheritance(strategy = InheritanceType.JOINED)` to separate project disciplines (Landscape vs. Interior) at the database level, preventing class explosion.

## 🏃 How to Run

1.  Clone the repository: `git clone <your-repo-url>`
2.  Ensure you have Java and Maven installed.
3.  Build the project: `mvn clean install`
4.  Run the main application class: `com.construction.app.ConstructionApp`

## 📄 Documentation

Comprehensive analytical and design class diagrams, use case scenarios, and state diagrams can be found in the [`ProjectDocumentation/`](docs/) directory.