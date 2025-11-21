
# 📱 WhereNow

Aplicación Android desarrollada en **Kotlin + Jetpack Compose**, como proyecto final del curso **Programación de Plataformas Móviles**.
WhereNow permite a los usuarios **descubrir eventos, crear círculos sociales y comunicarse dentro de ellos**, integrando Firebase para autenticación, datos en tiempo real y persistencia.

---

# 👥 Equipo de desarrollo

**Grupo #5 — Coordinadora: Alejandra Sierra**

* **Alejandro Pérez** — HomeScreen, implementación de círculos, motor de búsqueda
* **Emily Góngora** — Diseño de pantallas, implementación de eventos, recursos UI
* **Esteban De la Peña** — Arquitectura de Firebase (Auth + Firestore)
* **Martín Villatoro** — Arquitectura MVVM, Quiz de gustos, categorías e intereses
* **Camila Sandoval** — Diseño UI, manejo de usuario y navegación inicial

---

# 📝 Descripción General

WhereNow guía al usuario desde **registro/login**, pasando por el **quiz de intereses**, hasta navegar por:

* Círculos sociales
* Eventos recomendados
* Buscador unificado
* Chats básicos dentro de círculos

La arquitectura utiliza:

✔ MVVM
✔ Repository Pattern
✔ Jetpack Compose
✔ Firebase Auth + Firestore

---

# 🎯 Funcionalidades Implementadas

### 🔐 Autenticación

* Registro (email + contraseña)
* Inicio y cierre de sesión
* Perfil guardado en Firestore

### 🧭 Navegación (NavHost)

* Rutas centralizadas en `NavRoutes`
* Flujo completo Auth → Quiz → Home
* Navegación segura con `popUpTo()`

### 👤 Perfil de Usuario

* Nombre
* Username
* Email
* Intereses
* Ubicación automática

### 🏷 Quiz de Intereses

* Categorías dinámicas desde Firestore
* Guardado en `/users/{id}/categories`
* Impacto en recomendaciones

### 👥 Círculos Sociales

* Crear círculo (nombre, descripción, categoría)
* Guardado en `/circles`
* Visualización en HomeScreen
* Vista detallada con chats básicos

### 🎫 Eventos

* Carga desde Firestore
* Tarjetas con descripción, distancia, precio
* Join Request (modal)

### 🔍 Buscador global

* Usuarios
* Eventos
* Círculos
* Filtros por categoría

### 💬 Chats (UI básica)

* Burbuja de mensajes
* Autoscroll
* Encabezado del círculo
* Lista de mensajes tipo mock (listo para Firebase Realtime/Firestore en el futuro)

---

# 🏗 Estructura del Proyecto

```
/data
   /model          Modelos (User, Event, Circle…)
   /repository     Conexión Firebase (Auth, Users, Events, Circles)

/navigation
   NavRoutes.kt    Rutas principales
   NavComposable   Administrador del flujo

/ui
   /auth           Login, Signup, AuthViewModel
   /home           HomeScreen, CreateCircle, Chats
   /events         Lista de eventos + ViewModel
   /circles        CircleViewModel
   /search         Search + resultados, SearchViewModel
   /components     Header, BottomBar, Dialogs reutilizables
   /quiz           Quiz de categorías
   /theme          Colores, tipografías, estilos

/util
   FirestoreSeeder Seeder de categorías
   MainActivity    Punto de entrada
```

---

# 🔥 Estructura de Base de Datos (Firestore)

### `/users/{userId}`

Información principal del usuario.

Subcolecciones:

* `/events` — eventos donde participa
* `/circles` — círculos donde está
* `/categories` — intereses seleccionados

---

### `/events/{eventId}`

Información de eventos activos.

Subcolecciones:

* `/comments`
* `/chat` (estructura compatible para mensajes)
* `/media`

---

### `/circles/{circleId}`

Información general del círculo.

Subcolecciones:

* `/members`
* `/posts`
* `/chat` — lista para chats en tiempo real

---

### `/categories/{categoryId}`

Catálogo de intereses multilenguaje.

---

# 🌱 Escalabilidad del Sistema

WhereNow fue diseñado para crecer sin romper la estructura:

### ✔ Arquitectura MVVM

Cada pantalla tiene su ViewModel → más fácil extender funcionalidades.

### ✔ Repositorios desacoplados

Cambiar Firebase por otra base de datos no rompe la app.

### ✔ Firestore modular

Colecciones limpias, subcolecciones livianas y queries rápidas.

### ✔ Chats listos para tiempo real

La UI ya está implementada — solo falta conectar listener de Firestore.

### ✔ Navegación flexible

Agregar pantallas nuevas es inmediato debido al sistema de rutas.

---

# 🚀 Conclusión

WhereNow es un proyecto completo, modular y escalable que combina:

* Arquitectura robusta
* Firebase real para producción
* UI moderna con Jetpack Compose
* Flujo sólido de usuario
* Funciones colaborativas (círculos, eventos, búsqueda, chat)

---

