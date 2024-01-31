package com.testproject.catalog.resources;

import com.testproject.catalog.dtos.LoginUserDTO;
import com.testproject.catalog.dtos.RegisterUserDTO;
import com.testproject.catalog.entities.User;
import com.testproject.catalog.repositories.UserRepository;
import com.testproject.catalog.resources.exceptions.FieldMessage;
import com.testproject.catalog.servicies.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthenticationResources {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepository repository;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginUserDTO dto){
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());
        var auth = authenticationManager.authenticate(usernamePassword);

        String token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok().body("token = " + token);
    }


    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterUserDTO dto){
        List<FieldMessage> list = new ArrayList<>();
        if(repository.findByEmail(dto.getEmail()) != null){
            list.add(new FieldMessage("Email", "Email already exists in the database"));
            return ResponseEntity.badRequest().body(list);
        }

        String encriptedPassword = new BCryptPasswordEncoder().encode(dto.getPassword());

        User user = new User(dto.getFirstName(), dto.getLastName(), dto.getEmail(), encriptedPassword, dto.getRole());

        repository.save(user);

        return ResponseEntity.ok().build();
    }
}
