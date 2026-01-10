package com.event.api.service.impl;

import com.event.api.entity.User;
import com.event.api.exception.DataNotFoundException;
import com.event.api.repository.UserRepository;
import com.event.api.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    final private static Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserMapper mapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper mapper){
        this.userRepository=userRepository;
        this.mapper = mapper;
    }

    @Override
    @Cacheable(key = "{#hostId,#root.methodName}",value = "EVENTS")
    public User getHost(String hostId) {
        return userRepository.findById(UUID.fromString(hostId))
                .orElseThrow(()->{
                    LOGGER.error("User with ID {} not found", hostId);
                    throw new DataNotFoundException("error.data.not.found");
                });
    }

    @Override
    @Transactional
    public UserResponse updateUser(UpdateUserRequest request) {

        User user = repository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        mapper.updateEntity(request, user);

        // JPA dirty checking will update automatically (You don’t have to call save() manually for updates in this case)
        // Loaded entity from repository + @Transactional  → JPA dirty checking handles UPDATE automatically
        
        return mapper.toResponse(user);
    }
}
