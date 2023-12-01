package com.bank.userservice.utils;

import com.bank.library.dtos.responses.UserResponse;
import com.bank.library.models.ERole;
import com.bank.userservice.models.Role;
import com.bank.userservice.models.User;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class Utils {

    public static List<ERole> getERoles(List<String> stringRoles) {
        List<ERole> eRoles = new ArrayList<>();

        for (String s : stringRoles) {
            for (ERole e : ERole.values()) {
                if(s.equals(e.name())) {
                    eRoles.add(e);
                }
            }
        }

        return eRoles;
    }

    public static UserResponse mapUserToResponse(User user) {
        List<String> stringRoles = user.getRoles().stream().map(Role::getRolename).toList();

        return UserResponse.builder()
                .id(user.getId())
                .nif(user.getNif())
                .firstname(user.getFirstname())
                .surname(user.getSurname())
                .createdAt(user.getCreatedAt())
                .roles(Utils.getERoles(stringRoles))
                .build();
    }

    public static List<UserResponse> mapUserListToResponse(List<User> list) {
        List<UserResponse> dtos = new ArrayList<>();
        for(User elem : list) {
            dtos.add(Utils.mapUserToResponse(elem));
        }
        return dtos;
    }

}
