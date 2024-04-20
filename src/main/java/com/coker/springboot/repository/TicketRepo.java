package com.coker.springboot.repository;

import com.coker.springboot.model.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface TicketRepo extends JpaRepository<Ticket,Integer> {

    @Query("Select T from Ticket T where T.id = :id")
    Page<Ticket> findById(@Param("id") int id, Pageable pageable);

    @Query("Select T from Ticket T where T.clientName like :clientName")
    Page<Ticket> findByClientName(@Param("clientName") String clientName, Pageable pageable);

    @Query("Select t from Ticket t where t.clientPhone like: clientPhone")
    Page<Ticket> findByClientPhone(@Param("clientPhone") String clientPhone, Pageable pageable);

    @Query("Select t from Ticket t" + "where t.createAt <= :start and t.createAt >= :end")
    Page<List<Ticket>> findByDate(@Param("start") Date start, @Param("end") Date end,Pageable pageable);

    @Query("Select t from Ticket t JOIN t.department d" + "where d.id = :id")
    Page<Ticket> findByDepartmentId(@Param("id") int id, Pageable pageable);

    @Query("Select t from Ticket t where t.idDone : status")
    Page<Ticket> findByStatus(@Param("status") boolean status, Pageable pageable);

    @Query("Select t from Ticket t JOIN t.department.name d where " +"d.name = :name")
    Page<Ticket> findByDepartmentName(@Param("name") String name, Pageable pageable);

}
