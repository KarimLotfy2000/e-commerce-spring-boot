# 🛍️ E-Commerce Backend (Spring Boot)

A backend system for an **e-commerce platform**, built with **Spring Boot**, featuring authentication, product management, cart functionality, and order processing.

---

## 🚀 Features

### 🔐 Authentication & Security
- User registration & login with **JWT authentication**.
- Role-based access control (**USER**, **ADMIN**).
- Secure API endpoints with **Spring Security**.
- CORS configuration for client communication.

### 🛒 Product & Category Management
- Browse products with **dynamic filtering** (gender, category, brand, price range, sorting).
- Optimized queries using **JPQL** for product previews.
- **Size variants management** (each product has multiple sizes with individual stock & price).
- **Category modification operations** (Admin-only).

### 🛍️ Cart Management
- Users can **add, update, and remove** items in the cart.
- Supports **size-specific stock validation** before adding items.
- Automatically updates **cart subtotal and total price**.

### 📦 Order Processing & Address Management
- Users can place orders using **saved addresses or new addresses**.
- Order history retrieval with **secure access control**.
- Stock validation and deduction upon order confirmation.

---

## ⚙️ Tech Stack

- **Spring Boot** (REST API)
- **Spring Security & JWT** (Authentication)
- **JPA & Hibernate** (ORM)
- **MySQL** (Relational Database)
- **ModelMapper** (DTO Mapping)

---

## 📖 API Endpoints

### 🔐 Authentication (`/api/v1/auth`)
- `POST /register` - Register a new user.
- `POST /login` - Authenticate user & get JWT token.
- `GET /me` - Fetch authenticated user profile.

### 🛒 Products (`/api/v1/products`)
- `GET /` - Get all products (with filters & sorting).
- `GET /{id}` - Get product details.
- `GET /brands` - Get available brands.
- `POST /` - Create a new product (Admin only).
- `PUT /{id}` - Update product (Admin only).
- `DELETE /{id}` - Delete product (Admin only).

### 📂 Categories (`/api/v1/categories`)
- `GET /` - Get all categories.
- `POST /` - Add a category (Admin only).

### 🛍️ Cart (`/api/v1/cart`)
- `GET /` - Get user's cart.
- `POST /add?sizeVariantId={id}&quantity={q}` - Add item to cart.
- `PUT /update?cartItemId={id}&quantity={q}` - Update item quantity.
- `DELETE /remove?cartItemId={id}` - Remove item from cart.
- `DELETE /clear` - Clear entire cart.

### 📦 Orders (`/api/v1/order`)
- `POST /` - Create an order  using existing items in Cart.
- `GET /` - Get all user orders.
- `GET /{id}` - Get order details.

### 🏠 Address Management (`/api/v1/address`)
- `POST /add` - Add a new address to user profile.
- `GET /` - Fetch all saved addresses.
---

