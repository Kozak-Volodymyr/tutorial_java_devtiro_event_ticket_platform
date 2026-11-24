package com.eventSystem.events.services.impl;

import com.eventSystem.events.domain.CreateEventRequest;
import com.eventSystem.events.domain.UpdateEventRequest;
import com.eventSystem.events.domain.UpdateTicketTypeRequest;
import com.eventSystem.events.domain.entities.Event;
import com.eventSystem.events.domain.entities.EventStatusEnum;
import com.eventSystem.events.domain.entities.TicketType;
import com.eventSystem.events.domain.entities.User;
import com.eventSystem.events.exceptions.EventNotFoundException;
import com.eventSystem.events.exceptions.EventUpdateException;
import com.eventSystem.events.exceptions.TicketTypeNotFoundException;
import com.eventSystem.events.exceptions.UserNotFoundException;
import com.eventSystem.events.mappers.EventMapper;
import com.eventSystem.events.repositories.EventRepository;
import com.eventSystem.events.repositories.UserRepository;
import com.eventSystem.events.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl  implements EventService {

    private final UserRepository userRepository;

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Override
    public Event createEvent(CreateEventRequest event, UUID organizerId) {
        User organizer=userRepository.findById(organizerId).orElseThrow(()->new UserNotFoundException(String.format("User with id %s not found",organizerId)));

        Event eventToCreate=new Event();

       List<TicketType> ticketTypeList= event.getTicketTypes().stream().map(
                ticketType ->{ TicketType ticketTypeToCreate=new TicketType();
                    ticketTypeToCreate.setEvent(eventToCreate);
                    ticketTypeToCreate.setName(ticketType.getName());
                    ticketTypeToCreate.setDescription(ticketType.getDescription());
                    ticketTypeToCreate.setPrice(ticketType.getPrice());
                    ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());
                    return ticketTypeToCreate;

                }).toList();

        eventToCreate.setName(event.getName());
        eventToCreate.setStart(event.getStart());
        eventToCreate.setEnd(event.getEnd());
        eventToCreate.setVenue(event.getVenue());
        eventToCreate.setSalesStart(event.getSalesStart());
        eventToCreate.setSalesEnd(event.getSalesEnd());
        eventToCreate.setStatus(event.getStatus());
        eventToCreate.setOrganizer(organizer);

        eventToCreate.setTicketTypes(ticketTypeList);

        return eventRepository.save(eventToCreate);
    }

    @Override
    public Page<Event> listEventsForOrganizer(UUID organizerId, Pageable pageable) {
      return eventRepository.findByOrganizerId(organizerId, pageable);
    }

    @Override
    public Optional<Event> getEventForOrganizer(UUID organizerId, UUID id) {
        return eventRepository.findByIdAndOrganizerId(id, organizerId);
    }

    @Override
    @Transactional
    public Event updateEventForOrganizer(UUID organizerId, UUID id, UpdateEventRequest event) {
        if(event.getId()==null){
            throw new EventUpdateException("Event id can't be null");
        }
        if(!id.equals(event.getId())){
            throw new EventUpdateException("Id's do not match ");
        }
        Event foundedEvent=eventRepository.findByIdAndOrganizerId(id, organizerId)
                .orElseThrow(()->new EventUpdateException("Event not found"));


        foundedEvent.setName(event.getName());
        foundedEvent.setStart(event.getStart());
        foundedEvent.setEnd(event.getEnd());
        foundedEvent.setVenue(event.getVenue());
        foundedEvent.setSalesStart(event.getSalesStart());
        foundedEvent.setSalesEnd(event.getSalesEnd());
        foundedEvent.setStatus(event.getStatus());

        Set<UUID> requestTicketTypeIds= event.getTicketTypes().stream()
                .map(UpdateTicketTypeRequest::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        foundedEvent.getTicketTypes().removeIf(ticketType -> !requestTicketTypeIds.contains(ticketType.getId()));

        Map<UUID,TicketType> existingTicketTypes=foundedEvent.getTicketTypes().stream()
                .collect(Collectors.toMap(TicketType::getId, Function.identity()));

        for(UpdateTicketTypeRequest ticketType : event.getTicketTypes()){
            if(null==ticketType.getId()){
                TicketType ticketTypeToCreate=new TicketType();
                ticketTypeToCreate.setEvent(foundedEvent);
                ticketTypeToCreate.setName(ticketType.getName());
                ticketTypeToCreate.setDescription(ticketType.getDescription());
                ticketTypeToCreate.setPrice(ticketType.getPrice());
                ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());
                foundedEvent.getTicketTypes().add(ticketTypeToCreate);
            }else if(existingTicketTypes.containsKey(ticketType.getId())){
                    TicketType existingTicketType=existingTicketTypes.get(ticketType.getId());
                    existingTicketType.setName(ticketType.getName());
                    existingTicketType.setDescription(ticketType.getDescription());
                    existingTicketType.setPrice(ticketType.getPrice());
                    existingTicketType.setTotalAvailable(ticketType.getTotalAvailable());

            }else{
                throw new TicketTypeNotFoundException("Ticket type does not exist");

            }
        }
        return eventRepository.save(foundedEvent);

    }

    @Override
    @Transactional
    public void deleteEventForOrganizer(UUID organizerId, UUID eventId) {
        getEventForOrganizer(organizerId, eventId).ifPresent(eventRepository::delete);
    }

    @Override
    public Page<Event> listPublishedEvents(Pageable pageable) {
        return eventRepository.findByStatus(EventStatusEnum.PUBLISHED,pageable);

    }

    @Override
    public Page<Event> searchPublishedEvents(String query, Pageable pageable) {
        return eventRepository.searchEvents(query, pageable);
    }

    @Override
    public Optional<Event> getPublishedEvent(UUID eventId) {
        return eventRepository.findByIdAndStatus(eventId, EventStatusEnum.PUBLISHED);
    }


}
