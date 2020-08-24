package com.eduardomallmann.compasso.technicaltest.domains.client;

import com.eduardomallmann.compasso.technicaltest.domains.city.City;
import com.eduardomallmann.compasso.technicaltest.domains.client.validators.FullName;

import javax.persistence.*;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * The class {@link Client} is the abstraction of the clients table on database.
 *
 * @author eduardomallmann
 * @since 0.0.1
 */
@Entity
@Table(name = "clients")
public class Client implements Serializable {

    private static final long serialVersionUID = 8575288244674078265L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clients_seq")
    @SequenceGenerator(name = "clients_seq", sequenceName = "clients_id_seq", allocationSize = 1)
    private Long id;
    @FullName
    @Column(name = "full_name", nullable = false)
    private String fullName;
    @Column(name = "gender")
    private String gender;
    @Past(message = "{client.validation.birthday.error}")
    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name="city_id")
    private City city;

    /**
     * Main constructor, empty.
     */
    public Client() {
    }

    /**
     * Optional constructor, with the properties needed to create a new register.
     *
     * @param fullName clients full name, mandatory
     * @param gender   clients gender, optional
     * @param birthday clients birthday, mandatory
     * @param city     clients city, optional
     */
    public Client(final String fullName,
                  final String gender,
                  final LocalDate birthday,
                  final City city) {
        this.fullName = fullName;
        this.gender = gender;
        this.birthday = birthday;
        this.city = city;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(final String fullname) {
        this.fullName = fullname;
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

    public City getCity() {
        return city;
    }

    public void setCity(final City city) {
        this.city = city;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Client client = (Client) o;

        if (!Objects.equals(id, client.id)) return false;
        if (!fullName.equals(client.fullName)) return false;
        if (!Objects.equals(gender, client.gender)) return false;
        if (!birthday.equals(client.birthday)) return false;
        return Objects.equals(city, client.city);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + fullName.hashCode();
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + birthday.hashCode();
        result = 31 * result + (city != null ? city.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Client{" +
                       "id=" + id +
                       ", fullname='" + fullName + '\'' +
                       ", gender='" + gender + '\'' +
                       ", birthday=" + birthday.toString() +
                       ", city=" + city.toString() +
                       '}';
    }
}
