# 🚀 ContactHub

ContactHub is a full-stack Spring Boot web application for storing and managing personal contacts in one place. It supports secure authentication, cloud image uploads, and efficient contact search with a clean UI.

---

## 🛠️ Tech Stack

* **Backend:** Java 17, Spring Boot 3.2.5, Spring MVC
* **Frontend:** Thymeleaf, Tailwind CSS
* **Security:** Spring Security, OAuth2 (Google & GitHub)
* **Database:** MySQL
* **Cloud & Services:** Cloudinary, Java Mail

---

## ✨ Features

* 🔐 User registration with email verification
* 🔑 Login with Email/Password, Google, and GitHub
* 📇 Create, update, delete, and view contacts (CRUD)
* 🖼️ Upload contact profile images (Cloudinary)
* 🔍 Search contacts by name, email, or phone
* 📄 Paginated contact listing
* 🌐 REST API for fetching contact details

---

## 📁 Project Structure

```
src/main/java/com/scm
├── config
├── controllers
├── entities
├── forms
├── helpers
├── repositories
├── services
├── validators

src/main/resources
├── templates
├── static
├── application.properties
├── application-dev.properties
├── application-prod.properties
```

---

## ⚙️ Prerequisites

* Java 17+
* Maven
* MySQL
* Node.js (optional for Tailwind CSS)

---

## 🔐 Environment Variables

```
BASE_URL=http://localhost:8080

MYSQL_HOST=localhost
MYSQL_PORT=3306
MYSQL_DB=contactHub
MYSQL_USER=root
MYSQL_PASSWORD=your_password

GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret

GITHUB_CLIENT_ID=your_github_client_id
GITHUB_CLIENT_SECRET=your_github_client_secret

CLOUDINARY_API_KEY=your_cloudinary_api_key
CLOUDINARY_API_SECRET=your_cloudinary_api_secret
CLOUDINARY_NAME=your_cloudinary_cloud_name

EMAIL_HOST=smtp.example.com
EMAIL_PORT=587
EMAIL_USERNAME=your_email_username
EMAIL_PASSWORD=your_email_password
```

---

## ▶️ Run Locally

```
mvn spring-boot:run
```

App runs at:
http://localhost:8080

---

## 🎨 Frontend Development

```
npm install
npm run dev
```

---

## 🧪 Testing

```
mvn test
```

---

## 🌐 Main Routes

* `/register` – Create a new account
* `/login` – Sign in (Email/OAuth)
* `/user/profile` – User profile page
* `/user/contacts` – Contact list
* `/user/contacts/add` – Add contact
* `/user/contacts/view/{contactId}` – Get contact JSON

---

## 🧠 Architecture

* **Controllers** → Handle HTTP requests
* **Services** → Business logic
* **Repositories** → Database access (Spring Data JPA)
* **Entities** → Data models
* **Templates** → UI (Thymeleaf)

---

## 📊 Project Status

🟢 Core features completed

Future improvements:

* Role-based access control
* Contact sharing
* UI/UX enhancements
* Expanded REST APIs

---

## 📌 Nikhil singh

Developed as a full-stack project to practice Spring Boot, authentication, and real-world backend concepts.
