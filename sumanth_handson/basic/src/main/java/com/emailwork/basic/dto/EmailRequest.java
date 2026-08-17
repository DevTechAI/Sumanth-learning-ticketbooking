package com.emailwork.basic.dto;

import java.util.List;

public record EmailRequest(
        String to,
        String subject,
        String body,
        List<String> cc
) {
}
