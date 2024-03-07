package com.home.financial.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.financial.app.domain.Role;
import com.home.financial.app.dto.ChangePasswordRequest;
import com.home.financial.app.dto.UserCreatedRequest;
import com.home.financial.app.dto.UserDto;
import com.home.financial.app.dto.UserUpdateRequest;
import com.home.financial.app.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.*;
import java.util.stream.IntStream;

import static com.home.financial.app.common.util.ConstantApiPath.CONTEXT_PATH;
import static com.home.financial.app.common.util.ConstantApiPath.USER_API;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
* we can make these tests run without enabling security by adding 2 annotations following
* @SpringBootTest(classes="ApplicationNoSecurity.class")
* @AutoConfigureMockMvc
*
* */
@WebMvcTest(
        value = UserController.class

        // this disables loading up the WebSecurityConfig.java file, otherwise it fails on start up
        , useDefaultFilters = false

        // this one indicates the specific filter to be used, in this case
        // related to the UserController we want to test
        , includeFilters = {
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                value = UserController.class
        )
}
)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerUnitTests {
    private final String USER_API_PATH = CONTEXT_PATH + USER_API;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDto userDto;
    private static final List<UserDto> PAGE_1_CONTENT = buildUsers(6, 0);
    private static final List<UserDto> PAGE_2_CONTENT = buildUsers(6, 6);
    private static final List<UserDto> PAGE_3_CONTENT = buildUsers(3, 12);
    private static final List<UserDto> PAGE_DEFAULT_CONTENT = buildUsers(10, 0);

    private static final List<UserDto> ALL_USERS = buildUsers(15, 0);

    private static Collection<Object[]> parametersTest() {
        return Arrays.asList(new Object[][] {
                {true, 0, 10, PAGE_DEFAULT_CONTENT, 15, 2},
                {false, 0, 6, PAGE_1_CONTENT, 15, 3},
                {false, 1, 6, PAGE_2_CONTENT, 15, 3},
                {false, 2, 6, PAGE_3_CONTENT, 15, 3}
        });
    }

    @BeforeEach
    public void setup() {
        userDto = buildUsers(1,0).get(0);
    }

    @ParameterizedTest
    @MethodSource("parametersTest")
    public void testGetAllUsers_WithPagingInformation(
            boolean isDefault,
            int pageNum,
            int pageSize,
            List<UserDto> pageContent,
            int totalElements,
            int totalPages
    ) throws Exception {
        if (isDefault) {
            pageNum = 0;
            pageSize = 10;
        }
        Page<UserDto> userPage = new PageImpl<>(
                pageContent,
                PageRequest.of(pageNum, pageSize),
                ALL_USERS.size()
        );
        when(userService.getAll(pageNum, pageSize)).thenReturn(userPage);
        MockHttpServletRequestBuilder reqBuilder = get(USER_API_PATH);
        if (!isDefault) {
            reqBuilder.queryParam("pageNum", String.valueOf(pageNum))
                    .queryParam("pageSize", String.valueOf(pageSize));
        }
        mockMvc.perform(reqBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(pageContent.size())))
                .andExpect(jsonPath("$.totalPages", is(null)))
                .andExpect(jsonPath("$.totalElements", is(totalElements)));
    }

    @Test
    public void testGetUserById() throws Exception {
        when(userService.getUser(userDto.username())).thenReturn(userDto);
        mockMvc.perform(get(USER_API_PATH+"/{username}", userDto.username()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.username", is(userDto.username())))
                .andExpect(jsonPath("$.email", is(userDto.email())));
    }

    @Test
    public void testCreateUser() throws Exception {
        UserCreatedRequest userCreate = UserCreatedRequest
                .builder()
                .email(userDto.email())
                .firstname(userDto.firstname())
                .lastname(userDto.lastname())
                .username(userDto.username())
                .role(userDto.role())
                .build();
        when(userService.create(userCreate)).thenReturn(userDto);
        mockMvc.perform(
                        post(USER_API_PATH)
                                .content(objectMapper.writeValueAsString(userCreate))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is(userDto.username())))
                .andExpect(jsonPath("$.role", is(userDto.role().name())))
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    public void testUpdateUser() throws Exception {
        UserUpdateRequest userUpdate = UserUpdateRequest
                .builder()
                .firstname("John")
                .lastname("Smith")
                .build();
        doNothing().when(userService).updateUser(userDto.username(), userUpdate);
        mockMvc.perform(
                        put(USER_API_PATH+"/{username}", userDto.username())
                                .content(objectMapper.writeValueAsString(userUpdate))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(userDto.username());
        mockMvc.perform(delete(USER_API_PATH+"/{username}", userDto.username()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testChangePassword() throws Exception {
        var req = ChangePasswordRequest
                .builder()
                .oldPassword("oldPassword")
                .newPassword("newPassword")
                .build();
        doNothing().when(userService).changePassword(req);
        mockMvc.perform(
                    post(USER_API_PATH+"/changePassword", userDto.username())
                            .content(objectMapper.writeValueAsString(req))
                            .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
    }

    private static List<UserDto> buildUsers(int numberOfElements, int offset) {
        List<UserDto> users = new ArrayList<>();
        IntStream.range(0, numberOfElements).forEach( i -> {
            UserDto user = new UserDto(
                    String.format("bella.dev%s", i + offset),
                    String.format("Bella %d", i + offset),
                    String.format("Dev %d", i + offset),
                    String.format("bella.dev%s@gmail.com", i + offset),
                    Role.USER
                    );
            users.add(user);
        });
        return users;
    }
}
