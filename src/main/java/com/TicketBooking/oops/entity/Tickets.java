package com.TicketBooking.oops.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(schema = "tickets")
public class Tickets
{

    //primary and foreign key -->Id,
    // total number of tickets

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int tid;

    //private String name;
    @Column(name="ticket_number")
    private String ticketNumber;

    @Column(name="mid")
    private int mid;

    @Column(name="uid")
    private int uid;

}
