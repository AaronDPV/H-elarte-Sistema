# 🍨 H-elarte - Sistema de Gestión de Heladería y Reservas

Sistema Web Full Stack para la gestión integral de reservas de mesas, catálogo de experiencias gastronómicas de heladería artesanal y administración de usuarios con control de accesos basado en roles.

---

## 🛠️ Tecnologías Utilizadas

### Backend
* **Java 17**
* **Spring Boot 3 / 4**
  * Spring Web MVC (API RESTful)
  * Spring Data JPA & Hibernate
  * Spring Security (HTTP Basic & BCrypt)
  * Jakarta Bean Validation
* **MySQL 8.0**
* **Maven**

---

## 🏗️ Arquitectura del Backend

El proyecto sigue una **arquitectura por capas limpia (Layered Architecture)**:

```
H-elarte-Sistema/
└── src/main/java/com/example/helarte/
    ├── controller/      # Endpoints REST (@RestController)
    ├── service/         # Lógica de negocio y transacciones (@Transactional)
    ├── repository/      # Persistencia y consultas JPQL (@Query)
    ├── model/           # Entidades JPA (Usuario, Mesa, Servicio, Reserva)
    ├── dto/             # Data Transfer Objects y validación (@Valid)
    ├── security/        # Configuración de seguridad y roles (SecurityConfig)
    ├── exception/       # Manejo global de errores (@RestControllerAdvice)
    └── config/          # Carga de datos iniciales (DataInitializer)
```

---

## 🚀 Puesta en Marcha

### 1. Base de Datos (MySQL)
1. Asegúrate de tener MySQL ejecutándose en el puerto `3306`.
2. Ejecuta el script SQL ubicado en `src/main/resources/database.sql`:
   ```bash
   mysql -u root -p < src/main/resources/database.sql
   ```
3. Verifica la configuración en `src/main/resources/application.properties`.

### 2. Ejecutar el Backend (Spring Boot)
En la raíz del proyecto:
```bash
./mvnw spring-boot:run
```
La API estará disponible en `http://localhost:8080`.

---

## 👥 Cuentas y Credenciales de Prueba

Al iniciar el sistema, el `DataInitializer` asegura automáticamente los siguientes usuarios con contraseñas encriptadas en BCrypt:

| Rol | Correo Electrónico | Contraseña | Permisos |
| :--- | :--- | :--- | :--- |
| **ADMIN** | `admin@helarte.com` | `admin123` | Control total (Usuarios, Catálogo, Mesas, Reservas) |
| **EMPLEADO** | `empleado@helarte.com` | `empleado123` | Gestión de sala (Mesas, Confirmar y Atender reservas) |
| **CLIENTE** | `cliente@helarte.com` | `cliente123` | Consulta pública, autoservicio y "Mis Reservas" |

---

## 🧪 Pruebas de la API REST

Consulta el archivo [POSTMAN_GUIA_PRUEBAS.md](file:///c:/Users/Usuario/Desktop/proyecto_helarte/H-elarte-Sistema/POSTMAN_GUIA_PRUEBAS.md) o importa la colección JSON `Helarte_API.postman_collection.json` en Postman para probar todos los casos de uso.
