package com.bank.userservice.utils;

import com.bank.library.dtos.requests.UserRequest;
import com.bank.library.dtos.responses.UserResponse;
import com.bank.library.models.ERole;
import com.bank.userservice.models.User;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class Utils {

    public static UserResponse mapUserToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .nif(user.getNif())
                .firstname(user.getFirstname())
                .surname(user.getSurname())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public static List<UserResponse> mapUserListToResponse(List<User> list) {
        List<UserResponse> dtos = new ArrayList<>();
        for(User elem : list) {
            dtos.add(Utils.mapUserToResponse(elem));
        }
        return dtos;
    }

    public static User mapRequestToUser(UserRequest user) {
        return User.builder()
                .nif(user.getNif())
                .firstname(user.getFirstname())
                .surname(user.getSurname())
                .build();
    }

}
