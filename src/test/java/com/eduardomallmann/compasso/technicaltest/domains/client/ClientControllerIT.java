package com.eduardomallmann.compasso.technicaltest.domains.client;

import com.eduardomallmann.compasso.technicaltest.domains.client.dto.ClientResponse;
import com.eduardomallmann.compasso.technicaltest.domains.client.dto.ClientNameRequest;
import com.eduardomallmann.compasso.technicaltest.domains.client.dto.ClientRequest;
import com.eduardomallmann.compasso.technicaltest.utils.MessageUtils;
import com.eduardomallmann.compasso.technicaltest.utils.Response;
import com.eduardomallmann.compasso.technicaltest.utils.ResponseContent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ClientControllerIT {

    private static final String CLIENT_ENDPOINT = "/clients";

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @AfterEach
    void tearDown() {
        this.clientRepository.deleteAll();
    }

    @Test
    void createClient_ShouldReturnClientCreated() {
        //given
        ClientRequest clientRequest = new ClientRequest("Eduardo Mallmann", "Male", LocalDate.of(1982, 5, 22), "Florianópolis", "Santa Catarina");
        //when
        ResponseEntity<Response<ClientResponse>> result = restTemplate.exchange(
                RequestEntity.post(URI.create(CLIENT_ENDPOINT))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(clientRequest),
                new ParameterizedTypeReference<Response<ClientResponse>>() {
                });
        //then
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(38, Objects.requireNonNull(result.getBody()).getContent().get(0).getAge());
        assertTrue(Objects.requireNonNull(result.getBody()).getContent().stream()
                           .allMatch(resp -> resp.getId() != null
                                                     && clientRequest.getCity().equalsIgnoreCase(resp.getCity())
                                                     && clientRequest.getState().equalsIgnoreCase(resp.getState())
                                                     && clientRequest.getName().equalsIgnoreCase(resp.getName())
                                                     && clientRequest.getGender().equalsIgnoreCase(resp.getGender())
                                                     && clientRequest.getBirthday().equals(resp.getBirthday())));
    }

    @Test
    void createClient_WithMandatoryOnly_ShouldReturnClientCreated() {
        //given
        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setName("Eduardo Mallmann");
        clientRequest.setBirthday(LocalDate.of(1982, 5, 22));
        //when
        ResponseEntity<Response<ClientResponse>> result = restTemplate.exchange(
                RequestEntity.post(URI.create(CLIENT_ENDPOINT))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(clientRequest),
                new ParameterizedTypeReference<Response<ClientResponse>>() {
                });
        //then
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(38, Objects.requireNonNull(result.getBody()).getContent().get(0).getAge());
        assertTrue(Objects.requireNonNull(result.getBody()).getContent().stream()
                           .allMatch(resp -> resp.getId() != null
                                                     && clientRequest.getName().equalsIgnoreCase(resp.getName())
                                                     && clientRequest.getBirthday().equals(resp.getBirthday())));
    }

    @Test
    void createClient_ShouldReturnFullnameValidationError() {
        //given
        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setName("Eduardo");
        clientRequest.setBirthday(LocalDate.of(1982, 5, 22));
        String errorMessage = MessageUtils.getMessage("client.validation.full-name.error");
        //when
        ResponseEntity<Response<ResponseContent>> result = restTemplate.exchange(
                RequestEntity.post(URI.create(CLIENT_ENDPOINT))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(clientRequest),
                new ParameterizedTypeReference<Response<ResponseContent>>() {
                });
        //then
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertTrue(Objects.requireNonNull(Objects.requireNonNull(result.getBody()).getContent()).stream()
                           .allMatch(resp -> errorMessage.equals(resp.getErrorMessage().getMessage())));
    }

    @Test
    void createClient_ShouldReturnPastValidationError() {
        //given
        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setName("Eduardo Mallmann");
        clientRequest.setBirthday(LocalDate.of(2082, 5, 22));
        String errorMessage = MessageUtils.getMessage("client.validation.birthday.error");
        //when
        ResponseEntity<Response<ResponseContent>> result = restTemplate.exchange(
                RequestEntity.post(URI.create(CLIENT_ENDPOINT))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(clientRequest),
                new ParameterizedTypeReference<Response<ResponseContent>>() {
                });
        //then
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertTrue(Objects.requireNonNull(Objects.requireNonNull(result.getBody()).getContent()).stream()
                           .allMatch(resp -> errorMessage.equals(resp.getErrorMessage().getMessage())));
    }

    @Test
    void createClient_ShouldReturnCityValidationError() {
        //given
        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setName("Eduardo Mallmann");
        clientRequest.setBirthday(LocalDate.of(1982, 5, 22));
        clientRequest.setCity("Florianópolis");
        String errorMessage = MessageUtils.getMessage("client.city.validation.error");
        //when
        ResponseEntity<Response<ResponseContent>> result = restTemplate.exchange(
                RequestEntity.post(URI.create(CLIENT_ENDPOINT))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(clientRequest),
                new ParameterizedTypeReference<Response<ResponseContent>>() {
                });
        //then
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertTrue(Objects.requireNonNull(Objects.requireNonNull(result.getBody()).getContent()).stream()
                           .allMatch(resp -> errorMessage.equals(resp.getErrorMessage().getMessage())));
    }

    @Test
    void deleteClientById_ShouldRemoveObject() {
        //given
        List<Client> clients = this.populateClients();
        Long id = clients.get(0).getId();
        //when
        ResponseEntity<Response<ResponseContent>> result = restTemplate.exchange(
                RequestEntity.delete(URI.create(CLIENT_ENDPOINT.concat("/").concat(String.valueOf(id))))
                        .accept(MediaType.APPLICATION_JSON)
                        .build(),
                new ParameterizedTypeReference<Response<ResponseContent>>() {
                });
        //then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(this.clientRepository.findAll().stream().noneMatch(client -> id.equals(client.getId())));
    }

    @Test
    void deleteClientById_ShouldReturnBusinessError() {
        //given
        Long id = 1L;
        String errorMessage = MessageUtils.getMessage("client.delete.error");
        //when
        ResponseEntity<Response<ResponseContent>> result = restTemplate.exchange(
                RequestEntity.delete(URI.create(CLIENT_ENDPOINT.concat("/").concat(String.valueOf(id))))
                        .accept(MediaType.APPLICATION_JSON)
                        .build(),
                new ParameterizedTypeReference<Response<ResponseContent>>() {
                });
        //then
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertTrue(Objects.requireNonNull(Objects.requireNonNull(result.getBody()).getContent()).stream()
                           .allMatch(resp -> errorMessage.equals(resp.getErrorMessage().getMessage())));
    }

    @Test
    void updateClientName_ShouldReturnClientWithNewName() {
        //given
        final ClientNameRequest clientNameRequest = new ClientNameRequest("Jing Dong");
        List<Client> clients = this.populateClients();
        ClientResponse client = new ClientResponse(clients.get(0));
        //when
        ResponseEntity<Response<ClientResponse>> result = restTemplate.exchange(
                RequestEntity.patch(URI.create(CLIENT_ENDPOINT.concat("/").concat(String.valueOf(client.getId()))))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(clientNameRequest),
                new ParameterizedTypeReference<Response<ClientResponse>>() {
                });
        //Then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(Objects.requireNonNull(result.getBody()).getContent().stream()
                           .allMatch(resp -> resp.getId() != null
                                                     && clientNameRequest.getName().equalsIgnoreCase(resp.getName())));
    }

    @Test
    void updateClientName_ShouldReturnBusinessError() {
        //given
        final ClientNameRequest clientNameRequest = new ClientNameRequest("Jing Dong");
        Long id = 1L;
        String errorMessage = MessageUtils.getMessage("client.update.response.error");
        //when
        ResponseEntity<Response<ResponseContent>> result = restTemplate.exchange(
                RequestEntity.patch(URI.create(CLIENT_ENDPOINT.concat("/").concat(String.valueOf(id))))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(clientNameRequest),
                new ParameterizedTypeReference<Response<ResponseContent>>() {
                });
        //Then
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertTrue(Objects.requireNonNull(Objects.requireNonNull(result.getBody()).getContent()).stream()
                           .allMatch(resp -> errorMessage.equals(resp.getErrorMessage().getMessage())));
    }

    @Test
    void getClientsByName() throws UnsupportedEncodingException {
        //given
        final String name = "Doe";
        List<Client> clients = this.populateClients().stream().filter(client -> client.getFullName().contains(name.toLowerCase())).collect(Collectors.toList());
        //when
        ResponseEntity<Response<ClientResponse>> result = restTemplate.exchange(
                RequestEntity.get(URI.create(CLIENT_ENDPOINT.concat("?name=").concat(URLEncoder.encode(name, "UTF-8"))))
                        .accept(MediaType.APPLICATION_JSON)
                        .build(),
                new ParameterizedTypeReference<Response<ClientResponse>>() {
                });
        //then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(clients.size(), Objects.requireNonNull(result.getBody()).getNumberOfElements());
        assertTrue(Objects.requireNonNull(result.getBody()).getContent().stream()
                           .allMatch(client -> client.getName().toLowerCase().contains(name.toLowerCase())));
    }

    @Test
    void getClientById_ShouldReturnTheClientWithTheSameId() {
        //given
        List<Client> clients = this.populateClients();
        ClientResponse clientResponse = new ClientResponse(clients.get(0));
        //when
        ResponseEntity<Response<ClientResponse>> result = restTemplate.exchange(
                RequestEntity.get(URI.create(CLIENT_ENDPOINT.concat("/").concat(String.valueOf(clientResponse.getId()))))
                        .accept(MediaType.APPLICATION_JSON)
                        .build(),
                new ParameterizedTypeReference<Response<ClientResponse>>() {
                });
        //then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(Objects.requireNonNull(result.getBody()).getContent().stream()
                           .allMatch(client -> client.equals(clientResponse)));
    }

    @Test
    void getClientById_ShouldReturnBusinessError() {
        //given
        Long id = 1L;
        String errorMessage = MessageUtils.getMessage("client.search.id.not-found");
        //when
        ResponseEntity<Response<ResponseContent>> result = restTemplate.exchange(
                RequestEntity.get(URI.create(CLIENT_ENDPOINT.concat("/").concat(String.valueOf(id))))
                        .accept(MediaType.APPLICATION_JSON)
                        .build(),
                new ParameterizedTypeReference<Response<ResponseContent>>() {
                });
        //then
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertTrue(Objects.requireNonNull(Objects.requireNonNull(result.getBody()).getContent()).stream()
                           .allMatch(resp -> errorMessage.equals(resp.getErrorMessage().getMessage())));
    }

    private List<Client> populateClients() {
        ClientRequest john = new ClientRequest("John Doe", "male", LocalDate.of(1920, 10, 11), null, null);
        ClientRequest jane = new ClientRequest("Jane Doe", "female", LocalDate.of(1925, 5, 6), null, null);
        ClientRequest mary = new ClientRequest("Mary Jane", "female", LocalDate.of(1973, 9, 23), null, null);
        ClientRequest coke = new ClientRequest("Coke Cola", "male", LocalDate.of(1840, 7, 15), null, null);
        List<Client> clients = Stream.of(john, jane, mary, coke).map(ClientRequest::getClient).collect(Collectors.toList());
        this.clientRepository.saveAll(clients);
        return this.clientRepository.findAll();
    }
}