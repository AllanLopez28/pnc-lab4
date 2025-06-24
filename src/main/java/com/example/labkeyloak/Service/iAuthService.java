package com.example.labkeyloak.Service;

import com.example.labkeyloak.Domain.DTO.CreateUserDTO;
import com.example.labkeyloak.Domain.DTO.KeycloakTokenResponse;

public interface iAuthService {

    KeycloakTokenResponse register(CreateUserDTO user) throws Exception;
    KeycloakTokenResponse login(String username, String password);
}