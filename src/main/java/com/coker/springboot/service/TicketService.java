package com.coker.springboot.service;


import com.coker.springboot.dto.*;
import com.coker.springboot.model.Ticket;
import com.coker.springboot.repository.TicketRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

public interface TicketService{
    TicketDTO findById(int id);
    List<TicketDTO> findAll();

    void update(TicketDTO ticketDTO);
    void create(TicketDTO ticketDTO);
    void delete(int id);
    PageDTO<List<TicketDTO>> searchByName(SearchTicketDTO searchTicketDTO);


}


@Service
 class TicketServiceIml implements TicketService {
    @Autowired
    TicketRepo ticketRepo;

    @Override
    public TicketDTO findById(int id){
        return convert(ticketRepo.findById(id).orElse(null));
    }


    @Override
    public List<TicketDTO> findAll() {
        ModelMapper modelMapper = new ModelMapper();
        List<Ticket> ticketList = ticketRepo.findAll();
        List<TicketDTO> ticketDTOs = ticketList.stream().map(u ->convert(u)).collect(Collectors.toList());
        return ticketDTOs;
    }

    @Override
    public void update(TicketDTO ticketDTO) {
        Ticket ticket = new ModelMapper().map(ticketDTO,Ticket.class);
        ticketRepo.save(ticket);
    }

    @Override
    public void create(TicketDTO ticketDTO) {
        Ticket ticket = new ModelMapper().map(ticketDTO, Ticket.class);
        ticketRepo.save(ticket);
    }

    @Override
    public void delete(int id) {
        ticketRepo.deleteById(id);
    }

    @Override
    public PageDTO<List<TicketDTO>> searchByName(SearchTicketDTO searchTicketDTO) {

        if(searchTicketDTO.getCurrentPage() == null){
            searchTicketDTO.setCurrentPage(0);
        }
        if(searchTicketDTO.getSize() == null){
            searchTicketDTO.setSize(10);
        }

        PageRequest pageRequest = PageRequest.of(searchTicketDTO.getCurrentPage(), searchTicketDTO.getSize());
        Page<Ticket> page = null;
        if(searchTicketDTO.getStart()!= null && searchTicketDTO.getEnd() != null){

        }
        Page<Ticket> ticketPage = ticketRepo.searchByName("%" +searchTicketDTO.getKeyword()+"%",pageRequest);

        PageDTO<List<TicketDTO>> pageDTO = new PageDTO<>();
        pageDTO.setTotalPage(ticketPage.getTotalPages());
        pageDTO.setTotalElement(ticketPage.getTotalElements());

        List<TicketDTO> ticketDTOS = ticketPage.get().map(u -> convert(u)).collect(Collectors.toList());

        pageDTO.setData(ticketDTOS);

        return pageDTO;
    }
    private TicketDTO convert(Ticket ticket){
        return new ModelMapper().map(ticket, TicketDTO.class);
    }
}
