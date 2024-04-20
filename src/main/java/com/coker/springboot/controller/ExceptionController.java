package com.coker.springboot.controller;

import jakarta.persistence.NoResultException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionController {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @ExceptionHandler({NoResultException.class})
    public String notFound(Exception e){
        e.printStackTrace();
        return "404.html";
    }
}
