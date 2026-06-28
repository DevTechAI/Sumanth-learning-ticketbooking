package com.TicketBooking.oops.dto;

import lombok.Data;

@Data
public class PaymentGatewayResponse
{
    private boolean success;
    private String transactionReference;
    private String failureReason;
}
