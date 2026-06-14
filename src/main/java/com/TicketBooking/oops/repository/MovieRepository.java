package com.TicketBooking.oops.repository;

import com.TicketBooking.oops.entity.MovieDetails;
import com.TicketBooking.oops.entity.UserDetails;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends ReactiveCrudRepository<Integer, MovieRepository>
{
    public List<MovieDetails> getAllMovieDetails();
}
