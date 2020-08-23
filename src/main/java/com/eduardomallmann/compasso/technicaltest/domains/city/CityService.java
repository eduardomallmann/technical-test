package com.eduardomallmann.compasso.technicaltest.domains.city;

import com.eduardomallmann.compasso.technicaltest.utils.Response;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Service responsible for the business logic of {@link City} domain.
 *
 * @author eduardomallmann
 * @since 0.0.1
 */
@Service
public class CityService {

    private final CityRepository cityRepository;

    /**
     * Main constructor with components injection.
     *
     * @param cityRepository {@link CityRepository} component
     */
    public CityService(final CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    /**
     * Save a new {@link City} object, if the object already exists it just returns the existent one.
     *
     * @param cityRequest a {@link CityDTO} object with the object creation request
     *
     * @return an asynchronous response with {@link CityDTO} object encapsulated in a {@link Response} object.
     *
     * @throws CityException in case of the application throws any kind of exception.
     */
    @Async
    public CompletableFuture<Response<CityDTO>> save(final CityDTO cityRequest) throws CityException {
        try {
            Optional<City> city = cityRepository.findByNameAndState(cityRequest.getCity().toLowerCase(), cityRequest.getState().toLowerCase());
            if (city.isPresent()) {
                return CompletableFuture.completedFuture(Response.of(cityRequest.getNormalized()));
            }
            City citySaved = this.cityRepository.save(cityRequest.getCityObject());
            return CompletableFuture.completedFuture(Response.of(new CityDTO(citySaved).getNormalized()));
        } catch (Exception e) {
            throw new CityException("city.save.error", e.getMessage());
        }
    }

    /**
     * Retrieves all {@link City} objects encapsulated into a {@link CityDTO} object, that matches the similar name passed as parameter.
     *
     * @param cityName partial name of the city
     *
     * @return an asynchronous response with {@link CityDTO} objects encapsulated in a {@link Response} object.
     *
     * @throws CityException in case of the application throws any kind of exception.
     */
    @Async
    public CompletableFuture<Response<CityDTO>> findAllByNameLike(final String cityName) throws CityException {
        try {
            String cityNameLike = "%".concat(cityName.toLowerCase()).concat("%");
            List<City> cities = cityRepository.findAllByNameLike(cityNameLike);
            return CompletableFuture.completedFuture(Response.of(cities.stream()
                                                                         .map(city -> new CityDTO(city).getNormalized())
                                                                         .collect(Collectors.toList())));
        } catch (Exception e) {
            throw new CityException("city.list.name.error", e.getMessage());
        }
    }

    /**
     * Retrieves all {@link City} objects encapsulated into a {@link CityDTO} object, that fully matches its state property passed as parameter.
     *
     * @param state full state name
     *
     * @return an asynchronous response with {@link CityDTO} objects encapsulated in a {@link Response} object.
     *
     * @throws CityException in case of the application throws any kind of exception.
     */
    @Async
    public CompletableFuture<Response<CityDTO>> findAllByState(final String state) throws CityException {
        try {
            List<City> cities = cityRepository.findAllByState(state.toLowerCase());
            return CompletableFuture.completedFuture(Response.of(cities.stream()
                                                                         .map(city -> new CityDTO(city).getNormalized())
                                                                         .collect(Collectors.toList())));
        } catch (Exception e) {
            throw new CityException("city.list.state.error", e.getMessage());
        }
    }

}
