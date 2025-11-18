// language: java
package com.ainalyse.service;

import com.ainalyse.repository.TableNameRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Objects;

@Service
public class DatabaseMetadataService {
    private final TableNameRepository tableNameRepository;
    private final EntityManager entityManager;

    public DatabaseMetadataService(TableNameRepository tableNameRepository, EntityManager entityManager) {
        this.tableNameRepository = tableNameRepository;
        this.entityManager = entityManager;
    }

    /**
     * Try using the injected Jpa repository first; if it's not available or returns null,
     * fall back to a native query via EntityManager.
     */
    public List<String> getTableNames(String schema) {
        try {
            if (tableNameRepository != null) {
                List<String> repoResult = tableNameRepository.findTableNames(schema);
                if (repoResult != null && !repoResult.isEmpty()) {
                    return repoResult;
                }
            }
            String sql = "SELECT table_name FROM information_schema.tables " +
                    "WHERE (:schema IS NULL OR table_schema = :schema) AND table_type = 'BASE TABLE'";
            Query nativeQuery = entityManager.createNativeQuery(sql);
            nativeQuery.setParameter("schema", schema);
            @SuppressWarnings("unchecked")
            List<String> result = nativeQuery.getResultList();
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to read table metadata: " + e.getMessage(), e);
        }
    }
}
