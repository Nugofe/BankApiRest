package com.api.bankapirest.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emitter_account_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Account emitterAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_account_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Account receiverAccount;

    @NotNull(message = "{valid.transaction.money.NotNull}")
    @DecimalMin(value = "0.1", message = "{valid.transaction.money.DecimalMin}")
    private Double money;

    @NotNull
    @Column(name = "created_at")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Valid
    private Date createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = new Date();
    }

}