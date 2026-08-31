# Basic Spring Batch Project

## Flow

customers.csv -> ItemReader -> ItemProcessor -> ItemWriter -> PostgreSQL

## Most-used concepts implemented

1. Job
2. Step
3. Chunk processing
4. ItemReader
5. ItemProcessor
6. ItemWriter
7. JobRepository
8. PlatformTransactionManager
9. JobLauncher
10. JobParameters
11. JobExecution
12. Job listener
13. Spring Batch metadata tables

## Learn files in this order

1. Customer.java
2. CustomerProcessor.java
3. BatchConfig.java
4. BatchController.java
5. JobCompletionListener.java
6. application.properties

## PostgreSQL

Create database:

```sql
CREATE DATABASE springbatchdb;
```

Update username/password in `application.properties`.

## Run

```bash
mvn clean spring-boot:run
```

Then in Postman:

```text
POST http://localhost:8080/batch/run
```

Verify:

```sql
SELECT * FROM customer ORDER BY id;
```

Metadata:

```sql
SELECT * FROM batch_job_instance;
SELECT * FROM batch_job_execution;
SELECT * FROM batch_step_execution;
```

## Chunk size

The Step uses `chunk(3, transactionManager)`. Simplified:

Read 3 -> Process 3 -> Write 3 -> COMMIT -> repeat.
