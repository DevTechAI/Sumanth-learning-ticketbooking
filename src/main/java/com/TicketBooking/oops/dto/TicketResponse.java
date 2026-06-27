package com.TicketBooking.oops.dto;

import lombok.Data;

@Data
public class TicketResponse
{
    private int ticketId;
    private int userid;
    private int movieid;
    private String ticketStatus;
    private String bookingReference;

}
