package com.bank.userservice.services.user;

import com.bank.library.dtos.requests.UserRequest;
import com.bank.library.exceptions.ApiException;
import com.bank.userservice.models.User;

import java.util.List;

public interface IUserService {
    List<User> findAll() throws ApiException;

    User findById(Long id) throws ApiException;

    User findByNif(String nif) throws ApiException;

    User create(UserRequest userRequest) throws ApiException;

    User update(User userDB, UserRequest userRequest) throws ApiException;

    void delete(Long id);

    Object getUserExamples() throws Throwable;

    //User buildUser(UserRequest userDTO);

    // account client
    /*public ResponseEntity<AccountDTO> getUserAccount(Long userId, Long accountId);

    public ResponseEntity<List<AccountDTO>> getAllUserAccounts(Long userId);*/
}
