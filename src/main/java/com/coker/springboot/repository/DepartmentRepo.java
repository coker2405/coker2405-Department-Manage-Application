package com.coker.springboot.repository;

import com.coker.springboot.model.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepo extends JpaRepository<Department, Integer> {
    @Query("SELECT u FROM Department u WHERE u.name LIKE :name")
    Page<Department> searchByName(@Param("name") String name, Pageable pageable);

}
