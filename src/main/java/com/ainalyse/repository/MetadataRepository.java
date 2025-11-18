package com.ainalyse.repository;

import com.ainalyse.dto.TableNameInterface;
import com.ainalyse.dto.ColumnInfoInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MetadataRepository extends JpaRepository<Object, Long> {

    // Fetch table names for schema = 'ainalyse'
    @Query(value = """
            SELECT table_name AS tableName
            FROM information_schema.tables
            WHERE table_schema = 'ainalyse'
            ORDER BY table_name
            """, nativeQuery = true)
    List<TableNameInterface> getTables();

    // Fetch column names for a table in schema 'ainalyse'
    @Query(value = """
            SELECT column_name AS columnName
            FROM information_schema.columns
            WHERE table_schema = 'ainalyse'
              AND table_name = :tableName
            ORDER BY ordinal_position
            """, nativeQuery = true)
    List<ColumnInfoInterface> getColumns(String tableName);
}
