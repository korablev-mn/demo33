package com.example.demo33.service.impl;

import com.example.demo33.dao.TaskRepository;
import com.example.demo33.exeptions.TaskNotFoundException;
import com.example.demo33.model.Task;
import com.example.demo33.service.TaskService;
import com.example.demo33.service.UsserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UsserService usserService;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UsserService usserService) {
        this.taskRepository = taskRepository;
        this.usserService = usserService;
    }

    @Override
    @Transactional
    public Task saveTask(Task task) {
        usserService.findOrInsertUser(task.getUser());
        return taskRepository.save(task);
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public Optional<Task> findById(long id) {
        return taskRepository.findById(id);
    }

    @Override
    public Optional<Task> findByTitle(String title) {
        return taskRepository.findByTitle(title);
    }

    @Override
    @Transactional
    public void deleteByIdAndUserId(long id, long userId) {
        taskRepository.findByIdAndUserId(id, userId)
                .ifPresentOrElse(v-> taskRepository.deleteByIdAndUserId(v.getId(), v.getUser().getId()),
                        () -> {throw new TaskNotFoundException("No match user: " + userId + " task id: " + id);});
    }

    @Override
    @Transactional
    public void deleteByTitleAndUserId(String title, long userId) {
        taskRepository.findByTitleAndUserId(title, userId)
                .ifPresentOrElse(v-> taskRepository.deleteByTitleAndUserId(v.getTitle(), v.getUser().getId()),
                        () -> {throw new TaskNotFoundException("No match user: " + userId + " task title: " + title);});
    }

    @Override
    @Transactional
    public Task update(Task task, long id, long userId) {
        Task updateTask = taskRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new TaskNotFoundException("Task not exist with id: " + id));
        updateTask.setTask(task);
        return taskRepository.save(updateTask);
    }

    @Override
    @Transactional
    public Task update(Task task, String title, long userId) {
        Task updateTask = taskRepository.findByTitleAndUserId(title, userId)
                .orElseThrow(() -> new TaskNotFoundException("Task not exist with title: " + title));
        updateTask.setTask(task);
        return taskRepository.save(updateTask);
    }
}