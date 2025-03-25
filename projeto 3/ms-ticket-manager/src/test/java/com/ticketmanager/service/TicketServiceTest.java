package com.ticketmanager.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.ticketmanager.model.Ticket;
import com.ticketmanager.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

class TicketServiceTest {

    @InjectMocks
    private TicketService ticketService;

    @Mock
    private TicketRepository ticketRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateTicketSuccessfully() {
        Ticket ticket = new Ticket(
            "1",
            "John Doe",
            "12345678900",
            "john@example.com",
            "E1",
            "Concert",
            50.0,
            10.0,
            "ativo"
        );

        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        Ticket createdTicket = ticketService.createTicket(ticket);

        assertNotNull(createdTicket);
        assertEquals("John Doe", createdTicket.getCustomerName());
    }

    @Test
    void shouldFindTicketById() {
        Ticket ticket = new Ticket(
            "1",
            "John Doe",
            "12345678900",
            "john@example.com",
            "E1",
            "Concert",
            50.0,
            10.0,
            "ativo"
        );

        when(ticketRepository.findById("1")).thenReturn(Optional.of(ticket));

        Optional<Ticket> foundTicket = ticketService.getTicketById("1");

        assertTrue(foundTicket.isPresent());
        assertEquals("John Doe", foundTicket.get().getCustomerName());
    }

    @Test
    void shouldCancelTicketById() {
        Ticket ticket = new Ticket(
            "1",
            "John Doe",
            "12345678900",
            "john@example.com",
            "E1",
            "Concert",
            50.0,
            10.0,
            "ativo"
        );

        when(ticketRepository.findById("1")).thenReturn(Optional.of(ticket));

        ticketService.cancelTicket("1");

        assertEquals("cancelado", ticket.getStatus());
        verify(ticketRepository).save(ticket);
    }

    @Test
    void shouldCancelTicketByCpf() {
        Ticket ticket = new Ticket(
            "1",
            "John Doe",
            "12345678900",
            "john@example.com",
            "E1",
            "Concert",
            50.0,
            10.0,
            "ativo"
        );

        when(ticketRepository.findByCpf("12345678900")).thenReturn(List.of(ticket));

        ticketService.cancelTicketByCpf("12345678900");

        assertEquals("cancelado", ticket.getStatus());
        verify(ticketRepository).save(ticket);
    }

    @Test
    void shouldCheckTicketsByEventId() {
        when(ticketRepository.existsByEventId("E1")).thenReturn(true);

        boolean hasTickets = ticketService.checkTicketsByEvent("E1");

        assertTrue(hasTickets);
    }
}
