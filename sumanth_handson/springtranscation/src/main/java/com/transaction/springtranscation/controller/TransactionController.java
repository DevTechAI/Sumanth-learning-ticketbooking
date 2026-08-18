package com.transaction.springtranscation.controller;

import com.transaction.springtranscation.entity.Account;
import com.transaction.springtranscation.entity.AuditLog;
import com.transaction.springtranscation.exception.DemoCheckedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.transaction.springtranscation.service.AuditService;
import com.transaction.springtranscation.service.AccountService;
import com.transaction.springtranscation.service.NestedTransactionService;
import com.transaction.springtranscation.service.NestedOuterService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/tx")
public class TransactionController {


    private final AccountService accountService;

    private final AuditService auditService;

    private final NestedOuterService nestedOuterService;


    public TransactionController(AccountService accountService, AuditService auditService,
                                 NestedOuterService nestedOuterService)
    {
        this.accountService = accountService;
        this.auditService = auditService;
        this.nestedOuterService = nestedOuterService;
    }


    @PostMapping("/reset")
    public Map<String, Object> reset()
    {
        return accountService.reset();
    }


    @GetMapping("/accounts")
    public List<Account> accounts()
    {
        return accountService.getAccounts();
    }

    @GetMapping("/audit-logs")
    public List<AuditLog> auditLogs()
    {
        return accountService.getAuditLogs();
    }


    // Concepts 1-7

    @PostMapping("/transfer")
    public Map<String, Object> transfer(@RequestParam String from,
                                        @RequestParam String to,
                                        @RequestParam BigDecimal amount)
    {
        return accountService.transfer(from, to, amount);
    }


    // Concept 8

    @PostMapping("/rollback/runtime")
    public void runtimeRollback()
    {
        accountService.transferThenRuntimeFailure(
                        "Alice",
                        "Bob",
                        new BigDecimal("100.00")
        );
    }


    // Concepts 9 & 10

    @PostMapping("/rollback/checked-default")
    public void checkedDefault() throws DemoCheckedException
    {
        accountService.checkedExceptionWithoutRollback();
    }


    @PostMapping("/rollback/checked-with-rule")
    public void checkedWithRule() throws DemoCheckedException
    {
        accountService.checkedExceptionWithRollback();
    }

    // REQUIRED
    @GetMapping("/propagation/required/outside")
    public Map<String, Object> requiredOutside()
    {
        return auditService.required("called directly from controller");
    }


    @GetMapping("/propagation/required/inside")
    public Map<String, Object> requiredInside() {

        return accountService.requiredInsideTransaction();
    }


    // REQUIRES_NEW

    @PostMapping("/propagation/requires-new")
    public void requiresNew() {

        accountService.requiresNewThenOuterFails();
    }


    // SUPPORTS

    @GetMapping("/propagation/supports/outside")
    public Map<String, Object> supportsOutside() {

        return auditService.supports();
    }


    @GetMapping("/propagation/supports/inside")
    public Map<String, Object> supportsInside() {

        return accountService.supportsInsideTransaction();
    }

    // MANDATORY

    @GetMapping("/propagation/mandatory/outside")
    public Map<String, Object> mandatoryOutside() {

        return auditService.mandatory("called without outer transaction");
    }


    @GetMapping("/propagation/mandatory/inside")
    public Map<String, Object> mandatoryInside() {

        return accountService.mandatoryInsideTransaction();
    }


    // NOT_SUPPORTED
    @GetMapping("/propagation/not-supported/inside")
    public Map<String, Object> notSupportedInside()
    {
        return accountService.notSupportedInsideTransaction();
    }


    // NEVER

    @GetMapping("/propagation/never/outside")
    public Map<String, Object> neverOutside() {

        return auditService.never();
    }


    @GetMapping("/propagation/never/inside")
    public Map<String, Object> neverInside() {

        return accountService.neverInsideTransaction();
    }


    // NESTED

    @PostMapping("/propagation/nested")
    public Map<String, Object> nested() {

        return nestedOuterService.demonstrateNested();
    }


    // READ_COMMITTED

    @GetMapping("/isolation/read-committed")
    public Map<String, Object> readCommitted(@RequestParam(defaultValue = "Alice") String owner)
    {
        return accountService.readCommittedBalance(owner);
    }


    // READ_UNCOMMITTED

    @GetMapping("/isolation/read-uncommitted")
    public Map<String, Object> readUncommitted(@RequestParam(defaultValue = "Alice") String owner) {

        return accountService.readUncommittedBalance(owner);
    }
}
