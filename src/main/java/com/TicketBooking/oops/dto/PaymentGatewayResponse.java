package com.TicketBooking.oops.dto;

import lombok.Data;

@Data
public class PaymentGatewayResponse
{
    private boolean success;
    private String keyId;
    private String orderId;
    private String transactionReference;
    private int amount;
    private String currency;
    private String receipt;
    private String status;
    private String failureReason;
}
