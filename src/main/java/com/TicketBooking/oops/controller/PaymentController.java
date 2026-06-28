package com.TicketBooking.oops.controller;

import com.TicketBooking.oops.dto.PaymentRequest;
import com.TicketBooking.oops.dto.PaymentResponse;
import com.TicketBooking.oops.entity.Payment;
import com.TicketBooking.oops.service.PaymentService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/payment/")
public class PaymentController
{
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService)
    {
        this.paymentService = paymentService;
    }

    @PostMapping("payprocess/")
    public Mono<PaymentResponse> processPayment(@RequestBody PaymentRequest paymentRequest)
    {
        return paymentService.processPayment(paymentRequest);
    }

    @PostMapping("createPay/")
    public Mono<Payment> createPayment(@RequestBody PaymentRequest paymentRequest)
    {
        return paymentService.createPayment(paymentRequest);
    }

    @PostMapping("/savepayment")
    public Mono<Payment> savePayment(@RequestBody Payment payment)
    {
        return paymentService.savePayment(payment);
    }

    @GetMapping("/getpayBytid/{tid}")
    public Mono<PaymentResponse> getPaymentByTicketId(@PathVariable int tid)
    {
        return paymentService.getPaymentByTicketId(tid);
    }
}
