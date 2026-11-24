package com.eventSystem.events.services.impl;

import com.eventSystem.events.domain.entities.Ticket;
import com.eventSystem.events.domain.entities.TicketStatusEnum;
import com.eventSystem.events.domain.entities.TicketType;
import com.eventSystem.events.domain.entities.User;
import com.eventSystem.events.exceptions.TicketSoldOutException;
import com.eventSystem.events.exceptions.TicketTypeNotFoundException;
import com.eventSystem.events.exceptions.UserNotFoundException;
import com.eventSystem.events.repositories.TicketRepository;
import com.eventSystem.events.repositories.TicketTypeRepository;
import com.eventSystem.events.repositories.UserRepository;
import com.eventSystem.events.services.QrCodeService;
import com.eventSystem.events.services.TicketTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketTypeServiceImpl implements TicketTypeService {

    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final QrCodeService qrCodeService;

    @Override
    @Transactional
    public Ticket purchaseTicket(UUID userId, UUID ticketTypeId) {
       User user= userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("User not found!"));
    TicketType ticketType=ticketTypeRepository.findByIdWithLock(ticketTypeId).orElseThrow(()->new TicketTypeNotFoundException("TicketType not found!"));
    int purchasedTickets=ticketRepository.countByTicketTypeId(ticketTypeId);
    Integer totalAvailable=ticketType.getTotalAvailable();
    if(purchasedTickets+1>totalAvailable){
        throw new TicketSoldOutException("Ticket Type Sold Out");
    }
    Ticket ticket=new Ticket();
    ticket.setStatus(TicketStatusEnum.PURCHASED);
    ticket.setTicketType(ticketType);
    ticket.setPurchaser(user);

    Ticket savedTicket=ticketRepository.save(ticket);
    qrCodeService.generateQrCode(savedTicket);

    return ticketRepository.save(savedTicket);

    }
}
