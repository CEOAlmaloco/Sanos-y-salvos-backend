package com.javadiseno.sanosysalvos.integration.repositories;

import com.javadiseno.sanosysalvos.integration.models.InstitutionApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InstitutionApiKeyRepository extends JpaRepository<InstitutionApiKey, UUID> {
    Optional<InstitutionApiKey> findByApiKey(String apiKey);
}
