package com.TicketBooking.oops.controller;

import com.TicketBooking.oops.dto.MovieRequest;
import com.TicketBooking.oops.entity.MovieDetails;
import com.TicketBooking.oops.service.MovieService;
import jakarta.annotation.Nonnull;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/movie")
public class MovieController
{

    private final MovieService movieService;

    public MovieController(MovieService movieService)
    {
        this.movieService = movieService;
    }

    @GetMapping("/getallMovies")
    public Flux<MovieDetails> getAllmovies()
    {
        return movieService.getAllmovies();
    }

    @GetMapping("/{id}")
    public Mono<MovieDetails> getMovieById(@PathVariable("id") @Nonnull int mid)
    {
        return movieService.getmovieById(mid);
    }

    @PostMapping("/addMovie")
    public Mono<MovieDetails> addMovie(@RequestBody MovieRequest movieRequest)
    {
        return movieService.createMovie(movieRequest);
    }

    @PutMapping("/updateMovie")
    public Mono<MovieDetails> updateMovie(@RequestBody MovieDetails movieDetails)
    {
        return movieService.updateMovie(movieDetails);
    }

    @GetMapping("/fullmovie")
    public Flux<MovieDetails> houseFullMovies()
    {
        return movieService.houseFullMovies();
    }

}