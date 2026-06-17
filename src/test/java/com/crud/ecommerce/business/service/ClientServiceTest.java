package com.crud.ecommerce.business.service;

import com.crud.ecommerce.business.mapper.ClientMapper;
import com.crud.ecommerce.business.util.DateUtils;
import com.crud.ecommerce.business.validation.ClientValidation;
import com.crud.ecommerce.dto.response.Response;
import com.crud.ecommerce.dto.response.client.ClientResponse;
import com.crud.ecommerce.dto.resquest.client.AddressCreateInput;
import com.crud.ecommerce.dto.resquest.client.ClientCreateInput;
import com.crud.ecommerce.dto.resquest.client.ClientUpdateInput;
import com.crud.ecommerce.exception.ConflictException;
import com.crud.ecommerce.exception.NotFoundException;
import com.crud.ecommerce.infrastructure.entity.client.Client;
import com.crud.ecommerce.infrastructure.repository.client.ClientRepository;
import com.crud.ecommerce.infrastructure.repository.review.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private ClientValidation clientValidation;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ClientService clientService;

    private Client validClient() {
        Client client = new Client();
        client.setId(1L);
        client.setFullName("João Pedro Silva");
        client.setEmail("joao.pedro@email.com");
        client.setPhone("(11) 99999-1234");
        return client;
    }

    private ClientCreateInput validCreateInput() {
        AddressCreateInput address = new AddressCreateInput(
                "Rua das Flores", "123", "Apto 45",
                "Centro", "São Paulo", "SP", "01310-100"
        );
        return new ClientCreateInput(
                "João Pedro Silva", "joao.pedro@email.com", "(11) 99999-1234",
                true, "123.456.789-09", "12.345.678-9", address
        );
    }

    // ==================== getAllClients ====================

    @Test
    void shouldReturnAllClientsSorted() {
        List<Client> clients = List.of(validClient());
        List<ClientResponse> expectedResponses = List.of(
                new ClientResponse(1L, "João Pedro Silva", "joao.pedro@email.com", "(11) 99999-1234",
                        null, DateUtils.databaseNow(), DateUtils.databaseNow())
        );

        when(clientRepository.findAll(any(org.springframework.data.domain.Sort.class))).thenReturn(clients);
        when(clientMapper.toClientResponseList(clients)).thenReturn(expectedResponses);

        List<ClientResponse> result = clientService.getAllClients("name");

        assertEquals(expectedResponses, result);
    }

    // ==================== getClientById ====================

    @Test
    void shouldReturnClientByIdWhenFound() {
        Client client = validClient();
        ClientResponse expectedResponse = new ClientResponse(
                1L, "João Pedro Silva", "joao.pedro@email.com", "(11) 99999-1234",
                null, DateUtils.databaseNow(), DateUtils.databaseNow()
        );

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientMapper.toClientResponse(client)).thenReturn(expectedResponse);

        ClientResponse result = clientService.getClientById(1L);

        assertEquals(expectedResponse, result);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenClientDoesNotExist() {
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> clientService.getClientById(99L));
    }

    // ==================== createClient ====================

    @Test
    void shouldCreateClientSuccessfully() {
        ClientCreateInput input = validCreateInput();

        doNothing().when(clientValidation).validateCreate(input);
        when(clientRepository.save(any(Client.class))).thenReturn(validClient());

        Response response = clientService.createClient(input);

        assertTrue(response.success());
        assertEquals("Cliente criado com sucesso", response.message());
        verify(clientValidation).validateCreate(input);
        verify(clientMapper).applyCreateInput(any(Client.class), eq(input));
        verify(clientRepository).save(any(Client.class));
    }

    @Test
    void shouldNotSaveClientWhenValidationFails() {
        ClientCreateInput input = validCreateInput();

        doThrow(new com.crud.ecommerce.exception.BadRequestException("Dados inválidos"))
                .when(clientValidation).validateCreate(input);

        assertThrows(com.crud.ecommerce.exception.BadRequestException.class,
                () -> clientService.createClient(input));

        verify(clientRepository, never()).save(any(Client.class));
    }

    // ==================== updateClient ====================

    @Test
    void shouldUpdateClientSuccessfully() {
        Client existingClient = validClient();
        ClientUpdateInput input = new ClientUpdateInput(
                "Nome Atualizado", null, null, null, null, null, null
        );

        when(clientRepository.findById(1L)).thenReturn(Optional.of(existingClient));
        when(clientRepository.save(any(Client.class))).thenReturn(existingClient);

        Response response = clientService.updateClient(1L, input);

        assertTrue(response.success());
        assertEquals("Cliente atualizado com sucesso", response.message());
        verify(clientValidation).validateUpdate(input);
        verify(clientMapper).applyUpdateInput(existingClient, input);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdatingNonExistentClient() {
        ClientUpdateInput input = new ClientUpdateInput(
                "Nome Atualizado", null, null, null, null, null, null
        );

        when(clientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> clientService.updateClient(99L, input));
        verify(clientRepository, never()).save(any(Client.class));
    }

    // ==================== deleteClient ====================

    @Test
    void shouldDeleteClientSuccessfully() {
        Client client = validClient();

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(reviewRepository.existsByClientId(1L)).thenReturn(false);

        Response response = clientService.deleteClient(1L);

        assertTrue(response.success());
        assertEquals("Cliente deletado com sucesso", response.message());
        verify(clientRepository).delete(client);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDeletingNonExistentClient() {
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> clientService.deleteClient(99L));
        verify(clientRepository, never()).delete(any(Client.class));
    }

    @Test
    void shouldThrowConflictExceptionWhenClientHasLinkedReviews() {
        Client client = validClient();

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(reviewRepository.existsByClientId(1L)).thenReturn(true);

        assertThrows(ConflictException.class, () -> clientService.deleteClient(1L));
        verify(clientRepository, never()).delete(any(Client.class));
    }
}