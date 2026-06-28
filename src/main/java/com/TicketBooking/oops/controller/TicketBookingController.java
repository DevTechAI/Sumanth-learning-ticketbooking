package com.TicketBooking.oops.controller;

import com.TicketBooking.oops.dto.BookTicketRequest;
import com.TicketBooking.oops.dto.TicketResponse;
import com.TicketBooking.oops.entity.Tickets;
import com.TicketBooking.oops.service.BookingService;
import com.TicketBooking.oops.service.TicketService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

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

    @GetMapping("/hello")
    public String HelloControl()
    {
        return "Hello WOrld";
    }

    @PostMapping("/book")
    public Mono<Tickets> bookTicket(@RequestBody BookTicketRequest request)
    {
        return bookingService.bookTicket(request);
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

