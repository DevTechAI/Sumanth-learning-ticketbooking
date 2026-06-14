package com.TicketBooking.oops.repository;

import com.TicketBooking.oops.entity.Tickets;
import com.TicketBooking.oops.entity.UserDetails;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends ReactiveCrudRepository<Integer, Tickets>
{
    public List<Tickets> getAllTicketDetails();
}
