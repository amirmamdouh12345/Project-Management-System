# Project Management System

## 📌 Overview

The **Project Management System** is a Spring Boot–based backend application designed to manage organizational structure and workflows within a company. It focuses on **Departments, Teams, Employees, Projects, and Vacation Management**, with clear role-based responsibilities and decision flows.

The system follows **clean architecture principles**, separation of concerns, and real-world business rules similar to enterprise HR and project management systems.

---

## 🏗️ Core Modules & Features

### 1️⃣ Department Management

* Create, update, delete departments
* Each department:

  * Has a **unique manager** (one active manager only)
  * Can have multiple projects
* Assign existing projects to a department or create new ones during department creation

**Business Rules:**

* Only one active manager is allowed per department
* Department manager is always an employee belonging to that department

---

### 2️⃣ Team Management

* Teams belong to a **single department**
* Each team can have:

  * Multiple employees
  * One Tech Lead

**Business Rules:**

* Teams cannot exist without a department
* Only one active Tech Lead is allowed per team

---

### 3️⃣ Employee Management

* Add, update, activate, deactivate employees
* Assign employees to:

  * Department only
  * OR Team (which implicitly assigns the department)

**Employee Roles:**

* EMPLOYEE
* TECHLEAD
* MANAGER

**Role Constraints:**

* MANAGER:

  * Assigned directly to a department
  * Cannot belong to a team
  * Only one active manager per department
* TECHLEAD:

  * Assigned to a team
  * Only one active tech lead per team

---

### 4️⃣ Project Management

* Create projects
* Assign projects to departments
* Update project details

Projects are always owned by **departments**, not teams.

---

### 5️⃣ Vacation Management System

#### Vacation Requests

* Employees can request vacations
* Each request has a lifecycle and status

#### Decision Flow (Strategy Pattern)

Vacation decisions are handled using a **Strategy-based design**:

* Common decision steps are shared
* Business logic differs by role

**Decision Types:**

* Team Lead Decision
* Department Manager Decision

Each decision type:

* Has its own request DTO
* Has its own decision service implementation
* Implements a common interface

This design:

* Avoids `if/else` role checks
* Is easily extendable (Open/Closed Principle)

---

### 6️⃣ Vacation Balance Logic

* Vacation and sick leave balances are calculated dynamically
* Based on the current day of the month

---

## 🧠 Architecture & Design Principles

### ✅ Clean Architecture

* Controllers → handle HTTP requests
* Services → business logic
* Repositories → database access
* DTOs → request/response isolation

### ✅ Dependency Injection

* Constructor / field injection via Spring
* Clear service boundaries
* Avoids tight coupling

### ✅ Strategy Pattern

Used in vacation decision handling to support multiple decision logics with shared behavior.

### ✅ Validation & Business Rules

* Role-based constraints enforced at service layer
* Data consistency ensured before persistence

---

## 🛠️ Technology Stack

* Java
* Spring Boot
* Spring Data JPA
* Hibernate
* Lombok
* RESTful APIs
* MySQL / PostgreSQL (configurable)

---

## 🔐 Future Enhancements (Planned)

* Authentication & Authorization (Spring Security, JWT)
* Role-based access control
* Audit logging
* API documentation (Swagger / OpenAPI)
* Pagination & filtering

---

## 📂 Project Structure

```
com.projectManagement.demo
├── Controllers
├── Services
│   ├── DecisionImplementations
│   ├── Core Services
├── Repos
├── Entities
├── DTOs
├── Enums
```

---

## 🎯 Key Takeaways

* Models real-world organizational structure
* Enforces strict business rules
* Designed for scalability and maintainability
* Interview-ready architecture and patterns

---

## 👨‍💻 Author

**Amir Mamdouh**

Backend Java Developer | Spring Boot

---

> This project demonstrates practical backend engineering skills, clean design patterns, and enterprise-level thinking.
