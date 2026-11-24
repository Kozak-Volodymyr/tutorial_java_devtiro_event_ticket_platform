package com.eventSystem.events.services.impl;

import com.eventSystem.events.domain.entities.*;
import com.eventSystem.events.exceptions.QrCodeNotFoundException;
import com.eventSystem.events.exceptions.TicketNotFoundException;
import com.eventSystem.events.exceptions.TicketTypeNotFoundException;
import com.eventSystem.events.repositories.QrCodeRepository;
import com.eventSystem.events.repositories.TicketRepository;
import com.eventSystem.events.repositories.TicketValidationRepository;
import com.eventSystem.events.services.TicketValidationService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketValidationServiceImpl implements TicketValidationService {
    private final TicketValidationRepository ticketValidationRepository;
    private final QrCodeRepository qrCodeRepository;
    private final TicketRepository ticketRepository;
    @Override
    public TicketValidation validateTicketByQrCode(UUID qrCodeID) {
        QrCode qrCode=qrCodeRepository.findByIdAndStatus(qrCodeID, QrCodeStatusEnum.ACTIVE).orElseThrow(()->new QrCodeNotFoundException("Qr Code with Id not found"));
        Ticket ticket=qrCode.getTicket();
        return ticketValidation(ticket,TicketValidationMethod.QR_SCAN);


    }

    private TicketValidation ticketValidation(Ticket ticket,TicketValidationMethod ticketValidationMethod) {
        TicketValidation ticketValidation=new TicketValidation();
        ticketValidation.setTicket(ticket);
        ticketValidation.setValidationMethod(ticketValidationMethod);

        TicketValidationStatusEnum ticketValidationStatusEnum= ticket.getValidations().stream().filter(v->TicketValidationStatusEnum.VALID.equals(v.getStatus()))
                .findFirst().map(v->TicketValidationStatusEnum.INVALID).orElse(TicketValidationStatusEnum.VALID);

        ticketValidation.setStatus(ticketValidationStatusEnum);
        return ticketValidationRepository.save(ticketValidation);
    }

    @Override
    public TicketValidation validateTicketManually(UUID ticketId) {
        Ticket ticket=ticketRepository.findById(ticketId).orElseThrow(()->new TicketNotFoundException());
        return ticketValidation(ticket,TicketValidationMethod.MANUAL);


    }
}
