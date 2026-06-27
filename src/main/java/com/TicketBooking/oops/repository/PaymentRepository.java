package com.TicketBooking.oops.repository;

import com.TicketBooking.oops.entity.Payment;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PaymentRepository extends ReactiveCrudRepository<Payment, Integer>
{
    Mono<Payment> findByTid(int tid);
}
