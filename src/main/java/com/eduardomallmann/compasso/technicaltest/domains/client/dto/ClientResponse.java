package com.eduardomallmann.compasso.technicaltest.domains.client.dto;

import com.eduardomallmann.compasso.technicaltest.domains.city.CityDTO;
import com.eduardomallmann.compasso.technicaltest.domains.client.Client;
import com.eduardomallmann.compasso.technicaltest.utils.GenericDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

/**
 * Client data transfer object responsible to encapsulate {@link Client} responses.
 *
 * @author eduardomallmann
 * @since 0.0.1
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(NON_EMPTY)
public class ClientResponse extends GenericDTO {

    private Long id;
    private String name;
    private String gender;
    private LocalDate birthday;
    private String city;
    private String state;

    /**
     * Optional constructor, built from a {@link Client} object.
     *
     * @param client the {@link Client} object
     */
    public ClientResponse(final Client client) {
        this.id = client.getId();
        this.name = this.normalizeField(client.getFullName());
        this.gender = client.getGender() != null ? this.normalizeField(client.getGender()) : null;
        this.birthday = client.getBirthday();
        if (client.getCity() != null) {
            CityDTO city = new CityDTO(client.getCity().getName(), client.getCity().getState()).getNormalized();
            this.city = city.getCity();
            this.state = city.getState();
        }
    }

    /**
     * Calculate the age of the {@link ClientResponse} considering its birthdate.
     *
     * @return an int.
     */
    public int getAge() {
        return Period.between(this.birthday, LocalDate.now()).getYears();
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(final String gender) {
        this.gender = gender;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(final LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(final String state) {
        this.state = state;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ClientResponse clientResponse = (ClientResponse) o;

        if (!Objects.equals(id, clientResponse.id)) return false;
        if (!name.equals(clientResponse.name)) return false;
        if (!Objects.equals(gender, clientResponse.gender)) return false;
        if (!birthday.equals(clientResponse.birthday)) return false;
        if (!Objects.equals(city, clientResponse.city)) return false;
        return Objects.equals(state, clientResponse.state);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + name.hashCode();
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + birthday.hashCode();
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ClientDTO{" +
                       "id=" + id +
                       ", fullname='" + name + '\'' +
                       ", gender='" + gender + '\'' +
                       ", birthday=" + birthday.toString() +
                       ", city='" + city + '\'' +
                       ", state='" + state + '\'' +
                       '}';
    }
}
