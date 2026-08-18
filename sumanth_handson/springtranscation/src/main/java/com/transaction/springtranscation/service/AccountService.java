package com.transaction.springtranscation.service;

import com.transaction.springtranscation.entity.Account;
import com.transaction.springtranscation.entity.AuditLog;
import com.transaction.springtranscation.repository.AccountRepository;
import com.transaction.springtranscation.repository.AuditLogRepository;
import com.transaction.springtranscation.exception.DemoCheckedException;
//import jakarta.transaction.Transactional;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.support.TransactionSynchronizationManager;

//import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Service
public class AccountService {

    private final AccountRepository
            accountRepository;

    private final AuditLogRepository
            auditLogRepository;

    private final AuditService
            auditService;


    public AccountService(
            AccountRepository accountRepository,
            AuditLogRepository auditLogRepository,
            AuditService auditService
    ) {

        this.accountRepository =
                accountRepository;

        this.auditLogRepository =
                auditLogRepository;

        this.auditService =
                auditService;
    }


    public List<Account> getAccounts() {

        return accountRepository
                .findAll();
    }


    public List<AuditLog> getAuditLogs() {

        return auditLogRepository
                .findAll();
    }


    // --------------------------------------
    // Utility - reset database
    // --------------------------------------

    @Transactional
    public Map<String, Object> reset() {

        auditLogRepository.deleteAllInBatch();

        accountRepository.deleteAllInBatch();

        accountRepository.save(
                new Account(
                        "Alice",
                        new BigDecimal("1000.00")
                )
        );

        accountRepository.save(
                new Account(
                        "Bob",
                        new BigDecimal("500.00")
                )
        );

        return Map.of(
                "message",
                "Reset complete",

                "accounts",
                accountRepository.findAll()
        );
    }


    // ======================================
    // CONCEPTS 1, 2, 3, 4, 7
    // ======================================

    @Transactional
    public Map<String, Object> transfer(
            String from,
            String to,
            BigDecimal amount
    ) {

        Account source =
                find(from);

        Account target =
                find(to);


        if (amount.signum() <= 0)
        {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }


        if (source.getBalance().compareTo(amount) < 0)
        {
            throw new IllegalArgumentException("Insufficient balance");
        }
        source.setBalance(source.getBalance().subtract(amount));

        target.setBalance(target.getBalance().add(amount));


        return Map.of(
                "message",
                "Transfer completed. " +
                        "Transaction will COMMIT.",

                "fromBalance",
                source.getBalance(),

                "toBalance",
                target.getBalance()
        );
    }


    // ======================================
    // CONCEPT 8
    // RuntimeException -> ROLLBACK
    // ======================================

    @Transactional
    public void transferThenRuntimeFailure(
            String from,
            String to,
            BigDecimal amount
    ) {

        moveMoney(
                from,
                to,
                amount
        );

        throw new RuntimeException(
                "Intentional RuntimeException: " +
                        "transfer is rolled back"
        );
    }


    // ======================================
    // CONCEPTS 9 + 10
    // Checked Exception - default behavior
    // ======================================

    @Transactional
    public void
    checkedExceptionWithoutRollback()
            throws DemoCheckedException {

        Account alice =
                find("Alice");

        alice.setBalance(
                new BigDecimal("222.00")
        );

        throw new DemoCheckedException(
                "Checked exception WITHOUT " +
                        "rollbackFor"
        );
    }


    // ======================================
    // CONCEPT 9
    // Checked Exception + rollbackFor
    // ======================================

    @Transactional(rollbackFor = DemoCheckedException.class)
    public void
    checkedExceptionWithRollback()
            throws DemoCheckedException {

        Account alice =
                find("Alice");

        alice.setBalance(
                new BigDecimal("111.00")
        );

        throw new DemoCheckedException(
                "Checked exception WITH " +
                        "rollbackFor"
        );
    }


    // ======================================
    // CONCEPT 12 - REQUIRED
    // ======================================

    @Transactional
    public Map<String, Object>
    requiredInsideTransaction() {

        return auditService.required(
                "called from AccountService"
        );
    }


    // ======================================
    // CONCEPT 13 - REQUIRES_NEW
    // ======================================

    @Transactional
    public void
    requiresNewThenOuterFails() {

        Account alice =
                find("Alice");


        alice.setBalance(
                new BigDecimal("333.00")
        );


        auditService.requiresNew(
                "This audit survives " +
                        "outer rollback"
        );


        throw new RuntimeException(
                "Outer transaction failed"
        );
    }


    // ======================================
    // CONCEPT 14 - SUPPORTS
    // ======================================

    @Transactional(readOnly = true)
    public Map<String, Object>
    supportsInsideTransaction() {

        return auditService.supports();
    }


    // ======================================
    // CONCEPT 15 - MANDATORY
    // ======================================

    @Transactional
    public Map<String, Object>
    mandatoryInsideTransaction() {

        return auditService.mandatory(
                "called inside transaction"
        );
    }


    // ======================================
    // CONCEPT 16 - NOT_SUPPORTED
    // ======================================

    @Transactional
    public Map<String, Object>
    notSupportedInsideTransaction() {

        return auditService
                .notSupported();
    }


    // ======================================
    // CONCEPT 17 - NEVER
    // ======================================

    @Transactional
    public Map<String, Object>
    neverInsideTransaction() {

        return auditService.never();
    }


    // ======================================
    // CONCEPT 19
    // READ_COMMITTED
    // ======================================

    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    public Map<String, Object>
    readCommittedBalance(
            String owner
    ) {

        return isolationResponse(
                owner,
                "READ_COMMITTED"
        );
    }


    // ======================================
    // CONCEPT 20
    // READ_UNCOMMITTED
    // ======================================

    @Transactional(
            isolation =
                    Isolation.READ_UNCOMMITTED,
            readOnly = true
    )
    public Map<String, Object>
    readUncommittedBalance(
            String owner
    ) {

        return isolationResponse(
                owner,

                "READ_UNCOMMITTED requested; " +
                        "PostgreSQL treats it as " +
                        "READ_COMMITTED"
        );
    }


    private Map<String, Object>
    isolationResponse(
            String owner,
            String requestedLevel
    ) {

        Account account =
                find(owner);


        Map<String, Object> response =
                new LinkedHashMap<>();


        response.put(
                "owner",
                account.getOwner()
        );


        response.put(
                "balance",
                account.getBalance()
        );


        response.put(
                "requestedIsolation",
                requestedLevel
        );


        response.put(
                "transactionActive",
                TransactionSynchronizationManager
                        .isActualTransactionActive()
        );


        response.put(
                "readOnly",
                TransactionSynchronizationManager
                        .isCurrentTransactionReadOnly()
        );


        return response;
    }


    private void moveMoney(
            String from,
            String to,
            BigDecimal amount
    ) {

        Account source =
                find(from);

        Account target =
                find(to);


        source.setBalance(
                source.getBalance()
                        .subtract(amount)
        );


        target.setBalance(
                target.getBalance()
                        .add(amount)
        );
    }


    private Account find(
            String owner
    ) {

        return accountRepository
                .findByOwner(owner)
                .orElseThrow(
                        () ->
                                new IllegalArgumentException(
                                        "Account not found: "
                                                + owner
                                )
                );
    }
}
