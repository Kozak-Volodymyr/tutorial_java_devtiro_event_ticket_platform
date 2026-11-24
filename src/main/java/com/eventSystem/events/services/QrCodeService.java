package com.eventSystem.events.services;

import com.eventSystem.events.domain.entities.QrCode;
import com.eventSystem.events.domain.entities.Ticket;

import java.util.UUID;

public interface QrCodeService {
    QrCode generateQrCode(Ticket ticket);

    byte[] getQrCodeImageForUserAndTicket(UUID userId, UUID ticketPurchaserId);
}
