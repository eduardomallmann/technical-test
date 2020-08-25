package com.eduardomallmann.compasso.technicaltest.domains.client.dto;

import com.eduardomallmann.compasso.technicaltest.domains.client.Client;
import com.eduardomallmann.compasso.technicaltest.domains.client.validators.FullName;
import com.eduardomallmann.compasso.technicaltest.utils.GenericDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

/**
 * Client data transfer object responsible to encapsulate {@link Client} full name update requests.
 *
 * @author eduardomallmann
 * @since 0.0.1
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(NON_EMPTY)
public class ClientNameRequest extends GenericDTO {

    @FullName
    private String name;

    /**
     * Main constructor, empty.
     */
    public ClientNameRequest() {
    }

    /**
     * Optional constructor with full params.
     *
     * @param name client full name param
     */
    public ClientNameRequest(@FullName final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ClientNameRequest that = (ClientNameRequest) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "ClientNameDTO{" +
                       "name='" + name + '\'' +
                       '}';
    }
}
