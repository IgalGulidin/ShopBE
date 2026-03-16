# Shopping Website – Backend (Spring Boot)

## Overview

This project is the backend service for a full-stack shopping website.
It provides REST APIs for managing users, items, favorites, and orders.

The backend is built using **Java and Spring Boot** and follows the architecture taught in the course:

* MVC structure
* DTOs and Mappers
* Repository layer with JDBC
* Service layer with business logic
* Controller layer for REST endpoints

The system uses an **H2 in-memory database**, so the data resets whenever the backend restarts.

---

# Technology Stack

Backend framework

* Java 17
* Spring Boot

Database

* H2 (in-memory)

Architecture

* MVC
* DTO mapping
* JDBC repositories

Build tool

* Gradle

---

# Features

### User System

Users can:

* Sign up
* Log in
* Delete their account

Each user contains:

* id
* first name
* last name
* email
* password hash
* phone
* country
* city

Deleting a user removes:

* favorites
* orders
* order items

---

### Items

The system provides a list of items that users can browse and search.

Each item contains:

* id
* title
* image url
* price (USD)
* stock quantity

Items can be searched by title.

Example:

```
GET /items?search=mouse
```

---

### Favorites

Logged-in users can manage a favorites list.

Users can:

* add items to favorites
* remove items from favorites
* view their favorites

Each item appears **only once** in the list.

Endpoints:

```
GET /favorites
POST /favorites/{itemId}
DELETE /favorites/{itemId}
```

---

### Orders

Users can create and manage orders.

Order fields:

* id
* user id
* created date
* shipping address
* total price
* status

Order status values:

* TEMP (pending order)
* CLOSE (completed order)

Rules:

* each user can have **only one TEMP order**
* users can add or remove items
* stock decreases when items are added
* order becomes CLOSE after payment

Endpoints:

```
GET /orders
GET /orders/pending
POST /orders/pending/items/{itemId}
POST /orders/pending/pay
GET /orders/{orderId}
```

---

# Running the Backend

Clone the repository:

```
git clone https://github.com/IgalGulidin/ShopBE.git
```

Navigate to the project folder:

```
cd ShopBE
```

Run the application:

```
./gradlew bootRun
```

The server will start on:

```
http://localhost:8080
```

---

# H2 Database Console

You can inspect the database at:

```
http://localhost:8080/h2-console
```

JDBC URL:

```
jdbc:h2:mem:shopdb
```

Username:

```
sa
```

Password:

(empty)

---

# Example API Flow

1. Sign up

```
POST /auth/signup
```

2. Login

```
POST /auth/login
```

3. Browse items

```
GET /items
```

4. Add item to order

```
POST /orders/pending/items/{itemId}
```

5. Pay order

```
POST /orders/pending/pay
```

---

# Author

Created as part of a **Full-Stack Web Development course project**.
