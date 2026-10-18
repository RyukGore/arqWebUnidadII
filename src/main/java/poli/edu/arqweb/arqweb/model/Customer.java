package poli.edu.arqweb.arqweb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(
        name = "clientes",
        uniqueConstraints = @UniqueConstraint(name = "uk_clientes_email", columnNames = "email")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @Column(nullable = false, length = 50)
    private String id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, length = 160)
    private String email;

    @Column(length = 30)
    private String telefono;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private Boolean estado = true;

    @ManyToMany
    @JoinTable(
            name = "clientes_productos",
            joinColumns = @JoinColumn(
                    name = "cliente_id",
                    nullable = false,
                    foreignKey = @ForeignKey(name = "fk_clientes_productos_cliente")
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "producto_id",
                    nullable = false,
                    foreignKey = @ForeignKey(name = "fk_clientes_productos_producto")
            ),
            uniqueConstraints = @UniqueConstraint(
                    name = "uk_clientes_productos",
                    columnNames = {"cliente_id", "producto_id"}
            ),
            indexes = @Index(name = "idx_clientes_productos_producto", columnList = "producto_id")
    )
    @OrderBy("nombre ASC")
    private Set<Product> productos = new LinkedHashSet<>();

    @PrePersist
    public void onCreate() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
        if (estado == null) {
            estado = true;
        }
    }
}
