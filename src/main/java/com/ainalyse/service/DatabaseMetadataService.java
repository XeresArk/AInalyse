// language: java
package com.ainalyse.service;

import com.ainalyse.dto.SchemaInfoDTO;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
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
}
