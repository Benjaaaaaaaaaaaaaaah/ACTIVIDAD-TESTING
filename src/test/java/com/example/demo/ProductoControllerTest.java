package com.example.demo;

import com.example.demo.controller.ProductoController;
import com.example.demo.models.Producto;
import com.example.demo.service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    // Escenario 1: POST /productos con datos válidos retorna 201 Created
    @Test
    void postProducto_datosValidos_retorna201() throws Exception {
        Producto entrada = new Producto();
        entrada.setNombre("Auriculares");
        entrada.setValor(75.0);
        entrada.setDescripcion("Auriculares bluetooth");

        Producto guardado = new Producto();
        guardado.setId(1L);
        guardado.setNombre("Auriculares");
        guardado.setValor(75.0);
        guardado.setDescripcion("Auriculares bluetooth");

        when(productoService.guardar(any(Producto.class))).thenReturn(guardado);

        mockMvc.perform(post("/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entrada)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Auriculares"))
                .andExpect(jsonPath("$.valor").value(75.0));
    }

    // Escenario 2: POST /productos con nombre vacío retorna 400 Bad Request
    @Test
    void postProducto_nombreVacio_retorna400() throws Exception {
        Producto entrada = new Producto();
        entrada.setNombre("");
        entrada.setValor(50.0);

        when(productoService.guardar(any(Producto.class)))
                .thenThrow(new IllegalArgumentException("El nombre del producto no puede estar vacío"));

        mockMvc.perform(post("/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entrada)))
                .andExpect(status().isBadRequest());
    }

    // Escenario 3: GET /productos retorna lista de productos y 200 OK
    @Test
    void getProductos_hayProductos_retorna200ConLista() throws Exception {
        Producto p1 = new Producto();
        p1.setId(1L);
        p1.setNombre("Webcam");
        p1.setValor(90.0);

        Producto p2 = new Producto();
        p2.setId(2L);
        p2.setNombre("Micrófono");
        p2.setValor(120.0);

        when(productoService.listar()).thenReturn(List.of(p1, p2));

        mockMvc.perform(get("/productos")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Webcam"))
                .andExpect(jsonPath("$[1].nombre").value("Micrófono"))
                .andExpect(jsonPath("$.length()").value(2));
    }

    // Escenario 4: GET /productos sin productos retorna lista vacía y 200 OK
    @Test
    void getProductos_sinProductos_retorna200ConListaVacia() throws Exception {
        when(productoService.listar()).thenReturn(List.of());

        mockMvc.perform(get("/productos")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // Escenario 5: POST /productos con valor negativo retorna 400 Bad Request
    @Test
    void postProducto_valorNegativo_retorna400() throws Exception {
        Producto entrada = new Producto();
        entrada.setNombre("Silla");
        entrada.setValor(-50.0);

        when(productoService.guardar(any(Producto.class)))
                .thenThrow(new IllegalArgumentException("El valor del producto no puede ser negativo"));

        mockMvc.perform(post("/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entrada)))
                .andExpect(status().isBadRequest());
    }

    // Escenario 6: GET /productos retorna JSON con todos los campos esperados
    @Test
    void getProductos_retornaJsonConCamposCompletos() throws Exception {
        Producto p = new Producto();
        p.setId(10L);
        p.setNombre("Tablet");
        p.setValor(250.0);
        p.setDescripcion("Tablet Android 10 pulgadas");

        when(productoService.listar()).thenReturn(List.of(p));

        mockMvc.perform(get("/productos")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].nombre").value("Tablet"))
                .andExpect(jsonPath("$[0].valor").value(250.0))
                .andExpect(jsonPath("$[0].descripcion").value("Tablet Android 10 pulgadas"));
    }
}
