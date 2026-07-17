package com.TicketBooking.oops.service;

import com.TicketBooking.oops.client.PaymentGatewayClient;
import com.TicketBooking.oops.dto.PaymentGatewayResponse;
import com.TicketBooking.oops.dto.PaymentRequest;
import com.TicketBooking.oops.dto.PaymentResponse;
import com.TicketBooking.oops.dto.PaymentVerificationRequest;
import com.TicketBooking.oops.entity.Payment;
import com.TicketBooking.oops.enums.PaymentStatus;
import com.TicketBooking.oops.enums.TicketStatus;
import com.TicketBooking.oops.mapper.PaymentMapper;
import com.TicketBooking.oops.repository.PaymentRepository;
import com.TicketBooking.oops.repository.TicketRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class PaymentService
{
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentGatewayClient paymentGatewayClient;
    private final TicketRepository ticketRepository;

    public PaymentService(PaymentRepository paymentRepository, PaymentMapper paymentMapper, PaymentGatewayClient paymentGatewayClient, TicketRepository ticketRepository)
    {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.paymentGatewayClient = paymentGatewayClient;
        this.ticketRepository = ticketRepository;
    }

    public Mono<PaymentResponse> processPayment(PaymentRequest paymentRequest)
    {
        return createPayment(paymentRequest)
                .flatMap(this::savePayment)
                .map(this::buildPaymentResponse);
    }

    public Mono<Payment> createPayment(PaymentRequest paymentRequest)
    {
        return Mono.fromCallable(() -> paymentGatewayClient.createOrder(paymentRequest.getAmount(), paymentRequest.getBookingReference()))
                .subscribeOn(Schedulers.boundedElastic())
                .map(gatewayResponse -> buildPayment(paymentRequest, gatewayResponse));
    }

    public Mono<Payment> savePayment(Payment payment)
    {
        return paymentRepository.save(payment);
    }

    public Mono<PaymentResponse> getPaymentByTicketId(int ticketId)
    {
        return paymentRepository.findByTid(ticketId)
                .map(this::buildPaymentResponse);
    }

    public Mono<PaymentResponse> verifyPayment(PaymentVerificationRequest verificationRequest)
    {
        return Mono.fromCallable(() -> paymentGatewayClient.verifyPayment(verificationRequest))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(verified -> paymentRepository.findByRazorpayOrderId(verificationRequest.getRazorpayOrderId())
                        .switchIfEmpty(Mono.error(new RuntimeException("Payment order doesn't exist")))
                        .flatMap(payment -> {
                            payment.setRazorpayPaymentId(verificationRequest.getRazorpayPaymentId());
                            if (verified) {
                                payment.setPaymentStatus(PaymentStatus.SUCCESS);
                                payment.setTransactionReference(verificationRequest.getRazorpayPaymentId());
                                return ticketRepository.findById(payment.getTid())
                                        .flatMap(ticket -> {
                                            ticket.setStatus(TicketStatus.BOOKED.toString());
                                            return ticketRepository.save(ticket);
                                        })
                                        .then(paymentRepository.save(payment));
                            } else {
                                payment.setPaymentStatus(PaymentStatus.FAILED);
                                return paymentRepository.save(payment);
                            }
                        }))
                .map(this::buildPaymentResponse);
    }

    public PaymentResponse buildPaymentResponse(Payment payment)
    {
        PaymentResponse response = paymentMapper.toPaymentResponse(payment);
        response.setRazorpayKeyId(paymentGatewayClient.getKeyId());
        response.setCurrency(paymentGatewayClient.getCurrency());
        return response;
    }

    private Payment buildPayment(PaymentRequest paymentRequest, PaymentGatewayResponse gatewayResponse)
    {
        Payment payment = paymentMapper.toPaymentEntity(paymentRequest);
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setTransactionReference(gatewayResponse.getTransactionReference());
        payment.setRazorpayOrderId(gatewayResponse.getOrderId());
        return payment;
    }
}
