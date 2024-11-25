# WARNING: This script is not intended to run directly

# ======= AUTH =======
# Register
curl -X 'POST' \
  'http://localhost:8080/api/auth/v1/register' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "email": "john@doe.com",
  "fullName": "John Doe",
  "password": "john123"
}'

# Login
curl -X 'POST' \
  'http://localhost:8080/api/auth/v1/login' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "email": "john@doe.com",
  "password": "john123"
}'


# ======= INVOICE =======

# Create Invoice
curl -X 'POST' \
  'http://localhost:8080/api/invoice/v1' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer <REDACTED>' \
  -H 'Content-Type: application/json' \
  -d '{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@doe.com",
  "amount": 100,
  "productCode": "P00001",
  "billNo": "FYS2024000000001"
}'

# Get Invoice
curl -X 'GET' \
  'http://localhost:8080/api/invoice/v1/67436a8af4e8e826ea033a58' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer <REDACTED>'

# Get All Invoices
curl -X 'GET' \
  'http://localhost:8080/api/invoice/v1?page=0&pageSize=10' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer <REDACTED>'

# Delete Invoice
curl -X 'DELETE' \
  'http://localhost:8080/api/invoice/v1/67436a8af4e8e826ea033a58' \
  -H 'accept: */*' \
  -H 'Authorization: Bearer <REDACTED>'


# ======= PRODUCT =======

# Create Product
curl -X 'POST' \
  'http://localhost:8080/api/product/v1' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer <REDACTED>' \
  -H 'Content-Type: application/json' \
  -d '{
  "code": "P00001",
  "name": "Product 1",
  "description": "Very nice product",
  "price": 50
}'

# Get All Products
curl -X 'GET' \
  'http://localhost:8080/api/product/v1?page=0&pageSize=10' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer <REDACTED>'

# Get Product
curl -X 'GET' \
  'http://localhost:8080/api/product/v1/6741da3c38bac77c991e3a17' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer <REDACTED>'

# Update Product
curl -X 'PUT' \
  'http://localhost:8080/api/product/v1/6741da3c38bac77c991e3a17' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer <REDACTED>' \
  -H 'Content-Type: application/json' \
  -d '{
  "description": "Awesome product",
  "price": 78
}'

# Delete Product
curl -X 'DELETE' \
  'http://localhost:8080/api/product/v1/6743a4045cca0f3640c95ef4' \
  -H 'accept: */*' \
  -H 'Authorization: Bearer <REDACTED>'


