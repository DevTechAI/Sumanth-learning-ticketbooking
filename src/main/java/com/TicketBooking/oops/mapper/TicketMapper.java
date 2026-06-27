package com.TicketBooking.oops.mapper;

import com.TicketBooking.oops.dto.TicketResponse;
import com.TicketBooking.oops.entity.Tickets;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketMapper
{
    @Mapping(source = "tid", target = "ticketId")
    @Mapping(source = "uid", target = "userid")
    @Mapping(source = "mid", target = "movieid")
    @Mapping(source = "status", target = "ticketStatus")
    TicketResponse toTicketResponse(Tickets ticket);
}
