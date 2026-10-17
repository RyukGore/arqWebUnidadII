package poli.edu.arqweb.arqweb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    private String id;
    private String nombre;
    private String descripcion;
    private double precio;
    private int cantidad;
    private LocalDateTime fecha_creacion = LocalDateTime.now();
    private Boolean estado = true;


    @PrePersist
    public void onCreate() {
            this.fecha_creacion = LocalDateTime.now();
            this.estado = true;
    }
}
