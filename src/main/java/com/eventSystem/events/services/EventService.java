package com.eventSystem.events.services;

import com.eventSystem.events.domain.CreateEventRequest;
import com.eventSystem.events.domain.UpdateEventRequest;
import com.eventSystem.events.domain.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


public interface EventService {
    Event createEvent(CreateEventRequest event, UUID organizerId);
    Page<Event> listEventsForOrganizer(UUID organizerId, Pageable pageable);
    Optional<Event> getEventForOrganizer(UUID organizerId,UUID id);
    Event updateEventForOrganizer(UUID organizerId, UUID id, UpdateEventRequest event);
    void deleteEventForOrganizer(UUID organizerId, UUID eventId);
    Page<Event> listPublishedEvents(Pageable pageable);
    Page<Event> searchPublishedEvents(String query,Pageable pageable);
    Optional<Event> getPublishedEvent(UUID eventId);
}
