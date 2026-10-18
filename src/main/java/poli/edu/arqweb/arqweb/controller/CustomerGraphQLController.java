package poli.edu.arqweb.arqweb.controller;

import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import poli.edu.arqweb.arqweb.dto.CustomerInput;
import poli.edu.arqweb.arqweb.model.Customer;
import poli.edu.arqweb.arqweb.service.CustomerService;

import java.util.List;

@Controller
@Validated
public class CustomerGraphQLController {

    private final CustomerService customerService;

    public CustomerGraphQLController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @QueryMapping
    public List<Customer> customers() {
        return customerService.getAllCustomers();
    }

    @QueryMapping
    public Customer customerById(@Argument String id) {
        return customerService.getCustomerById(id);
    }

    @MutationMapping
    public Customer createCustomer(@Argument("input") @Valid CustomerInput input) {
        return customerService.createCustomer(input);
    }

    @MutationMapping
    public Customer updateCustomer(@Argument("input") @Valid CustomerInput input) {
        return customerService.updateCustomer(input);
    }

    @MutationMapping
    public Customer deleteCustomer(@Argument String id) {
        return customerService.deleteCustomerById(id);
    }
}
