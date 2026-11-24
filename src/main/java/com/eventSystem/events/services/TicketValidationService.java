package com.eventSystem.events.services;

import com.eventSystem.events.domain.entities.TicketValidation;

import java.util.UUID;

public interface TicketValidationService {
    TicketValidation validateTicketByQrCode(UUID qrCodeID);
    TicketValidation validateTicketManually(UUID ticketId);

}
