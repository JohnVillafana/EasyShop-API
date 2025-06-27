# EasyShop E-Commerce API 🛍️

A robust Spring Boot REST API for an e-commerce platform, developed as a capstone project for the YearUp Java Development Program. This project demonstrates full-stack backend development skills including RESTful API design, database management, authentication, and security implementation.

## 📋 Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Database Schema](#database-schema)
- [Testing](#testing)
- [Project Structure](#project-structure)
- [Contributors](#contributors)

## 🎯 Overview

EasyShop API is a comprehensive backend solution for an e-commerce website that enables users to:
- Browse and search products by category
- Manage shopping carts with full CRUD operations
- Update user profiles
- Secure authentication with role-based access control

This project involved fixing bugs in an existing codebase and implementing new features for version 2 of the platform.

## ✨ Features

### Implemented Functionality
- ✅ **Product Categories Management**
  - Browse all categories
  - View category details
  - Admin-only: Create, update, and delete categories

- ✅ **Product Search & Filtering** 
  - Search products by name
  - Filter by category, price range, and color
  - Fixed SQL parameter ordering bugs

- ✅ **Shopping Cart Operations**
  - View cart contents with calculated totals
  - Add products to cart
  - Update product quantities
  - Remove individual products
  - Clear entire cart

- ✅ **User Profile Management**
  - View profile information
  - Update profile details
  - Secure access with JWT authentication

### Security Features
- JWT token-based authentication
- Role-based access control (USER, ADMIN)
- Secured endpoints with Spring Security
- Password encryption

## 🛠️ Technologies Used

### Backend
- **Java 11**
- **Spring Boot 2.7**
- **Spring Security**
- **Spring Data JDBC**
- **Maven**

### Database
- **MySQL 8.0**
- **HikariCP** (Connection Pooling)

### Authentication
- **JWT (JSON Web Tokens)**
- **BCrypt** (Password Hashing)

### Testing & Documentation
- **Postman** (API Testing)
- **Swagger/OpenAPI** (API Documentation)

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- Git

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/JohnVillafana/EasyShop-API.git
   cd EasyShop-API
   ```

2. **Set up the database**
   ```bash
   mysql -u root -p
   CREATE DATABASE easyshop;
   USE easyshop;
   SOURCE create_database.sql;
   ```

3. **Configure application properties**
   
   Update `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/easyshop
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

4. **Build and run the application**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

   The API will be available at `http://localhost:8080`

## 📚 API Documentation

### Authentication

All protected endpoints require a JWT token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

To obtain a token:
```http
POST /login
Content-Type: application/json

{
    "username": "user",
    "password": "user"
}
```

### Endpoints

#### Categories
| Method | Endpoint | Description | Auth Required | Role |
|--------|----------|-------------|---------------|------|
| GET | `/categories` | Get all categories | No | - |
| GET | `/categories/{id}` | Get category by ID | No | - |
| POST | `/categories` | Create new category | Yes | ADMIN |
| PUT | `/categories/{id}` | Update category | Yes | ADMIN |
| DELETE | `/categories/{id}` | Delete category | Yes | ADMIN |

#### Products
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/products` | Get all products with filters | No |
| GET | `/products/{id}` | Get product by ID | No |
| POST | `/products` | Create new product | Yes (ADMIN) |
| PUT | `/products/{id}` | Update product | Yes (ADMIN) |
| DELETE | `/products/{id}` | Delete product | Yes (ADMIN) |

**Query Parameters for GET /products:**
- `cat` - Filter by category ID
- `minPrice` - Minimum price
- `maxPrice` - Maximum price
- `color` - Filter by color
- `name` - Search by product name

#### Shopping Cart
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/cart` | Get user's cart | Yes |
| POST | `/cart/products/{productId}` | Add product to cart | Yes |
| PUT | `/cart/products/{productId}` | Update product quantity | Yes |
| DELETE | `/cart/products/{productId}` | Remove product from cart | Yes |
| DELETE | `/cart` | Clear entire cart | Yes |

#### User Profile
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/profile` | Get user profile | Yes |
| PUT | `/profile` | Update user profile | Yes |

### Example Requests

#### Add Product to Cart
```http
POST /cart/products/1
Authorization: Bearer <token>
```

#### Update Product Quantity
```http
PUT /cart/products/1
Authorization: Bearer <token>
Content-Type: application/json

{
    "quantity": 3
}
```

#### Update Profile
```http
PUT /profile
Authorization: Bearer <token>
Content-Type: application/json

{
    "firstName": "John",
    "lastName": "Doe",
    "phone": "555-1234",
    "email": "john@example.com",
    "address": "123 Main St",
    "city": "New York",
    "state": "NY",
    "zip": "10001"
}
```

## 🗄️ Database Schema

### Tables

#### users
- `user_id` (PK)
- `username`
- `hashed_password`
- `role`

#### profiles
- `user_id` (FK)
- `first_name`
- `last_name`
- `phone`
- `email`
- `address`
- `city`
- `state`
- `zip`

#### categories
- `category_id` (PK)
- `name`
- `description`

#### products
- `product_id` (PK)
- `name`
- `price`
- `category_id` (FK)
- `description`
- `color`
- `stock`
- `featured`
- `image_url`

#### shopping_cart
- `user_id` (FK)
- `product_id` (FK)
- `quantity`

## 🧪 Testing

### Using Postman

1. Import the Postman collection from `/capstone_postman_collection`
2. Set up environment variables:
   - `baseUrl`: http://localhost:8080
   - `token`: (will be set after login)

### Test Credentials
- **Regular User**: username: `user`, password: `user`
- **Admin User**: username: `admin`, password: `admin`

### Running Tests
The Postman collection includes tests for:
- Authentication flow
- All CRUD operations
- Error handling
- Authorization checks

## 📁 Project Structure

```
easyshop/
├── src/
│   ├── main/
│   │   ├── java/org/yearup/
│   │   │   ├── controllers/
│   │   │   │   ├── AuthenticationController.java
│   │   │   │   ├── CategoriesController.java
│   │   │   │   ├── ProductsController.java
│   │   │   │   ├── ProfileController.java
│   │   │   │   └── ShoppingCartController.java
│   │   │   ├── data/
│   │   │   │   ├── mysql/
│   │   │   │   │   ├── MySqlCategoryDao.java
│   │   │   │   │   ├── MySqlProductDao.java
│   │   │   │   │   ├── MySqlProfileDao.java
│   │   │   │   │   ├── MySqlShoppingCartDao.java
│   │   │   │   │   └── MySqlUserDao.java
│   │   │   │   └── interfaces/
│   │   │   ├── models/
│   │   │   │   ├── Category.java
│   │   │   │   ├── Product.java
│   │   │   │   ├── Profile.java
│   │   │   │   ├── ShoppingCart.java
│   │   │   │   └── User.java
│   │   │   └── security/
│   │   └── resources/
│   │       └── application.properties
├── database/
│   └── create_database.sql
└── README.md
```

## 🐛 Bug Fixes Implemented

1. **Product Search Parameter Order**: Fixed SQL query parameter ordering in product search functionality
2. **Product Update Duplication**: Resolved issue where updating products created duplicates instead of modifying existing records
3. **Shopping Cart State Management**: Implemented proper checking for existing cart items before adding
4. **Authentication Token Validation**: Enhanced JWT token validation and error handling

## 🚧 Future Enhancements

- [ ] Order processing and checkout functionality
- [ ] Payment gateway integration
- [ ] Product reviews and ratings
- [ ] Wishlist functionality
- [ ] Email notifications
- [ ] Advanced product search with elasticsearch
- [ ] Product image upload
- [ ] Inventory management system
Thank you 
