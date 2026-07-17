package com.Handson.Versioning.controller;

import com.Handson.Versioning.model.StudentV1;
import com.Handson.Versioning.model.StudentV2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentController
{
    //url Versioning
    @GetMapping("/v1/student")
    public StudentV1 studentV1()
    {
        return new StudentV1("Sai Sumanth");
    }

    @GetMapping("/v2/student")
    public StudentV2 studentV2() {

        return new StudentV2("Venky", "Version url student2");
    }

    //2. Request Parameter Versioning

    @GetMapping(value="/student",params="version=1")
    public StudentV1 studentParamV1()
    {
        return new StudentV1("Request Parameter Version1");
    }

    @GetMapping(value="/student",params="version=2")
    public StudentV2 studentParamV2()
    {
        return new StudentV2("Request Parameter Versioning","version2");
    }


    //3. Header Versioning
    @GetMapping(value="/student/header", headers="header_VERSION=1")
    public StudentV1 headerV1()
    {
        return new StudentV1(" Header Versioning--1 Sumanth");
    }

    @GetMapping(value="/student/header", headers="header_VERSION=2")
    public StudentV2 headerV2()
    {
        return new StudentV2("Sai","Sumanth");
    }


}
