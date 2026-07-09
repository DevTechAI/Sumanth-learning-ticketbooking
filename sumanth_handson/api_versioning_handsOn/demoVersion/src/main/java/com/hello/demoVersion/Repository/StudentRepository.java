package com.hello.demoVersion.Repository;

import com.hello.demoVersion.Entity.Student;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long>
{
    List<Student> findByIdGreaterThanOrderByIdAsc(Long lastId, Pageable pageable);

    List<Student> findByCreatedAtLessThanOrderByCreatedAtDesc(LocalDateTime lastCreatedAt, Pageable pageable);
}