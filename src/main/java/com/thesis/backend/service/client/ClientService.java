package com.thesis.backend.service.client;

import com.thesis.backend.entity.client.Client;
import com.thesis.backend.infrastructure.client.dto.ClientDTO;
import com.thesis.backend.common.exceptions.DataNotFoundException;
import com.thesis.backend.infrastructure.client.dto.ClientResponseDTO;

import java.util.List;

public interface ClientService {
    Client createClient(ClientDTO clientDTO);
    List<ClientResponseDTO> searchClients(String name, String email, String phoneNumber, String sortBy, String sortDir);
    List<Client> getAllClients();
    List<ClientResponseDTO> getClientsByUserId();
    Client getClientById(Long id) throws DataNotFoundException;
    Client updateClient(Long id, ClientDTO clientDTO) throws DataNotFoundException;
    void deleteClient(Long id) throws DataNotFoundException;
}
