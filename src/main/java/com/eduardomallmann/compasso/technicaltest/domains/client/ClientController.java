package com.eduardomallmann.compasso.technicaltest.domains.client;

import com.eduardomallmann.compasso.technicaltest.domains.client.dto.ClientResponse;
import com.eduardomallmann.compasso.technicaltest.domains.client.dto.ClientNameRequest;
import com.eduardomallmann.compasso.technicaltest.domains.client.dto.ClientRequest;
import com.eduardomallmann.compasso.technicaltest.exceptions.BusinessException;
import com.eduardomallmann.compasso.technicaltest.utils.GenericRestController;
import com.eduardomallmann.compasso.technicaltest.utils.Response;
import com.eduardomallmann.compasso.technicaltest.utils.ResponseContent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
@Validated
@RestController
@RequestMapping("clients")
@Tag(name = "Clients Endpoints",
        description = "Describes the access and calls to clients api endpoints. These endpoints are responsible for all interaction between this domain and other systems.")
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
     * Creates a new {@link Client} asynchronously with the {@link ClientRequest} request body.
     *
     * @param clientRequest {@link ClientRequest} request object
     *
     * @return an asynchronous response with {@link ClientResponse} object encapsulated in a {@link Response} object.
     *
     * @throws BusinessException in case of the application throws any kind of exception.
     */
    @Operation(summary = "Create new Client", description = "Creates a new Client asynchronously with the properties informed in the request body.",
            tags = {"Clients Endpoints"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Client created"),
            @ApiResponse(responseCode = "400", description = "Client creation failed")})
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<ResponseEntity<Response<ClientResponse>>> createClient(@Valid @RequestBody final ClientRequest clientRequest) throws BusinessException {
        log.info("Create client request call with: {}", clientRequest.toJson());
        DeferredResult<ResponseEntity<Response<ClientResponse>>> deferredResult = new DeferredResult<>();
        CompletableFuture<Response<ClientResponse>> future = this.clientService.save(clientRequest);
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
    @Operation(summary = "Delete a Client", description = "Removes a client from the application by its database identifier.",
            tags = {"Clients Endpoints"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client removed"),
            @ApiResponse(responseCode = "400", description = "Client removal failed")})
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
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
     * @param clientName {@link ClientNameRequest} request body with the value to be exchanged as a full name of the register
     *
     * @return an asynchronous response with {@link ClientResponse} object encapsulated in a {@link Response} object.
     *
     * @throws BusinessException in case of the application throws any kind of exception.
     */
    @Operation(summary = "Updates a Client name", description = "Updates the client name with a new one, informed in the request",
            tags = {"Clients Endpoints"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client name changed"),
            @ApiResponse(responseCode = "400", description = "Client name change failed")})
    @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<ResponseEntity<Response<ClientResponse>>> updateClientName(@PathVariable("id") final Long id,
                                                                                     @RequestBody final ClientNameRequest clientName) throws BusinessException {
        log.info("Update client name request call for id {} with: {}", id, clientName.toJson());
        return this.getSearchResult(this.clientService.updateClientName(id, clientName.getName()));
    }

    /**
     * Search for all {@link Client} objects that contains in its name with the similar name passed as params.
     *
     * @param clientName partial name of the city
     *
     * @return an asynchronous response with {@link ClientResponse} objects encapsulated in a {@link Response} object.
     *
     * @throws BusinessException in case of the application throws any kind of exception.
     */
    @Operation(summary = "Get the Clients by the Name", description = "Search for all clients that contains in its name the similar name passed as params.",
            tags = {"Clients Endpoints"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clients found"),
            @ApiResponse(responseCode = "400", description = "Clients search failed")})
    @GetMapping(params = "name", produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<ResponseEntity<Response<ClientResponse>>> getClientsByName(@RequestParam("name") final String clientName) throws BusinessException {
        log.info("Get clients by name request call with: {}", clientName);
        return this.getSearchResult(this.clientService.findAllByFullNameLike(clientName));
    }

    /**
     * Search for a {@link Client} object by its database identifier.
     *
     * @param id {@link Client} database identifier
     *
     * @return an asynchronous response with {@link ClientResponse} object encapsulated in a {@link Response} object.
     *
     * @throws BusinessException in case of the application throws any kind of exception.
     */
    @Operation(summary = "Get a Client", description = "Search for a Client by its database identifier.",
            tags = {"Clients Endpoints"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clients found"),
            @ApiResponse(responseCode = "400", description = "Clients search failed")})
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<ResponseEntity<Response<ClientResponse>>> getClientById(@PathVariable("id") final Long id) throws BusinessException {
        log.info("Get client by id request call with id: {}", id);
        return this.getSearchResult(this.clientService.findClientById(id));
    }
}
