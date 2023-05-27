package com.example.demo33.dao;

import com.example.demo33.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    List<Task> findAll();

    @Transactional
    <S extends Task> S save(S entity);

    @Override
    <S extends Task> S saveAndFlush(S entity);

    Optional<Task> findByTitle(String id);

    void deleteByIdAndUserId(long id, long userId);

    void deleteByTitleAndUserId(String title, long userId);

    Optional<Task> findByTitleAndUserId(String title, long userId);

    Optional<Task> findByIdAndUserId(long id, long userId);
}