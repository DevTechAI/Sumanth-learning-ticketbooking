package com.hello.demoVersion.Controller;

import com.hello.demoVersion.DTO.StudentResponse;
import com.hello.demoVersion.Entity.Student;
import com.hello.demoVersion.Service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService)
    {
        this.studentService = studentService;
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student)
    {
        return studentService.createStudent(student);
    }

    @GetMapping("/offset")
    public Page<StudentResponse> getStudentsOffsetPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy)
    {
        return studentService.getStudentsOffsetPagination(page, size, sortBy);
    }

    @GetMapping("/cursor")
    public List<StudentResponse> getStudentsCursorPagination(
            @RequestParam(defaultValue = "0") Long lastId,
            @RequestParam(defaultValue = "5") int limit)
    {
        return studentService.getStudentsCursorPagination(lastId, limit);
    }

    @GetMapping("/keyset")
    public List<StudentResponse> getStudentsKeysetPagination(
            @RequestParam String lastCreatedAt,
            @RequestParam(defaultValue = "5") int limit
    ) {
        return studentService.getStudentsKeysetPagination(
                LocalDateTime.parse(lastCreatedAt),
                limit
        );
    }
}
