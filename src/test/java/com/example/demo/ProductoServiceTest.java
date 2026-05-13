package com.example.demo;

import com.example.demo.Repository.ProductoRepository;
import com.example.demo.models.Producto;
import com.example.demo.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    // Escenario 1: Guardar un producto válido exitosamente
    @Test
    void guardar_productoValido_retornaProductoGuardado() {
        Producto producto = new Producto();
        producto.setNombre("Laptop");
        producto.setValor(1500.0);
        producto.setDescripcion("Laptop de alta gama");

        Producto guardado = new Producto();
        guardado.setId(1L);
        guardado.setNombre("Laptop");
        guardado.setValor(1500.0);
        guardado.setDescripcion("Laptop de alta gama");

        when(productoRepository.save(producto)).thenReturn(guardado);

        Producto resultado = productoService.guardar(producto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Laptop", resultado.getNombre());
        verify(productoRepository, times(1)).save(producto);
    }

    // Escenario 2: Guardar producto con nombre vacío lanza excepción
    @Test
    void guardar_productoConNombreVacio_lanzaExcepcion() {
        Producto producto = new Producto();
        producto.setNombre("");
        producto.setValor(100.0);

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> productoService.guardar(producto)
        );

        assertEquals("El nombre del producto no puede estar vacío", ex.getMessage());
        verify(productoRepository, never()).save(any());
    }

    // Escenario 3: Guardar producto con valor negativo lanza excepción
    @Test
    void guardar_productoConValorNegativo_lanzaExcepcion() {
        Producto producto = new Producto();
        producto.setNombre("Mouse");
        producto.setValor(-10.0);

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> productoService.guardar(producto)
        );

        assertEquals("El valor del producto no puede ser negativo", ex.getMessage());
        verify(productoRepository, never()).save(any());
    }

    // Escenario 4: Listar productos retorna todos los registros
    @Test
    void listar_hayProductos_retornaLista() {
        Producto p1 = new Producto();
        p1.setId(1L);
        p1.setNombre("Teclado");
        p1.setValor(50.0);

        Producto p2 = new Producto();
        p2.setId(2L);
        p2.setNombre("Monitor");
        p2.setValor(300.0);

        when(productoRepository.findAll()).thenReturn(List.of(p1, p2));

        Iterable<Producto> resultado = productoService.listar();

        assertNotNull(resultado);
        List<Producto> lista = (List<Producto>) resultado;
        assertEquals(2, lista.size());
        verify(productoRepository, times(1)).findAll();
    }

    // Escenario 5: Listar sin productos retorna lista vacía
    @Test
    void listar_sinProductos_retornaListaVacia() {
        when(productoRepository.findAll()).thenReturn(List.of());

        Iterable<Producto> resultado = productoService.listar();

        assertNotNull(resultado);
        assertFalse(resultado.iterator().hasNext());
        verify(productoRepository, times(1)).findAll();
    }

    // Escenario 6: Buscar producto por ID inexistente retorna Optional vacío
    @Test
    void buscarPorId_idInexistente_retornaOptionalVacio() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Producto> resultado = productoService.buscarPorId(99L);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(productoRepository, times(1)).findById(99L);
    }
}
