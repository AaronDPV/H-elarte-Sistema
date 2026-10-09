# GUÍA DE PRUEBAS EN POSTMAN - SISTEMA HELARTE (RESERVAS)

## Autenticación (HTTP Basic o Endpoint)
En Postman, en la pestaña **Authorization**, selecciona:
- **Type**: `Basic Auth`
- **Username**: `admin@helarte.com` (o `empleado@helarte.com` / `cliente@helarte.com`)
- **Password**: `admin123` (o `empleado123` / `cliente123`)

---

## 1. Módulo de Autenticación (`/auth`)

### 1.1 Registro de Usuario (Público)
- **Método**: `POST`
- **URL**: `http://localhost:8080/auth/registro`
- **Body** (`raw` -> `JSON`):
```json
{
  "user": "Lucia Fernandez",
  "email": "lucia@gmail.com",
  "password": "password123",
  "rol": "CLIENTE",
  "telefono": "987654321"
}
```

### 1.2 Login de Usuario
- **Método**: `POST`
- **URL**: `http://localhost:8080/auth/login`
- **Authorization**: Basic Auth (`admin@helarte.com` / `admin123`)

---

## 2. Módulo de Usuarios (`/usuarios`)

### 2.1 Listar Todos los Usuarios
- **Método**: `GET`
- **URL**: `http://localhost:8080/usuarios`
- **Authorization**: Basic Auth (ADMIN o EMPLEADO)

### 2.2 Obtener Usuario por ID
- **Método**: `GET`
- **URL**: `http://localhost:8080/usuarios/1`

### 2.3 Crear Usuario por Administrador
- **Método**: `POST`
- **URL**: `http://localhost:8080/usuarios`
- **Authorization**: Basic Auth (ADMIN)
- **Body**:
```json
{
  "user": "Pedro Colaborador Turno Tarde",
  "email": "pedro@helarte.com",
  "password": "pedro12345",
  "rol": "EMPLEADO",
  "telefono": "912345678"
}
```

### 2.4 Actualizar Usuario
- **Método**: `PUT`
- **URL**: `http://localhost:8080/usuarios/2`
- **Authorization**: Basic Auth (ADMIN)
- **Body**:
```json
{
  "user": "Carlos Colaborador Principal",
  "rol": "EMPLEADO",
  "telefono": "999888777",
  "activo": true
}
```

### 2.5 Eliminar Usuario
- **Método**: `DELETE`
- **URL**: `http://localhost:8080/usuarios/4`
- **Authorization**: Basic Auth (ADMIN)

### 2.6 Búsqueda Personalizada JPQL por Filtro
- **Método**: `GET`
- **URL**: `http://localhost:8080/usuarios/buscar?filtro=helarte`

---

## 3. Módulo de Servicios de Heladería (`/servicios`)

### 3.1 Listar Servicios Activos (Público)
- **Método**: `GET`
- **URL**: `http://localhost:8080/servicios`

### 3.2 Crear Nuevo Servicio de Heladería
- **Método**: `POST`
- **URL**: `http://localhost:8080/servicios`
- **Authorization**: Basic Auth (ADMIN)
- **Body**:
```json
{
  "nombre": "Cata Sensorial de Helados Veganos y Sin Azúcar",
  "descripcion": "Experiencia guiada de degustación de helados a base de leche de almendras y frutas de estación.",
  "precio": 55.00,
  "duracionMinutos": 50,
  "activo": true
}
```

### 3.3 Actualizar Servicio
- **Método**: `PUT`
- **URL**: `http://localhost:8080/servicios/1`
- **Authorization**: Basic Auth (ADMIN)
- **Body**:
```json
{
  "nombre": "Reserva Mesa Clásica Heladería",
  "descripcion": "Disfruta en mesa de la mejor variedad de copas y barquillos artesanales.",
  "precio": 18.00,
  "duracionMinutos": 60,
  "activo": true
}
```

### 3.4 Búsqueda JPQL por Nombre
- **Método**: `GET`
- **URL**: `http://localhost:8080/servicios/buscar?nombre=Gourmet`

---

## 4. Módulo de Mesas (`/mesas`)

### 4.1 Listar Todas las Mesas
- **Método**: `GET`
- **URL**: `http://localhost:8080/mesas`
- **Authorization**: Basic Auth (Cualquier usuario autenticado)

### 4.2 Listar Mesas Disponibles
- **Método**: `GET`
- **URL**: `http://localhost:8080/mesas/disponibles`

### 4.3 Registrar Nueva Mesa
- **Método**: `POST`
- **URL**: `http://localhost:8080/mesas`
- **Authorization**: Basic Auth (ADMIN o EMPLEADO)
- **Body**:
```json
{
  "numeroMesa": 6,
  "capacidad": 4,
  "ubicacion": "Terraza Exterior Helarte",
  "estado": "DISPONIBLE"
}
```

---

## 5. Módulo de Reservas (`/reservas`)

### 5.1 Crear Reserva para la Heladería
- **Método**: `POST`
- **URL**: `http://localhost:8080/reservas`
- **Authorization**: Basic Auth (`cliente@helarte.com` / `cliente123`)
- **Body**:
```json
{
  "mesaId": 2,
  "servicioId": 2,
  "fechaReserva": "2026-10-15",
  "horaReserva": "17:30",
  "cantidadPersonas": 4,
  "observaciones": "Cumpleaños, por favor preparar velita en la copa degustación."
}
```

### 5.2 Listar Todas las Reservas
- **Método**: `GET`
- **URL**: `http://localhost:8080/reservas`
- **Authorization**: Basic Auth (ADMIN o EMPLEADO)

### 5.3 Consultar "Mis Reservas"
- **Método**: `GET`
- **URL**: `http://localhost:8080/reservas/mis-reservas`
- **Authorization**: Basic Auth (`cliente@helarte.com` / `cliente123`)

### 5.4 Consultar Reservas por Fecha (JPQL)
- **Método**: `GET`
- **URL**: `http://localhost:8080/reservas/fecha?fecha=2026-10-15`
- **Authorization**: Basic Auth (ADMIN o EMPLEADO)

### 5.5 Cambiar Estado de la Reserva (Empleado/Admin)
- **Método**: `PUT`
- **URL**: `http://localhost:8080/reservas/1/estado`
- **Authorization**: Basic Auth (ADMIN o EMPLEADO)
- **Body**:
```json
{
  "estado": "CONFIRMADA"
}
```

### 5.6 Cancelar Reserva
- **Método**: `DELETE`
- **URL**: `http://localhost:8080/reservas/1`
- **Authorization**: Basic Auth (`cliente@helarte.com` / `cliente123`)
