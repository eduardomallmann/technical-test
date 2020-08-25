package com.eduardomallmann.compasso.technicaltest.domains.client;

import com.eduardomallmann.compasso.technicaltest.exceptions.BusinessException;
import com.eduardomallmann.compasso.technicaltest.utils.GenericRestController;
import com.eduardomallmann.compasso.technicaltest.utils.Response;
import com.eduardomallmann.compasso.technicaltest.utils.ResponseContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

/**
 * Class responsible for the rest controllers of the {@link Client} domain.
 *
 * @author eduardomallmann
 * @since 0.0.1
 */
@RestController
@RequestMapping("clients")
public class ClientController implements GenericRestController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ClientService clientService;

    /**
     * Main constructor with components injection.
     *
     * @param clientService {@link ClientService} component
     */
    public ClientController(final ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * Creates a new {@link Client} asynchronously with the {@link ClientDTO} request body.
     *
     * @param clientRequest {@link ClientDTO} request object
     *
     * @return an asynchronous response with {@link ClientDTO} object encapsulated in a {@link Response} object.
     *
     * @throws BusinessException in case of the application throws any kind of exception.
     */
    @PostMapping
    public DeferredResult<ResponseEntity<Response<ClientDTO>>> createClient(@Valid @RequestBody final ClientDTO clientRequest) throws BusinessException {
        log.info("Create client request call with: {}", clientRequest.toJson());
        DeferredResult<ResponseEntity<Response<ClientDTO>>> deferredResult = new DeferredResult<>();
        CompletableFuture<Response<ClientDTO>> future = this.clientService.save(clientRequest);
        future.whenCompleteAsync((result, throwable) -> {
            if (throwable != null) {
                this.addErrorMessage(deferredResult, throwable);
            } else {
                deferredResult.setResult(ResponseEntity.status(HttpStatus.CREATED).body(result));
            }
        });
        return deferredResult;
    }

    /**
     * Removes a {@link Client} object from the application by its database identifier.
     *
     * @param id {@link Client} database identifier
     *
     * @return a response with {@link ResponseContent} object encapsulated in a {@link Response} object.
     *
     * @throws BusinessException in case of the application throws any kind of exception.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Response<ResponseContent>> deleteClientById(@PathVariable("id") final Long id) throws BusinessException {
        log.info("Delete client request call with id: {}", id);
        this.clientService.removeClient(id);
        return ResponseEntity.ok(Response.of(ResponseContent.builder()
                                                     .status(HttpStatus.OK.name())
                                                     .build()));
    }

    /**
     * Changes the {@link Client} full name for the informed register.
     *
     * @param id         {@link Client} database identifier
     * @param clientName {@link ClientNameDTO} request body with the value to be exchanged as a full name of the register
     *
     * @return an asynchronous response with {@link ClientDTO} object encapsulated in a {@link Response} object.
     *
     * @throws BusinessException in case of the application throws any kind of exception.
     */
    @PatchMapping("/{id}")
    public DeferredResult<ResponseEntity<Response<ClientDTO>>> updateClientName(@PathVariable("id") final Long id,
                                                                                @RequestBody final ClientNameDTO clientName) throws BusinessException {
        log.info("Update client name request call for id {} with: {}", id, clientName.toJson());
        return this.getSearchResult(this.clientService.updateClientName(id, clientName.getName()));
    }

    /**
     * Search for all {@link Client} objects that matches its full name with the similar name passed as params.
     *
     * @param clientName partial name of the city
     *
     * @return an asynchronous response with {@link ClientDTO} objects encapsulated in a {@link Response} object.
     *
     * @throws BusinessException in case of the application throws any kind of exception.
     */
    @GetMapping(params = "name")
    public DeferredResult<ResponseEntity<Response<ClientDTO>>> getClientsByName(@RequestParam("name") final String clientName) throws BusinessException {
        log.info("Get clients by name request call with: {}", clientName);
        return this.getSearchResult(this.clientService.findAllByFullNameLike(clientName));
    }

    /**
     * Search for a {@link Client} object by its database identifier.
     *
     * @param id {@link Client} database identifier
     *
     * @return an asynchronous response with {@link ClientDTO} object encapsulated in a {@link Response} object.
     *
     * @throws BusinessException in case of the application throws any kind of exception.
     */
    @GetMapping("/{id}")
    public DeferredResult<ResponseEntity<Response<ClientDTO>>> getClientById(@PathVariable("id") final Long id) throws BusinessException {
        log.info("Get client by id request call with id: {}", id);
        return this.getSearchResult(this.clientService.findClientById(id));
    }
}
