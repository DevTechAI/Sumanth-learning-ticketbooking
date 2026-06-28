package com.TicketBooking.oops.entity;


import com.TicketBooking.oops.enums.PaymentMethod;
import com.TicketBooking.oops.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "payment")
public class Payment
{
    @Id
    @Column("pid")
    private int pid;

    @Column("tid")
    private int tid;

    @Column("amount")
    private int amount;

    @Column("payment_method")
    private PaymentMethod paymentMethod;

    @Column("payment_status")
    private PaymentStatus paymentStatus;

    @Column("transactionReference")
    private String transactionReference;

}
