package com.TicketBooking.oops.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table("movie_details")
public class MovieDetails
{
    @Id
    @Column("mid")
    private int mid; //movieid

    @Column("movie_name") //title
    private String movieName;

    @Column("movie_time")
    private String movieTime;

    @Column("ticket_price")
    private int ticketPrice;

    @Column("total_tickets")
    private volatile int totalTickets;  //volatile variable

    // new entities
    @Column("releaseDate")
    private LocalDate releaseDate;

    @Column("duration")
    private String duration;

    @Column("description")
    private String description;

    @Column("availableTickets")
    private int availableTickets;

    @Version
    @Column("version")
    private Long version;
}
