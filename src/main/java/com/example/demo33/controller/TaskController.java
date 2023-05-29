package com.example.demo33.controller;

import com.example.demo33.exeptions.BadRequestException;
import com.example.demo33.exeptions.TaskNotFoundException;
import com.example.demo33.model.Task;
import com.example.demo33.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody @Valid Task task) {
        return new ResponseEntity<>(taskService.saveTask(task), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Task> update(
            @RequestHeader("user") long userId,
            @RequestParam("id") long id,
            @Valid @RequestBody Task task) {
        return new ResponseEntity<>(taskService.update(task, id, userId), HttpStatus.OK);
    }

    @PutMapping("/update/{title}")
    public ResponseEntity<Task> update(
            @RequestHeader("user") long userId,
            @PathVariable String title,
            @Valid @RequestBody Task task) {
        return new ResponseEntity<>(taskService.update(task, title, userId), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{title}")
    public ResponseEntity<String> delete(@RequestHeader("user") long userId,
                                         @PathVariable String title) {
        try {
            taskService.deleteByTitleAndUserId(title, userId);
        } catch (RuntimeException e) {
            throw new BadRequestException(e.getMessage());
        }
        return new ResponseEntity<>("Task title: '" + title + "' deleted by user: " + userId, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestHeader("user") long userId,
                                         @RequestParam("id") long id) {
        try {
            taskService.deleteByIdAndUserId(id, userId);
        } catch (RuntimeException e) {
            throw new BadRequestException(e.getMessage());
        }
        return new ResponseEntity<>("Task id: '" + id + "' deleted by user: " + userId, HttpStatus.OK);
    }

    @GetMapping("/task")
    public Task find(@RequestParam("id") long id) {
        return taskService.findById(id).orElseThrow(() ->
                new TaskNotFoundException("No Task by ID: '" + id + "'")
        );
    }

    @GetMapping("/task/{title}")
    public Task findByTitle(@PathVariable String title) {
        return taskService.findByTitle(title).orElseThrow(() ->
                new TaskNotFoundException("No Task by title: '" + title +"'")
        );
    }

    @GetMapping("/tasks")
    public List<Task> findAllTask() {
        return taskService.findAll();
    }
}