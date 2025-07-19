package dev.heisen.metadata.repository;

import dev.heisen.metadata.model.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, String> {
    Optional<Publication> findByHash(String hash);
}
