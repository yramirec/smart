package com.example.smart.service;

import com.example.smart.dto.UserDTO;
import com.example.smart.entity.Users;

public interface UserService {

    public Users registerUser(UserDTO userDTO);
}
