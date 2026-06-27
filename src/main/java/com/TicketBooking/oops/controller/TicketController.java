package com.TicketBooking.oops.controller;

import com.TicketBooking.oops.dto.BookTicketRequest;
import com.TicketBooking.oops.dto.BookingResponse;
import com.TicketBooking.oops.entity.Tickets;
import com.TicketBooking.oops.service.BookingService;
import com.TicketBooking.oops.service.MovieService;
import com.TicketBooking.oops.service.TicketService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class TicketController
{
//    private final TicketService ticketService;
//    private final BookingService bookingService;
//    private final MovieService movieService;
//
//    public TicketController (TicketService ticketService, BookingService bookingService, MovieService movieService)
//    {
//        this.ticketService = ticketService;
//        this.bookingService = bookingService;
//        this.movieService = movieService;
//    }
//
//    @PostMapping("bookTicket/")
//    public Mono<BookingResponse> bookTicket(@RequestBody BookTicketRequest bookTicketRequest)
//    {
//        return bookingService.bookTicket(bookTicketRequest);
//    }
//
//    @GetMapping("/getseatcount/{moviename}")
//    public Mono<Integer> getSeatCount(@PathVariable("moviename") String movieName)
//    {
//        return movieService.getAvailableTicketsByMovieName(movieName);
//    }
//
//    @GetMapping("/Booked/{uid}")
//    public Flux<Tickets> ticketsBooked(@PathVariable int uid)
//    {
//        return ticketService.getTicketsBookedByUser(uid);
//    }
//
//    @DeleteMapping("/cancel/{tid}")
//    public Mono<Tickets> cancelTicket(@PathVariable int tid)
//    {
//        return bookingService.cancelTicket(tid);
//    }

}

