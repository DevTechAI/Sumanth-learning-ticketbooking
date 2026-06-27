package com.TicketBooking.oops.dto;

import com.TicketBooking.oops.enums.PaymentMethod;
import lombok.Data;

@Data
public class PaymentRequest
{
    private int ticketid;
    private String bookingReference;
    private PaymentMethod payment_Method;
    private int amount;

}
