package com.example.smart.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.smart.dto.UserDTO;
import com.example.smart.entity.Phone;
import com.example.smart.entity.Users;
import com.example.smart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Value("${app.email-regex}")
    private String emailRegex;

    @Value("${app.password-regex}")
    private String passwordRegex;

    @Override
    public Users registerUser(UserDTO userDTO) {
        if (!isValidEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("El correo no tiene un formato válido");
        }

        if (!isValidPassword(userDTO.getPassword())) {
            throw new IllegalArgumentException("La contraseña no cumple los requisitos");
        }

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        Users users = new Users();
        users.setId(UUID.randomUUID());
        users.setName(userDTO.getName());
        users.setEmail(userDTO.getEmail());
        users.setPassword(userDTO.getPassword());
        users.setPhones(userDTO.getPhones().stream().map(phoneDTO -> {
            Phone phone = new Phone();
            phone.setNumber(phoneDTO.getNumber());
            phone.setCitycode(phoneDTO.getCitycode());
            phone.setContrycode(phoneDTO.getContrycode());
            return phone;
        }).collect(Collectors.toList()));

        LocalDateTime now = LocalDateTime.now();
        users.setCreated(now);
        users.setModified(now);
        users.setLastLogin(now);
        users.setActive(true);

        String token = generateToken(users);
        users.setToken(token);

        return userRepository.save(users);
    }

    private boolean isValidEmail(String email) {
        return Pattern.compile(emailRegex).matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return Pattern.compile(passwordRegex).matcher(password).matches();
    }

    private String generateToken(Users user) {
        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("id", user.getId().toString())
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC256(jwtSecret));
    }
}
