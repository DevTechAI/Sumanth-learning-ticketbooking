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
    @Mapping(source = "ticket.status", target = "status_res")
    @Mapping(source = "payment.pid", target = "paymentId")
    @Mapping(source = "payment.paymentStatus", target = "paymentStatus")
    @Mapping(source = "payment.razorpayOrderId", target = "razorpayOrderId")
    @Mapping(source = "payment.amount", target = "amount")
    @Mapping(target = "razorpayKeyId", ignore = true)
    @Mapping(target = "currency", ignore = true)
    BookingResponse toBookingResponse(Tickets ticket, Payment payment);
}
