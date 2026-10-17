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

        if (!p.getFechaCreacion().equals(Timestamp.getDefaultInstance())) {
            fechaCreacion = LocalDateTime.ofEpochSecond(
                    p.getFechaCreacion().getSeconds(),
                    p.getFechaCreacion().getNanos(),
                    ZoneOffset.UTC );
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
        return ProductoProto.Producto.newBuilder()
                .setId(p.getId())
                .setNombre(p.getNombre())
                .setDescripcion(p.getDescripcion())
                .setPrecio(p.getPrecio())
                .setCantidad(p.getCantidad())
                .setFechaCreacion(Timestamp.newBuilder())
                .setEstado(p.getEstado())
                .build();
    }

}
