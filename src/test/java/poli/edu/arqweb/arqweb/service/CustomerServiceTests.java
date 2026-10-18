package poli.edu.arqweb.arqweb.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import poli.edu.arqweb.arqweb.dto.CustomerInput;
import poli.edu.arqweb.arqweb.model.Customer;
import poli.edu.arqweb.arqweb.model.Product;
import poli.edu.arqweb.arqweb.repository.CustomerRepository;
import poli.edu.arqweb.arqweb.repository.ProductRepository;

import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTests {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerService = new CustomerService(customerRepository, productRepository);
    }

    @Test
    void createsCustomerWithExistingProducts() {
        Product product = new Product("PROD-001", "Monitor", null, 799900, 10, null, true);
        CustomerInput input = new CustomerInput(
                "CLI-001",
                "Ana Pérez",
                "ANA@EXAMPLE.COM",
                "3001234567",
                true,
                new LinkedHashSet<>(List.of("PROD-001"))
        );

        when(customerRepository.existsById("CLI-001")).thenReturn(false);
        when(customerRepository.existsByEmailIgnoreCase("ANA@EXAMPLE.COM")).thenReturn(false);
        when(productRepository.findAllById(input.getProductoIds())).thenReturn(List.of(product));
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Customer created = customerService.createCustomer(input);

        assertEquals("ana@example.com", created.getEmail());
        assertEquals(1, created.getProductos().size());
        assertEquals("PROD-001", created.getProductos().iterator().next().getId());
    }

    @Test
    void rejectsUnknownProducts() {
        CustomerInput input = new CustomerInput(
                "CLI-002",
                "Carlos Gómez",
                "carlos@example.com",
                null,
                true,
                new LinkedHashSet<>(List.of("PROD-999"))
        );

        when(customerRepository.existsById("CLI-002")).thenReturn(false);
        when(customerRepository.existsByEmailIgnoreCase("carlos@example.com")).thenReturn(false);
        when(productRepository.findAllById(input.getProductoIds())).thenReturn(List.of());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> customerService.createCustomer(input)
        );

        assertEquals("No existen los productos: PROD-999", exception.getMessage());
    }
}
