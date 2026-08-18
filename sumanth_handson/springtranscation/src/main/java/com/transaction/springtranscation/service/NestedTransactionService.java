package com.transaction.springtranscation.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NestedTransactionService {

    private final JdbcTemplate
            jdbcTemplate;

    public NestedTransactionService(
            JdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate =
                jdbcTemplate;
    }


    @Transactional(
            transactionManager =
                    "jdbcTransactionManager",

            propagation =
                    Propagation.NESTED
    )
    public void insertNestedRowThenFail() {

        jdbcTemplate.update(
                """
                INSERT INTO audit_logs
                (message, created_at)
                VALUES (?, CURRENT_TIMESTAMP)
                """,

                "NESTED: this row should " +
                        "roll back to the savepoint"
        );


        throw new RuntimeException(
                "Intentional nested failure"
        );
    }
}
