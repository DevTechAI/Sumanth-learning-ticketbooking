package com.transaction.springtranscation.service;

import com.transaction.springtranscation.entity.AuditLog;
import com.transaction.springtranscation.repository.AuditLogRepository;
//import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class AuditService {

    private final AuditLogRepository
            auditLogRepository;

    public AuditService(
            AuditLogRepository
                    auditLogRepository
    ) {
        this.auditLogRepository =
                auditLogRepository;
    }


    // -----------------------------
    // 12. REQUIRED
    // -----------------------------

    @Transactional(propagation = Propagation.REQUIRED)
    public Map<String, Object> required(String message)
    {
        auditLogRepository.save(new AuditLog("REQUIRED: " + message));

        return transactionInfo(
                "REQUIRED joins an existing " +
                        "transaction or creates one"
        );
    }


    // -----------------------------
    // 13. REQUIRES_NEW
    // -----------------------------

    @Transactional(
            propagation =
                    Propagation.REQUIRES_NEW
    )
    public Map<String, Object> requiresNew(
            String message
    ) {

        auditLogRepository.save(
                new AuditLog(
                        "REQUIRES_NEW: "
                                + message
                )
        );

        return transactionInfo(
                "REQUIRES_NEW uses an " +
                        "independent transaction"
        );
    }


    // -----------------------------
    // 14. SUPPORTS
    // -----------------------------

    @Transactional(
            propagation =
                    Propagation.SUPPORTS,
            readOnly = true
    )
    public Map<String, Object> supports() {

        return transactionInfo(
                "SUPPORTS joins a transaction " +
                        "if one exists"
        );
    }


    // -----------------------------
    // 15. MANDATORY
    // -----------------------------

    @Transactional(
            propagation =
                    Propagation.MANDATORY
    )
    public Map<String, Object> mandatory(
            String message
    ) {

        auditLogRepository.save(
                new AuditLog(
                        "MANDATORY: " + message
                )
        );

        return transactionInfo(
                "MANDATORY requires an " +
                        "existing transaction"
        );
    }


    // -----------------------------
    // 16. NOT_SUPPORTED
    // -----------------------------

    @Transactional(
            propagation =
                    Propagation.NOT_SUPPORTED
    )
    public Map<String, Object>
    notSupported() {

        return transactionInfo(
                "NOT_SUPPORTED runs " +
                        "without a transaction"
        );
    }


    // -----------------------------
    // 17. NEVER
    // -----------------------------

    @Transactional(
            propagation =
                    Propagation.NEVER
    )
    public Map<String, Object> never() {

        return transactionInfo(
                "NEVER only works when " +
                        "no transaction exists"
        );
    }


    private Map<String, Object>
    transactionInfo(
            String explanation
    ) {

        Map<String, Object> info =
                new LinkedHashMap<>();

        info.put(
                "explanation",
                explanation
        );

        info.put(
                "transactionActive",
                TransactionSynchronizationManager
                        .isActualTransactionActive()
        );

        info.put(
                "transactionName",
                TransactionSynchronizationManager
                        .getCurrentTransactionName()
        );

        return info;
    }
}