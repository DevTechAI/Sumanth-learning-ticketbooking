package com.TicketBooking.oops.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieRequest
{

    private String movieName;

    private String movieTime;
    private int ticketPrice;

    private int totalTickets;  //volatile variable

    private LocalDate releaseDate;

    private String duration;
    private String description;

    private int availableTickets;
}
