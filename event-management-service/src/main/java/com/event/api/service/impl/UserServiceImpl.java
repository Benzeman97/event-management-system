package com.event.api.service.impl;

import com.event.api.entity.User;
import com.event.api.exception.DataNotFoundException;
import com.event.api.repository.UserRepository;
import com.event.api.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    final private static Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Override
    public User getHost(String hostId) {
        return userRepository.findById(UUID.fromString(hostId))
                .orElseThrow(()->{
                    LOGGER.error("User with ID {} not found", hostId);
                    throw new DataNotFoundException("error.data.not.found");
                });
    }
}
