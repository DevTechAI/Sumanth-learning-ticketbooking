package com.TicketBooking.oops.repository;

import com.TicketBooking.oops.entity.MovieDetails;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface MovieRepository extends ReactiveCrudRepository<MovieDetails, Integer>
{

    @Modifying
    @Query("""
        UPDATE movie_details
        SET availableTickets = availableTickets - 1
        WHERE mid = :mid
        AND availableTickets > 0
    """)
    Mono<Integer> decreaseAvailableTickets(Integer mid);

    Mono<MovieDetails> findByMid(Integer mid);

    Mono<MovieDetails> findByMovieName(String movieName);
}
