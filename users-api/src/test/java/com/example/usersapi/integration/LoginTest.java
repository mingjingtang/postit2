package com.example.usersapi.integration;

import com.example.usersapi.model.User;
import com.example.usersapi.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoginTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

//    @InjectMocks
//    private SignupTest signupTest;

    @Before
    public void init(){
//        signupTest.Signup_User_Success();
    }

    @After
    public void clear(){
        User user = userRepository.findByUsername("user1");
        if(userRepository.findByUsername("user1") != null) {
            userRepository.delete(user);
        }
    }


    @Test
    public void Login_User_Success() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" + "\"email\":\"admin@email\"," + "\"password\":\"admin\"" + "}");

        MvcResult token = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        assertNotNull(token);
        System.out.println(token.getResponse().getContentAsString());
    }


    @Test
    public void Login_validator_EmailOrPassword() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" + "\"email\":\"email1email.com\"," + "\"password\":\"pwd1\"" + "}");

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().is4xxClientError())
                .andReturn();

        System.out.println(result.getResolvedException().getMessage());
    }

}
