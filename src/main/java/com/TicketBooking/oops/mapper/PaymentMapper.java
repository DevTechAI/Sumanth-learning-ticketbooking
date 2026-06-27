package com.TicketBooking.oops.mapper;

import com.TicketBooking.oops.dto.PaymentRequest;
import com.TicketBooking.oops.dto.PaymentResponse;
import com.TicketBooking.oops.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper
{
    @Mapping(source = "ticketid", target = "tid")
    @Mapping(source = "payment_Method", target = "paymentMethod")
    @Mapping(target = "pid", ignore = true)
    @Mapping(target = "paymentStatus", ignore = true)
    @Mapping(target = "transactionReference", ignore = true)
    @Mapping(target = "razorpayOrderId", ignore = true)
    @Mapping(target = "razorpayPaymentId", ignore = true)
    Payment toPaymentEntity(PaymentRequest paymentRequest);


    @Mapping(source = "pid", target = "payid")
    @Mapping(source = "paymentStatus", target = "payment_Status")
    @Mapping(source = "transactionReference", target = "transaction_Reference")
    @Mapping(source = "amount", target = "amount_res")
    @Mapping(source = "razorpayOrderId", target = "razorpayOrderId")
    @Mapping(source = "razorpayPaymentId", target = "razorpayPaymentId")
    @Mapping(target = "razorpayKeyId", ignore = true)
    @Mapping(target = "currency", ignore = true)
    PaymentResponse toPaymentResponse(Payment payment);



}
