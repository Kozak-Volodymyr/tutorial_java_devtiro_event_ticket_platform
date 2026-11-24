package com.eventSystem.events.controllers;

import com.eventSystem.events.domain.dtos.TicketValidationRequestDto;
import com.eventSystem.events.domain.dtos.TicketValidationResponseDto;
import com.eventSystem.events.domain.entities.TicketValidation;
import com.eventSystem.events.domain.entities.TicketValidationMethod;
import com.eventSystem.events.mappers.TicketValidationMapper;
import com.eventSystem.events.services.TicketValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/api/v1/ticket-validations")
@RequiredArgsConstructor
public class TicketValidationController {
    private final TicketValidationService ticketValidationService;
    private final TicketValidationMapper ticketValidationMapper;

    @PostMapping
    public ResponseEntity<TicketValidationResponseDto> validateTicket(@RequestBody TicketValidationRequestDto ticketValidationRequestDto){
        TicketValidation ticketValidation;
        if(ticketValidationRequestDto.getMethod().equals(TicketValidationMethod.QR_SCAN)){
            ticketValidation=ticketValidationService.validateTicketByQrCode(ticketValidationRequestDto.getId());
        }else{
           ticketValidation=ticketValidationService.validateTicketManually(ticketValidationRequestDto.getId());
        }
        return ResponseEntity.ok(ticketValidationMapper.toTicketValidationResponseDto(ticketValidation));
    }
}
