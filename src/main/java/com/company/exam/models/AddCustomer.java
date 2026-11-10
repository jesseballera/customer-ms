package com.company.exam.models;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Setter
@Getter
public class AddCustomer {

    @NotBlank(message = "Customer name is required.")
    private String customerName;
    @NotBlank(message = "Customer mobile is required")
    private String customerMobile;
    @NotBlank(message = "Customer email is required")
    @Email(message = "Enter valid email")
    private String customerEmail;
    @NotBlank(message = "Customer address is required")
    private String address1;
    private String address2;
    @NotNull(message = "Account type is required")
    private Account.AccountType accountType;
    private BigDecimal availableBalance;

}
