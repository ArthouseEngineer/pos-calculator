# POS Price and points Backend API

##### How to run tests

Run test:
```./gradlew clean test```

##### Run app steps:

- First Up docker container from compose file

```bash	
docker-compose up -d --wait
```

- Second migrate DB if first time run, for DB initialization

```./gradlew flywayMigrate```

- Finally run the app :)

```./gradlew bootRun```

- After stop working with app, don't forget down containers and stop the running application

```bash	
sh stop-app.sh
docker-compose down
```

### Solution explanation

This API enables e-commerce operations, providing:
1. **Calculation of prices and reward points** for various payment methods.
2. **Sales reporting** based on date ranges, with data aggregated hourly.

## **Endpoints Overview**

### 1. **Price and Points Calculation**
- **URL**: `/api/calculate`
- **Method**: `POST`
- **Description**: Calculates the final price and reward points based on the payment method, applying validation and rules.

### 2. **Sales Report Generation**
- **URL**: `/api/sales/range`
- **Method**: `POST`
- **Description**: Returns hourly aggregated sales and points data for a specified date range.

For testing these endpoints, you can use Integrated IDEA http-client for make request to endpoints: [**http-client.http**](http-client.http)

---

## **Request Validation Algorithm**

### **Overview**
The request validation mechanism ensures that the provided input adheres to the rules defined for each payment method. The validation logic dynamically retrieves and processes rules stored in the database.

### **Validation Workflow**
1. **Retrieve Validation Rules**:
    - The `payment_methods` table contains a `validation_rules` JSONB column, which defines the required fields, their types, and custom checks for each payment method.
    - When a request is received, the system queries the relevant `validation_rules` based on the specified payment method.

2. **Field Presence Check**:
    - The rules include a list of `requiredFields`. The algorithm iterates through this list and ensures all specified fields exist in the request.

3. **Custom Checks**:
    - Certain payment methods may have specific constraints. For example:
        - **CASH_ON_DELIVERY**: Validates that the `courier` field is one of the allowed values (`YAMATO`, `SAGAWA`).
        - **BANK_TRANSFER**: Ensures that the `bankDetails` field is an object containing `bankName` and `bankAccount`.

4. **Error Handling**:
    - If any validation step fails, the system returns an error response detailing the missing or invalid field and the specific rule violated.

  











