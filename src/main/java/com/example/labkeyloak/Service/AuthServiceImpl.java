package com.example.labkeyloak.Service;

import com.example.labkeyloak.Client.Keyloak.iKeycloakAdminClient;
import com.example.labkeyloak.Client.Keyloak.iKeycloakAuthClient;
import com.example.labkeyloak.Config.Keyloak.KeycloakProperties;
import com.example.labkeyloak.Domain.DTO.CreateUserDTO;
import com.example.labkeyloak.Domain.DTO.KeycloakTokenResponse;
import feign.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

import static com.example.labkeyloak.Utils.Mappers.GeneralMappers.createUserDtoToMap;
import static com.example.labkeyloak.Utils.Mappers.GeneralMappers.loginToFormData;
import static com.example.labkeyloak.Utils.UserIdFromKeycloak.getUserIdFromKeycloakResponse;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements iAuthService {
    private final iKeycloakAdminClient keycloakAdminClient;
    private final iKeycloakAuthClient keycloakAuthClient;
    private final KeycloakProperties keycloakProperties;

    @Override
    public KeycloakTokenResponse register(CreateUserDTO user) throws Exception {
        Response response = keycloakAdminClient.createUser(createUserDtoToMap(user));
        if (response.status() != 201) throw new Exception("Failed to create user: " + new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8));
        String userId = getUserIdFromKeycloakResponse(response);
        return login(user.getUserName(), user.getPassword());
    }

    @Override
    public KeycloakTokenResponse login(String username, String password) {
        return keycloakAuthClient.getToken(loginToFormData(username, password, keycloakProperties.getClientId(), keycloakProperties.getClientSecret()));
    }
}