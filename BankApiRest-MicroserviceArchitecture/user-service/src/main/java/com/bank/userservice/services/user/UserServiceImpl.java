package com.bank.userservice.services.user;

import com.bank.library.exceptions.ApiException;
import com.bank.library.exceptions.BadRequestException;
import com.bank.library.exceptions.ConflictException;
import com.bank.library.exceptions.NotFoundException;
import com.bank.library.dtos.requests.UserRequest;
import com.bank.userservice.clients.IAccountClient;
import com.bank.userservice.models.User;
import com.bank.userservice.repositories.IUserRepository;
import com.bank.userservice.utils.Utils;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames={"users"})
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;

    private final IAccountClient accountClient;

    @Transactional(readOnly = true)
    @Cacheable()
    public List<User> findAll() throws ApiException {
        List<User> users = userRepository.findAll();
        if(users.isEmpty()) {
            throw new NotFoundException("Users");
        }
        return users;
    }

    @Transactional(readOnly = true)
    @Cacheable(key="{ #root.methodName, #id }")
    public User findById(Long id) throws ApiException {
        User user = userRepository.findById(id).orElse(null);
        if(user == null) {
            throw new NotFoundException("User with id=" + id);
        }
        return user;
    }

    @Transactional(readOnly = true)
    @Cacheable(key="{ #root.methodName, #nif }")
    public User findByNif(String nif) throws ApiException {
        User user = userRepository.findByNif(nif).orElse(null);
        if(user == null) {
            throw new NotFoundException("User of NIF=" + nif);
        }
        return user;
    }

    @Transactional
    @CacheEvict(allEntries=true)
    public User create(UserRequest userRequest) throws ApiException {
        User userDB = userRepository.findByNif(userRequest.getNif()).orElse(null);
        if(userDB != null) {
            throw new ConflictException("User already created");
        }
        return userRepository.save(Utils.mapRequestToUser(userRequest));
    }

    @Transactional
    @CacheEvict(allEntries=true)
    public User update(User userDB, UserRequest userRequest) throws ApiException {
        if(!Objects.equals(userDB.getNif(), userRequest.getNif())) {
            throw new BadRequestException("Field NIF is not modifiable");
        }

        User user = Utils.mapRequestToUser(userRequest);
        user.setId(userDB.getId());
        user.setCreatedAt(userDB.getCreatedAt());

        return userRepository.save(user);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value="users", allEntries=true),
            @CacheEvict(value="login", allEntries=true)
    })
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @CircuitBreaker(name = "UserExamples")
    public Object[] getUserExamples() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject("https://jsonplaceholder.typicode.com/users", Object[].class);
    }

    /*@Override
    public ResponseEntity<AccountDTO> getUserAccount(Long userId, Long accountId) {
        return accountClient.getAccountByUser(accountId, userId);
    }

    @Override
    public ResponseEntity<List<AccountDTO>> getAllUserAccounts(Long userId) {
        return accountClient.getAccounts(userId);
    }*/
}
