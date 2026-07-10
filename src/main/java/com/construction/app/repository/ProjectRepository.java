package com.construction.app.repository;
import com.construction.app.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT DISTINCT p FROM Project p " +
            "LEFT JOIN FETCH p.projectRoles pr " +
            "LEFT JOIN FETCH pr.employee")
    List<Project> findAllWithRolesAndEmployees();
}
