package com.TicketBooking.oops.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(schema="movie_details")
public class MovieDetails
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="mid")
    private int mid; //movieid

    @Column(name="movie_name")
    private String movieName;

    @Column(name="movie_time")
    private String movieTime;

    @Column(name="ticket_price")
    private int ticketPrice;

    @Column(name="total_tickets")
    private int totalTickets;  //volatile variable

}
