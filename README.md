🌍 This README is also available in [Русский](./README_RU.md)

# 🏡 Online Home and Garden Products Shop

### 📌 About the Project

Backend part of an online shop for home and garden products, built with Java Spring. The system includes functionality for customers, admins, and managers: from product catalog to order tracking and analytics.

🔗 [Technical Requirements (Google Docs)](https://docs.google.com/document/d/10vnhDHE8lb7rTIRdbskId9ESLQE2EJ8y/edit?tab=t.0#heading=h.e2bcw3kuo1da)  
🔗 [Swagger API Docs](http://localhost:8080/swagger-ui/index.html#/)

---

### 🧾 Key Features

- User registration and login
- Profile management
- Full CRUD for categories and products
- Filtering and sorting
- Cart and order creation
- Delivery status management (with auto-updates)
- Discounts and “product of the day”
- Order history
- Favorites list
- Analytics (top sales, revenue, etc.)

---

### 🧪 Tech Stack

| Technology              | Purpose                                                                 |
|-------------------------|-------------------------------------------------------------------------|
| **Java 23**             | Programming language                                                    |
| **Spring Boot 3.4.3**   | REST API development                                                    |
| **Spring Web**          | REST controllers                                                        |
| **Spring Data JPA**     | ORM and DB operations                                                   |
| **Hibernate**           | JPA implementation                                                      |
| **Spring Security**     | Authentication and authorization (JWT)                                 |
| **JWT (jjwt)**          | Token-based authentication                                              |
| **Liquibase**           | Database versioning                                                     |
| **MySQL**               | Main relational database                                                |
| **H2**                  | In-memory DB for testing                                                |
| **Spring Mail**         | Email sending                                                           |
| **Log4j2**              | Logging                                                                 |
| **Lombok**              | Boilerplate reduction                                                   |
| **MapStruct**           | DTO ↔ Entity mapping                                                    |
| **Swagger / Springdoc** | API documentation                                                       |
| **JUnit 5 / Mockito**   | Testing                                                                 |
| **Docker / Compose**    | Containerization and deployment                                         |
| **Maven**               | Build and dependency management                                         |

---

### ▶️ How to Run the Project

#### ☑️ Option 1: Local Launch (Maven + Local MySQL)

```bash
# 1. Set up MySQL and update application.properties:
spring.datasource.url=jdbc:mysql://localhost:3306/onlinegardenshop
spring.datasource.username=your_username
spring.datasource.password=your_password

# 2. Run
mvn clean install
mvn spring-boot:run
```

Access URL: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

#### 🐳 Option 2: Docker (with cloud DB)

```bash
docker-compose up --build
```

The app connects to the remote MySQL database automatically.

---

#### 🐋 Option 3: Docker (with local DB)

```bash
docker-compose -f docker-compose.local.yml up --build
```

✅ To stop and clean:
```bash
docker-compose -f docker-compose.local.yml down -v
```

---

### 👥 Project Authors

| Name             | Role      | Contacts                | Contribution                                  |
|------------------|-----------|-------------------------|-----------------------------------------------|
| Deyan Spasov     | Teamlead  | [GitHub/LinkedIn/email] | - Implemented cart, orders, authentication... |
| Artur            | Developer | [GitHub/LinkedIn/email] | - Implemented cart, orders, authentication... |
| Dmitrii Shkolnyi | Developer | [GitHub/LinkedIn/email] | - Implemented cart, orders, authentication... |

