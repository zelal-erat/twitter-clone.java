package com.twitter.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitter.api.dto.TweetRequest;
import com.twitter.api.dto.TweetResponse;
import com.twitter.api.service.TweetService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TweetController.class)
class TweetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TweetService tweetService;

    @Autowired
    private ObjectMapper objectMapper;

    private TweetResponse mockTweetResponse() {
        return TweetResponse.builder()
                .id(1L)
                .content("Hello Twitter!")
                .userId(10L)
                .username("testuser")
                .createdAt(LocalDateTime.now())
                .likesCount(5)
                .commentsCount(2)
                .retweetsCount(1)
                .build();
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("POST /tweet - create tweet")
    void testCreateTweet() throws Exception {
        TweetRequest request = new TweetRequest("Hello Twitter!");
        TweetResponse response = mockTweetResponse();
        Mockito.when(tweetService.createTweet(Mockito.any(), eq("testuser")))
                .thenReturn(response);

        mockMvc.perform(post("/tweet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("Hello Twitter!"))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.likesCount").value(5));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /tweet/findByUserId - list tweets by user ID")
    void testFindByUserId() throws Exception {
        TweetResponse response = mockTweetResponse();

        Mockito.when(tweetService.findByUserId(10L))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/tweet/findByUserId")
                        .param("userId", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("Hello Twitter!"))
                .andExpect(jsonPath("$[0].userId").value(10L));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /tweet/findById - find tweet by ID")
    void testFindById() throws Exception {
        TweetResponse response = mockTweetResponse();

        Mockito.when(tweetService.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/tweet/findById")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("Hello Twitter!"));
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("PUT /tweet/{id} - update tweet")
    void testUpdateTweet() throws Exception {
        TweetRequest request = new TweetRequest("Updated Tweet");
        TweetResponse response = mockTweetResponse();
        response.setContent("Updated Tweet");

        Mockito.when(tweetService.updateTweet(eq(1L), Mockito.any(), eq("testuser")))

                .thenReturn(response);

        mockMvc.perform(put("/tweet/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Updated Tweet"));
    }


    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("DELETE /tweet/{id} - delete tweet")
    void testDeleteTweet() throws Exception {
        Mockito.doNothing().when(tweetService).deleteTweet(1L, "testuser");

        mockMvc.perform(delete("/tweet/1").with(csrf())) // 👈 CSRF token burada!
                .andExpect(status().isNoContent());
    }
}
