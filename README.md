# customer-ms

### Swagger UI
##### Access in this endpoint
``
http://localhost:8080/swagger-ui/index.html
``

###
`
H2 Console -> http://localhost:8080/h2-console
`

#### 1. Account creation service (POST)
```
/api/vi/account
```

`return 201 - Created document`

![Screenshot 2026-11-10 at 16.27.59.png](src/main/resources/files/Screenshot%202026-11-10%20at%2016.27.59.png)


`return 400 - Bad Request`

![Screenshot 2026-11-10 at 16.29.30.png](src/main/resources/files/Screenshot%202026-11-10%20at%2016.29.30.png)

#### 2. Customer inquiry service (GET)
```
/api/v1/account/{customerNumber}
```

`return 302 - Customer found`

![Screenshot 2026-11-10 at 16.30.31.png](src/main/resources/files/Screenshot%202026-11-10%20at%2016.30.31.png)

`return 404 - Customer not found`
`
![Screenshot 2026-11-10 at 16.31.58.png](src/main/resources/files/Screenshot%202026-11-10%20at%2016.31.58.png)
