package com.eventmanager.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.eventmanager.model.Event;
import com.eventmanager.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;

class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private EventRepository eventRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateEventSuccessfully() {
        Event event = new Event("1", "Show", "2024-12-30T21:00:00", "01020-000");

        when(eventRepository.save(any(Event.class))).thenReturn(event);

        Event createdEvent = eventService.createEvent(event);

        assertNotNull(createdEvent);
        assertEquals("Show", createdEvent.getEventName());
    }

    @Test
    void shouldFindEventById() {
        Event event = new Event("1", "Show", "2024-12-30T21:00:00", "01020-000");

        when(eventRepository.findById("1")).thenReturn(Optional.of(event));

        Optional<Event> foundEvent = eventService.getEventById("1");

        assertTrue(foundEvent.isPresent());
        assertEquals("Show", foundEvent.get().getEventName());
    }
}
