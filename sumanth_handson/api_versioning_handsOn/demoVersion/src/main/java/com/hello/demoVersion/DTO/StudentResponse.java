package com.hello.demoVersion.DTO;

import java.time.LocalDateTime;

public record StudentResponse(
        Long id,
        String name,
        String email,
        String course,
        LocalDateTime createdAt)
{

}
