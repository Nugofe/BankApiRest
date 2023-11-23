package com.api.bankapirest.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data // creates getters and setters
@NoArgsConstructor
@Entity
@Table(name = "roles")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rolename;
}