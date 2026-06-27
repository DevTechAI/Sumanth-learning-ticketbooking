package com.TicketBooking.oops.dto;

import lombok.Data;

@Data
public class BookingResponse
{
    //1)mapstruct

    private int ticketId;
    private String booking_Reference;
    private String status_res;
    private int paymentId;
    private String paymentStatus;
    private String razorpayKeyId;
    private String razorpayOrderId;
    private int amount;
    private String currency;
}
