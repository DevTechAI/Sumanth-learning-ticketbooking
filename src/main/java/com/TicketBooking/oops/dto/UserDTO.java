package com.TicketBooking.oops.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int uid;

    private String userName;
    private String ticketNumber;
    private String email;

}
