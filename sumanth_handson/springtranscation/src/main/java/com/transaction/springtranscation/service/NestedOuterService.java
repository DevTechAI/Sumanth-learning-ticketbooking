package com.transaction.springtranscation.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Service
public class NestedOuterService {

    private final JdbcTemplate
            jdbcTemplate;

    private final NestedTransactionService
            nestedTransactionService;


    public NestedOuterService(
            JdbcTemplate jdbcTemplate,

            NestedTransactionService
                    nestedTransactionService
    ) {

        this.jdbcTemplate =
                jdbcTemplate;

        this.nestedTransactionService =
                nestedTransactionService;
    }


    @Transactional(
            transactionManager =
                    "jdbcTransactionManager"
    )
    public Map<String, Object>
    demonstrateNested() {


        jdbcTemplate.update(
                """
                INSERT INTO audit_logs
                (message, created_at)
                VALUES (?, CURRENT_TIMESTAMP)
                """,

                "OUTER: before nested call"
        );


        String nestedResult;


        try {

            nestedTransactionService
                    .insertNestedRowThenFail();

            nestedResult =
                    "unexpected success";

        } catch (
                RuntimeException exception
        ) {

            nestedResult =
                    "nested scope rolled back " +
                            "to savepoint: " +
                            exception.getMessage();
        }


        jdbcTemplate.update(
                """
                INSERT INTO audit_logs
                (message, created_at)
                VALUES (?, CURRENT_TIMESTAMP)
                """,

                "OUTER: after nested rollback"
        );


        List<String> messages =
                jdbcTemplate.queryForList(
                        """
                        SELECT message
                        FROM audit_logs
                        ORDER BY id
                        """,

                        String.class
                );


        Map<String, Object> response =
                new LinkedHashMap<>();


        response.put(
                "nestedResult",
                nestedResult
        );


        response.put(
                "messagesVisibleInsideOuterTransaction",
                messages
        );


        response.put(
                "expected",
                "OUTER rows remain; " +
                        "NESTED row is absent"
        );


        return response;
    }
}
