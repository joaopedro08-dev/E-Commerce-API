package com.crud.ecommerce.business.service;

import com.crud.ecommerce.business.mapper.ClientMapper;
import com.crud.ecommerce.business.util.EntityFinderUtils;
import com.crud.ecommerce.business.util.EntityOperationUtils;
import com.crud.ecommerce.business.validation.ClientValidation;
import com.crud.ecommerce.dto.response.Response;
import com.crud.ecommerce.dto.response.client.ClientResponse;
import com.crud.ecommerce.dto.resquest.client.ClientCreateInput;
import com.crud.ecommerce.dto.resquest.client.ClientUpdateInput;
import com.crud.ecommerce.infrastructure.entity.client.Client;
import com.crud.ecommerce.infrastructure.repository.client.ClientRepository;
import com.crud.ecommerce.infrastructure.repository.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final ClientValidation clientValidation;
    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public List<ClientResponse> getAllClients(String sortBy) {
        return clientMapper.toClientResponseList(
                clientRepository.findAll(Sort.by(sortBy).ascending()));
    }

    @Transactional(readOnly = true)
    public ClientResponse getClientById(Long id) {
        return clientMapper.toClientResponse(findClientById(id));
    }

    @Transactional
    public Response createClient(ClientCreateInput input) {
        clientValidation.validateCreate(input);

        Client client = new Client();

        clientMapper.applyCreateInput(client, input);
        return EntityOperationUtils.create(clientRepository, client, "Cliente criado com sucesso");
    }

    @Transactional
    public Response updateClient(Long id, ClientUpdateInput input) {
        Client client = findClientById(id);

        clientValidation.validateUpdate(input);
        clientMapper.applyUpdateInput(client, input);
        return EntityOperationUtils.update(clientRepository, client, "Cliente atualizado com sucesso");
    }

    @Transactional
    public Response deleteClient(Long id) {
        Client client = findClientById(id);

        EntityOperationUtils.validateNoLinks(reviewRepository.existsByClientId(id),
                "Cliente possui avaliações vinculadas e não pode ser deletado.");
        return EntityOperationUtils.delete(clientRepository, client, "Cliente deletado com sucesso");
    }

    private Client findClientById(Long id) {
        return EntityFinderUtils.findById(clientRepository, id, "Cliente não encontrado!");
    }
}