package com.eventSystem.events.services.impl;

import com.eventSystem.events.domain.entities.Ticket;
import com.eventSystem.events.repositories.TicketRepository;
import com.eventSystem.events.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;

    @Override
    public Page<Ticket> listTicketsForUser(UUID purchaserId, Pageable pageable) {
        return ticketRepository.findByPurchaserId(purchaserId, pageable);
    }

    @Override
    public Optional<Ticket> getTicketForUser(UUID purchaserId, UUID id) {
        return ticketRepository.findByIdAndPurchaserId(id, purchaserId);
    }
}
