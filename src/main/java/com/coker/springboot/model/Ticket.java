package com.coker.springboot.model;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "ticket")
@Data
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ticketId;

    private String clientName;
    private String clientPhone;


    private String content;

    @CreatedDate
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    private Date createdAt;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date processDate;

    private boolean status;

    @ManyToOne
    private Department department;
}
