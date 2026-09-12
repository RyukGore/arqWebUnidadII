package poli.edu.arqweb.arqweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poli.edu.arqweb.arqweb.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
}
