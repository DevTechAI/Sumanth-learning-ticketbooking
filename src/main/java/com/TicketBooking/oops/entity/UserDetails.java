package com.TicketBooking.oops.entity;


import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
//@Table("User")
public class UserDetails
{

    //userid will be connected to movieid(movie)
    //moveid connected to ticketid(tickets)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int uid;

    private String userName;

    @Nonnull
    private String PassWord;
    private String ticketNumber;
    private String movieName;
    private String email;
}
