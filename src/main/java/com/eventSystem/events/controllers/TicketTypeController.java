package com.eventSystem.events.controllers;

import com.eventSystem.events.domain.entities.TicketType;
import com.eventSystem.events.domain.entities.User;
import com.eventSystem.events.services.TicketTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.eventSystem.events.services.util.JwtUtil.parseUserId;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/api/v1/events/{eventId}/ticket-types")
public class TicketTypeController {
    private final TicketTypeService ticketTypeService;

    @PostMapping(path="/{ticketTypeId}/tickets")
    public ResponseEntity<Void> purchaseTicket(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID ticketTypeId) {
        UUID userId=parseUserId(jwt);
        ticketTypeService.purchaseTicket(userId, ticketTypeId);
        return ResponseEntity.noContent().build();


    }
}
