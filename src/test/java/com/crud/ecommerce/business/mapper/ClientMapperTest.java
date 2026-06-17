package com.crud.ecommerce.business.mapper;

import com.crud.ecommerce.dto.response.client.ClientResponse;
import com.crud.ecommerce.dto.resquest.client.AddressCreateInput;
import com.crud.ecommerce.dto.resquest.client.AddressUpdateInput;
import com.crud.ecommerce.dto.resquest.client.ClientCreateInput;
import com.crud.ecommerce.dto.resquest.client.ClientUpdateInput;
import com.crud.ecommerce.infrastructure.entity.client.Client;
import com.crud.ecommerce.infrastructure.entity.client.ClientProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClientMapperTest {

    private ClientMapper clientMapper;

    @BeforeEach
    void setUp() {
        clientMapper = new ClientMapper();
    }

    private AddressCreateInput validAddressCreateInput() {
        return new AddressCreateInput(
                "Rua das Flores",
                "123",
                "Apto 45",
                "Centro",
                "São Paulo",
                "SP",
                "01310-100"
        );
    }

    private AddressUpdateInput validAddressUpdateInput() {
        return new AddressUpdateInput(
                "Rua Nova",
                "50",
                null,
                "Vila Madalena",
                "São Paulo",
                "SP",
                "05435-000"
        );
    }

    private ClientCreateInput validClientCreateInput() {
        return new ClientCreateInput(
                "João Pedro Silva",
                "joao.pedro@email.com",
                "(11) 99999-1234",
                true,
                "123.456.789-09",
                "12.345.678-9",
                validAddressCreateInput()
        );
    }

    // ==================== toClientResponse ====================

    @Test
    void shouldMapClientToClientResponseWhenProfileIsNull() {
        Client client = new Client();
        client.setId(1L);
        client.setFullName("João Pedro Silva");
        client.setEmail("joao.pedro@email.com");
        client.setPhone("(11) 99999-1234");
        client.setProfile(null);

        ClientResponse response = clientMapper.toClientResponse(client);

        assertEquals(client.getId(), response.id());
        assertEquals(client.getFullName(), response.name());
        assertEquals(client.getEmail(), response.email());
        assertEquals(client.getPhone(), response.phone());
        assertNull(response.profile());
    }

    @Test
    void shouldMapClientToClientResponseWhenProfileIsPresent() {
        Client client = new Client();
        client.setId(1L);
        client.setFullName("João Pedro Silva");
        client.setEmail("joao.pedro@email.com");
        client.setPhone("(11) 99999-1234");

        ClientProfile profile = new ClientProfile();
        profile.setCpf("123.456.789-09");
        profile.setRg("12.345.678-9");
        client.setProfile(profile);

        ClientResponse response = clientMapper.toClientResponse(client);

        assertNotNull(response.profile());
        assertEquals("123.456.789-09", response.profile().cpf());
        assertEquals("12.345.678-9", response.profile().rg());
    }

    // ==================== toClientResponseList ====================

    @Test
    void shouldMapClientListToClientResponseList() {
        Client client1 = new Client();
        client1.setId(1L);
        client1.setFullName("Cliente Um");
        client1.setEmail("um@email.com");
        client1.setPhone("(11) 91111-1111");

        Client client2 = new Client();
        client2.setId(2L);
        client2.setFullName("Cliente Dois");
        client2.setEmail("dois@email.com");
        client2.setPhone("(11) 92222-2222");

        List<ClientResponse> responses = clientMapper.toClientResponseList(List.of(client1, client2));

        assertEquals(2, responses.size());
        assertEquals("Cliente Um", responses.get(0).name());
        assertEquals("Cliente Dois", responses.get(1).name());
    }

    @Test
    void shouldReturnEmptyListWhenClientListIsEmpty() {
        List<ClientResponse> responses = clientMapper.toClientResponseList(List.of());

        assertTrue(responses.isEmpty());
    }

    // ==================== applyCreateInput ====================

    @Test
    void shouldApplyCreateInputToClient() {
        Client client = new Client();
        ClientCreateInput input = validClientCreateInput();

        clientMapper.applyCreateInput(client, input);

        assertEquals("João Pedro Silva", client.getFullName());
        assertEquals("joao.pedro@email.com", client.getEmail());
        assertEquals("(11) 99999-1234", client.getPhone());
        assertTrue(client.getStatusClient());
    }

    @Test
    void shouldCreateProfileWhenApplyingCreateInput() {
        Client client = new Client();
        ClientCreateInput input = validClientCreateInput();

        clientMapper.applyCreateInput(client, input);

        assertNotNull(client.getProfile());
        assertEquals("123.456.789-09", client.getProfile().getCpf());
        assertEquals("12.345.678-9", client.getProfile().getRg());
    }

    @Test
    void shouldBindProfileToClientWhenApplyingCreateInput() {
        Client client = new Client();
        ClientCreateInput input = validClientCreateInput();

        clientMapper.applyCreateInput(client, input);

        assertSame(client, client.getProfile().getClient());
    }

    @Test
    void shouldApplyAddressToProfileWhenApplyingCreateInput() {
        Client client = new Client();
        ClientCreateInput input = validClientCreateInput();

        clientMapper.applyCreateInput(client, input);

        assertNotNull(client.getProfile().getAddress());
        assertEquals("Rua das Flores", client.getProfile().getAddress().getStreet());
        assertEquals("01310-100", client.getProfile().getAddress().getZipCode());
    }

    // ==================== applyUpdateInput ====================

    @Test
    void shouldApplyUpdateInputOnlyToProvidedFields() {
        Client client = new Client();
        client.setFullName("Nome Antigo");
        client.setEmail("antigo@email.com");
        client.setPhone("(11) 90000-0000");
        client.setProfile(new ClientProfile());
        client.getProfile().setCpf("111.111.111-11");
        client.getProfile().setRg("11.111.111-1");

        ClientUpdateInput input = new ClientUpdateInput(
                "Nome Novo", null, null, null, null, null, null
        );

        clientMapper.applyUpdateInput(client, input);

        assertEquals("Nome Novo", client.getFullName());
        assertEquals("antigo@email.com", client.getEmail());
        assertEquals("(11) 90000-0000", client.getPhone());
        assertEquals("111.111.111-11", client.getProfile().getCpf());
    }

    @Test
    void shouldUpdateProfileCpfAndRgWhenProvided() {
        Client client = new Client();
        client.setProfile(new ClientProfile());
        client.getProfile().setCpf("111.111.111-11");
        client.getProfile().setRg("11.111.111-1");

        ClientUpdateInput input = new ClientUpdateInput(
                null, null, null, null, "222.222.222-22", "22.222.222-2", null
        );

        clientMapper.applyUpdateInput(client, input);

        assertEquals("222.222.222-22", client.getProfile().getCpf());
        assertEquals("22.222.222-2", client.getProfile().getRg());
    }

    @Test
    void shouldUpdateAddressWhenProvided() {
        Client client = new Client();
        client.setProfile(new ClientProfile());

        ClientUpdateInput input = new ClientUpdateInput(
                null, null, null, null, null, null, validAddressUpdateInput()
        );

        clientMapper.applyUpdateInput(client, input);

        assertNotNull(client.getProfile().getAddress());
        assertEquals("Rua Nova", client.getProfile().getAddress().getStreet());
        assertEquals("05435-000", client.getProfile().getAddress().getZipCode());
    }

    @Test
    void shouldNotChangeClientWhenAllUpdateFieldsAreNull() {
        Client client = new Client();
        client.setFullName("Nome Original");
        client.setEmail("original@email.com");
        client.setPhone("(11) 99999-9999");
        client.setProfile(new ClientProfile());

        ClientUpdateInput input = new ClientUpdateInput(
                null, null, null, null, null, null, null
        );

        clientMapper.applyUpdateInput(client, input);

        assertEquals("Nome Original", client.getFullName());
        assertEquals("original@email.com", client.getEmail());
        assertEquals("(11) 99999-9999", client.getPhone());
    }
}