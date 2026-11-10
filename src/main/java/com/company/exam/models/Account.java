package com.company.exam.models;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
@Getter
@Setter
public class Account {

    @Id
    @GeneratedValue(generator = "unique-8-digit-generator")
    @GenericGenerator(
            name = "unique-8-digit-generator",
            strategy = "com.company.exam.utils.UniqueIdGenerator"
    )
    @Column(name = "accountNumber")
    private  Long accountNumber;
    @NotNull(message = "Select valid account type")
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    private BigDecimal availableBalance;

    @Getter
    @AllArgsConstructor
    public enum AccountType {
        S("SAVINGS"),
        C("CHECKING");

        private final String value;
    }
}
