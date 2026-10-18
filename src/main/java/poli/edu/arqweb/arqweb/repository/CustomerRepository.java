package poli.edu.arqweb.arqweb.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import poli.edu.arqweb.arqweb.model.Customer;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    @Override
    @EntityGraph(attributePaths = "productos")
    List<Customer> findAll();

    @Override
    @EntityGraph(attributePaths = "productos")
    Optional<Customer> findById(String id);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, String id);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "DELETE FROM clientes_productos WHERE producto_id = :productId", nativeQuery = true)
    int deleteProductAssociations(@Param("productId") String productId);
}
