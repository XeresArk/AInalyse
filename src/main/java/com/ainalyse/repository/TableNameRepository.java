// language: java
package com.ainalyse.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.Repository;
import java.util.List;

public interface TableNameRepository extends Repository<Object, Long> {
    @Query(value = "SELECT table_name FROM information_schema.tables " +
            "WHERE (:schema IS NULL OR table_schema = :schema) AND table_type = 'BASE TABLE'",
            nativeQuery = true)
    List<String> findTableNames(@Param("schema") String schema);
}
