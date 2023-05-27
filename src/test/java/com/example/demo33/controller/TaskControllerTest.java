package com.example.demo33.controller;

import com.example.demo33.dao.TaskRepository;
import com.example.demo33.dao.UsserRepository;
import com.example.demo33.model.Task;
import com.example.demo33.model.Usser;
import com.example.demo33.service.TaskService;
import com.example.demo33.service.UsserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {
    @Autowired
    private TaskController taskController;

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
    void createTask() {

        mvc.perform(post())
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        Task task = new Task(1L, "title", "desk", new Usser(1L, "fio"), null, null);
        ResponseEntity<Task> taskResponse = taskController.createTask(task);


        assertThat(taskResponse.getStatusCodeValue()).isEqualTo(201);
        assertThat(taskResponse.getBody().getTitle()).isEqualTo(task.getTitle());
        assertThat(taskResponse.getBody().getCreated()).isNotNull();
        assertThat(taskResponse.getBody().getUpdated()).isNotNull();
        assertThat(taskResponse.getBody().getCreated().getEpochSecond()).isEqualTo(
                taskResponse.getBody().getUpdated().getEpochSecond()
        );

        Task task2 = new Task(1L, "title", "desk89112584758 des",
                new Usser(1L, "fio"), null, null);
     //   assertThat(taskController.createTask(task2)).info()

    //    assertThat(taskResponse.getStatusCodeValue()).isEqualTo(201);
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
    void find() {
    }

    @Test
    void findByTitle() {
    }

    @Test
    void findAllTask() throws Exception {
        mvc.perform(get("/api/tasks"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}