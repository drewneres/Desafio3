package com.eventmanager.controller;

import com.ticketmanager.controller.TicketController;
import com.ticketmanager.model.Ticket;
import com.ticketmanager.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TicketControllerTest {

    @InjectMocks
    private TicketController ticketController;

    @Mock
    private TicketService ticketService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(ticketController).build();
    }

    @Test
    void shouldCreateTicket() throws Exception {
        Ticket ticket = new Ticket("1", "John Doe", "12345678900", "john@example.com", "E1", "Concert", 50.0, 10.0, "ativo");

        when(ticketService.createTicket(any(Ticket.class))).thenReturn(ticket);

        mockMvc.perform(post("/tickets/create-ticket")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"1\", \"customerName\":\"John Doe\", \"cpf\":\"12345678900\", \"customerMail\":\"john@example.com\", \"eventId\":\"E1\", \"eventName\":\"Concert\", \"BRLamount\":50.0, \"USDamount\":10.0, \"status\":\"ativo\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("John Doe"))
                .andExpect(jsonPath("$.cpf").value("12345678900"));
    }

    @Test
    void shouldGetTicketById() throws Exception {
        Ticket ticket = new Ticket("1", "John Doe", "12345678900", "john@example.com", "E1", "Concert", 50.0, 10.0, "ativo");

        when(ticketService.getTicketById("1")).thenReturn(Optional.of(ticket));

        mockMvc.perform(get("/tickets/get-ticket/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("John Doe"))
                .andExpect(jsonPath("$.cpf").value("12345678900"));
    }

    @Test
    void shouldReturnNotFoundWhenTicketNotExists() throws Exception {
        when(ticketService.getTicketById("1")).thenReturn(Optional.empty());

        mockMvc.perform(get("/tickets/get-ticket/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCheckTicketsByEventId() throws Exception {
        when(ticketService.checkTicketsByEvent("E1")).thenReturn(true);

        mockMvc.perform(get("/tickets/check-tickets-by-event/E1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value(true));
    }

    @Test
    void shouldUpdateTicket() throws Exception {
        Ticket ticket = new Ticket("1", "John Doe", "12345678900", "john@example.com", "E1", "Concert", 50.0, 10.0, "ativo");

        when(ticketService.updateTicket(eq("1"), any(Ticket.class))).thenReturn(ticket);

        mockMvc.perform(put("/tickets/update-ticket/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"1\", \"customerName\":\"John Doe\", \"cpf\":\"12345678900\", \"customerMail\":\"john@example.com\", \"eventId\":\"E1\", \"eventName\":\"Concert\", \"BRLamount\":50.0, \"USDamount\":10.0, \"status\":\"ativo\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("John Doe"));
    }

    @Test
    void shouldCancelTicket() throws Exception {
        doNothing().when(ticketService).cancelTicket("1");

        mockMvc.perform(delete("/tickets/cancel-ticket/1"))
                .andExpect(status().isNoContent());
    }
}
