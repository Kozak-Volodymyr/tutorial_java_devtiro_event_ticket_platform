package com.eventSystem.events.controllers;

import com.eventSystem.events.domain.dtos.GetPublishedEventDetailsResponseDto;
import com.eventSystem.events.domain.dtos.ListPublishedEventResponseDto;
import com.eventSystem.events.domain.entities.Event;
import com.eventSystem.events.mappers.EventMapper;
import com.eventSystem.events.services.EventService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path="/api/v1/published-events")
@RequiredArgsConstructor
public class PublishedEventController {
    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping
    public ResponseEntity<Page<ListPublishedEventResponseDto>> listAllPublishedEvents(
            @RequestParam(required=false) String q,
            Pageable pageable){
        Page<Event> events;
        if(null!=q && !q.trim().isEmpty()){
            events=eventService.searchPublishedEvents(q, pageable);
        }else {
            events=eventService.listPublishedEvents(pageable);
        }
        return ResponseEntity.ok(events.map(eventMapper::toListPublishedEventResponseDto));
    }

    @GetMapping(path="/{eventId}")
    public ResponseEntity<GetPublishedEventDetailsResponseDto> getPublishedEventDetailsResponseDtoResponseEntity
            (@PathVariable UUID eventId){
        return eventService.getPublishedEvent(eventId)
                .map(eventMapper::toGetPublishedEventDetailsResponseDto)
        .map(events->ResponseEntity.ok(events))
                .orElse(ResponseEntity.notFound().build());
    }


}
