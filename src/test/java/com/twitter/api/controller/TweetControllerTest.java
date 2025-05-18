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
// Sadece TweetController sınıfını test etmek için Spring MVC testi başlatır, Spring Security desteği de dahil
class TweetControllerTest {

    @Autowired
    private MockMvc mockMvc;
    // Controller endpointlerini HTTP isteği gibi test etmek için kullanılır

    @MockBean
    private TweetService tweetService;
    // TweetService servisini mocklar, gerçek servis çağrılmaz

    @Autowired
    private ObjectMapper objectMapper;
    // Java nesnelerini JSON'a çevirmek veya JSON'dan Java nesnesi oluşturmak için kullanılır

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
    // Testlerde kullanılacak örnek TweetResponse nesnesi döner

    @Test
    @WithMockUser(username = "testuser")
    // Test sırasında "testuser" isimli kullanıcı ile oturum açılmış gibi davranır (Spring Security)
    @DisplayName("POST /tweet - create tweet")
    void testCreateTweet() throws Exception {
        TweetRequest request = new TweetRequest("Hello Twitter!");
        // Gönderilecek tweet içeriği oluşturulur
        TweetResponse response = mockTweetResponse();
        // Mock servisten dönmesi beklenen cevap hazırlanır
        Mockito.when(tweetService.createTweet(Mockito.any(), eq("testuser")))
                .thenReturn(response);
        // Servisin createTweet metodu çağrıldığında mock response dönecek şekilde ayarlandı

        mockMvc.perform(post("/tweet")  // POST isteği gönderiliyor
                        .contentType(MediaType.APPLICATION_JSON)  // İçerik tipi JSON
                        .content(objectMapper.writeValueAsString(request))  // JSON body olarak request nesnesi
                        .with(csrf()))  // CSRF token ekleniyor (Spring Security için gerekli)
                .andExpect(status().isCreated())  // HTTP 201 beklenir
                .andExpect(jsonPath("$.id").value(1L))  // JSON yanıtındaki id alanı 1 olmalı
                .andExpect(jsonPath("$.content").value("Hello Twitter!"))  // content doğru mu kontrolü
                .andExpect(jsonPath("$.username").value("testuser"))  // username kontrolü
                .andExpect(jsonPath("$.likesCount").value(5));  // likesCount kontrolü
    }

    @Test
    @WithMockUser
    @DisplayName("GET /tweet/findByUserId - list tweets by user ID")
    void testFindByUserId() throws Exception {
        TweetResponse response = mockTweetResponse();

        Mockito.when(tweetService.findByUserId(10L))
                .thenReturn(List.of(response));
        // Belirli userId ile tweet servisi çağrıldığında mock tweet listesi döner

        mockMvc.perform(get("/tweet/findByUserId")  // GET isteği
                        .param("userId", "10"))  // userId parametresi verildi
                .andExpect(status().isOk())  // 200 OK beklenir
                .andExpect(jsonPath("$[0].content").value("Hello Twitter!"))  // Dönen listenin ilk tweetinin içeriği kontrol edilir
                .andExpect(jsonPath("$[0].userId").value(10L));  // userId doğrulaması
    }

    @Test
    @WithMockUser
    @DisplayName("GET /tweet/findById - find tweet by ID")
    void testFindById() throws Exception {
        TweetResponse response = mockTweetResponse();

        Mockito.when(tweetService.findById(1L)).thenReturn(response);
        // id ile tweet servisi çağrıldığında mock tweet döner

        mockMvc.perform(get("/tweet/findById")
                        .param("id", "1"))  // id parametresiyle GET isteği
                .andExpect(status().isOk())  // 200 OK beklenir
                .andExpect(jsonPath("$.id").value(1L))  // id kontrolü
                .andExpect(jsonPath("$.content").value("Hello Twitter!"));  // içerik kontrolü
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("PUT /tweet/{id} - update tweet")
    void testUpdateTweet() throws Exception {
        TweetRequest request = new TweetRequest("Updated Tweet");
        // Güncelleme için yeni tweet içeriği
        TweetResponse response = mockTweetResponse();
        response.setContent("Updated Tweet");
        // Mock tweet cevabının içeriği güncellendi

        Mockito.when(tweetService.updateTweet(eq(1L), Mockito.any(), eq("testuser")))
                .thenReturn(response);
        // updateTweet çağrıldığında güncellenmiş mock response dönsün

        mockMvc.perform(put("/tweet/1")  // PUT isteği, path parametre 1
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))  // JSON içerik
                        .with(csrf()))
                .andExpect(status().isOk())  // 200 OK beklenir
                .andExpect(jsonPath("$.content").value("Updated Tweet"));  // güncellenmiş içerik doğrulanır
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("DELETE /tweet/{id} - delete tweet")
    void testDeleteTweet() throws Exception {
        Mockito.doNothing().when(tweetService).deleteTweet(1L, "testuser");
        // Servisin deleteTweet metodu çağrıldığında hiçbir şey yapmaz (mock)

        mockMvc.perform(delete("/tweet/1").with(csrf())) // DELETE isteği, CSRF token ekleniyor
                .andExpect(status().isNoContent());  // 204 No Content beklenir (silme başarılı)
    }
}
