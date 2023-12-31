package com.api.bankapirest.services.user;

import com.api.bankapirest.dtos.request.RegisterRequest;
import com.api.bankapirest.exceptions.ApiException;
import com.api.bankapirest.exceptions.BadRequestException;
import com.api.bankapirest.exceptions.ConflictException;
import com.api.bankapirest.exceptions.NotFoundException;
import com.api.bankapirest.models.ERole;
import com.api.bankapirest.models.Role;
import com.api.bankapirest.models.User;
import com.api.bankapirest.repositories.IRoleRepository;
import com.api.bankapirest.repositories.IUserRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private final PasswordEncoder passwordEncoder;
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;

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
    public User create(RegisterRequest userRequest) throws ApiException {
        User userDB = userRepository.findByNif(userRequest.getNif()).orElse(null);
        if(userDB != null) {
            throw new ConflictException("User already created");
        }
        return userRepository.save(buildUser(userRequest));
    }

    @Transactional
    @CacheEvict(allEntries=true)
    public User update(User userDB, RegisterRequest userRequest) throws ApiException {
        if(!Objects.equals(userDB.getNif(), userRequest.getNif())) {
            throw new BadRequestException("Field NIF is not modifiable");
        }

        User user = buildUser(userRequest);
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


    public List<Role> buildRoles(List<ERole> rolesTypes) {
        List<Role> roles = new ArrayList<>();
        for (ERole r : rolesTypes) {
            // get the role and if found, add it to the list
            roleRepository.findByRolename(r.getName()).ifPresent(roles::add);
        }
        return roles;
    }

    public User buildUser(RegisterRequest userDTO) {
        // get the roles according to role types requested
        List<Role> roles = buildRoles(userDTO.getRoles());

        // build the user
        return User.builder()
                .nif(userDTO.getNif())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .firstname(userDTO.getFirstname())
                .surname(userDTO.getSurname())
                .roles(roles)
                .build();
    }
}
