package com.eduardomallmann.compasso.technicaltest.domains.client;

import com.eduardomallmann.compasso.technicaltest.domains.city.City;
import com.eduardomallmann.compasso.technicaltest.domains.city.CityDTO;
import com.eduardomallmann.compasso.technicaltest.domains.city.CityRepository;
import com.eduardomallmann.compasso.technicaltest.exceptions.BusinessException;
import com.eduardomallmann.compasso.technicaltest.utils.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Service responsible for the business logic of {@link Client} domain.
 *
 * @author eduardomallmann
 * @since 0.0.1
 */
@Service
public class ClientService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ClientRepository clientRepository;
    private final CityRepository cityRepository;

    /**
     * Main constructor with components injection.
     *
     * @param clientRepository {@link ClientRepository} component
     * @param cityRepository   {@link CityRepository} component
     */
    public ClientService(final ClientRepository clientRepository, final CityRepository cityRepository) {
        this.clientRepository = clientRepository;
        this.cityRepository = cityRepository;
    }

    /**
     * Save a new {@link Client} object. It verifies if the {@link City} already exists and uses the existent one, otherwise creates a new {@link City} object.
     * <p>It also normalizes the</p>
     *
     * @param clientRequest a {@link ClientDTO} object with the object creation request
     *
     * @return an asynchronous response with {@link ClientDTO} object encapsulated in a {@link Response} object.
     *
     * @throws BusinessException in case of the application throws any kind of exception.
     */
    @Async
    public CompletableFuture<Response<ClientDTO>> save(final ClientDTO clientRequest) throws BusinessException {
        try {
            if (clientRequest.getId() != null) {
                log.error("Client creation attempt failed due existent id in the request: {}", clientRequest.toJson());
                throw new BusinessException("client.save.new.error");
            }
            Client client = clientRequest.getClient();
            final boolean hasCity = client.getCity() != null;
            if (hasCity) {
                Optional<City> city = this.cityRepository.findByNameAndState(client.getCity().getName().toLowerCase(), client.getCity().getState().toLowerCase());
                if (city.isPresent()) {
                    client.setCity(city.get());
                } else {
                    log.debug("Client creation creating also a new city: {}", new CityDTO(client.getCity()).getNormalized().toJson());
                    this.cityRepository.save(client.getCity());
                }
            }
            this.clientRepository.save(client);
            ClientDTO result = new ClientDTO(client);
            log.debug("Client created: {}", result.toJson());
            return CompletableFuture.completedFuture(Response.of(result));
        } catch (Exception e) {
            if (e instanceof BusinessException) throw e;
            log.error("Error on creating a new client: {} ", clientRequest.toJson());
            log.error("Exception catched: {}", e.getMessage());
            throw new BusinessException("client.save.error", e.getMessage());
        }
    }

    /**
     * Retrieves all {@link Client} objects encapsulated into a {@link ClientDTO} object, that matches the similar name passed as parameter.
     *
     * @param clientName partial or full name of the client
     *
     * @return an asynchronous response with {@link ClientDTO} objects encapsulated in a {@link Response} object.
     *
     * @throws BusinessException in case of the application throws any kind of exception.
     */
    @Async
    public CompletableFuture<Response<ClientDTO>> findAllByFullNameLike(final String clientName) throws BusinessException {
        try {
            final String[] fullName = clientName.split("\\s");
            List<ClientDTO> clients = new ArrayList<>();
            for (String name : fullName) {
                final String clientNameLike = "%".concat(name.toLowerCase()).concat("%");
                clients.addAll(this.clientRepository.findAllByFullNameLike(clientNameLike).stream()
                                       .map(ClientDTO::new)
                                       .collect(Collectors.toList()));
            }
            List<ClientDTO> result = clients.stream().distinct().collect(Collectors.toList());
            log.debug("Total of clients found by name {}: {}", clientName, result.size());
            return CompletableFuture.completedFuture(Response.of(result));
        } catch (Exception e) {
            log.error("Error on searching client by name {}: {}", clientName, e.getMessage());
            throw new BusinessException("client.list.name.error", e.getMessage());
        }
    }

    /**
     * Retrieves a {@link Client} object encapsulated into a {@link ClientDTO} object, that matches the identifier.
     *
     * @param id {@link Client} database identifier
     *
     * @return an asynchronous response with {@link ClientDTO} object encapsulated in a {@link Response} object.
     *
     * @throws BusinessException in case of the application throws any kind of exception.
     */
    @Async
    public CompletableFuture<Response<ClientDTO>> findClientById(final Long id) throws BusinessException {
        try {
            Optional<Client> client = this.clientRepository.findById(id);
            if (client.isPresent()) {
                ClientDTO result = new ClientDTO(client.get());
                log.debug("Client found for id {}: {}", id, result.toJson());
                return CompletableFuture.completedFuture(Response.of(result));
            } else {
                log.error("Client not found for id: {}", id);
                throw new BusinessException("client.search.id.not-found");
            }
        } catch (Exception e) {
            if (e instanceof BusinessException) throw e;
            log.error("Error on searching client by id {}: {}", id, e.getMessage());
            throw new BusinessException("client.search.id.error", e.getMessage());
        }
    }

    /**
     * Updates the {@link Client} fullname property.
     *
     * @param id         id {@link Client} database identifier
     * @param clientName new {@link Client} fullname property
     *
     * @return an asynchronous response with {@link ClientDTO} object encapsulated in a {@link Response} object.
     *
     * @throws BusinessException in case of the application throws any kind of exception.
     */
    @Async
    @Transactional
    public CompletableFuture<Response<ClientDTO>> updateClientName(final Long id, final String clientName) throws BusinessException {
        try {
            this.clientRepository.updateClientFullName(id, clientName.toLowerCase());
            Optional<Client> client = this.clientRepository.findById(id);
            if (client.isPresent()) {
                ClientDTO result = new ClientDTO(client.get());
                log.debug("Client name updated for id {}: {}", id, result.toJson());
                return CompletableFuture.completedFuture(Response.of(result));
            } else {
                log.error("Client error on update name, id {} not found", id);
                throw new BusinessException("client.update.response.error");
            }
        } catch (Exception e) {
            if (e instanceof BusinessException) throw e;
            log.error("Error on updating client name with id {}: {}", id, e.getMessage());
            throw new BusinessException("client.update.error", e.getMessage());
        }
    }

    /**
     * Deletes a {@link Client} by its database identification.
     *
     * @param id {@link Client} database identification
     *
     * @throws BusinessException in case of the application throws any kind of exception.
     */
    public void removeClient(final Long id) throws BusinessException {
        try {
            this.clientRepository.deleteById(id);
            log.debug("Client with id {} removed", id);
        } catch (Exception e) {
            log.error("Error on removing client with id {}: {}", id, e.getMessage());
            throw new BusinessException("client.delete.error", e.getMessage());
        }
    }

}
