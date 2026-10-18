package poli.edu.arqweb.arqweb.mapper;

import com.google.protobuf.Timestamp;
import org.springframework.stereotype.Component;
import poli.edu.arqweb.arqweb.model.Product;
import poli.edu.arqweb.arqweb.proto.ProductoProto;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class ProductoMapper {

    public Product toEntity(ProductoProto.Producto p) {
        LocalDateTime fechaCreacion = null;

        if (p.hasFechaCreacion()) {
            fechaCreacion = LocalDateTime.ofEpochSecond(
                    p.getFechaCreacion().getSeconds(),
                    p.getFechaCreacion().getNanos(),
                    ZoneOffset.UTC);
        }

        return new Product(
                p.getId(),
                p.getNombre(),
                p.getDescripcion(),
                p.getPrecio(),
                p.getCantidad(),
                fechaCreacion,
                p.getEstado()
        );
    }

    public ProductoProto.Producto toProto(Product p) {
        ProductoProto.Producto.Builder builder = ProductoProto.Producto.newBuilder()
                .setId(p.getId())
                .setNombre(valueOrEmpty(p.getNombre()))
                .setDescripcion(valueOrEmpty(p.getDescripcion()))
                .setPrecio(p.getPrecio())
                .setCantidad(p.getCantidad())
                .setEstado(Boolean.TRUE.equals(p.getEstado()));

        if (p.getFecha_creacion() != null) {
            builder.setFechaCreacion(Timestamp.newBuilder()
                    .setSeconds(p.getFecha_creacion().toEpochSecond(ZoneOffset.UTC))
                    .setNanos(p.getFecha_creacion().getNano())
                    .build());
        }

        return builder.build();
    }

    private String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }
}
