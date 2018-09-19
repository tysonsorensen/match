# Buy, Sell, Matching Test

A simple matching service that handles matching buy and sell requests and reporting the availble transactions.


## Getting Started

Get your hands on the code (clone or download)

Run 'gradle' to get the dependencies.

Run application.

#### Database
This service is using an embedded SQL database (H2). The database is not persisted through restarts.
So if the service is stopped all data is thrown away. Great for testing.

## Running the tests

All test can be ran from gradle.

Just run all unit tests:
```
./gradlew test --rerun-tasks
```

A test script is in the root directory:
```
test.sh
```
This runs a series of curl commands and outputs the results. (output is a bit ugly)

## Deployment

Running from code with gradle:
```
./gradlew bootRun
```

Create a Bootable jar:
```
./gradlew bootJar
```

Running the jar:

java -jar build/libs/Medici-0.0.1.jar

## REST API
GET /book
Response Body:
{
    "buys": [{ "qty":INTEGER_NUMBER, "prc":DECIMAL_NUMBER }, ...],
    "sells": [{ "qty":INTEGER_NUMBER, "prc":DECIMAL_NUMBER }, ...]
}

POST /sell
Body:
{
    "qty":INTEGER_NUMBER,
    "prc":DECIMAL_NUMBER
}
Response Body: empty

POST /buy
Body:
{
    "qty":INTEGER_NUMBER,
    "prc":DECIMAL_NUMBER
}
Response Body: empty

