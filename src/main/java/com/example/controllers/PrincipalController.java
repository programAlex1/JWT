package com.example.controllers;


import com.example.models.request.CreateUserDto;
import com.example.models.ERoles;
import com.example.models.RoleEntity;
import com.example.models.UserEntity;
import com.example.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping
public class PrincipalController {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @GetMapping("/hello")
    public String hello(){
        return "Hello World not secured";
    }

    @GetMapping("/helloSecured")
    public String helloSecured(){
        return "Hello World secured";
    }

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserDto createUserDto){

        Set<RoleEntity> roles = createUserDto.getRoles().stream().map(role ->
                RoleEntity.builder().name(ERoles.valueOf(role)).build()).collect(Collectors.toSet());

        UserEntity user = UserEntity.builder()
                .username(createUserDto.getUsername())
                .email(createUserDto.getEmail())
                .password(passwordEncoder.encode(createUserDto.getPassword()))
                .roles(roles)
                .build();
        userRepository.save(user);

        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/deleteUser")
    public String deleteUser(@RequestParam String id){
        userRepository.deleteById(Long.parseLong(id));
        return "Se ha borrado el user con id ".concat(id);
    }
}
