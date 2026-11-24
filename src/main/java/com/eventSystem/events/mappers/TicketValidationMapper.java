package com.eventSystem.events.mappers;

import com.eventSystem.events.domain.dtos.TicketValidationRequestDto;
import com.eventSystem.events.domain.dtos.TicketValidationResponseDto;
import com.eventSystem.events.domain.entities.TicketValidation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketValidationMapper {
    @Mapping(target="ticketId",source="ticket.id")
    TicketValidationResponseDto toTicketValidationResponseDto(TicketValidation ticketValidation);

}
