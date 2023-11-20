package com.api.bankapirest.services.user;

import com.api.bankapirest.dtos.request.RegisterRequest;
import com.api.bankapirest.models.ERole;
import com.api.bankapirest.models.Role;
import com.api.bankapirest.models.User;
import com.api.bankapirest.repositories.IRoleRepository;
import com.api.bankapirest.repositories.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames={"users"})
public class UserServiceImpl implements IUserService {

    private final PasswordEncoder passwordEncoder;
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;

    @Transactional(readOnly = true)
    @Cacheable()
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Cacheable(key="{ #root.methodName, #id }")
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    @Cacheable(key="{ #root.methodName, #nif }")
    public User findByNif(String nif) {
        return userRepository.findByNif(nif).orElse(null);
    }

    @Transactional
    @CacheEvict(allEntries=true)
    public void save(User user) {
        userRepository.save(user);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value="users", allEntries=true),
            @CacheEvict(value="login", allEntries=true)
    })
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public User buildUser(RegisterRequest userDTO) {
        // get the roles according to role types requested
        List<Role> roles = new ArrayList<>();
        for (ERole r : userDTO.getRoles()) {
            // get the role and if found, add it to the list
            roleRepository.findByRolename(r.getName()).ifPresent(roles::add);
        }

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
