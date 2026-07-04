package com.TicketBooking.oops.controller;

import com.TicketBooking.oops.dto.BookTicketRequest;
import com.TicketBooking.oops.dto.BookingResponse;
import com.TicketBooking.oops.dto.TicketResponse;
import com.TicketBooking.oops.entity.Tickets;
import com.TicketBooking.oops.service.BookingService;
import com.TicketBooking.oops.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TicketBookingController
{
    private final BookingService bookingService;
    private final TicketService ticketService;

    public TicketBookingController(BookingService bookingService, TicketService ticketService)
    {
        this.bookingService= bookingService;
        this.ticketService = ticketService;
    }


    @PostMapping("/book")
    public Mono<BookingResponse> bookTicket(@RequestBody BookTicketRequest request)
    {
        return bookingService.bookTicket(request);
    }

    @PostMapping("/multiple")
    public Flux<BookingResponse> bookMultipleTickets(@RequestBody List<BookTicketRequest> requests)
    {
            return Flux.fromIterable(requests)
                    .flatMap(request ->
                                    bookingService.bookTicket(request)
                                            .subscribeOn(Schedulers.boundedElastic()),
                            10 // concurrency: 10 parallel calls
                    );
    }


    @GetMapping("/getticket/{bookingReference}")
    public Mono<TicketResponse> getTicket(@PathVariable String bookingReference)
    {
        return ticketService.getTicketByBookingReference(bookingReference);
    }

    @PutMapping("/cancel/{tid}")
    public Mono<Tickets> cancelTicket(@PathVariable int tid)
    {
        return bookingService.cancelTicket(tid);
    }

    @GetMapping("/getbookingcode/{bookingReference}")
    public Mono<TicketResponse> getTicketByBookingCode(@PathVariable String bookingReference)
    {
        return ticketService.getTicketByBookingReference(bookingReference);
    }

}

