package com.TicketBooking.oops.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Tickets
{

    //primary and foreign key -->Id,
    // total number of tickets

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tid;

    //private String name;
    private String ticketNumber;

}
