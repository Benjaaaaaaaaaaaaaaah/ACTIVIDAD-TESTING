# Actividad 11 - Testing Unitario en Spring Boot con JUnit y Mockito

## Parte 1: Escenarios de Prueba en Gherkin

Entidad utilizada: **Producto** (con campos: "id", "nombre", "valor", "descripción")





### Escenario 1: Guardar un producto válido

gherkin
Feature: Gestión de Productos

  Scenario: Guardar un producto con datos válidos
    Given un producto con nombre "Laptop", valor 1500.0 y descripción "Laptop de alta gama"
    When se envía una petición POST a /productos
    Then el sistema responde con código 201 Created
    And el cuerpo de la respuesta contiene el producto con un id generado




### Escenario 2: Rechazar producto con nombre vacío

gherkin
  Scenario: Intentar guardar un producto con nombre vacío
    Given un producto con nombre "" y valor 100.0
    When se envía una petición POST a /productos
    Then el sistema responde con código 400 Bad Request
    And el mensaje de error indica "El nombre del producto no puede estar vacío"


### Escenario 3: Rechazar producto con valor negativo

gherkin
  Scenario: Intentar guardar un producto con valor negativo
    Given un producto con nombre "Mouse" y valor -10.0
    When se envía una petición POST a /productos
    Then el sistema responde con código 400 Bad Request
    And el mensaje de error indica "El valor del producto no puede ser negativo"


### Escenario 4: Listar productos cuando hay registros

gherkin
  Scenario: Obtener la lista de productos cuando existen registros
    Given existen 2 productos en la base de datos: "Teclado" y "Monitor"
    When se envía una petición GET a /productos
    Then el sistema responde con código 200 OK
    And el JSON de respuesta contiene una lista con 2 elementos


### Escenario 5: Listar productos cuando no hay registros

gherkin
  Scenario: Obtener la lista de productos cuando no hay ninguno registrado
    Given la base de datos de productos está vacía
    When se envía una petición GET a /productos
    Then el sistema responde con código 200 OK
    And el JSON de respuesta contiene una lista vacía


### Escenario 6: Buscar un producto por ID inexistente

gherkin
  Scenario: Buscar un producto con un ID que no existe
    Given no existe ningún producto con id 99 en la base de datos
    When el servicio intenta buscar el producto con id 99
    Then el resultado es un Optional vacío
    And el repositorio es consultado exactamente una vez


