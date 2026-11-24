package com.eventSystem.events.controllers;

import com.eventSystem.events.domain.dtos.GetTicketResponseDto;
import com.eventSystem.events.domain.dtos.ListTicketResponseDto;
import com.eventSystem.events.domain.entities.Ticket;
import com.eventSystem.events.domain.entities.TicketType;
import com.eventSystem.events.mappers.TicketMapper;
import com.eventSystem.events.services.QrCodeService;
import com.eventSystem.events.services.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

import static com.eventSystem.events.services.util.JwtUtil.parseUserId;

@Slf4j
@RestController
@RequestMapping(path="/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketMapper ticketMapper;
    private final TicketService ticketService;
    private final QrCodeService qrCodeService;

    @GetMapping
    public Page<ListTicketResponseDto> listTickets(
            @AuthenticationPrincipal Jwt jwt,
            Pageable pageable
    ) {
        return ticketService.listTicketsForUser(parseUserId(jwt),pageable).map(ticketMapper::toListTicketResponseDto);
    }

    @GetMapping(path = "/{ticketId}")
    public ResponseEntity<GetTicketResponseDto> getTicketForUser(
            @AuthenticationPrincipal Jwt jwt, @PathVariable UUID ticketId
    ){
       return ticketService.getTicketForUser(parseUserId(jwt),ticketId)
               .map(ticketMapper::toGetTicketResponseDto)
               .map(ResponseEntity::ok)
               .orElse(ResponseEntity.notFound().build());

    }

    @GetMapping(path="/{ticketId}/qr-codes")
    public ResponseEntity<byte[]> getTicketQrCode(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID ticketId
    ){
        byte[] qrCodeImage=qrCodeService.getQrCodeImageForUserAndTicket(parseUserId(jwt),ticketId);
        log.info("QrCodeImage:"+qrCodeImage);
        HttpHeaders headers=new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_PNG);
                headers.setContentLength(qrCodeImage.length);
        return ResponseEntity.ok().headers(headers).body(qrCodeImage);

    }
}
