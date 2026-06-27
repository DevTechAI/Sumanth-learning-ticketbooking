package com.TicketBooking.oops.dto;

import lombok.Data;

@Data
public class PaymentResponse
{
    private int payid;
    private int ticketId;
    private String payment_Status;
    private String transaction_Reference;
    private int amount_res;

}
