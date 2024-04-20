package com.coker.springboot.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name")
    private String first_name;
    private String last_name;
    @Column(unique = true)
    private String email;
    private String password;
    private int age;
    private String sex;
    @Temporal(TemporalType.DATE)
    private Date DOB;
    private String address;

    @ManyToOne
    private Department department;


}
