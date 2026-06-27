package com.TicketBooking.oops.repository;

import com.TicketBooking.oops.entity.Tickets;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TicketRepository extends ReactiveCrudRepository<Tickets, Integer>
{
    Mono<Tickets> findByBookingReference(String bookingReference);

    Flux<Tickets> findByUid(int uid);

    Flux<Tickets> findByMid(int mid);
}
