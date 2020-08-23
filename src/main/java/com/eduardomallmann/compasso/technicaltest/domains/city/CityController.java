package com.eduardomallmann.compasso.technicaltest.domains.city;

import com.eduardomallmann.compasso.technicaltest.utils.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

/**
 * Class responsible for the rest controllers of the {@link City} domain.
 *
 * @author eduardomallmann
 * @since 0.0.1
 */
@RestController
@RequestMapping("cities")
public class CityController {

    private final CityService cityService;

    /**
     * Main constructor with components injection.
     *
     * @param cityService {@link CityService} component
     */
    public CityController(final CityService cityService) {
        this.cityService = cityService;
    }

    /**
     * Creates a new {@link City} asynchronously with the name and state informed in the request body. If the city already exists it simply returns the same object.
     *
     * @param cityRequest {@link CityDTO} request object
     *
     * @return an asynchronous response with {@link CityDTO} object encapsulated in a {@link Response} object.
     *
     * @throws CityException in case of the application throws any kind of exception.
     */
    @PostMapping
    public DeferredResult<ResponseEntity<Response<CityDTO>>> createCity(@Valid @RequestBody CityDTO cityRequest) throws CityException {
        DeferredResult<ResponseEntity<Response<CityDTO>>> deferredResult = new DeferredResult<>();
        CompletableFuture<Response<CityDTO>> future = this.cityService.save(cityRequest);
        future.whenCompleteAsync((result, throwable) -> deferredResult.setResult(ResponseEntity.status(HttpStatus.CREATED).body(result)));
        return deferredResult;
    }

    /**
     * Search for all {@link City} objects that matches the similar name passed as params.
     *
     * @param cityName partial name of the city
     *
     * @return an asynchronous response with {@link CityDTO} objects encapsulated in a {@link Response} object.
     *
     * @throws CityException in case of the application throws any kind of exception.
     */
    @GetMapping(params = "name")
    public DeferredResult<ResponseEntity<Response<CityDTO>>> getCitiesByName(@RequestParam("name") final String cityName) throws CityException {
        return this.getSearchResult(this.cityService.findAllByNameLike(cityName));
    }

    /**
     * Search for all {@link City} objects that fully matches its state property passed as params.
     *
     * @param state full state name
     *
     * @return an asynchronous response with {@link CityDTO} objects encapsulated in a {@link Response} object.
     *
     * @throws CityException in case of the application throws any kind of exception.
     */
    @GetMapping(params = "state")
    public DeferredResult<ResponseEntity<Response<CityDTO>>> getCitiesByState(@RequestParam("state") final String state) throws CityException {
        return this.getSearchResult(this.cityService.findAllByState(state));
    }

    /**
     * Retrieves the search result and wraps it inside the asynchronous response.
     *
     * @param future asynchronous search result
     *
     * @return an asynchronous response with {@link CityDTO} objects encapsulated in a {@link Response} object.
     */
    private DeferredResult<ResponseEntity<Response<CityDTO>>> getSearchResult(final CompletableFuture<Response<CityDTO>> future) {
        DeferredResult<ResponseEntity<Response<CityDTO>>> deferredResult = new DeferredResult<>();
        future.whenCompleteAsync((result, throwable) -> deferredResult.setResult(ResponseEntity.ok(result)));
        return deferredResult;
    }
}
