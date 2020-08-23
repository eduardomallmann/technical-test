package com.eduardomallmann.compasso.technicaltest.domains.city;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

/**
 * City data transfer object responsible to encapsulate {@link City} responses and requests.
 *
 * @author eduardomallmann
 * @since 0.0.1
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(NON_EMPTY)
public class CityDTO {

    private String city;
    private String state;

    /**
     * Main constructor, empty.
     */
    public CityDTO() {
    }

    /**
     * Optional constructor, with non null properties.
     *
     * @param city  name of the city
     * @param state the state that the city belongs to
     */
    public CityDTO(final String city, final String state) {
        this.city = city;
        this.state = state;
    }

    /**
     * Optional constructor, built from {@link City} object.
     *
     * @param city {@link CityDTO} object
     */
    public CityDTO(final City city) {
        this.city = city.getName();
        this.state = city.getState();
    }

    /**
     * Gets a {@link City} object from this data and normalize the fields values to lower case.
     *
     * @return a {@link City} object.
     */
    @JsonIgnore
    public City getCityObject() {
        return new City(this.city.toLowerCase(), this.state.toLowerCase());
    }

    /**
     * Gets a {@link CityDTO} object with its fields normalized with Camelcase and ignoring prepositions.
     *
     * @return a {@link CityDTO} object.
     */
    @JsonIgnore
    public CityDTO getNormalized() {
        this.city = this.normalizeField(this.city);
        this.state = this.normalizeField(this.state);
        return this;
    }

    /**
     * Normalizes a string parameter informed with Camelcase and ignoring prepositions.
     *
     * @param field a String parameter
     *
     * @return a String.
     */
    private String normalizeField(final String field) {
        final String[] words = field.split("\\s");
        StringBuilder normalizeField = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (i == 0 || i == words.length - 1) {
                normalizeField
                        .append(word.substring(0, 1).toUpperCase())
                        .append(word.substring(1).toLowerCase());
            } else if (word.length() >= 3) {
                normalizeField
                        .append(" ")
                        .append(word.toLowerCase());
            } else {
                normalizeField
                        .append(" ")
                        .append(word.substring(0, 1).toUpperCase())
                        .append(word.substring(1).toLowerCase());
            }
        }
        return normalizeField.toString();
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

        final CityDTO cityDTO = (CityDTO) o;

        if (!city.equals(cityDTO.city)) return false;
        return state.equals(cityDTO.state);
    }

    @Override
    public int hashCode() {
        int result = city.hashCode();
        result = 31 * result + state.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CityDTO{" +
                       "city='" + city + '\'' +
                       ", state='" + state + '\'' +
                       '}';
    }
}
