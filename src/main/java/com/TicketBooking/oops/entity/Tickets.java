package com.TicketBooking.oops.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Table("tickets")
public class Tickets
{


    @Id
    @Column("tid")
    private int tid;

    //private String name;
    @Column("ticket_number")
    private String ticketNumber;

    @Column("mid")
    private int mid;

    @Column("uid")
    private int uid;

    // "C = Cancel and B= booked, SE = Seat Empty, cancelled Seats = CCT"
    @Column("status")
    private String status;

    //new entities
    @Column("price")
    private int price;

    @Column("bookingReference")
    private String bookingReference;

    @Column("createdAt")
    private LocalDateTime createdAt;

}
