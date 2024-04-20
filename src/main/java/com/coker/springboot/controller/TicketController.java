package com.coker.springboot.controller;

import java.util.Date;

import com.coker.springboot.dto.TicketDTO;
import com.coker.springboot.service.DepartmentService;
import com.coker.springboot.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.coker.springboot.model.Ticket;

@Controller
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    DepartmentService departmentService;

    @Autowired
    TicketService ticketService;

    @GetMapping("/new")
    public String add(Model model) {
        model.addAttribute("departmentList", departmentService.findAll());
        return "ticket/add.html";
    }

    @PostMapping("/new")
    public String add(@ModelAttribute Ticket t) {
        ticketService.create(t);
        return "redirect:/ticket/search";
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "id", required = false) Integer id,
                         @RequestParam(name = "name", required = false) String name,
                         @RequestParam(name = "departmentId", required = false) Integer departmentId,

                         @RequestParam(name = "start", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm") Date start,
                         @RequestParam(name = "end", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm") Date end,

                         @RequestParam(name = "size", required = false) Integer size,
                         @RequestParam(name = "page", required = false) Integer page, Model model) {

        size = size == null ? 10 : size;
        page = page == null ? 0 : page;

        Pageable pageable = PageRequest.of(page, size);


        // show lai form
        model.addAttribute("id", id);
        model.addAttribute("name", name);
        model.addAttribute("start", start);
        model.addAttribute("end", end);

        model.addAttribute("page", page);
        model.addAttribute("size", size);

        // show trong form search
        model.addAttribute("departmentList", departmentService.findAll());

        return "ticket/search.html";
    }

    @GetMapping("/get/{id}")
    public String get(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", ticketService.findById(id));
        return "ticket/detail.html";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("id") int id) {
        ticketService.delete(id);
        return "redirect:/ticket/search";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam("id") int id, Model model) {
        model.addAttribute("ticket", ticketService.findById(id));
        return "ticket/edit.html";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute Ticket editTicket) {
        TicketDTO current = ticketService.findById(editTicket.getTicketId());

        if (current != null) {
            current.setClientName(editTicket.getClientName());
            ticketRepo.save(current);
        }

        return "redirect:/ticket/search";
    }
}