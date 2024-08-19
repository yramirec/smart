# API de Registro de Usuarios - Proyecto Smart

Esta API, desarrollada en Java con Spring Boot, permite el registro seguro de nuevos usuarios, incluyendo su información de contacto. La autenticación se gestiona mediante tokens JWT.

## Características Principales

- **Registro de Usuarios:** Permite a los nuevos usuarios crear una cuenta proporcionando su nombre, correo electrónico, contraseña e teléfono.
- **Validación de Datos:** Implementa validaciones para asegurar el formato correcto del correo electrónico y la contraseña.
- **Generación de Tokens JWT:** Tras un registro exitoso, se genera un token JWT único para el usuario.
- **Seguridad:** Utiliza Spring Security para proteger los endpoints de la API, asegurando que solo los usuarios autenticados puedan acceder a recursos protegidos.
- **Base de Datos en Memoria (H2):** Ideal para desarrollo y pruebas, almacena los datos de usuarios y teléfonos en memoria, lo que facilita la configuración y el despliegue rápido.
- **Test Unitarios:** Test unitarios con JUnit y Mockito para la covertura de codigo.

## Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.3.2**
- **Spring Data JPA (con Hibernate)**
- **H2 Database**
- **JWT (JSON Web Tokens)**

## Cómo Ejecutar la Aplicación

1. **Clonar el Repositorio:**

    ```bash
    git clone <URL_DEL_REPOSITORIO>
    ```

2. **Compilar y Ejecutar:**

    ```bash
    mvn spring-boot:run
    ```

3. **Acceder a la API:** La API estará disponible en [http://localhost:8080/api/users](http://localhost:8080/api/users).

## Endpoints

### `POST /api/users/register`

Registra un nuevo usuario.

**Request Body:**

```json
{
    "name": "Nombre del Usuario",
    "email": "correo@ejemplo.com",
    "password": "ContraseñaSegura123",
    "phones": [
        {
            "number": "123456789",
            "citycode": "01",
            "contrycode": "57"
        }
    ]
}
```

**Response (Éxito - 201 Created):**

```json
{
    "id": "uuid-generado",
    "name": "Nombre del Usuario",
    "email": "correo@ejemplo.com",
    "phones": [
        {
            "id": 1,
            "number": "123456789",
            "citycode": "01",
            "contrycode": "57"
        }
    ],
    "created": "2023-11-20T10:30:00", 
    "modified": "2023-11-20T10:30:00", 
    "lastLogin": "2023-11-20T10:30:00", 
    "token": "token-jwt-generado",
    "isActive": true
}
```

**Response (Error - 400 Bad Request):**

```json
{
    "mensaje": "El correo ya está registrado" 
}
```
## Pruebas en Postman

**Usar el siguiente comando cURL en Postman para el registro de usuario:**

```bash
curl --location 'http://localhost:8080/api/users/register' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "Juan Rodriguez",
    "email": "juan@rodriguez.org",
    "password": "PasswordSegura123!",
    "phones": [
        {
            "number": "1234567",
            "citycode": "1",
            "contrycode": "57"
        }
    ]
}'
```


