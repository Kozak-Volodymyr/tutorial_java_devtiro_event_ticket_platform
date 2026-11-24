package com.eventSystem.events.mappers;

import com.eventSystem.events.domain.CreateEventRequest;
import com.eventSystem.events.domain.CreateTicketTypeRequest;
import com.eventSystem.events.domain.UpdateEventRequest;
import com.eventSystem.events.domain.UpdateTicketTypeRequest;
import com.eventSystem.events.domain.dtos.*;
import com.eventSystem.events.domain.entities.Event;
import com.eventSystem.events.domain.entities.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {
    CreateTicketTypeRequest fromCreateTicketTypeRequestDto(CreateTicketTypeRequestDto createTicketTypeRequestDto);

    CreateEventRequest fromDto(CreateEventRequestDto createEventRequestDto);

    CreateEventResponseDto toDto(Event event);

    ListEventTicketTypeResponseDto toDto(TicketType ticketType);

    ListEventResponseDto toListEventResponseDto(Event event);

    GetEventTicketTypeResponseDto toGetEventTicketTypeResponseDto(TicketType ticketType);

    GetEventDetailsResponseDto toGetEventDetailsResponseDto(Event event);

    UpdateTicketTypeRequest fromDto(UpdateTicketTypeRequestDto updateTicketTypeRequest);

   UpdateEventRequest fromDto(UpdateEventRequestDto updateEventRequestDto);

   UpdateTicketTypeResponseDto toUpdateTicketTypeResponseDto(TicketType ticketType);

   UpdateEventResponseDto toUpdateEventResponseDto(Event event);

   ListPublishedEventResponseDto toListPublishedEventResponseDto(Event event);

    GetPublishedEventTicketTypeResponseDto toGetPublishedEventTicketTypeResponseDto(TicketType ticketType);

   GetPublishedEventDetailsResponseDto toGetPublishedEventDetailsResponseDto(Event event);


}
