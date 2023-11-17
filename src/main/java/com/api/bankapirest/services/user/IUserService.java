package com.api.bankapirest.services.user;

import com.api.bankapirest.dtos.request.RegisterRequest;
import com.api.bankapirest.models.User;

import java.util.List;

public interface IUserService {
    public List<User> findAll();

    public User findById(Long id);

    public User findByNif(String nif);

    public void save(User user);

    public void delete(Long id);

    public User buildUser(RegisterRequest userDTO);
}
