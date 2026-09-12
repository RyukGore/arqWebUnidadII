package poli.edu.arqweb.arqweb.service;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import poli.edu.arqweb.arqweb.mapper.ProductoMapper;
import poli.edu.arqweb.arqweb.model.Product;
import poli.edu.arqweb.arqweb.proto.ProductoProto;
import poli.edu.arqweb.arqweb.proto.ProductoServiceGrpc;
import poli.edu.arqweb.arqweb.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class ProductServiceImpl extends ProductoServiceGrpc.ProductoServiceImplBase {

    private final ProductRepository productRepository;
    private final ProductoMapper productoMapper;

    public ProductServiceImpl(ProductRepository productRepository, ProductoMapper productoMapper) {
        this.productRepository = productRepository;
        this.productoMapper = new ProductoMapper();
    }

    @Override
    public void listarProductos(Empty request, StreamObserver<ProductoProto.Productos> responseObserver) {
        List<ProductoProto.Producto> items =
                productRepository.findAll().stream()
                        .map(productoMapper::toProto)
                        .collect(Collectors.toList());
        ProductoProto.Productos result = ProductoProto.Productos.newBuilder()
                .addAllProductos(items)
                .build();
        responseObserver.onNext(result);
        responseObserver.onCompleted();
    }

    @Override
    public void obtenerProducto(ProductoProto.ProductoId request, StreamObserver<ProductoProto.Producto> responseObserver) {
        Product product = productRepository.findById(request.getId()).orElse(null);
        if (product != null) {
            responseObserver.onNext(productoMapper.toProto(product));
        } else {
            responseObserver.onNext(ProductoProto.Producto.newBuilder().build());
        }        responseObserver.onCompleted();
    }

    @Override
    public void crearProducto(ProductoProto.Producto request, StreamObserver<ProductoProto.Producto> responseObserver) {
        Product saved = productRepository.save(productoMapper.toEntity(request));
        responseObserver.onNext(productoMapper.toProto(saved));
        responseObserver.onCompleted();
    }

    @Override
    public void actualizarProducto(ProductoProto.Producto request, StreamObserver<ProductoProto.Producto> responseObserver) {
        Product existingProduct = productRepository.findById(request.getId()).orElse(null);
        if (existingProduct != null) {
            existingProduct.setNombre(request.getNombre());
            existingProduct.setDescripcion(request.getDescripcion());
            existingProduct.setPrecio(request.getPrecio());
            existingProduct.setCantidad(request.getCantidad());
            Product updated = productRepository.save(existingProduct);
            responseObserver.onNext(productoMapper.toProto(updated));
        } else {
            responseObserver.onNext(ProductoProto.Producto.newBuilder().build());
        }        responseObserver.onCompleted();
    }

    @Override
    public void eliminarProducto(ProductoProto.ProductoId request, StreamObserver<ProductoProto.Producto> responseObserver) {
        Product product = productRepository.findById(request.getId()).orElse(null);
        if (product != null) {
            productRepository.deleteById(request.getId());
            responseObserver.onNext(productoMapper.toProto(product));
        } else {
            responseObserver.onNext(ProductoProto.Producto.newBuilder().build());
        }        responseObserver.onCompleted();
    }

}