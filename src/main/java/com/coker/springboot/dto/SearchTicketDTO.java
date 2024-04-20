package com.coker.springboot.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Data
@EqualsAndHashCode(callSuper = false)
public class SearchTicketDTO extends SearchDTO{

    @DateTimeFormat(pattern = "dd/mm/yyyy")
    private  Date Start;
    @DateTimeFormat(pattern = "dd/mm/yyyy")
    private Date End;
    private boolean isDone;

    public Date getStart() {
        return Start;
    }

    public void setStart(Date start) {
        Start = start;
    }

    public Date getEnd() {
        return End;
    }

    public void setEnd(Date end) {
        End = end;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
