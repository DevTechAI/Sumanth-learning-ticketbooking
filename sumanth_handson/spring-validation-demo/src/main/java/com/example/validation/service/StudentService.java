package com.example.validation.service;

import com.example.validation.dto.StudentRequest;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    public String createStudent(StudentRequest request) {
        return "Student " + request.getName() + " created successfully";
    }
}
