package com.crud.ecommerce.business.mapper;

import com.crud.ecommerce.dto.response.client.ClientProfileResponse;
import com.crud.ecommerce.dto.response.client.ClientResponse;
import com.crud.ecommerce.dto.resquest.client.ClientCreateInput;
import com.crud.ecommerce.dto.resquest.client.ClientUpdateInput;
import com.crud.ecommerce.dto.resquest.interfaces.client.AddressField;
import com.crud.ecommerce.infrastructure.entity.client.Address;
import com.crud.ecommerce.infrastructure.entity.client.Client;
import com.crud.ecommerce.infrastructure.entity.client.ClientProfile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClientMapper {

    public List<ClientResponse> toClientResponseList(List<Client> clients) {
        return clients.stream().map(this::toClientResponse).toList();
    }

    public ClientResponse toClientResponse(Client client) {
        ClientProfileResponse profile = client.getProfile() != null ?
                new ClientProfileResponse(client.getProfile().getCpf(),
                        client.getProfile().getRg(), client.getProfile().getAddress()) : null;

        return new ClientResponse(client.getId(), client.getFullName(), client.getEmail(),
                client.getPhone(), profile, client.getCreatedAt(), client.getUpdatedAt());
    }

    public void applyCreateInput(Client model, ClientCreateInput input) {
        model.setFullName(input.name());
        model.setEmail(input.email());
        model.setPhone(input.phone());
        model.setStatusClient(input.statusClient());

        applyCreateProfile(input, model);
    }

    private void applyCreateProfile(ClientCreateInput input, Client model) {
        ClientProfile profile = new ClientProfile();

        profile.setCpf(input.cpf());
        profile.setRg(input.rg());
        profile.setAddress(buildAddress(input.address()));
        profile.setClient(model);
        model.setProfile(profile);
    }

    public void applyUpdateInput(Client client, ClientUpdateInput input) {
        if (input.name() != null) client.setFullName(input.name());
        if (input.email() != null) client.setEmail(input.email());
        if (input.phone() != null) client.setPhone(input.phone());
        if (input.statusClient() != null) client.setStatusClient(input.statusClient());

        applyUpdateProfile(client, input);
    }

    private void applyUpdateProfile(Client client, ClientUpdateInput input) {
        if (input.cpf() != null) client.getProfile().setCpf(input.cpf());
        if (input.rg() != null) client.getProfile().setRg(input.rg());
        if (input.address() != null) client.getProfile().setAddress(buildAddress(input.address()));
    }

    private Address buildAddress(AddressField input) {
        return new Address(input.street(), input.number(), input.complement(),
                input.neighborhood(), input.city(), input.state(), input.zipCode());
    }
}