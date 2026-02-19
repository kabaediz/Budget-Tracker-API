# Budget Tracker API

A comprehensive REST API for personal budget management built with Spring Boot 3, Java 17, and PostgreSQL. This project demonstrates modern backend development practices including Domain-Driven Design, Bean Validation, OpenAPI documentation, and comprehensive testing with JUnit 5 and Testcontainers.

## 📋 Table of Contents

- [User Stories](#user-stories)
- [Data Model](#data-model)
- [Tech Stack](#tech-stack)
- [Features](#features)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Project Structure](#project-structure)

## 🎯 User Stories

### MVP (Core Features)

**US-1: Budget Management**
- Als Nutzer möchte ich ein monatliches Budget erstellen, um meine Einnahmen und Ausgaben für einen bestimmten Monat zu planen.
- Akzeptanzkriterien:
  - Budget hat Name, Beschreibung, Jahr und Monat
  - Nur ein Budget pro Monat möglich (Unique Constraint)
  - CRUD-Operationen verfügbar

**US-2: Category Management**
- Als Nutzer möchte ich Kategorien (Einnahmen/Ausgaben) zu einem Budget hinzufügen, um meine Finanzen zu strukturieren.
- Akzeptanzkriterien:
  - Kategorien haben Namen, Typ (INCOME/EXPENSE) und geplanten Betrag
  - Kategorien sind einem Budget zugeordnet
  - Automatische Berechnung von Ist-Betrag und verbleibender Summe

**US-3: Transaction Management**
- Als Nutzer möchte ich Transaktionen zu Kategorien hinzufügen, um meine tatsächlichen Ausgaben/Einnahmen zu erfassen.
- Akzeptanzkriterien:
  - Transaktionen haben Beschreibung, Betrag, Datum und optionale Notizen
  - Transaktionen sind einer Kategorie zugeordnet
  - Filter nach Kategorie und Zeitraum möglich

### Extended Features (Erweiterungen)

**US-4: Budget Overview & Analytics**
- Als Nutzer möchte ich eine Übersicht über mein Budget mit Soll/Ist-Vergleich sehen
- Berechnung von Gesamtsummen pro Budget und Kategorie

**US-5: Data Validation**
- Alle Eingaben werden validiert (Bean Validation)
- Aussagekräftige Fehlermeldungen nach RFC 7807 (Problem Details)

**US-6: API Documentation**
- Interaktive API-Dokumentation mit Swagger UI
- Request/Response-Beispiele für alle Endpoints

## 📊 Data Model

### Entity Relationship Diagram

```
┌─────────────┐         ┌──────────────┐         ┌──────────────────┐
│   Budget    │1      N │   Category   │1      N │   Transaction    │
│─────────────│─────────│──────────────│─────────│──────────────────│
│ id          │         │ id           │         │ id               │
│ name        │         │ name         │         │ description      │
│ description │         │ type         │         │ amount           │
│ year        │         │ plannedAmount│         │ transactionDate  │
│ month       │         │ budgetId (FK)│         │ categoryId (FK)  │
│ createdAt   │         │ createdAt    │         │ notes            │
│ updatedAt   │         │ updatedAt    │         │ createdAt        │
│ version     │         │ version      │         │ updatedAt        │
└─────────────┘         └──────────────┘         │ version          │
                                                   └──────────────────┘
```

### Domain Rules

- **Budget**: Unique constraint on (year, month) - only one budget per month
- **Category**: Type can be INCOME or EXPENSE
- **Transaction**: Date cannot be in the future (@PastOrPresent)
- **Cascade**: Deleting a Budget deletes all associated Categories and Transactions

## 🛠 Tech Stack

- **Java**: 17
- **Spring Boot**: 3.2.2
- **Build Tool**: Maven
- **Database**: PostgreSQL 15 (production), H2 (tests)
- **Database Migration**: Flyway
- **Validation**: Bean Validation (Hibernate Validator)
- **Documentation**: SpringDoc OpenAPI 3 (Swagger UI)
- **Testing**: JUnit 5, Mockito, MockMvc, H2
- **Containerization**: Docker, Docker Compose

## ✨ Features

- ✅ RESTful API with HATEOAS principles
- ✅ Comprehensive input validation with detailed error messages
- ✅ Global exception handling (RFC 7807 Problem Details)
- ✅ Database versioning with Flyway migrations
- ✅ Interactive API documentation (Swagger UI)
- ✅ Unit and integration tests
- ✅ Docker Compose for easy setup
- ✅ Optimistic locking (@Version)
- ✅ Audit fields (createdAt, updatedAt)

## 🚀 Getting Started

### Prerequisites

- Docker and Docker Compose
- (Optional) JDK 17+ and Maven if running locally

### Running with Docker Compose

1. Clone the repository:
```bash
git clone https://github.com/kabaediz/Budget-Tracker-API.git
cd Budget-Tracker-API
```

2. Start the application:
```bash
docker-compose up --build
```

3. Access the application:
   - API Base URL: http://localhost:8080/api/v1
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - OpenAPI Spec: http://localhost:8080/api-docs

4. Stop the application:
```bash
docker-compose down
```

### Running Locally

1. Start PostgreSQL database:
```bash
docker-compose up postgres
```

2. Run the application:
```bash
mvn spring-boot:run
```

3. Run tests:
```bash
mvn test
```

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api/v1
```

### Budgets API

#### Create Budget
```http
POST /api/v1/budgets
Content-Type: application/json

{
  "name": "January 2024 Budget",
  "description": "My budget for January",
  "year": 2024,
  "month": 1
}
```

**Response: 201 Created**
```json
{
  "id": 1,
  "name": "January 2024 Budget",
  "description": "My budget for January",
  "year": 2024,
  "month": 1,
  "categories": [],
  "createdAt": "2024-01-15T10:00:00",
  "updatedAt": "2024-01-15T10:00:00"
}
```

#### Get All Budgets
```http
GET /api/v1/budgets
```

#### Get Budget by ID
```http
GET /api/v1/budgets/{id}
```

#### Update Budget
```http
PUT /api/v1/budgets/{id}
Content-Type: application/json

{
  "name": "Updated Budget Name",
  "description": "Updated description",
  "year": 2024,
  "month": 1
}
```

#### Delete Budget
```http
DELETE /api/v1/budgets/{id}
```

### Categories API

#### Create Category
```http
POST /api/v1/categories
Content-Type: application/json

{
  "name": "Salary",
  "type": "INCOME",
  "plannedAmount": 5000.00,
  "budgetId": 1
}
```

**Response: 201 Created**
```json
{
  "id": 1,
  "name": "Salary",
  "type": "INCOME",
  "plannedAmount": 5000.00,
  "actualAmount": 0.00,
  "remaining": 5000.00,
  "budgetId": 1,
  "createdAt": "2024-01-15T10:00:00",
  "updatedAt": "2024-01-15T10:00:00"
}
```

#### Get All Categories (optionally filtered by budget)
```http
GET /api/v1/categories
GET /api/v1/categories?budgetId=1
```

#### Get Category by ID
```http
GET /api/v1/categories/{id}
```

#### Update Category
```http
PUT /api/v1/categories/{id}
```

#### Delete Category
```http
DELETE /api/v1/categories/{id}
```

### Transactions API

#### Create Transaction
```http
POST /api/v1/transactions
Content-Type: application/json

{
  "description": "Monthly Salary Payment",
  "amount": 5000.00,
  "transactionDate": "2024-01-15",
  "categoryId": 1,
  "notes": "First payment of the month"
}
```

**Response: 201 Created**
```json
{
  "id": 1,
  "description": "Monthly Salary Payment",
  "amount": 5000.00,
  "transactionDate": "2024-01-15",
  "categoryId": 1,
  "categoryName": "Salary",
  "notes": "First payment of the month",
  "createdAt": "2024-01-15T10:00:00",
  "updatedAt": "2024-01-15T10:00:00"
}
```

#### Get All Transactions (with optional filters)
```http
GET /api/v1/transactions
GET /api/v1/transactions?categoryId=1
GET /api/v1/transactions?startDate=2024-01-01&endDate=2024-01-31
```

#### Get Transaction by ID
```http
GET /api/v1/transactions/{id}
```

#### Update Transaction
```http
PUT /api/v1/transactions/{id}
```

#### Delete Transaction
```http
DELETE /api/v1/transactions/{id}
```

### Error Responses

The API uses RFC 7807 Problem Details for error responses:

**400 Bad Request - Validation Error**
```json
{
  "type": "about:blank",
  "title": "Validation Failed",
  "status": 400,
  "detail": "One or more fields have validation errors",
  "instance": "/api/v1/budgets",
  "timestamp": "2024-01-15T10:00:00",
  "errors": [
    {
      "field": "name",
      "message": "Name must be between 3 and 100 characters",
      "rejectedValue": "AB"
    },
    {
      "field": "month",
      "message": "Month must be between 1 and 12",
      "rejectedValue": 13
    }
  ]
}
```

**404 Not Found**
```json
{
  "type": "about:blank",
  "title": "Resource Not Found",
  "status": 404,
  "detail": "Budget with id 999 not found",
  "instance": "/api/v1/budgets/999",
  "timestamp": "2024-01-15T10:00:00"
}
```

**409 Conflict**
```json
{
  "type": "about:blank",
  "title": "Duplicate Resource",
  "status": 409,
  "detail": "Budget for year 2024 and month 1 already exists",
  "instance": "/api/v1/budgets",
  "timestamp": "2024-01-15T10:00:00"
}
```

## 🧪 Testing

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=BudgetServiceTest
mvn test -Dtest=BudgetControllerIntegrationTest
```

### Test Coverage

The project includes:
- **Unit Tests**: Service layer with Mockito mocks
- **Integration Tests**: Full Spring context with MockMvc and H2 database
- **Validation Tests**: Testing Bean Validation constraints
- **Error Handling Tests**: Testing exception scenarios and Problem Details responses

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/budgettracker/api/
│   │   ├── BudgetTrackerApplication.java
│   │   ├── config/
│   │   │   └── OpenApiConfig.java
│   │   ├── controller/
│   │   │   ├── BudgetController.java
│   │   │   ├── CategoryController.java
│   │   │   └── TransactionController.java
│   │   ├── dto/
│   │   │   ├── BudgetRequest.java
│   │   │   ├── BudgetResponse.java
│   │   │   ├── CategoryRequest.java
│   │   │   ├── CategoryResponse.java
│   │   │   ├── TransactionRequest.java
│   │   │   └── TransactionResponse.java
│   │   ├── exception/
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   ├── ProblemDetail.java
│   │   │   ├── ResourceNotFoundException.java
│   │   │   └── DuplicateResourceException.java
│   │   ├── model/
│   │   │   ├── Budget.java
│   │   │   ├── Category.java
│   │   │   └── Transaction.java
│   │   ├── repository/
│   │   │   ├── BudgetRepository.java
│   │   │   ├── CategoryRepository.java
│   │   │   └── TransactionRepository.java
│   │   └── service/
│   │       ├── BudgetService.java
│   │       ├── CategoryService.java
│   │       └── TransactionService.java
│   └── resources/
│       ├── application.yml
│       └── db/migration/
│           └── V1__create_initial_schema.sql
└── test/
    ├── java/com/budgettracker/api/
    │   ├── BudgetTrackerApplicationTests.java
    │   ├── controller/
    │   │   └── BudgetControllerIntegrationTest.java
    │   └── service/
    │       └── BudgetServiceTest.java
    └── resources/
        └── application-test.yml
```

## 🔒 Validation Rules

### Budget
- `name`: required, 3-100 characters
- `year`: required, 2000-2100
- `month`: required, 1-12
- Unique constraint: (year, month)

### Category
- `name`: required, 2-100 characters
- `type`: required (INCOME or EXPENSE)
- `plannedAmount`: required, > 0, max 17 integer digits, 2 decimal places
- `budgetId`: required, must reference existing budget

### Transaction
- `description`: required, 3-200 characters
- `amount`: required, > 0, max 17 integer digits, 2 decimal places
- `transactionDate`: required, cannot be in the future
- `categoryId`: required, must reference existing category
- `notes`: optional, max 1000 characters

## 🎨 Design Patterns & Best Practices

- **Layered Architecture**: Clear separation between Controller, Service, Repository
- **DTO Pattern**: Separate request/response objects from entities
- **Repository Pattern**: Spring Data JPA repositories
- **Exception Handling**: Global exception handler with RFC 7807
- **Validation**: Declarative validation with Bean Validation
- **Optimistic Locking**: @Version for concurrent updates
- **Auditing**: Automatic createdAt/updatedAt timestamps
- **Builder Pattern**: Lombok @Builder for clean object creation

## 🔮 Future Enhancements

- [ ] JWT Authentication & Authorization
- [ ] User management with multi-tenancy
- [ ] Budget templates and recurring transactions
- [ ] Export to CSV/PDF
- [ ] Budget analytics and charts
- [ ] Email notifications for budget limits
- [ ] Mobile app integration
- [ ] GraphQL API

## 📝 License

This project is licensed under the MIT License.

## 👨‍💻 Author

Created as a portfolio project for Wirtschaftsinformatik (Business Informatics).

## 🤝 Contributing

Contributions, issues, and feature requests are welcome!

---

**Happy Budgeting! 💰📊**