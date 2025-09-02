package com.anujl.collaborative_code_editor.controller;

import com.anujl.collaborative_code_editor.dto.UserLoginDto;
import com.anujl.collaborative_code_editor.dto.UserResponseDTO;
import com.anujl.collaborative_code_editor.entity.UserEntity;
import com.anujl.collaborative_code_editor.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class UserController {
    @Autowired
    UserService userService;
//Todo: add request respose dto
    @PostMapping("/register")
    public String saveUser(@RequestBody UserEntity userEntity){

        userService.saveUser(userEntity);
        return "saved";
    }
    @PostMapping("/login")
    public String loginUser(@RequestBody UserLoginDto userLoginDto){
        System.out.println("Login attempt for user: " + userLoginDto.getUsername());
        return userService.verify(userLoginDto);
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(@RequestBody Map<String,String> body){
        String token = body.get("token").trim();

        userService.logout(token.replace(" ",""));
        return ResponseEntity.ok("Success" );
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDTO>> allUsers(){
        System.out.println("Fetching all users");
       return ResponseEntity.ok(userService.getAllUsers());
    }
}
