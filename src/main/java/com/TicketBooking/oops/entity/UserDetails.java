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
@Table(schema = "user_details")
public class UserDetails
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid")
    private int uid;

    @Column(name = "user_name")
    private String userName;

    @Nonnull
    @Column(name = "pass_word")
    private String PassWord;

    @Column(name = "ticket_number")
    private String ticketNumber;

    @Column(name = "email")
    private String email;

}
