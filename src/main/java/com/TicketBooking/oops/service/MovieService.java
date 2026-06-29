package com.TicketBooking.oops.service;

import com.TicketBooking.oops.dto.MovieRequest;
import com.TicketBooking.oops.entity.MovieDetails;
import com.TicketBooking.oops.mapper.MovieRequestMapper;
import com.TicketBooking.oops.repository.MovieRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MovieService
{
    private final MovieRepository movieRepository;
    private final MovieRequestMapper movieRequestMapper;

    public MovieService (MovieRepository movieRepository, MovieRequestMapper movieRequestMapper)
    {
        this.movieRepository = movieRepository;
        this.movieRequestMapper = movieRequestMapper;
    }

    public Flux<MovieDetails> getAllmovies()
    {
        return movieRepository.findAll();
    }

    public Mono<MovieDetails> getmovieById(int mid)
    {
        return movieRepository.findById(mid);
    }

    public Flux<MovieDetails> houseFullMovies()
    {
       return movieRepository.findAll()
               .filter(movie -> movie.getAvailableTickets() <= 0 || movie.getTotalTickets() <= 0);

    }

    public Mono<MovieDetails> createMovie(MovieRequest movieRequest)
    {
        if (movieRequest.getAvailableTickets() == 0) {
            movieRequest.setAvailableTickets(movieRequest.getTotalTickets());
        }
        MovieDetails movieDetails = movieRequestMapper.toEntity(movieRequest);
        return movieRepository.save(movieDetails);
    }

    public Mono<MovieDetails> updateMovie(MovieDetails movieDetails)
    {
        return movieRepository.findById(movieDetails.getMid())
                .flatMap(movie -> {
                    movie.setMovieName(movieDetails.getMovieName());
                    movie.setTotalTickets(movieDetails.getTotalTickets());
                    movie.setTicketPrice(movieDetails.getTicketPrice());
                    movie.setMovieTime(movieDetails.getMovieTime());
                    movie.setReleaseDate(movieDetails.getReleaseDate());
                    movie.setDuration(movieDetails.getDuration());
                    movie.setDescription(movieDetails.getDescription());
                    movie.setAvailableTickets(movieDetails.getAvailableTickets());

                    return movieRepository.save(movie);
                });
    }

    public Mono<Integer> getAvailableTicketsByMovieName(String movieName)
    {
        return movieRepository.findByMovieName(movieName)
                .map(MovieDetails::getAvailableTickets);
    }

}
