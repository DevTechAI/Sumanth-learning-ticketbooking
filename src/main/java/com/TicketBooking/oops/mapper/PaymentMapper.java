package com.TicketBooking.oops.mapper;

import com.TicketBooking.oops.dto.PaymentRequest;
import com.TicketBooking.oops.dto.PaymentResponse;
import com.TicketBooking.oops.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper
{
    @Mapping(source = "payment_Method", target = "paymentMethod")
    Payment toPaymentEntity(PaymentRequest paymentRequest);

    @Mapping(source = "pid", target = "payid")
    @Mapping(source = "tid", target = "ticketId")
    @Mapping(source = "paymentStatus", target = "payment_Status")
    @Mapping(source = "transactionReference", target = "transaction_Reference")
    @Mapping(source = "amount", target = "amount_res")
    PaymentResponse toPaymentResponse(Payment payment);


}
