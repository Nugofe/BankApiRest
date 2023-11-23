package com.api.bankapirest.services.user;

import com.api.bankapirest.dtos.request.RegisterRequest;
import com.api.bankapirest.exceptions.ApiException;
import com.api.bankapirest.models.User;

import java.util.List;

public interface IUserService {
    public List<User> findAll() throws ApiException;

    public User findById(Long id) throws ApiException;

    public User findByNif(String nif) throws ApiException;

    public User create(RegisterRequest userRequest) throws ApiException;

    public User update(User userDB, RegisterRequest userRequest) throws ApiException;

    public void delete(Long id);

    public Object getUserExamples() throws Throwable;

    public User buildUser(RegisterRequest userDTO);
}
