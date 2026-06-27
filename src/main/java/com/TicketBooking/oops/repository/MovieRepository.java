package com.TicketBooking.oops.repository;

import com.TicketBooking.oops.entity.MovieDetails;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface MovieRepository extends ReactiveCrudRepository<MovieDetails, Integer>
{
    Mono<MovieDetails> findByMovieName(String movieName);
}
