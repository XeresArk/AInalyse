package com.ainalyse.repository;

import com.ainalyse.entity.HackathonSearchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImpactRepository extends JpaRepository<HackathonSearchResult, Integer> {

    // Stored Procedure with 3 parameters
    @Procedure(procedureName = "PR_GET_TABLE_IMPACT")
    void callImpactProcedure(
            @Param("schema") String schema,
            @Param("table") String table,
            @Param("column") String column
    );

    // Return list of search_desc for the latest run
    @Query(value = """
        SELECT search_desc
        FROM hackathon_search_result
        WHERE run_id = (SELECT MAX(run_id) FROM hackathon_search_result)
        """, nativeQuery = true)
    List<String> findLatestSearchDescList();
}
