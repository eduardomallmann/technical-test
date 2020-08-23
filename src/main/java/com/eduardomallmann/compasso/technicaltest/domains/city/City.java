package com.eduardomallmann.compasso.technicaltest.domains.city;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.util.Objects;

/**
 * The class {@link City} is the abstraction of the cities table on database.
 *
 * @author eduardomallmann
 * @since 0.0.1
 */
@Entity
@Table(name = "cities",
        uniqueConstraints=@UniqueConstraint(name = "uc_cities_name_state", columnNames={"name", "state"})
)
public class City implements Serializable {

    private static final long serialVersionUID = -3557055890514081553L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cities_seq")
    @SequenceGenerator(name = "cities_seq", sequenceName = "cities_id_seq", allocationSize = 1)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "state", nullable = false)
    private String state;

    /**
     * Main constructor, empty.
     */
    public City() {
    }

    /**
     * Optional constructor, with non null properties.
     *
     * @param name  the name of the city
     * @param state the state that the city belongs to
     */
    public City(final String name, final String state) {
        this.name = name;
        this.state = state;
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

        final City city = (City) o;

        if (!Objects.equals(id, city.id)) return false;
        if (!name.equals(city.name)) return false;
        return state.equals(city.state);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + name.hashCode();
        result = 31 * result + state.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "City{" +
                       "id=" + id +
                       ", name='" + name + '\'' +
                       ", state='" + state + '\'' +
                       '}';
    }
}
