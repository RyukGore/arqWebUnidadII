package poli.edu.arqweb.arqweb.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import poli.edu.arqweb.arqweb.dto.CustomerInput;
import poli.edu.arqweb.arqweb.model.Customer;
import poli.edu.arqweb.arqweb.service.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable String id) {
        Customer customer = customerService.getCustomerById(id);
        return customer == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(customer);
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody CustomerInput input) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(input));
    }

    @PutMapping
    public ResponseEntity<Customer> updateCustomer(@Valid @RequestBody CustomerInput input) {
        Customer customer = customerService.updateCustomer(input);
        return customer == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(customer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable String id) {
        Customer customer = customerService.deleteCustomerById(id);
        return customer == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(customer);
    }
}
