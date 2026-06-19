package com.crud.ecommerce.controller;

import com.crud.ecommerce.business.service.ClientService;
import com.crud.ecommerce.dto.response.Response;
import com.crud.ecommerce.dto.response.client.ClientResponse;
import com.crud.ecommerce.dto.resquest.client.ClientCreateInput;
import com.crud.ecommerce.dto.resquest.client.ClientUpdateInput;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/list")
    public List<ClientResponse> getClientsAll(@RequestParam(defaultValue = "fullName") String sortBy) {
        return clientService.getAllClients(sortBy);
    }

    @GetMapping("/{id}")
    public ClientResponse getClientById(@PathVariable Long id) {
        return clientService.getClientById(id);
    }

    @PostMapping
    public Response createClient(@RequestBody ClientCreateInput input) {
        return clientService.createClient(input);
    }

    @PutMapping("/{id}")
    public Response updateClient(@PathVariable Long id, @RequestBody ClientUpdateInput input) {
        return clientService.updateClient(id, input);
    }

    @DeleteMapping("/{id}")
    public Response deleteClient(@PathVariable Long id) {
        return clientService.deleteClient(id);
    }
}