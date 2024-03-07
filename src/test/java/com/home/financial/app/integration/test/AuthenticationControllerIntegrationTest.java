package com.home.financial.app.integration.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.financial.app.dto.AuthenticationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static com.home.financial.app.common.util.ConstantApiPath.AUTH_API;
import static com.home.financial.app.common.util.ConstantApiPath.CONTEXT_PATH;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String AUTH_API_PATH = CONTEXT_PATH + AUTH_API;

    @Test
    void givenValidCredentials_whenPerformingLogin_thenReturnJwtToken() throws Exception {
        var req = new AuthenticationRequest("username", "password");

        mockMvc.perform(MockMvcRequestBuilders.post(AUTH_API_PATH+"/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }
/*
    @Test
    void givenInvalidCredentials_whenPerformingLogin_thenReturnUnauthorized() throws Exception {
        String username = "testuser";
        String password = "invalidpassword";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequest(username, password))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void givenAuthenticatedUser_whenPerformingLogout_thenReturnOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/logout")
                        .with(SecurityMockMvcRequestPostProcessors.user("testuser"))
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    void givenUnauthenticatedUser_whenPerformingLogout_thenReturnUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/logout")
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }*/
}
