package com.example.demo.service;

import com.example.demo.Repository.ProductoRepository;
import com.example.demo.models.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public Producto guardar(Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío");
        }
        if (producto.getValor() == null || producto.getValor() < 0) {
            throw new IllegalArgumentException("El valor del producto no puede ser negativo");
        }
        return productoRepository.save(producto);
    }

    public Iterable<Producto> listar() {
        return productoRepository.findAll();
    }

    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }
}
