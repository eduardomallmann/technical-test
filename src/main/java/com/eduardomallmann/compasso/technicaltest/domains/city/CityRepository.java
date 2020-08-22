package com.eduardomallmann.compasso.technicaltest.domains.city;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface that defines the calls of {@link City} to cities table in the database.
 *
 * @author eduardomallmann
 * @since 0.0.1
 */
@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    /**
     * Find all {@link City} registers with a name like the param passed.
     * <p>The name param can be full or partial.</p>
     *
     * @param name full or partial city name
     *
     * @return A list of {@link City} objects.
     */
    List<City> findAllByNameLike(final String name);

    /**
     * Find all {@link City} registers with the same state property.
     * <p>The state param must be fully compatible with the property value</p>
     *
     * @param state full state name
     *
     * @return A list of {@link City} objects.
     */
    List<City> findAllByState(final String state);
}
