package com.eduardomallmann.compasso.technicaltest.domains.city;

import com.eduardomallmann.compasso.technicaltest.exceptions.BusinessException;
import com.eduardomallmann.compasso.technicaltest.utils.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger log = LoggerFactory.getLogger(this.getClass());

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
     * @throws BusinessException in case of the application throws any kind of exception.
     */
    @Async
    public CompletableFuture<Response<CityDTO>> save(final CityDTO cityRequest) throws BusinessException {
        try {
            Optional<City> city = cityRepository.findByNameAndState(cityRequest.getCity().toLowerCase(), cityRequest.getState().toLowerCase());
            if (city.isPresent()) {
                log.debug("City already exists: {}", cityRequest.getNormalized().toJson());
                return CompletableFuture.completedFuture(Response.of(cityRequest.getNormalized()));
            }
            City citySaved = this.cityRepository.save(cityRequest.getCityObject());
            CityDTO result = new CityDTO(citySaved).getNormalized();
            log.debug("City created: {}", result.toJson());
            return CompletableFuture.completedFuture(Response.of(result));
        } catch (Exception e) {
            log.error("Error on creating creating a new city: {} ", cityRequest.getNormalized().toJson());
            log.error("Exception catched: {}", e.getMessage());
            throw new BusinessException("city.save.error", e.getMessage());
        }
    }

    /**
     * Retrieves all {@link City} objects encapsulated into a {@link CityDTO} object, that matches the similar name passed as parameter.
     *
     * @param cityName partial name of the city
     *
     * @return an asynchronous response with {@link CityDTO} objects encapsulated in a {@link Response} object.
     *
     * @throws BusinessException in case of the application throws any kind of exception.
     */
    @Async
    public CompletableFuture<Response<CityDTO>> findAllByNameLike(final String cityName) throws BusinessException {
        try {
            String cityNameLike = "%".concat(cityName.toLowerCase()).concat("%");
            List<CityDTO> cities = cityRepository.findAllByNameLike(cityNameLike).stream()
                                           .map(city -> new CityDTO(city).getNormalized())
                                           .collect(Collectors.toList());
            log.debug("Total of city found by name {}: {}", cityName, cities.size());
            return CompletableFuture.completedFuture(Response.of(cities));
        } catch (Exception e) {
            log.error("Error on searching city by name {}: {}", cityName, e.getMessage());
            throw new BusinessException("city.list.name.error", e.getMessage());
        }
    }

    /**
     * Retrieves all {@link City} objects encapsulated into a {@link CityDTO} object, that fully matches its state property passed as parameter.
     *
     * @param state full state name
     *
     * @return an asynchronous response with {@link CityDTO} objects encapsulated in a {@link Response} object.
     *
     * @throws BusinessException in case of the application throws any kind of exception.
     */
    @Async
    public CompletableFuture<Response<CityDTO>> findAllByState(final String state) throws BusinessException {
        try {
            List<City> cities = cityRepository.findAllByState(state.toLowerCase());
            log.debug("Total of city found by state {}: {}", state, cities.size());
            return CompletableFuture.completedFuture(Response.of(cities.stream()
                                                                         .map(city -> new CityDTO(city).getNormalized())
                                                                         .collect(Collectors.toList())));
        } catch (Exception e) {
            log.error("Error on searching city by state {}: {}", state, e.getMessage());
            throw new BusinessException("city.list.state.error", e.getMessage());
        }
    }

}
