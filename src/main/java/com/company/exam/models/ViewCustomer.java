package com.company.exam.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class ViewCustomer {

    private Long customerNumber;
    private String customerName;
    private String customerMobile;
    private String customerEmail;
    private String address1;
    private String address2;
    private List<Account> savings;
    private int transactionStatusCode;
    private String transactionStatusDescription;
}
