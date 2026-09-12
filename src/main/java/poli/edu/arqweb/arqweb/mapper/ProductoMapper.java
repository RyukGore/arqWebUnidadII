package poli.edu.arqweb.arqweb.mapper;

import org.springframework.stereotype.Component;
import poli.edu.arqweb.arqweb.model.Product;
import poli.edu.arqweb.arqweb.proto.ProductoProto;

@Component
public class ProductoMapper {

    public Product toEntity(ProductoProto.Producto p) {
        return new Product(
                p.getId(),
                p.getNombre(),
                p.getDescripcion(),
                p.getPrecio(),
                p.getCantidad()
        );
    }

    public ProductoProto.Producto toProto(Product p) {
        return ProductoProto.Producto.newBuilder()
                .setId(p.getId())
                .setNombre(p.getNombre())
                .setDescripcion(p.getDescripcion())
                .setPrecio(p.getPrecio())
                .setCantidad(p.getCantidad())
                .build();
    }

}
