<div align="center">
  <img src="https://img.icons8.com/fluency/96/000000/shield.png" alt="InsurAI Logo" width="80"/>
  <h1>🛡️ InsurAI</h1>
  <p><strong>Corporate Policy Automation & Intelligent Claim System</strong></p>

  <p>
    <img src="https://img.shields.io/badge/Java-17-orange.svg" alt="Java" />
    <img src="https://img.shields.io/badge/Spring_Boot-3.x-brightgreen.svg" alt="Spring Boot" />
    <img src="https://img.shields.io/badge/PostgreSQL-15-blue.svg" alt="PostgreSQL" />
    <img src="https://img.shields.io/badge/Node.js-18.x-success.svg" alt="Node" />
  </p>
</div>

---

## 📖 Overview
**InsurAI** is a robust, full-stack enterprise application built to digitize and automate the lifecycle of corporate insurance policies. It empowers administrators to design tailored insurance plans, enables employees to submit dynamic claims, and provides claim staff with an AI-augmented dashboard for rapid decision-making and fraud detection.

## ✨ Features
- **Role-Based Access Control (RBAC)**: Secure multi-tier architecture supporting **Admin**, **Employee**, and **Staff** personas.
- **Dynamic Policy Management**: Create, update, and deploy corporate policies instantly.
- **Intelligent Claim Processing**: End-to-end claim submission featuring rich document uploads, status tracking, and automated fraud-risk scoring.
- **Glassmorphism Analytics Dashboards**: Highly polished, modern UI with interactive visualizations.
- **Secure Authentication**: Stateless JWT (JSON Web Tokens) providing scalable security.

---

## 🛠️ Tech Stack

### Backend
- **Core**: Java 17, Spring Boot, Spring Web
- **Data**: Spring Data JPA, Hibernate, PostgreSQL
- **Security**: Spring Security, JWT (JSON Web Tokens)
- **Build Tool**: Maven

### Frontend
- **Core**: HTML5, Vanilla JavaScript (ES6+), Premium CSS3 (Glassmorphism UI)
- **Server**: Node.js / Express (Lightweight static asset delivery)
- **Libraries**: Chart.js (Analytics), Lucide Icons

---

## 📂 Project Structure
```text
📦 InsurAI
 ┣ 📂 backend/               # Spring Boot Application
 ┃ ┣ 📂 src/main/java...     # Standard Layered Architecture (Controllers, Services, Repositories)
 ┃ ┗ 📜 pom.xml              # Maven Configuration
 ┣ 📂 frontend/              # Web Client
 ┃ ┣ 📂 admin/               # Admin Dashboard Module
 ┃ ┣ 📂 employee/            # Employee Claim Module
 ┃ ┣ 📂 staff/               # Staff Review Module
 ┃ ┗ 📜 serve.js             # Local Node HTTP Server
 ┣ 📂 database/              # PostgreSQL Schemas
 ┃ ┗ 📂 seed/                # Core Initialization SQL Scripts
 ┣ 📂 docs/                  # API Guides & Test Checklists
 ┣ 📜 README.md              # Project Documentation
 ┗ 📜 .gitignore             # Centralized Security & Ignored Build Output
```

---

## 🚀 Quick Start Guide

### 1. Database Setup
1. Ensure **PostgreSQL** is running on your local machine (default port `5432`).
2. Create an empty database named `insurai_db`.
```sql
CREATE DATABASE insurai_db;
```

### 2. Backend Initialization
The backend relies on secure environment secrets.
1. Navigate to the Java configuration:`backend/src/main/resources/`
2. Copy `application.properties.example` to `application.properties`.
3. Update the copied file with your local PostgreSQL credentials and provide a secure JWT secret key.
4. Run the backend server:
```bash
cd backend
mvn clean install -DskipTests
mvn spring-boot:run
```
*(Hibernate will automatically execute DDL to generate your tables.)*

### 3. Frontend Initialization
Serve the client portal using Node:
```bash
cd frontend
npm install   # (If dependencies exist)
node serve.js
```
The application will securely boot to `http://localhost:3000`.

---

## 🔑 Default Roles & Access
*For initial testing, register via the frontend UI and assign yourself the appropriate roles. For detailed API endpoints, see `docs/postman_guide.md`.*

---

## 🛡️ License & Acknowledgements
Designed and maintained as a professional portfolio-grade engineering project.
