package com.TicketBooking.oops.repository;

import com.TicketBooking.oops.entity.UserDetails;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ReactiveCrudRepository<UserDetails, Integer>
{
}
