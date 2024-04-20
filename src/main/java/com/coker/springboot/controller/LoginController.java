package com.coker.springboot.controller;

import com.coker.springboot.dto.DepartmentDTO;
import com.coker.springboot.dto.PageDTO;
import com.coker.springboot.dto.SearchDTO;
import com.coker.springboot.dto.UserDTO;
import com.coker.springboot.service.DepartmentService;
import com.coker.springboot.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class LoginController{
    @Autowired
    private UserService userService;
    @Autowired
    private DepartmentService departmentService;
    @GetMapping("/login")
    public String login() {
        return "login.html";
    }
    @PostMapping("/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        HttpSession session,
                        Model model) {
        if (email.equals("admin") && password.equals("123")) {
            return "redirect:/admin";
        } else {
            UserDTO loginUser = userService.findByName(email);
            if (loginUser == null || !password.equals(loginUser.getPassword())) {
                model.addAttribute("errorMessage", "Invalid email or password");
                return "login.html";
            } else {
                session.setAttribute("loggedInUser", loginUser);
                return "redirect:/dashboard";
            }

        }
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        List<DepartmentDTO> departmentList = departmentService.findAll();

        model.addAttribute("departmentList", departmentList);
        model.addAttribute("userDTO",new UserDTO());
        return "register.html";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("userDTO") UserDTO userDTO) {
            userService.add(userDTO);
            return "redirect:/register";

    }
    @GetMapping("/delete")
    public String deleteUser(@RequestParam("id") int id){
        userService.deleteUser(id);
        return "redirect:/admin";
    }
    @GetMapping("/update")
    public String updateUser(@RequestParam("id") int id,
                             Model model){
            UserDTO owner = userService.findById(id);

            List<DepartmentDTO> departmentList = departmentService.findAll();
            model.addAttribute("departmentList",departmentList);
            model.addAttribute("owner",owner);

            return "update.html";
    }
    @GetMapping("/dashboard")
    public String welcome(){
        return "dashboard.html";
    }
    @GetMapping("/admin")
    public String admin(Model model){
        List<UserDTO> userList = userService.findAll();
        model.addAttribute("userList", userList);
        model.addAttribute("searchDTO", new SearchDTO());
        return "admin.html";
    }
    @PostMapping("/update")
    public String pushUpdateUser(@ModelAttribute("owner") @Valid UserDTO userDTO,
                                 BindingResult bindingResult,
                                 Model model){
        if (bindingResult.hasErrors()){
            List<DepartmentDTO> departmentList = departmentService.findAll();
            model.addAttribute("departmentList",departmentList);
            return "redirect:/dashboard";
        }

            List<DepartmentDTO> departmentList = departmentService.findAll();
            model.addAttribute("departmentList",departmentList);
            userService.update(userDTO);

        return "redirect:/admin";
    }
    @GetMapping("/search")
    public String searchUser(Model model,
                             @Valid SearchDTO searchDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return "admin.html";
        }
        PageDTO<List<UserDTO>> pageUser  = userService.searchUser(searchDTO);
        model.addAttribute("userList",pageUser.getData());
        model.addAttribute("totalPage", pageUser.getTotalPage());
        model.addAttribute("totalElement",pageUser.getTotalElement());
        return "admin.html";
    }

}
