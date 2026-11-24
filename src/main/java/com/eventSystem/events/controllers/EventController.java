package com.eventSystem.events.controllers;

import com.eventSystem.events.domain.CreateEventRequest;
import com.eventSystem.events.domain.UpdateEventRequest;
import com.eventSystem.events.domain.dtos.*;
import com.eventSystem.events.domain.entities.Event;
import com.eventSystem.events.mappers.EventMapper;
import com.eventSystem.events.services.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.eventSystem.events.services.util.JwtUtil.parseUserId;

@RestController
@RequestMapping(path="/api/v1/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final EventMapper eventMapper;



    @PostMapping
    public ResponseEntity<CreateEventResponseDto> createEvent(@Valid  @RequestBody CreateEventRequestDto createEventRequestDto,
        @AuthenticationPrincipal Jwt jwt){
        CreateEventRequest createEventRequest=eventMapper.fromDto(createEventRequestDto);
        UUID userId=UUID.fromString(jwt.getSubject());

        Event createdEvent=eventService.createEvent(createEventRequest,userId);
        return new ResponseEntity<>(eventMapper.toDto(createdEvent), HttpStatus.CREATED);

    }

    @GetMapping
    public ResponseEntity<Page<ListEventResponseDto>> listEvents(@AuthenticationPrincipal Jwt jwt,
                                                                 Pageable pageable){
       Page<Event> events= eventService.listEventsForOrganizer(parseUserId(jwt),pageable);
       return new ResponseEntity<>(events.map(eventMapper::toListEventResponseDto), HttpStatus.OK);
    }



    @GetMapping(path="/{eventId}")
    public ResponseEntity<GetEventDetailsResponseDto> getEvent(@PathVariable UUID eventId,
                                                               @AuthenticationPrincipal Jwt jwt){
        UUID userId=parseUserId(jwt);
        return eventService
                .getEventForOrganizer(userId,eventId)
                .map(eventMapper::toGetEventDetailsResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(path="/{eventId}")
    public ResponseEntity<UpdateEventResponseDto> updateEvent(@Valid  @RequestBody UpdateEventRequestDto eventRequestDto,
                                                              @AuthenticationPrincipal Jwt jwt,
                                                                @PathVariable UUID eventId){
        UpdateEventRequest userRequest=eventMapper.fromDto(eventRequestDto);
        UUID userId=UUID.fromString(jwt.getSubject());

        Event updatedEvent=eventService.updateEventForOrganizer(userId,eventId,userRequest);
        return new ResponseEntity<>(eventMapper.toUpdateEventResponseDto(updatedEvent), HttpStatus.OK);

    }
    @DeleteMapping(path="/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID eventId,
                                            @AuthenticationPrincipal Jwt jwt){
        UUID userId=parseUserId(jwt);
        eventService.deleteEventForOrganizer(userId,eventId);
        return ResponseEntity.noContent().build();
    }

}
