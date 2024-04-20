package com.coker.springboot.service;

import com.coker.springboot.dto.PageDTO;
import com.coker.springboot.dto.SearchDTO;
import com.coker.springboot.dto.UserDTO;
import com.coker.springboot.model.User;
import com.coker.springboot.repository.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class  UserService {
    @Autowired
    UserRepo userRepo;
    @Autowired
    DepartmentService departmentService;

    @Transactional
        public List<UserDTO> findAll(){
            List<User> users = userRepo.findAll();
            return users.stream().map(u -> convert(u)).collect(Collectors.toList());
        }
        @Transactional
        public void deleteUser(int id){
            userRepo.deleteById(id);
        }
        @Transactional
        public  UserDTO findByName(String email){
            ModelMapper modelMapper = new ModelMapper();
            UserDTO userDTO = modelMapper.map(userRepo.findByUsername(email),UserDTO.class);
            return userDTO;
        }
        @Transactional
        public  void add(UserDTO userDTO){
            ModelMapper modelMapper = new ModelMapper();
            User user = modelMapper.map(userDTO, User.class);
            userRepo.save(user);
        }
        @Transactional
        public UserDTO findById(int id){
            User user = userRepo.findById(id).orElse(null);
            return convert(user) ;
        }
        @Transactional
        private UserDTO convert(User user){
            ModelMapper modelMapper = new ModelMapper();
            UserDTO userDTO = modelMapper.map(user,UserDTO.class);
            return userDTO;
        }

        @Transactional
        public void update(UserDTO userDTO){
            User userUpdate = userRepo.findById(userDTO.getId()).orElse(null);
            if(userUpdate!=null){
                userUpdate.setAddress(userDTO.getAddress());
                userUpdate.setFirst_name(userUpdate.getFirst_name());
                userUpdate.setLast_name(userUpdate.getLast_name());
                userUpdate.setDepartment(userUpdate.getDepartment());
                userUpdate.setDOB(userUpdate.getDOB());
            }

            userRepo.save(userUpdate);
        }
        @Transactional
        public PageDTO<List<UserDTO>> searchUser(SearchDTO searchDTO){
            Sort sort = Sort.by("id").ascending();
            if(StringUtils.hasText(searchDTO.getSortedByColumn())){
                sort = Sort.by(searchDTO.getSortedByColumn()).ascending();
            }
            if(searchDTO.getSize() == null){
                searchDTO.setSize(10);
            }
            if(searchDTO.getCurrentPage() == null){
                searchDTO.setCurrentPage(0);
            }

            PageRequest pageRequest = PageRequest.of(searchDTO.getCurrentPage(), searchDTO.getSize());

            Page<User> userPage = userRepo.findAllUserName("%" + searchDTO.getKeyword() +"%",pageRequest);

            PageDTO<List<UserDTO>> pageUserDTO = new PageDTO<>();
            pageUserDTO.setTotalPage(userPage.getTotalPages());
            pageUserDTO.setTotalElement(userPage.getTotalElements());

            List<UserDTO> userDTOList = userPage.get().map(u -> convert(u)).collect(Collectors.toList());
            pageUserDTO.setData(userDTOList);

            return pageUserDTO;
        }

}
