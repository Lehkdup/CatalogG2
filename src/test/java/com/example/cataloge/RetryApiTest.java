package com.example.cataloge;

import com.example.cataloge.service.RetryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RetryApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RetryService retryService;

    @Test
    void contextLoads() {
    }

    @Test
    public void testRetryMechanism() throws Exception {
        when(retryService.callRetryApi())
                .thenThrow(new RuntimeException("First attempt failed"))
                .thenThrow(new RuntimeException("Second attempt failed"))
                .thenReturn("retryApi called");

        this.mockMvc.perform(get("/retry")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("retryApi called")));

        verify(retryService, times(3)).callRetryApi();
    }

    @Test
    public void testFallbackAfterRetries() throws Exception {
        when(retryService.callRetryApi()).thenThrow(new RuntimeException("All attempts failed"));

        mockMvc.perform(get("/retry"))
                .andExpect(status().isOk())
                .andExpect(content().string("retries didn't work"));

        verify(retryService, times(3)).callRetryApi();
    }
}

