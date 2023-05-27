package com.example.demo33.service;

import com.example.demo33.model.Task;
import java.util.List;
import java.util.Optional;

public interface TaskService {

    Task saveTask(Task task);

    List<Task> findAll();

    Optional<Task> findById(long id);

    Optional<Task> findByTitle(String title);

    void deleteByIdAndUserId(long id, long userId);

    void deleteByTitleAndUserId(String title, long userId);

    Task update(Task task, long id, long userId);

    Task update(Task task, String title, long userId);
}