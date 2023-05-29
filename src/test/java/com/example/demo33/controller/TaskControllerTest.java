package com.example.demo33.controller;
import com.example.demo33.exeptions.BadRequestException;
import com.example.demo33.exeptions.TaskNotFoundException;
import com.example.demo33.model.Task;
import com.example.demo33.model.Usser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.context.WebApplicationContext;
import java.io.IOException;
import java.util.Arrays;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mvc;

    String[] error = new String[]{"Required request header 'user' for method parameter type long is not present"};

    @BeforeEach
    void printApplicationContext() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        Arrays.stream(webApplicationContext.getBeanDefinitionNames())
                .map(name -> webApplicationContext.getBean(name).getClass().getName())
                .sorted()
                .forEach(System.out::println);
    }

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new JavaTimeModule());

    public static <T> T parseResponse(MvcResult result, Class<T> responseClass) {
        try {
            String contentAsString = result.getResponse().getContentAsString();
            return MAPPER.readValue(contentAsString, responseClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createTask() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        mvc.perform(post("/api/create")
                .content(objectMapper.writeValueAsString(new Task(2L, "title2", "xx 89112547896x cc5",
                        new Usser(1L, "fio"), null, null)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertTrue(result.getResolvedException().getMessage().contains("no Russian phones in the description")));

        String[] error = new String[]{"user: must not be null", "title: must not be blank"};
        Task test = new Task();
        mvc.perform(post("/api/create")
                .content(objectMapper.writeValueAsString(test))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.error", hasSize(2)))
                .andExpect(jsonPath("$.error", containsInAnyOrder(error)));

        var bodyContent = mvc.perform(post("/api/create")
                .content(objectMapper.writeValueAsString(new Task(3L, "title3", "desk3",
                        new Usser(1L, "fio"), null, null)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.created").exists())
                .andExpect(jsonPath("$.updated").exists())
                .andExpect(jsonPath("$.title").value("title3"))
                .andExpect(jsonPath("$.description").value("desk3"))
                .andExpect(jsonPath("$.user.id").value(1))
                .andExpect(jsonPath("$.user.fio").value("fio"))
                .andReturn();
        Task taskResponse = parseResponse(bodyContent, Task.class);
        assertEquals(taskResponse.getUpdated().toString(), taskResponse.getCreated().toString());
    }

    @Test
    void update() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        Task test = new Task();
        test.setTitle("title5");
        test.setUser(new Usser(1L, "fio"));
        mvc.perform(post("/api/create")
                .content(objectMapper.writeValueAsString(test))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mvc.perform(put("/api/update/title6")
                .content(objectMapper.writeValueAsString(new Task(3L, "title5", "desk6",
                        new Usser(1L, "fio"), null, null)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MissingRequestHeaderException))
                .andExpect(jsonPath("$.error", hasSize(1)))
                .andExpect(jsonPath("$.error").value(error[0]));
    }

    @Test
    void testUpdate() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        Task test = new Task();
        test.setTitle("title7");
        test.setUser(new Usser(1L, "fio"));
        mvc.perform(post("/api/create")
                .content(objectMapper.writeValueAsString(test))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mvc.perform(put("/api/update/title8")
                .content(objectMapper.writeValueAsString(new Task(3L, "title6", "desk6",
                        new Usser(1L, "fio"), null, null)))
                .header("user", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TaskNotFoundException))
                .andExpect(jsonPath("$.error", hasSize(1)))
                .andExpect(jsonPath("$.error").value("Task not exist with title: title8"));

        var bodyContent = mvc.perform(put("/api/update/title7")
                .content(objectMapper.writeValueAsString(new Task(3L, "title8", "desk3",
                        new Usser(1L, "fio"), null, null)))
                .header("user", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.created").exists())
                .andExpect(jsonPath("$.updated").exists())
                .andExpect(jsonPath("$.title").value("title8"))
                .andExpect(jsonPath("$.description").value("desk3"))
                .andReturn();
        Task taskResponse = parseResponse(bodyContent, Task.class);
        assertTrue(taskResponse.getUpdated().isAfter(taskResponse.getCreated()));
    }

    @Test
    void delete() throws Exception {

        mvc.perform(MockMvcRequestBuilders
                .delete("/api/delete?id=101")
                .header("user", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestException))
                .andExpect(jsonPath("$.error", hasSize(1)))
                .andExpect(jsonPath("$.error").value("No match user: 1 task id: 101"));

        mvc.perform(MockMvcRequestBuilders
                .delete("/api/delete?id=101")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MissingRequestHeaderException))
                .andExpect(jsonPath("$.error", hasSize(1)))
                .andExpect(jsonPath("$.error").value(error[0]));
    }

    @Test
    void testDelete() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Task test = new Task();
        test.setTitle("title10");
        test.setUser(new Usser(1L, "fio"));
        mvc.perform(post("/api/create")
                .content(objectMapper.writeValueAsString(test))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mvc.perform(MockMvcRequestBuilders
                .delete("/api/delete/title10")
                .header("user", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value("Task title: 'title10' deleted by user: 1"));
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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.description").value("desk"))
                .andExpect(jsonPath("$.user.id").value(1))
                .andExpect(jsonPath("$.user.fio").value("fio"));
    }

    @Test
    void findByTitle() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/task/noTask")
                .contentType(MediaType.APPLICATION_JSON);
        mvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TaskNotFoundException))
                .andExpect(result -> assertEquals("No Task by title: 'noTask'", result.getResolvedException()
                        .getMessage()));
    }

    @Test
    void findAllTask() throws Exception {
        mvc.perform(get("/api/tasks"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}