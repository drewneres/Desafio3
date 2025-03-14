package com.eventmanager.service;

import com.eventmanager.model.Event;
import com.eventmanager.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public Event createEvent(Event event) {
        // Monta a URL da API ViaCEP com o CEP informado
        String viacepUrl = UriComponentsBuilder
                .fromHttpUrl("https://viacep.com.br/ws/" + event.getCep() + "/json/")
                .toUriString();
        
        // Faz a requisição à API e recebe os dados do endereço
        Map<String, String> address = restTemplate.getForObject(viacepUrl, Map.class);
        
        // Verifica se os dados foram retornados corretamente
        if (address != null && address.containsKey("logradouro")) {
            event.setLogradouro(address.get("logradouro"));
            event.setBairro(address.get("bairro"));
            event.setCidade(address.get("localidade"));
            event.setUf(address.get("uf"));
        } else {
            throw new RuntimeException("Endereço não encontrado para o CEP informado.");
        }
        
        // Salva o evento com os dados do endereço preenchidos
        return eventRepository.save(event);
    }

    public Event getEventById(String id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public void deleteEvent(String id) {
        eventRepository.deleteById(id);
    }
}
