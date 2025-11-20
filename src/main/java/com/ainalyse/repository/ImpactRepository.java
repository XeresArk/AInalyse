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
            @Param("P_OWNER") String schema,
            @Param("P_TABLE_NAME") String table,
            @Param("P_COLUMN_NAME") String column
    );

    // Return list of search_desc for the latest run
    @Query("Select h from HackathonSearchResult h where h.runId = (Select Max(h2.runId) from HackathonSearchResult h2)")
    List<HackathonSearchResult> findLatestSearchDescList();
}
