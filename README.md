# 📊 SSE Order Dashboard — Real-Time Live Updates using Spring Boot SSE

A simple **Server-Sent Events (SSE) module** integrated with a clean Order Management example.
This project demonstrates how to:

* Create a reusable **SSE broadcasting module**
* Build **real-time dashboards** without WebSockets
* Build a simple **Order system** with status updates
* Stream real-time counts to a browser
* Use **Spring Boot + Postgres + SSE** cleanly

---

## 🚀 Features

### Backend (Spring Boot)

✔ Server-Sent Events (SSE) channel manager

✔ Real-time broadcast of dashboard counts

✔ CRUD APIs for Orders (Pending / Approved / Delivered)

✔ PostgreSQL support with Spring Data JPA

✔ Custom SseEmitter timeout per channel

✔ Zero Spring Security needed

✔ Auto-serving frontend from `/static/index.html`

### Frontend

✔ Pure HTML/CSS/JS (no framework)

✔ Beautiful UI with animated cards

✔ Automatic reconnection

✔ Live activity log

✔ Works from `http://localhost:8080` without CORS issues

---

# 📁 Project Structure

```
sse-order-dashboard/
├── src/main/java/com/demo/sse_order_dashboard/
│   ├── SseOrderDashboardApplication.java
│   ├── model/
│   │   └── Order.java
│   │   └── DashboardCount.java
│   ├── controller/
│   │   └── OrderController.java
│   ├── service/
│   │   ├── OrderService.java
│   │   └── SseChannelService.java
│   └── config/
│       └── WebCorsConfig.java
│
├── src/main/resources/
│   ├── application.properties
│   └── static/
│       └── index.html   ← frontend dashboard
│
└── pom.xml
```

---

# 🛠️ Technologies Used

* **Java 17**
* **Spring Boot 3**
* **Spring Web (SSE)**
* **Spring Data JPA**
* **PostgreSQL**
* **HTML / CSS / Vanilla JS**

---

# ⚙️ Setup Instructions

## 1️⃣ Clone the repository

```bash
git clone https://github.com/arifulnoman/sse-order-dashboard.git
cd sse-order-dashboard
```

## 2️⃣ Configure PostgreSQL

Create a DB:

```sql
CREATE DATABASE sse_order_db;
```

Update `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/sse_order_db
spring.datasource.username=postgres
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.application.name=sse-order-dashboard
```

---

## 3️⃣ Run the application

Make sure Maven is installed and added to PATH:

```bash
mvn spring-boot:run
```

Backend starts at:

```
http://localhost:8080
```

Frontend dashboard (static HTML) opens at:

```
http://localhost:8080/
```

---

# 🧪 Testing the Real-Time Dashboard

## 1️⃣ Start listening to SSE

Open in browser:

```
http://localhost:8080/
```

You’ll see real-time dashboard cards & log area.

---

## 2️⃣ Create an order (POST)

Use Postman:

```
POST http://localhost:8080/api/orders

Body (JSON):
{
  "customerName": "John Doe"
}
```

Dashboard instantly updates.

---

## 3️⃣ Update order status (PUT)

```
PUT http://localhost:8080/orders/{id}/status?status=APPROVED
```

Or:

```
PUT http://localhost:8080/orders/{id}/status?status=DELIVERED
```

Dashboard updates live.

---

# 🔄 How SSE Works Here

Whenever an order is created or its status changes:

```
OrderService → SseChannelService → broadcast()
```

The frontend receives events like:

```
event: order-dashboard
data: {"pending":2,"approved":1,"delivered":0,"total":3}
```

The UI updates instantly on every event.

---

# 🧩 SSE Module Overview

The reusable SSE module includes:

### ✔ `SseChannelService`

* Manages multiple channels
* Keeps active emitters
* Cleans dead connections
* Sends events to all subscribers

### ✔ `subscribe(channel, timeout)`

Allows custom timeout per emitter instance.

### ✔ `sendToChannel(channel, eventName, data)`

Broadcasts structured updates.

You can plug this module into any Spring Boot project.

---

# 📸 Demo Preview

![SSE Dashboard](images/dashboard.png)
---

# 📦 Build for Production

To package:

```bash
mvn clean package
```

JAR will be located at:

```
target/sse-order-dashboard-0.0.1-SNAPSHOT.jar
```

Run with:

```bash
java -jar target/sse-order-dashboard-0.0.1-SNAPSHOT.jar
```

# ⭐ If you like this project

Give it a star ⭐ on GitHub!
This encourages more open-source SSE tools.
