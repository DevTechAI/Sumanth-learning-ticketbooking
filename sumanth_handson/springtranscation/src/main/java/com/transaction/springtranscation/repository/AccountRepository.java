package com.transaction.springtranscation.repository;


import com.transaction.springtranscation.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long>
{
    Optional<Account> findByOwner(String owner);
}