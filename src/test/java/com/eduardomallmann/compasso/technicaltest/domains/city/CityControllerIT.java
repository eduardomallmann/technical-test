package com.eduardomallmann.compasso.technicaltest.domains.city;

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
import org.springframework.test.context.ActiveProfiles;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CityControllerIT {

    private static final String CITY_ENDPOINT = "/cities";

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        this.cityRepository.deleteAll();
    }

    @Test
    void createNewCity_ShouldReturnCityCreated() {
        //given
        CityDTO cityRequest = new CityDTO("Florianópolis", "Santa Catarina");
        //when
        ResponseEntity<Response<CityDTO>> result = restTemplate.exchange(
                RequestEntity.post(URI.create(CITY_ENDPOINT))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(cityRequest),
                new ParameterizedTypeReference<Response<CityDTO>>() {
                });
        //then
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertTrue(Objects.requireNonNull(result.getBody()).getContent().stream()
                           .allMatch(resp -> cityRequest.getCity().equalsIgnoreCase(resp.getCity())
                                                     && cityRequest.getState().equalsIgnoreCase(resp.getState())));
    }

    @Test
    void createNewCity_ShouldReturnValidationError() {
        //given
        CityDTO cityRequest = new CityDTO();
        String errorMessage = MessageUtils.getMessage("city.validation.error");
        //when
        ResponseEntity<Response<ResponseContent>> result = restTemplate.exchange(
                RequestEntity.post(URI.create(CITY_ENDPOINT))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(cityRequest),
                new ParameterizedTypeReference<Response<ResponseContent>>() {
                });
        //then
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertTrue(Objects.requireNonNull(Objects.requireNonNull(result.getBody()).getContent()).stream()
                           .allMatch(resp -> errorMessage.equals(resp.getErrorMessage().getMessage())));
    }

    @Test
    void createNewCity_ShouldReturnCityExistent() {
        //given
        CityDTO cityRequest = new CityDTO("Florianópolis", "Santa Catarina");
        this.cityRepository.save(cityRequest.getCityObject());
        //when
        ResponseEntity<Response<CityDTO>> result = restTemplate.exchange(
                RequestEntity.post(URI.create(CITY_ENDPOINT))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(cityRequest),
                new ParameterizedTypeReference<Response<CityDTO>>() {
                });
        //then
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertTrue(Objects.requireNonNull(result.getBody()).getContent().stream()
                           .allMatch(resp -> cityRequest.getCity().equalsIgnoreCase(resp.getCity())
                                                     && cityRequest.getState().equalsIgnoreCase(resp.getState())));
    }

    @Test
    void getCitiesByName_ShouldReturnCitiesFound() throws UnsupportedEncodingException {
        //given
        final String name = "são";
        List<CityDTO> cities = this.populateCities().stream()
                                       .filter(city -> city.getCity().toLowerCase().contains(name))
                                       .map(CityDTO::getNormalized)
                                       .collect(Collectors.toList());
        //when
        ResponseEntity<Response<CityDTO>> result = restTemplate.exchange(
                RequestEntity.get(URI.create(CITY_ENDPOINT.concat("?name=").concat(URLEncoder.encode(name, "UTF-8"))))
                        .accept(MediaType.APPLICATION_JSON)
                        .build(),
                new ParameterizedTypeReference<Response<CityDTO>>() {
                });
        //then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(cities.size(), Objects.requireNonNull(result.getBody()).getContent().size());
        assertEquals(cities, Objects.requireNonNull(result.getBody()).getContent());
        assertTrue(Objects.requireNonNull(result.getBody()).getContent().stream().allMatch(city -> city.getCity().toLowerCase().contains(name)));
    }

    @Test
    void getCitiesByState_ShouldReturnCitiesFound() throws UnsupportedEncodingException {
        //given
        final String state = "são paulo";
        List<CityDTO> cities = this.populateCities().stream()
                                       .filter(city -> state.equalsIgnoreCase(city.getState()))
                                       .map(CityDTO::getNormalized)
                                       .collect(Collectors.toList());
        //when
        ResponseEntity<Response<CityDTO>> result = restTemplate.exchange(
                RequestEntity.get(URI.create(CITY_ENDPOINT.concat("?state=").concat(URLEncoder.encode(state, "UTF-8"))))
                        .accept(MediaType.APPLICATION_JSON)
                        .build(),
                new ParameterizedTypeReference<Response<CityDTO>>() {
                });
        //then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(cities.size(), Objects.requireNonNull(result.getBody()).getContent().size());
        assertEquals(cities, Objects.requireNonNull(result.getBody()).getContent());
        assertTrue(Objects.requireNonNull(result.getBody()).getContent().stream().allMatch(city -> state.equalsIgnoreCase(city.getState())));
    }

    private List<CityDTO> populateCities() {
        CityDTO saoPaulo = new CityDTO("São Paulo", "São Paulo");
        CityDTO saoJoseDoRioPreto = new CityDTO("São José do Rio Preto", "São Paulo");
        CityDTO saoBernardo = new CityDTO("São Bernardo", "São Paulo");
        CityDTO saoJose = new CityDTO("São Paulo", "Santa Catarina");
        CityDTO florianopolis = new CityDTO("Florianópolis", "Santa Catarina");
        List<CityDTO> cities = Arrays.asList(saoPaulo, saoJoseDoRioPreto, saoBernardo, saoJose, florianopolis);
        this.cityRepository.saveAll(cities.stream().map(CityDTO::getCityObject).collect(Collectors.toList()));
        return cities;
    }
}