package com.javadiseno.sanosysalvos.integration.repository;

import com.javadiseno.sanosysalvos.integration.model.InstitutionApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InstitutionApiKeyRepository extends JpaRepository<InstitutionApiKey, UUID> {
    Optional<InstitutionApiKey> findByApiKey(String apiKey);
}
