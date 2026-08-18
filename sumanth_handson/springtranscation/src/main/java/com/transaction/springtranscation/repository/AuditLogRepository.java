package com.transaction.springtranscation.repository;

import com.transaction.springtranscation.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long>
{

}
