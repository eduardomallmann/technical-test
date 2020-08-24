package com.eduardomallmann.compasso.technicaltest.domains.client;

import com.eduardomallmann.compasso.technicaltest.domains.city.City;
import com.eduardomallmann.compasso.technicaltest.domains.city.CityRepository;
import com.eduardomallmann.compasso.technicaltest.exceptions.BusinessException;
import com.eduardomallmann.compasso.technicaltest.utils.Response;
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
                throw new BusinessException("client.save.new.error");
            }
            Client client = clientRequest.getClient();
            final boolean hasCity = client.getCity() != null;
            if (hasCity) {
                Optional<City> city = this.cityRepository.findByNameAndState(client.getCity().getName().toLowerCase(), client.getCity().getState().toLowerCase());
                if (city.isPresent()) {
                    client.setCity(city.get());
                } else {
                    this.cityRepository.save(client.getCity());
                }
            }
            this.clientRepository.save(client);
            return CompletableFuture.completedFuture(Response.of(new ClientDTO(client)));
        } catch (Exception e) {
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
            return CompletableFuture.completedFuture(Response.of(clients.stream().distinct().collect(Collectors.toList())));
        } catch (Exception e) {
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
                return CompletableFuture.completedFuture(Response.of(new ClientDTO(client.get())));
            } else {
                throw new BusinessException("client.search.id.not-found");
            }
        } catch (Exception e) {
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
                return CompletableFuture.completedFuture(Response.of(new ClientDTO(client.get())));
            } else {
                throw new BusinessException("client.update.response.error");
            }
        } catch (Exception e) {
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
        } catch (Exception e) {
            throw new BusinessException("client.delete.error", e.getMessage());
        }
    }

}
