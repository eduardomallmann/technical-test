package com.eduardomallmann.compasso.technicaltest.domains.client.dto;

import com.eduardomallmann.compasso.technicaltest.domains.city.City;
import com.eduardomallmann.compasso.technicaltest.domains.client.Client;
import com.eduardomallmann.compasso.technicaltest.domains.client.validators.ClientCityValidation;
import com.eduardomallmann.compasso.technicaltest.domains.client.validators.FullName;
import com.eduardomallmann.compasso.technicaltest.utils.GenericDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

/**
 * Client data transfer object responsible to encapsulate {@link Client} requests.
 *
 * @author eduardomallmann
 * @since 0.0.1
 */
@JsonInclude(NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@ClientCityValidation
public class ClientRequest extends GenericDTO {

    @FullName
    private String name;
    private String gender;
    @Past(message = "{client.validation.birthday.error}")
    private LocalDate birthday;
    private String city;
    private String state;

    /**
     * Main constructor, empty.
     */
    public ClientRequest() {
    }

    /**
     * Optional constructor, used for tests purposes.
     *
     * @param name     client full name
     * @param gender   client gender
     * @param birthday client date of birth
     * @param city     client city
     * @param state    client state
     */
    public ClientRequest(@FullName final String name,
                         final String gender,
                         @Past(message = "{client.validation.birthday.error}") final LocalDate birthday,
                         final String city,
                         final String state) {
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.city = city;
        this.state = state;
    }

    /**
     * Gets a {@link Client} object from this data.
     *
     * @return a {@link Client} object.
     */
    @JsonIgnore
    public Client getClient() {
        Client client = new Client(
                this.name.toLowerCase(),
                this.gender != null ? this.gender.toLowerCase() : null,
                this.birthday, null);
        if (this.city != null && this.state != null) {
            client.setCity(new City(this.city.toLowerCase(), this.state.toLowerCase()));
        }
        return client;
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

        final ClientRequest that = (ClientRequest) o;

        if (!name.equals(that.name)) return false;
        if (!Objects.equals(gender, that.gender)) return false;
        if (!birthday.equals(that.birthday)) return false;
        if (!Objects.equals(city, that.city)) return false;
        return Objects.equals(state, that.state);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + birthday.hashCode();
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ClientRequest{" +
                       "name='" + name + '\'' +
                       ", gender='" + gender + '\'' +
                       ", birthday=" + birthday +
                       ", city='" + city + '\'' +
                       ", state='" + state + '\'' +
                       '}';
    }
}
