package com.company.exam;

import com.company.exam.controller.CustomerController;
import com.company.exam.models.Account;
import com.company.exam.models.AddCustomer;
import com.company.exam.models.Customer;
import com.company.exam.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {
    private MockMvc mockMvc;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private Customer mockCustomer;
    private Account mockAccount;
    private final Long customerNumber = 12345678L;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController)
                .setControllerAdvice(new com.company.exam.exception.GlobalExceptionHandler())
                .build();

        mockAccount = Account.builder()
                .accountNumber(10001L)
                .accountType(Account.AccountType.S)
                .availableBalance(new BigDecimal("500.00"))
                .build();

        mockCustomer = Customer.builder()
                .customerNumber(customerNumber)
                .customerName("Test")
                .customerMobile("09081234567")
                .customerEmail("test12345@gmail.com")
                .address1("test")
                .address2("test")
                .savings(mockAccount)
                .build();
    }

    @Test
    void getCustomer_WhenRecordExists_Returns302AndCustomDtoPayload() throws Exception {
        when(customerService.getCustomerByNumber(customerNumber)).thenReturn(Optional.of(mockCustomer));

        mockMvc.perform(get("/api/v1/account/{customerNumber}", customerNumber)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.customerNumber").value(customerNumber))
                .andExpect(jsonPath("$.customerName").value("Test"))
                .andExpect(jsonPath("$.customerMobile").value("09081234567"))
                .andExpect(jsonPath("$.customerEmail").value("test12345@gmail.com"))
                .andExpect(jsonPath("$.address1").value("test"))
                .andExpect(jsonPath("$.address2").value("test"))

                .andExpect(jsonPath("$.savings").isArray())
                .andExpect(jsonPath("$.savings[0].accountNumber").value(10001))
                .andExpect(jsonPath("$.savings[0].accountType").value("S"))
                .andExpect(jsonPath("$.savings[0].availableBalance").value(500.00))

                .andExpect(jsonPath("$.transactionStatusCode").value(302))
                .andExpect(jsonPath("$.transactionStatusDescription").value("Customer Account found"));

        verify(customerService, times(1)).getCustomerByNumber(customerNumber);
    }

    @Test
    void getCustomer_WhenRecordDoesNotExist_Returns404CustomErrorFormat() throws Exception {
        Long nonExistentId = 99999999L;

        when(customerService.getCustomerByNumber(nonExistentId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/account/{customerNumber}", nonExistentId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.transactionStatusCode").value(404))
                .andExpect(jsonPath("$.transactionStatusDescription").value("Customer not found"))

                .andExpect(jsonPath("$.customerNumber").doesNotExist());

        verify(customerService, times(1)).getCustomerByNumber(nonExistentId);
    }

    @Test
    void addCustomer_WithValidPayload_Returns201AndCustomSuccessBlock() throws Exception {

        AddCustomer incomingDto = new AddCustomer();
        incomingDto.setCustomerName("John Doe");
        incomingDto.setCustomerMobile("0917328732");
        incomingDto.setCustomerEmail("john.doe@example.com");
        incomingDto.setAddress1("Makati");
        incomingDto.setAddress2("");
        incomingDto.setAccountType(Account.AccountType.S);

        Customer savedRecordInstance = Customer.builder()
                .customerNumber(82240258L)
                .customerName("John Doe")
                .build();

        when(customerService.addCustomer(any(AddCustomer.class)))
                .thenReturn(savedRecordInstance);

        String jsonRequestPayload = "{"
                + "\"customerName\":\"John Doe\","
                + "\"customerMobile\":\"0917328732\","
                + "\"customerEmail\":\"john.doe@example.com\","
                + "\"address1\":\"Makati\","
                + "\"address2\":\"\","
                + "\"accountType\":\"S\""
                + "}";

        mockMvc.perform(post("/api/v1/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestPayload)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()) // Verifies HTTP Status Code 201
                .andExpect(jsonPath("$.customerNumber").value(82240258))
                .andExpect(jsonPath("$.transactionStatusCode").value(201))
                .andExpect(jsonPath("$.transactionStatusDescription").value("Customer account created"));

        verify(customerService, times(1)).addCustomer(any(com.company.exam.models.AddCustomer.class));
    }

    @Test
    void addCustomer_WithMalformedEmailPayload_Returns400WithCustomValidationMessage() throws Exception {
        String badEmailPayload = "{"
                + "\"customerName\":\"John Doe\","
                + "\"customerMobile\":\"0917328732\","
                + "\"customerEmail\":\"invalid-email-format\"," // ❌ Fails @Email constraint
                + "\"address1\":\"Makati\","
                + "\"address2\":\"\","
                + "\"accountType\":\"S\""
                + "}";


        mockMvc.perform(post("/api/v1/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badEmailPayload)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.transactionStatusCode").value(400))

                .andExpect(jsonPath("$.transactionStatusDescription").value("Enter valid email"))

                .andExpect(jsonPath("$.customerNumber").doesNotExist());

        verify(customerService, never()).addCustomer(any(com.company.exam.models.AddCustomer.class));
    }
}
