package com.TicketBooking.oops.service;

import com.TicketBooking.oops.dto.TicketResponse;
import com.TicketBooking.oops.entity.Tickets;
import com.TicketBooking.oops.enums.TicketStatus;
import com.TicketBooking.oops.mapper.TicketMapper;
import com.TicketBooking.oops.repository.TicketRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TicketService
{
   private final TicketRepository ticketRepository;
   private final TicketMapper ticketMapper;

   public TicketService(TicketRepository ticketRepository, TicketMapper ticketMapper)
   {
       this.ticketRepository = ticketRepository;
       this.ticketMapper = ticketMapper;
   }

   public Mono<Tickets> getTicketNumber(int tid)
   {
       return ticketRepository.findById(tid);
   }

    public Flux<Tickets> getTicketDetails()
    {
        return ticketRepository.findAll();
    }

    public Mono<TicketResponse> getTicketByBookingReference(String bookingReference)
    {
        return ticketRepository.findByBookingReference(bookingReference)
                .map(ticketMapper::toTicketResponse);
    }

    public Flux<Tickets> getTicketsBookedByUser(int uid)
    {
        return ticketRepository.findByUid(uid)
                .filter(ticket -> TicketStatus.BOOKED.toString().equals(ticket.getStatus()));
    }

    public Mono<Tickets> cancelTicket(int tid)
    {
        return ticketRepository.findById(tid)
                .flatMap(ticket -> {
                    ticket.setStatus(TicketStatus.CANCELLED.toString());
                    return ticketRepository.save(ticket);
                });
    }

}
