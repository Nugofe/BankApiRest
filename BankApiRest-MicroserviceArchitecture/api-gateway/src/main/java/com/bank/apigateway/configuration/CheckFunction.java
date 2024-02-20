package com.bank.apigateway.configuration;

import com.bank.library.models.ERole;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CheckFunction {

    public interface Preauthorize {
        Boolean test(String role);
    }

    public static Preauthorize AdminRole = (r) ->
            Objects.equals(r, ERole.ADMIN.getName());
    public static Preauthorize UserRole = (r) ->
            Objects.equals(r, ERole.USER.getName());
    public static Preauthorize AdminOrUserRole = (r) ->
            Objects.equals(r, ERole.ADMIN.getName()) ||
            Objects.equals(r, ERole.USER.getName());


    public Preauthorize checkType;

    public CheckFunction(Preauthorize checkType) {
        this.checkType = checkType;
    }

    public boolean perform(HttpHeaders headers) {
        if (headers == null || !headers.containsKey("roles")) {
            return false;
        }

        String rolesString = headers.getFirst("roles");

        for(String r : convertStringToList(rolesString)) {
            if(checkType.test(r)) {
                return true;
            }
        }
        return false;
    }

    public static List<String> convertStringToList(String listAsString) {
        // delete brackets [] and spaces, get the elements that will be
        // delimited by commas and store them in an array

        if(listAsString == null) {
            return new ArrayList<>();
        }

        return Arrays.stream(
                        listAsString.replaceAll("[\\[\\] ]", "").split(",")
                )
                .collect(Collectors.toList());
    }
}