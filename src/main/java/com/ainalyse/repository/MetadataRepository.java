package com.ainalyse.repository;

import com.ainalyse.dto.ColumnInfoInterface;
import com.ainalyse.dto.TableNameInterface;
import com.ainalyse.entity.AInalyse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MetadataRepository extends Repository<AInalyse, Long> {

    // Fetch table names for a given schema
    @Query(value = """
            SELECT table_name AS tableName
            FROM information_schema.tables
            WHERE table_schema = :schemaName
            ORDER BY table_name
            """, nativeQuery = true)
    List<TableNameInterface> getTables(@Param("schemaName") String schemaName);

    // Fetch column names for a table in a given schema
    @Query(value = """
            SELECT column_name AS columnName
            FROM information_schema.columns
            WHERE table_schema = :schemaName
              AND table_name = :tableName
            ORDER BY ordinal_position
            """, nativeQuery = true)
    List<ColumnInfoInterface> getColumns(@Param("schemaName") String schemaName, @Param("tableName") String tableName);
}
