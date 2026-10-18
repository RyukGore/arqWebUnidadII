package poli.edu.arqweb.arqweb.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poli.edu.arqweb.arqweb.dto.CustomerInput;
import poli.edu.arqweb.arqweb.model.Customer;
import poli.edu.arqweb.arqweb.model.Product;
import poli.edu.arqweb.arqweb.repository.CustomerRepository;
import poli.edu.arqweb.arqweb.repository.ProductRepository;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public CustomerService(CustomerRepository customerRepository, ProductRepository productRepository) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(String id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Transactional
    public Customer createCustomer(CustomerInput input) {
        if (customerRepository.existsById(input.getId())) {
            throw new IllegalStateException("Ya existe un cliente con el id " + input.getId());
        }
        if (customerRepository.existsByEmailIgnoreCase(input.getEmail())) {
            throw new IllegalStateException("Ya existe un cliente con el email " + input.getEmail());
        }

        Customer customer = new Customer();
        customer.setId(input.getId().trim());
        applyChanges(customer, input);
        return customerRepository.save(customer);
    }

    @Transactional
    public Customer updateCustomer(CustomerInput input) {
        Customer customer = customerRepository.findById(input.getId()).orElse(null);
        if (customer == null) {
            return null;
        }
        if (customerRepository.existsByEmailIgnoreCaseAndIdNot(input.getEmail(), input.getId())) {
            throw new IllegalStateException("Ya existe otro cliente con el email " + input.getEmail());
        }

        applyChanges(customer, input);
        return customerRepository.save(customer);
    }

    @Transactional
    public Customer deleteCustomerById(String id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer == null) {
            return null;
        }
        customerRepository.delete(customer);
        return customer;
    }

    private void applyChanges(Customer customer, CustomerInput input) {
        customer.setNombre(input.getNombre().trim());
        customer.setEmail(input.getEmail().trim().toLowerCase(Locale.ROOT));
        customer.setTelefono(normalizeOptional(input.getTelefono()));
        if (input.getEstado() != null) {
            customer.setEstado(input.getEstado());
        }
        customer.setProductos(resolveProducts(input.getProductoIds()));
    }

    private Set<Product> resolveProducts(Set<String> requestedIds) {
        Set<String> ids = requestedIds == null
                ? Set.of()
                : requestedIds.stream()
                        .map(String::trim)
                        .filter(id -> !id.isEmpty())
                        .collect(Collectors.toCollection(LinkedHashSet::new));

        List<Product> products = productRepository.findAllById(ids);
        Set<String> foundIds = products.stream()
                .map(Product::getId)
                .collect(Collectors.toSet());

        Set<String> missingIds = ids.stream()
                .filter(id -> !foundIds.contains(id))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        if (!missingIds.isEmpty()) {
            throw new IllegalArgumentException("No existen los productos: " + String.join(", ", missingIds));
        }

        return new LinkedHashSet<>(products);
    }

    private String normalizeOptional(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
