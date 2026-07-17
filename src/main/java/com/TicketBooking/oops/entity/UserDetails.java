package com.TicketBooking.oops.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

//@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table( "user_details")
public class UserDetails
{
    @Id
    @Column("uid")
    private int uid;

    @Column( "user_name")
    private String userName;

    @Column( "pass_word")
    private String PassWord;

    @Column( "ticket_number")
    private String ticketNumber;

    @Column( "email")
    private String email;

}
