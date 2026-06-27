package com.TicketBooking.oops.dto;

import com.TicketBooking.oops.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookTicketRequest
{
    private int uid;
    private int mid;
    private PaymentMethod paymentMethod;
}
