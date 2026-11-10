package com.company.exam.controller;

import com.company.exam.exception.ResourceNotFoundException;
import com.company.exam.models.AddCustomer;
import com.company.exam.models.Customer;
import com.company.exam.models.ViewCustomer;
import com.company.exam.service.CustomerService;
import com.company.exam.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;

@RestController
@RequestMapping("/api/v1/account")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    @Operation(summary = "Create a new customer account", description = "Generates a unique 8-digit identifier and creates the profile mapping.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Success / Customer account created",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Failed / Validation Error",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse> addCustomer(@Valid @RequestBody AddCustomer entity) {
        Customer customer = customerService.addCustomer(entity);
        ApiResponse apiResponse = new ApiResponse(customer.getCustomerNumber(),
                HttpStatus.CREATED.value(), "Customer account created");
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{customerNumber}")
    @Operation(summary = "Retrieve customer details", description = "Fetch profile metadata using the assigned 8-digit unique number.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "302", description = "Customer details retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Customer.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Customer profile not found")
    })
    public ResponseEntity<?> getCustomer(@PathVariable Long customerNumber) {
        return customerService.getCustomerByNumber(customerNumber)
                .map(customer -> {
                    // Initialize and map database entity values into your DTO frame
                    ViewCustomer dto = new ViewCustomer();
                    dto.setCustomerNumber(customer.getCustomerNumber()); // assuming entity field is customerNumber
                    dto.setCustomerName(customer.getCustomerName());
                    dto.setCustomerMobile(customer.getCustomerMobile());
                    dto.setCustomerEmail(customer.getCustomerEmail());
                    dto.setAddress1(customer.getAddress1());
                    dto.setAddress2(customer.getAddress2());

                    if (customer.getSavings() != null) {
                        dto.setSavings(Collections.singletonList(customer.getSavings()));
                    }

                    dto.setTransactionStatusCode(302);
                    dto.setTransactionStatusDescription("Customer Account found");

                    return new ResponseEntity<>(dto, HttpStatus.FOUND);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
    }

}
