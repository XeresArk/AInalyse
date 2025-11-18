// language: java
package com.ainalyse.service;

import com.ainalyse.dto.SchemaInfoDTO;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DatabaseMetadataService {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public SchemaInfoDTO getAllSchemasWithTablesAndColumns() {
       String schemaName = (String) entityManager.createNativeQuery("SELECT current_schema()").getSingleResult();

       List<String> tableNames = entityManager.createNativeQuery(
               "SELECT table_name FROM information_schema.tables WHERE table_schema = :schemaName")
               .setParameter("schemaName", schemaName)
               .getResultList();

       Map<String, List<String>> tables = new LinkedHashMap<>();
         for (String tableName : tableNames) {
              List<String> columnNames = entityManager.createNativeQuery(
                     "SELECT column_name FROM information_schema.columns WHERE table_schema = :schemaName AND table_name = :tableName" +
                             " ORDER BY table_name")
                     .setParameter("schemaName", schemaName)
                     .setParameter("tableName", tableName)
                     .getResultList();
              tables.put(tableName, columnNames);
         }
         return new SchemaInfoDTO(schemaName, tables);
    }

    @Transactional
    public List performDatabaseImpactAnalysis(String schema, String table, String column) {
        //Call the procedure asynchronously (fire and forget)
        if (column == null || column.isEmpty()) {
             entityManager.createNativeQuery("CALL PR_GET_TABLE_IMPACT(:schema, :table, NULL)")
                    .setParameter("schema", schema)
                    .setParameter("table", table)
                    .executeUpdate();
        } else {
             entityManager.createNativeQuery("CALL PR_GET_TABLE_IMPACT(:schema, :table, :column)")
                    .setParameter("schema", schema)
                    .setParameter("table", table)
                    .setParameter("column", column)
                    .executeUpdate();
        }
        //Immediately fetch the latest results from the HACKATHON_SEARCH_RESULT table
        String sql = "SELECT * FROM HACKATHON_SEARCH_RESULT WHERE run_id = (SELECT MAX(run_id) FROM HACKATHON_SEARCH_RESULT)";
        return entityManager.createNativeQuery(sql).getResultList();
    }
}
