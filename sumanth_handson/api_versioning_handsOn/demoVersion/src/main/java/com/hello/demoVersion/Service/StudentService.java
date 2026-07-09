package com.hello.demoVersion.Service;

import com.hello.demoVersion.DTO.StudentResponse;
import com.hello.demoVersion.Entity.Student;
import com.hello.demoVersion.Repository.StudentRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        student.setCreatedAt(LocalDateTime.now());
        return studentRepository.save(student);
    }

    public Page<StudentResponse> getStudentsOffsetPagination(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());

        return studentRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    public List<StudentResponse> getStudentsCursorPagination(Long lastId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);

        return studentRepository.findByIdGreaterThanOrderByIdAsc(lastId, pageable)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<StudentResponse> getStudentsKeysetPagination(LocalDateTime lastCreatedAt, int limit) {
        Pageable pageable = PageRequest.of(0, limit);

        return studentRepository.findByCreatedAtLessThanOrderByCreatedAtDesc(lastCreatedAt, pageable)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private StudentResponse mapToResponse(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getCourse(),
                student.getCreatedAt()
        );
    }
}
