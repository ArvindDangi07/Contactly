# Contactly – Smart Contact Manager

**Contactly** is a full-stack **Java + Spring Boot** web application that allows users to securely **store, manage, and organize contacts** with profile images, dashboards, and user authentication powered by Spring Security.
It features a responsive UI built with **Thymeleaf, HTML, CSS, and JavaScript**, and uses **MySQL + Hibernate/JPA** for data persistence.

---

## 🚀 Features

### 🔐 Authentication & Security

* User Signup & Login
* Password encryption using **BCrypt**
* **Spring Security** authentication + role-based access
* Custom `UserDetailsService` implementation
* Secure session handling

### 👥 Contact Management

* Add new contacts
* Edit contact details
* Delete contacts
* View contact details
* Upload contact images
* Pagination-ready UI design (via Thymeleaf)

### 👤 User Profile

* View profile
* Update profile information
* Upload profile picture

### 📱 UI & UX

* Fully responsive front-end (mobile + desktop)
* Thymeleaf dynamic templates
* Custom CSS & JavaScript interactions
* Reusable base layout (`base.html`)

### ⚙️ Backend System

* Spring MVC + Spring Boot architecture
* Hibernate/JPA ORM with MySQL
* DAO layer (`UserRepository`, `ContactRepository`)
* Service-layer-based security
* Custom `Message` helper for flash messages

---

## 🛠️ Tech Stack

**Backend:**

* Java
* Spring Boot
* Spring MVC
* Spring Security
* Hibernate / JPA
* Maven

**Frontend:**

* HTML5
* CSS3
* JavaScript
* Thymeleaf

**Database:**

* MySQL

**Tools:**

* Git & GitHub
* Spring Initializr

---

## 📂 Project Structure (Real Folder Mapping)

```
Contactly-main/
 ├── src/main/java/com/smart/
 │   ├── config/                 # Security configuration
 │   │   ├── CustomUserDetails.java
 │   │   ├── MyConfig.java
 │   │   └── UserDetailsServiceImpl.java
 │   ├── controller/             # MVC Controllers
 │   │   ├── HomeController.java
 │   │   └── UserController.java
 │   ├── dao/                    # Repositories
 │   │   ├── ContactRepository.java
 │   │   └── UserRepository.java
 │   ├── entities/               # Hibernate Entities
 │   │   ├── Contact.java
 │   │   └── User.java
 │   └── helper/                 # Utility classes
 │       └── Message.java
 ├── src/main/resources/
 │   ├── application.properties
 │   ├── static/
 │   │   ├── css/style.css
 │   │   ├── js/script.js
 │   │   └── image/
 │   └── templates/
 │       ├── base.html
 │       ├── home.html
 │       ├── login.html
 │       ├── signup.html
 │       └── user/
 │           ├── add_contact.html
 │           ├── contact_detail.html
 │           ├── show_contacts.html
 │           ├── profile.html
 │           └── setting.html
 ├── uploads/                    # Runtime image uploads
 ├── pom.xml
 └── mvnw / mvnw.cmd
```

---

## ⚙️ Configuration

### Database (`application.properties`)

```
spring.datasource.url=jdbc:mysql://localhost:3306/contactly
spring.datasource.username=root
spring.datasource.password=yourPassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

---

## ▶️ Running the Project

### 1. Clone the repo

```bash
git clone https://github.com/<your-username>/Contactly.git
cd Contactly-main
```

### 2. Build the project

```bash
mvn clean install
```

### 3. Run using Spring Boot

```bash
mvn spring-boot:run
```

Application runs on:
👉 **[http://localhost:8080](http://localhost:8080)**

---

## 🔒 Security Implementation

* Custom `UserDetailsServiceImpl` loads users from DB
* `MyConfig` defines:

  * Security filter chain
  * PasswordEncoder (BCrypt)
  * Login + Logout behavior
* Role-based URL restrictions
* Form-based login saved in Thymeleaf templates

---

## 🖼️ Screenshots

*(Add screenshots here)*

---

## 📌 F![Wh![W![WhatsApp Image 2025-11-30 at 12 20 42 AM](https://github.com/user-attachments/assets/d261f0f3-e905-41d7-8236-98f8098134f7)
hatsApp Image 2025-11-30 at 12 1![WhatsApp Image 2025-11-30 at 12 21 10 AM](https://github.com/user-attachments/assets/54d9518b-10e7-436e-9be6-6c3f65345a5a)
1 10 AM](https://github.com/user-attachments/assets/88459820-655b-4a3a-a02d-91156a42e43d)
atsApp Image 2025-11-30![WhatsApp Image 2025-11-30 at 12 22 52 AM](https://github.com/user-attachments/assets/d2587d3c-ac5d-4ab0-b7d2-7ecdb875d48f)
 ![WhatsApp Image 2025-11-30 at 12 21 21 AM](https://github.com/user-attachments/assets/73bb44fb-63ad-4b3f-bafe-9911040208e5)
at 12 10 49![WhatsApp Image 2025-11-30 at 12 26 08 AM](https://github.com/user-attachments/assets/f62d4620-e014-4788-96ac-8d9b17fa0e06)
 AM](https://github.com/user-attachments/assets/1b8c35cf-c8d7-4d69-8aae-e11dd1517f03)
uture Enhancements

* Pagination for contacts
* Search functionality
* Export contacts as PDF
* Email OTP verification
* Cloud image storage (AWS S3)

---

## 🤝 Contributing

Pull requests are welcome!
Please open an issue first to discuss changes.

---

## 📞 Contact

**Arvind Kumar**
GitHub:https://github.com/ArvindDangi07

---
