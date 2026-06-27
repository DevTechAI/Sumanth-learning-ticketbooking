package com.TicketBooking.oops.service;


import com.TicketBooking.oops.dto.BookTicketRequest;
import com.TicketBooking.oops.dto.BookingResponse;
import com.TicketBooking.oops.dto.PaymentRequest;
import com.TicketBooking.oops.dto.PaymentResponse;
import com.TicketBooking.oops.entity.MovieDetails;
import com.TicketBooking.oops.entity.Tickets;
import com.TicketBooking.oops.entity.UserDetails;
import com.TicketBooking.oops.enums.TicketStatus;
import com.TicketBooking.oops.repository.MovieRepository;
import com.TicketBooking.oops.repository.TicketRepository;
import com.TicketBooking.oops.repository.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class BookingService
{
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final TicketRepository ticketRepository;
    private final PaymentService paymentService;

    public BookingService(UserRepository userRepository, MovieRepository movieRepository, TicketRepository ticketRepository, PaymentService paymentService)
    {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
        this.ticketRepository = ticketRepository;
        this.paymentService = paymentService;
    }

    public Mono<UserDetails> validateUser(int userId)
    {
        //return userRepository.findby
        return userRepository.findById(userId).switchIfEmpty(Mono.error(new RuntimeException("create user")));
    }

    public Mono<MovieDetails> validateMovie(int movieId)
    {
        return movieRepository.findById(movieId)
                .switchIfEmpty(Mono.error(new RuntimeException("Movie doesn't exist")));
    }

    private String generateTicketNumber()
    {
        return "TKT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String generateBookingReference()
    {
        return "BK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }


    public Mono<BookingResponse> bookTicket(BookTicketRequest bookTicketRequest)
    {
        Mono<UserDetails> user = validateUser(bookTicketRequest.getUid());
        Mono<MovieDetails> movie = validateMovie(bookTicketRequest.getMid());

        return Mono.zip(user, movie)
                .flatMap(tuple -> {
                    MovieDetails movieDetails = tuple.getT2();
                    int availableTickets = movieDetails.getAvailableTickets() > 0
                            ? movieDetails.getAvailableTickets()
                            : movieDetails.getTotalTickets();

                    if (availableTickets <= 0) {
                        return Mono.error(new RuntimeException("Tickets are not available for this movie"));
                    }

                    Tickets tickets = new Tickets();
                    tickets.setUid(bookTicketRequest.getUid());
                    tickets.setMid(bookTicketRequest.getMid());
                    tickets.setTicketNumber(generateTicketNumber());
                    tickets.setPrice(movieDetails.getTicketPrice());
                    tickets.setBookingReference(generateBookingReference());
                    tickets.setCreatedAt(LocalDateTime.now());
                    tickets.setStatus(TicketStatus.PENDING.toString());

                    movieDetails.setAvailableTickets(availableTickets - 1);
                    movieDetails.setTotalTickets(Math.max(movieDetails.getTotalTickets() - 1, 0));

                    return movieRepository.save(movieDetails)
                            .then(ticketRepository.save(tickets))
                            .flatMap(savedTicket ->
                                    paymentService.processPayment(toPaymentRequest
                                                    (savedTicket, bookTicketRequest))
                                    .map(paymentResponse -> buildBookingResponse(savedTicket, paymentResponse)));
                });
    }

    public Mono<Tickets> cancelTicket(int tid)
    {
        return ticketRepository.findById(tid)
                .switchIfEmpty(Mono.error(new RuntimeException("Ticket doesn't exist")))
                .flatMap(ticket -> {
                    if (TicketStatus.CANCELLED.toString().equals(ticket.getStatus())) {
                        return Mono.just(ticket);
                    }

                    ticket.setStatus(TicketStatus.CANCELLED.toString());
                    Mono<MovieDetails> updateMovieTickets = movieRepository.findById(ticket.getMid())
                            .flatMap(movie -> {
                                movie.setAvailableTickets(movie.getAvailableTickets() + 1);
                                movie.setTotalTickets(movie.getTotalTickets() + 1);
                                return movieRepository.save(movie);
                            });

                    return updateMovieTickets.then(ticketRepository.save(ticket));
                });
    }

    public Mono<Tickets> getBookingDetails(String bookingReference)
    {
        return ticketRepository.findByBookingReference(bookingReference)
                .switchIfEmpty(Mono.error(new RuntimeException("Booking doesn't exist")));
    }

    private PaymentRequest toPaymentRequest(Tickets ticket, BookTicketRequest bookTicketRequest)
    {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setTicketid(ticket.getTid());
        paymentRequest.setBookingReference(ticket.getBookingReference());
        paymentRequest.setPayment_Method(bookTicketRequest.getPaymentMethod());
        paymentRequest.setAmount(ticket.getPrice());
        return paymentRequest;
    }

    private BookingResponse buildBookingResponse(Tickets ticket, PaymentResponse paymentResponse)
    {
        BookingResponse bookingResponse = new BookingResponse();
        bookingResponse.setTicketId(ticket.getTid());
        bookingResponse.setBooking_Reference(ticket.getBookingReference());
        bookingResponse.setStatus_res(ticket.getStatus());
        bookingResponse.setPaymentId(paymentResponse.getPayid());
        bookingResponse.setPaymentStatus(paymentResponse.getPayment_Status());
        bookingResponse.setRazorpayKeyId(paymentResponse.getRazorpayKeyId());
        bookingResponse.setRazorpayOrderId(paymentResponse.getRazorpayOrderId());
        bookingResponse.setAmount(paymentResponse.getAmount_res());
        bookingResponse.setCurrency(paymentResponse.getCurrency());
        return bookingResponse;
    }

}
