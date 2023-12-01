package com.bank.userservice.services.user;

import com.bank.userservice.dtos.request.UserRequestDTO;
//import com.bank.userservice.exceptions.ApiException;
import com.bank.userservice.dtos.response.AccountDTO;
import com.bank.userservice.models.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IUserService {
    public List<User> findAll() /*throws ApiException*/;

    public User findById(Long id) /*throws ApiException*/;

    public User findByNif(String nif) /*throws ApiException*/;

    public User create(UserRequestDTO userRequest) /*throws ApiException*/;

    public User update(User userDB, UserRequestDTO userRequest) /*throws ApiException*/;

    public void delete(Long id);

    public Object getUserExamples() throws Throwable;

    public User buildUser(UserRequestDTO userDTO);

    // account client
    /*public ResponseEntity<AccountDTO> getUserAccount(Long userId, Long accountId);

    public ResponseEntity<List<AccountDTO>> getAllUserAccounts(Long userId);*/
}
