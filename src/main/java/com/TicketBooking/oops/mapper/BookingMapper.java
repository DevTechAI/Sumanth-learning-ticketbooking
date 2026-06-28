package com.TicketBooking.oops.mapper;

import com.TicketBooking.oops.dto.BookingResponse;
import com.TicketBooking.oops.entity.Payment;
import com.TicketBooking.oops.entity.Tickets;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper
{

    @Mapping(source = "ticket.tid", target = "ticketId")
    @Mapping(source = "ticket.bookingReference", target = "booking_Reference")
    @Mapping(source = "ticket.status", target = "status_res")  //bookingResponse
   // @Mapping(source = "payment.pid", target = "payid")
   // @Mapping(source = "payment.transactionReference", target = "transaction_Reference")
   // @Mapping(source = "payment.paymentStatus", target = "payment_Status")
   // @Mapping(source = "payment.amount", target = "amount")
   // @Mapping(target = "message", constant = "Ticket booked successfully")
    BookingResponse toBookingResponse(Tickets ticket, Payment payment);
}
