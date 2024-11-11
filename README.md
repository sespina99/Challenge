# Mendel Challenge

## Descripción

Este proyecto es una API RESTful hecha con Spring Boot para administrar transacciones.
Expone endpoints para almacenar y actualizar transacciones.
También expone endpoints para obtener todas las transacciones de cierto tipo y para ver el gasto total de una 
transacción y de sus hijos.
La aplicación se encuentra Dockerizada.

## Instalación y Setup

### 1. Clonar el repositorio

```bash
git clone git@github.com:sespina99/Challenge.git
cd Challenge
```

### 2. Armar la Imagen de Docker
```bash
mvn clean install
docker build -t transaction-service .
```

### 3. Correr el Container de Docker
```bash
docker run -p 8080:8080 transaction-service
```

### 4. Acceder a la Aplicación
```bash
docker run -p 8080:8080 transaction-service
```
La aplicación ahora corre en http://localhost:8080


## Uso
### Endpoints
1. **Agregar una transacción**
   - **URL**: `/transactions`
   - **Method**: `POST`
   - **Body**:
     ```json
     {
       "amount": double,
       "type": string,
       "parentId": long(opcional)
     }
     ```
   - **Response**:
       - 200 OK: Transacción creada exitosamente. Devuelve un objeto TransactionResponseDTO con los detalles de la transacción creada.
       - 400 Bad Request: La creación de la transacción falló.

   - **Descripción**: Crea una nueva transaccion. El `parentId` puede ser null o un long con el id de otra 
     transacción. Si se da un parentId que no existe en la memoria, devuelve 400 Bad Request. Si se agrega la 
     transacción devuelve 200 ok.


2. **Modificar una transacción**
    - **URL**: `/transactions/{transaction_id}`
    - **Method**: `PUT`
    - **Body**:
      ```json
      {
        "amount": double,
        "type": string,
        "parentId": long(optional)
      }
      ```
    - **Response**:
        - 200 OK: Transacción actualizada exitosamente. Devuelve un objeto TransactionResponseDTO con los detalles de la transacción actualizada.
        - 400 Bad Request: La creación de la transacción falló.
    - **Descripción**: Modifica la transacción de `transaction_id`. El `parentId` puede ser null o un 
      long con el id de otra transacción. Si se da un parentId que no existe en la memoria o se da que el parenId dado es el mismo que el 
transaction_id, devuelve 400 Bad Request. Si se modifica la transacción devuelve 200 ok. Es importante que si no se 
      especifica un parentId, se pierde el parentId previo, ya que se está utilizando el verbo PUT y no PATCH.


      

3. **Obtener transacciones de un tipo**
    - **URL**: `/transactions/types/{type}`
    - **Method**: `GET`
    - **Response**:
      - 200 OK: Devuelve una lista de IDs de transacciones que coinciden con el tipo especificado. 
      - 204 No Content: No se encontraron transacciones con el tipo solicitado.

    - **Descripción**: Devuelve una lista de todos los `transaction_id` de ese tipo. Si se pide por un tipo que no 
      tiene ninguna transacción cargada, devuelve 204 No Content.


4. **Obtener Suma Total de Transacciones**
    - **URL**: `/transactions/sum/{transaction_id}`
    - **Method**: `GET`
      - **Response**:
          - 200 OK: Devuelve un objeto json con el total de la suma de la transacción y de todos sus hijos
            ```json
            {
            "sum":double
            }
            ```
          - 404 Not Found: No se encontró la transacción especificada.

    - **Descripción**: Devuelve la suma de todas las transacciones que estan transitivamente conectadas por su 
      parent_id a `transaction_id`. Si no existe esa transacción, devuelve 404.


## Ejemplo de Uso
### Creo una Transacción

```bash
curl --location --request POST 'localhost:8080/transactions' \
--header 'Content-Type: application/json' \
--data-raw '{"amount": 5000, "type": "planes"}
'
```
- Response Status 200 OK
- Response Body
    ````json
    {
    "id": 0,
    "amount": 5000.0,
    "type": "planes",
    "parentId": null
  }
    ````

### La modifico
```bash
curl --location --request PUT 'localhost:8080/transactions/0' \
--header 'Content-Type: application/json' \
--data-raw '{"amount": 5000, "type": "cars"}
'
```
- Response Status 200 OK
- Response Body
    ````json
    {
    "id": 0,
    "amount": 5000.0,
    "type": "cars",
    "parentId": null
  }
    ````
  
### Agrego un hijo
```bash
curl --location --request POST 'localhost:8080/transactions' \
--header 'Content-Type: application/json' \
--data-raw '{"amount": 6000, "type": "cars", "parentId": 0}
'
```
- Response Status 200 OK
- Response Body
    ````json
    {
    "id": 1,
    "amount": 6000.0,
    "type": "cars",
    "parentId": 0
  }
  ````
### Consulto por Id de tipos cars
```bash
curl --location --request GET 'localhost:8080/transactions/types/cars' \
--header 'Content-Type: application/json' \
--data-raw '{"amount": 6000, "type": "cars", "parentId": 0}
'
```
- Response Status 200 OK
- Response Body
[0,1]

### Consulto por la suma del Id 0
```bash
curl --location --request GET 'localhost:8080/transactions/sum/0' \
--header 'Content-Type: application/json' \
--data-raw '{"amount": 6000, "type": "cars", "parentId": 0}
'
```
- Response Status 200 OK
- Response Body
````json
{
"sum": 11000.0
}
````

## Consideraciones Tomadas
Se decidió por cambiar algunas cosas con respecto al enunciado del challenge. Principalmente el challenge 
especificaba que con el PUT se generaban nuevas transacciones pero debido a que esto no sigue el paradigma RESTful 
en su totalidad se optó por generar las transacciones con el POST y que el PUT sea solo para modificar transacciones 
previamente creadas.
Tabién en los ejemplos de uno del challenge se veía como se envíaba el {"status":"ok"} en el body de la respuesta 
del PUT. Esto se cambió para que el status llegue con el código 200 de HTML OK y que el body del response se 
utilize para mostrar a la Transferencia modificada con el put. Lo mismo se hace con el POST.

Cabe aclarar que se implemento la aplicación respetando los principios SOLID y con TDD. Los tests se pueden correr 
al estar en la carpeta de challenge ejecutando:

```bash
mvn clean compile test
```

