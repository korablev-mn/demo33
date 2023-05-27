package com.example.demo33.dao;

import com.example.demo33.model.Usser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UsserRepository extends JpaRepository<Usser, Long>, JpaSpecificationExecutor<Usser> {

    @Override
    <S extends Usser> S saveAndFlush(S entity);

    @Override
    <S extends Usser> S save(S entity);

    @Override
    boolean existsById(Long id);
}