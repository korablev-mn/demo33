package com.example.demo33.service.impl;

import com.example.demo33.dao.UsserRepository;
import com.example.demo33.model.Usser;
import com.example.demo33.service.UsserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsserServiceImpl implements UsserService {

    private final UsserRepository usserRepository;

    @Autowired
    public UsserServiceImpl(UsserRepository usserRepository) {
        this.usserRepository = usserRepository;
    }

    @Override
    public void findOrInsertUser(Usser user) {
        if(!usserRepository.existsById(user.getId())){
            usserRepository.saveAndFlush(user);
        }
    }
}