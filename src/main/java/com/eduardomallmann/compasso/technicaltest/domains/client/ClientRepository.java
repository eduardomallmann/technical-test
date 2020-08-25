package com.eduardomallmann.compasso.technicaltest.domains.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface that defines the calls of {@link Client} to clients table in the database.
 *
 * @author eduardomallmann
 * @since 0.0.1
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    /**
     * Find all {@link Client} registers with a name like the param passed.
     * <p>The name param can be full or partial.</p>
     *
     * @param name full or partial client full name property
     *
     * @return A list of {@link Client} objects.
     */
    List<Client> findAllByFullNameLike(final String name);

    /**
     * Updates the {@link Client} register fullName property, identified by its id.
     *
     * @param id   {@link Client} register database identification
     * @param name value to be placed in {@link Client} fullName property.
     */
    @Modifying
    @Query("update Client c set c.fullName = :name where c.id = :id")
    void updateClientFullName(@Param("id") Long id,
                              @Param("name") String name);
}
