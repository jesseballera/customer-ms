package com.company.exam.service;

import com.company.exam.models.Account;
import com.company.exam.models.AddCustomer;
import com.company.exam.models.Customer;
import com.company.exam.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public Customer addCustomer(AddCustomer entity) {

        Account account = Account.builder()
                .accountType(entity.getAccountType())
                .availableBalance(entity.getAvailableBalance())
                .build();

        Customer customer = Customer.builder()
                .customerName(entity.getCustomerName())
                .customerMobile(entity.getCustomerMobile())
                .customerEmail(entity.getCustomerEmail())
                .address1(entity.getAddress1())
                .address2(entity.getAddress2())
                .savings(account)
                .build();

        return  customerRepository.save(customer);

    }

    public Optional<Customer> getCustomerByNumber(Long customerNumber) {
        return customerRepository.findByCustomerNumber(customerNumber);
    }

}
