package com.example.demo.Repository;

import org.springframework.data.repository.CrudRepository;
import com.example.demo.models.Producto;

public interface ProductoRepository extends CrudRepository<Producto, Long> {
}