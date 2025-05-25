package backend.assignment.controller;

import backend.assignment.controller.WordController;
import backend.assignment.exception.ResourceNotFoundException;
import backend.assignment.model.Word;
import backend.assignment.service.WordService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WordController.class)
class WordControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private WordService service;

    @Autowired
    private ObjectMapper mapper;

    private Word word;

    @BeforeEach
    void setUp() {
        word = new Word(1L, "Test", "Def");
    }

    @Test
    void getAll_returnsOk() throws Exception {
        when(service.getAll()).thenReturn(List.of(word));
        mvc.perform(get("/words"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].name").value("Test"));
    }

    @Test
    void getOne_found_returnsOk() throws Exception {
        when(service.getById(1L)).thenReturn(word);
        mvc.perform(get("/words/1"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.definition").value("Def"));
    }

    @Test
    void getOne_notFound_returns404() throws Exception {
        when(service.getById(2L)).thenThrow(new ResourceNotFoundException("X"));
        mvc.perform(get("/words/2"))
           .andExpect(status().isNotFound());
    }

    @Test
    void create_valid_returnsCreated() throws Exception {
        when(service.create(any())).thenReturn(word);
        mvc.perform(post("/words")
               .contentType(MediaType.APPLICATION_JSON)
               .content(mapper.writeValueAsString(word)))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void update_valid_returnsOk() throws Exception {
        Word updated = new Word(1L, "New", "NewDef");
        when(service.update(eq(1L), any())).thenReturn(updated);
        mvc.perform(put("/words/1")
               .contentType(MediaType.APPLICATION_JSON)
               .content(mapper.writeValueAsString(updated)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.name").value("New"));
    }

    @Test
    void update_notFound_returns404() throws Exception {
        when(service.update(eq(3L), any()))
           .thenThrow(new ResourceNotFoundException("X"));
        mvc.perform(put("/words/3")
               .contentType(MediaType.APPLICATION_JSON)
               .content(mapper.writeValueAsString(word)))
           .andExpect(status().isNotFound());
    }

    @Test
    void delete_valid_returnsNoContent() throws Exception {
        mvc.perform(delete("/words/1"))
           .andExpect(status().isNoContent());
    }

    @Test
    void delete_notFound_returns404() throws Exception {
        doThrow(new ResourceNotFoundException("X")).when(service).delete(5L);
        mvc.perform(delete("/words/5"))
           .andExpect(status().isNotFound());
    }
}