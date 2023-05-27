package com.example.demo33.controller;
import com.example.demo33.exeptions.TaskNotFoundException;
import com.example.demo33.model.Task;
import com.example.demo33.model.Usser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.WebApplicationContext;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mvc;

    @BeforeEach
    void printApplicationContext() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        Arrays.stream(webApplicationContext.getBeanDefinitionNames())
                .map(name -> webApplicationContext.getBean(name).getClass().getName())
                .sorted()
                .forEach(System.out::println);
    }

    @Test
    void createTask() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        mvc.perform(post("/api/create")
                .content(objectMapper.writeValueAsString(new Task(3L, "title3", "desk3",
                        new Usser(1L, "fio"), null, null)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.created").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.updated").exists());

        mvc.perform(post("/api/create")
                .content(objectMapper.writeValueAsString(new Task(2L, "title2", "xx 89112547896x cc5",
                        new Usser(1L, "fio"), null, null)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertTrue(result.getResolvedException().getMessage().contains("no Russian phones in the description")));
    }

    @Test
    void update() {
    }

    @Test
    void testUpdate() {
    }

    @Test
    void delete() {
    }

    @Test
    void testDelete() {
    }

    @Test
    void find() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        mvc.perform(post("/api/create")
                .content(objectMapper.writeValueAsString(new Task(1L, "title", "desk",
                        new Usser(1L, "fio"), null, null)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/task?id=1")
                .contentType(MediaType.APPLICATION_JSON);
        mvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void findByTitle() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/task/noTask")
                .contentType(MediaType.APPLICATION_JSON);
        mvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TaskNotFoundException))
                .andExpect(result -> assertEquals("No Task by title: noTask", result.getResolvedException()
                        .getMessage()));
    }

    @Test
    void findAllTask() throws Exception {
        mvc.perform(get("/api/tasks"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}