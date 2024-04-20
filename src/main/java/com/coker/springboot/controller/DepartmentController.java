package com.coker.springboot.controller;

import com.coker.springboot.dto.DepartmentDTO;
import com.coker.springboot.dto.SearchDTO;
import com.coker.springboot.model.Department;
import com.coker.springboot.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RequestMapping("/department")
@Controller
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @PostMapping("/create")
    public  String createDepartment(@ModelAttribute("department") @Valid DepartmentDTO departmentDTO){
        departmentService.create(departmentDTO);
        return "redirect:/department/create";
    }
    @GetMapping("/create")
    public  String createDepartment(Model model){
        model.addAttribute("department", new DepartmentDTO());
        return "new_department.html";
    }
    @GetMapping("/delete")
    public String deleteDepartment(@RequestParam("id") int id){
        departmentService.delete(id);
        return "redirect:/dashboard";
    }
    @GetMapping("/update")
    public String updateUser(@RequestParam("id") int id,
                             Model model){
        DepartmentDTO owner = departmentService.getById(id);
        model.addAttribute("owner",owner);
        return "update.html";
    }

    @GetMapping("/dashboard")
    public String admin(Model model){
        List<DepartmentDTO> departmentList = departmentService.findAll();
        model.addAttribute("departmentList", departmentList);
        model.addAttribute("searchDTO", new SearchDTO());
        return "department.html";
    }
    @PostMapping("/update")
    public String pushUpdateDepartment(@ModelAttribute("department") DepartmentDTO departmentDTO){
        if (departmentDTO != null){
            departmentService.update(departmentDTO);
        }
        return "redirect:/dashboard";
    }
    @GetMapping("/search")
    public String search(@RequestParam(name = "id", required = false) Integer id,
                         @RequestParam(name = "name", required = false) String name,

                         @RequestParam(name = "start", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm") Date start,
                         @RequestParam(name = "end", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm") Date end,

                         @RequestParam(name = "size", required = false) Integer size,
                         @RequestParam(name = "page", required = false) Integer page, Model model) {

        size = size == null ? 10 : size;
        page = page == null ? 0 : page;

        Pageable pageable = PageRequest.of(page, size);

        if (id != null) {
            List<Department> departments = departmentService.findAll().stream().map(u->convert(u));

            model.addAttribute("totalPage", 1);
            model.addAttribute("count", departments.size());
            model.addAttribute("departmentList", departments);
        } else {
            Page<Department> pageRS = null;

            if (StringUtils.hasText(name) && start != null && end != null) {
                pageRS = departmentRepo.searchByNameAndDate("%" + name + "%", start, end, pageable);
            } else if (StringUtils.hasText(name) && start != null) {
                pageRS = departmentRepo.searchByNameAndStartDate("%" + name + "%", start, pageable);
            } else if (StringUtils.hasText(name) && end != null) {
                pageRS = departmentRepo.searchByNameAndEndDate("%" + name + "%", end, pageable);
            } else if (StringUtils.hasText(name)) {
                pageRS = departmentRepo.searchByName("%" + name + "%", pageable);
            } else if (start != null && end != null) {
                pageRS = departmentRepo.searchByDate(start, end, pageable);
            } else if (start != null) {
                pageRS = departmentRepo.searchByStartDate(start, pageable);
            } else if (end != null) {
                pageRS = departmentRepo.searchByEndDate(end, pageable);
            } else {
                pageRS = departmentRepo.findAll(pageable);
            }

            model.addAttribute("totalPage", pageRS.getTotalPages());
            model.addAttribute("count", pageRS.getTotalElements());
            model.addAttribute("departmentList", pageRS.getContent());
        }

        //luu lai du lieu set sang view lai
        model.addAttribute("id", id);
        model.addAttribute("name", name);
        model.addAttribute("start", start);
        model.addAttribute("end", end);

        model.addAttribute("page", page);
        model.addAttribute("size", size);

        return "department/search.html";
    }

}
