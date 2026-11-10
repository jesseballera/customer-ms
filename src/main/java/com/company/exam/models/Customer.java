package com.company.exam.models;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(generator = "unique-8-digit-generator")
    @GenericGenerator(
            name = "unique-8-digit-generator",
            strategy = "com.company.exam.utils.UniqueIdGenerator"
    )
    @Column(name = "customerNumber")
    private Long customerNumber;
    @NotBlank
    private String customerName;
    @NotBlank
    private String customerMobile;
    @NotBlank
    @Email
    private String customerEmail;
    @NotBlank
    private String address1;
    private String address2;
    @OneToOne(cascade = CascadeType.ALL)
    @NotNull(message = "Savings account information is required")
    private Account savings;

}
