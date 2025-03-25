package com.eventmanager.service;

import com.eventmanager.model.Event;
import com.eventmanager.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EventService eventService;

    private final String validCep = "01020000";
    private final String invalidCep = "00000000";

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(eventService, "restTemplate", restTemplate);
    }

    @Test
    void createEvent_WithValidCEP_ShouldSetAddressFields() {
        // Arrange
        Event event = createSampleEvent(validCep);
        Map<String, String> mockResponse = Map.of(
            "logradouro", "Rua Teste",
            "bairro", "Bairro Teste",
            "localidade", "São Paulo",
            "uf", "SP"
        );

        when(restTemplate.getForObject(anyString(), any())).thenReturn(mockResponse);
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        // Act
        Event result = eventService.createEvent(event);

        // Assert
        verify(restTemplate).getForObject(contains(validCep), eq(Map.class));
        assertEquals("Rua Teste", result.getLogradouro());
        assertEquals("Bairro Teste", result.getBairro());
        assertEquals("São Paulo", result.getCidade());
        assertEquals("SP", result.getUf());
    }

    @Test
    void createEvent_WithInvalidCEP_ShouldThrowException() {
        // Arrange
        Event event = createSampleEvent(invalidCep);
        when(restTemplate.getForObject(anyString(), any())).thenReturn(null);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> eventService.createEvent(event));
    }

    @Test
    void getEventById_ExistingId_ShouldReturnEvent() {
        // Arrange
        Event expectedEvent = createSampleEvent(validCep);
        when(eventRepository.findById("1")).thenReturn(Optional.of(expectedEvent));

        // Act
        Event result = eventService.getEventById("1");

        // Assert
        assertEquals(expectedEvent, result);
    }

    @Test
    void getEventById_NonExistingId_ShouldThrowException() {
        // Arrange
        when(eventRepository.findById("999")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> eventService.getEventById("999"));
    }

    @Test
    void getAllEvents_ShouldReturnAllEvents() {
        // Arrange
        List<Event> expectedEvents = Collections.singletonList(createSampleEvent(validCep));
        when(eventRepository.findAll()).thenReturn(expectedEvents);

        // Act
        List<Event> result = eventService.getAllEvents();

        // Assert
        assertEquals(1, result.size());
        assertEquals(expectedEvents, result);
    }

    @Test
    void deleteEvent_ShouldCallRepositoryDelete() {
        // Arrange
        doNothing().when(eventRepository).deleteById("1");

        // Act
        eventService.deleteEvent("1");

        // Assert
        verify(eventRepository).deleteById("1");
    }

    private Event createSampleEvent(String cep) {
        return new Event(
            "1",                       // id
            "Show",                    // eventName
            LocalDateTime.parse("2024-12-30T21:00:00"), // dateTime
            cep,                       // cep
            null,                      // logradouro (será preenchido pelo service)
            null,                      // bairro
            null,                      // cidade
            null,                      // uf
            "Descrição do evento"      // description
        );
    }
}