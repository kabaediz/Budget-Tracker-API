# Example API Requests

## Create a Budget
```bash
curl -X POST http://localhost:8080/api/v1/budgets \
  -H "Content-Type: application/json" \
  -d '{
    "name": "January 2024 Budget",
    "description": "My budget for January",
    "year": 2024,
    "month": 1
  }'
```

## Create Income Category
```bash
curl -X POST http://localhost:8080/api/v1/categories \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Salary",
    "type": "INCOME",
    "plannedAmount": 5000.00,
    "budgetId": 1
  }'
```

## Create Expense Category
```bash
curl -X POST http://localhost:8080/api/v1/categories \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Groceries",
    "type": "EXPENSE",
    "plannedAmount": 500.00,
    "budgetId": 1
  }'
```

## Create Transaction
```bash
curl -X POST http://localhost:8080/api/v1/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "description": "Monthly Salary Payment",
    "amount": 5000.00,
    "transactionDate": "2024-01-15",
    "categoryId": 1,
    "notes": "First payment of the month"
  }'
```

## Get All Budgets
```bash
curl http://localhost:8080/api/v1/budgets
```

## Get Budget by ID
```bash
curl http://localhost:8080/api/v1/budgets/1
```

## Get Categories for a Budget
```bash
curl http://localhost:8080/api/v1/categories?budgetId=1
```

## Get Transactions for a Category
```bash
curl http://localhost:8080/api/v1/transactions?categoryId=1
```

## Get Transactions by Date Range
```bash
curl "http://localhost:8080/api/v1/transactions?startDate=2024-01-01&endDate=2024-01-31"
```

## Update a Budget
```bash
curl -X PUT http://localhost:8080/api/v1/budgets/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Updated Budget Name",
    "description": "Updated description",
    "year": 2024,
    "month": 1
  }'
```

## Delete a Budget
```bash
curl -X DELETE http://localhost:8080/api/v1/budgets/1
```
