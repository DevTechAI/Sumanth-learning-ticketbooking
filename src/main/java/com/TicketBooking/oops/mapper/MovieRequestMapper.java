package com.TicketBooking.oops.mapper;


import com.TicketBooking.oops.dto.MovieRequest;
import com.TicketBooking.oops.entity.MovieDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovieRequestMapper
{
    @Mapping(target = "mid", ignore = true)
    MovieDetails toEntity(MovieRequest movieRequest);

    MovieRequest toDto(MovieDetails movieDetails);
}
