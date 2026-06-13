package com.TicketBooking.oops.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MovieDetails
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mid; //movieid

    private String movieName;
    private String movieTime;
    private int ticketPrice;
    private int totalTickets;  //volatile variable

}
