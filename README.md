# 📱 WhereNow

Aplicación Android desarrollada en **Kotlin con Jetpack Compose** para la clase de Programación de Plataformas Móviles 
El objetivo de WhereNow es permitir a los usuarios crear una cuenta, y en base a un quiz, poder recomendar eventos ocurriendo cerca del usuario para que este pueda involucrarse en actividades sociales y tener círculos personalizados con amigos, conocidos o personas con los mismos intereses.

---

## 👥 Equipo de desarrollo
**Grupo #5 - coordinadora: Alejandra Sierra**

- **Alejandro Pérez**  
- **Emily Góngora**  
- **Esteban De la Peña** 
- **Martín Villatoro**   
- **Camila Sandoval** 

---

## 📝 Descripción general

WhereNow guía al usuario a través de un flujo de pantallas que van desde el **registro/login** hasta la **selección de intereses**, utilizando un sistema de navegación basado en **NavHost** y rutas centralizadas.  
La aplicación también implementa **buenas prácticas**, como el uso de `strings.xml` para los textos y un ícono personalizado en el launcher.

---

# 📘 WhereNow – Estructura de Base de Datos (Firestore)

## 📖 Descripción General

WhereNow es una aplicación móvil desarrollada en Kotlin + Jetpack Compose que conecta usuarios con eventos locales y comunidades afines.  
Esta base de datos en **Firebase Firestore** utiliza un modelo **híbrido** entre relaciones implícitas y subcolecciones para equilibrar escalabilidad, rendimiento y simplicidad en consultas.

El objetivo es permitir:
- Autenticación y perfiles de usuario.
- Gestión de eventos geolocalizados.
- Creación y participación en círculos (grupos sociales).
- Comunicación en tiempo real mediante subcolecciones de chat.
- Personalización de experiencia mediante intereses (categorías).

---

## 🎗️ Arquitectura de Datos

### 📂 Colección general: `/users`

Guarda la información principal de cada usuario.

```json
{
  "id": "user_123",
  "name": "David Hernandez",
  "username": "david_hdz",
  "email": "david@example.com",
  "photoUrl": "https://.../avatar.jpg",
  "bio": "Music & fitness lover.",
  "language": "es",
  "location": {
    "city": "Guatemala City",
    "lat": 14.6349,
    "lng": -90.5069
  },
  "createdAt": <serverTimestamp>,
  "status": "active"
}
```

#### 🔸 Subcolección: `/users/{userId}/events`
Eventos donde el usuario participa o que ha creado.
```json
{
  "eventId": "E001",
  "joinedAt": <serverTimestamp>,
  "role": "participant"
}
```

#### 🔸 Subcolección: `/users/{userId}/circles`
Círculos a los que pertenece el usuario.
```json
{
  "circleId": "C001",
  "joinedAt": <serverTimestamp>,
  "role": "member"
}
```

#### 🔸 Subcolección: `/users/{userId}/categories`
Intereses o categorías seleccionadas por el usuario (relación usuario ↔ categoría).
```json
{
  "categoryId": "music",
  "selectedAt": <serverTimestamp>
}
```

---

### 📂 Colección principal: `/events`

Cada documento dentro de esta colección representa un evento publicado en la aplicación **WhereNow**.  
La estructura refleja los campos actualmente utilizados por la app en Firestore.


```json
{
  "eventId": "EVT001",
  "name": "Food Truck Festival",
  "description": "Over 20 gourmet food trucks featuring cuisines from around the world.",
  "location": "Brooklyn Bridge Park",
  "distanceText": "1.2 miles away",
  "priceText": "15",
  "interested": 89,
  "status": "active",
  "createdAt": "2025-10-27T23:45:00Z",
  "startAt": "2025-12-05T18:00:00Z"
}

```

#### 🔸 Subcolección: `/events/{eventId}/comments`
Comentarios de los usuarios dentro del evento.
```json
{
  "id": "comment_001",
  "userId": "user_456",
  "text": "Great music yesterday!",
  "createdAt": <serverTimestamp>
}
```

#### 🔸 Subcolección: `/events/{eventId}/chat`
Mensajes en tiempo real del evento.
```json
{
  "id": "message_001",
  "senderId": "user_789",
  "message": "Who's coming tonight?",
  "createdAt": <serverTimestamp>
}
```

#### 🔸 Subcolección: `/events/{eventId}/media`
Archivos multimedia asociados al evento (imágenes, flyers, etc.).
```json
{
  "id": "media_001",
  "url": "https://firebasestorage.googleapis.com/...",
  "type": "image",
  "uploadedBy": "user_123",
  "createdAt": <serverTimestamp>
}
```

---

### 📂 Colección general: `/circles`

Representa grupos sociales creados por usuarios con intereses comunes.

```json
{
  "id": "C001",
  "name": "Music Lovers Network",
  "description": "Discussing concerts and music events.",
  "category": "music",
  "creatorId": "user_456",
  "visibility": "public",
  "membersCount": 24,
  "lastActivity": <timestamp>,
  "status": "active",
  "createdAt": <serverTimestamp>
}
```

#### 🔸 Subcolección: `/circles/{circleId}/members`
Miembros del círculo.
```json
{
  "userId": "user_123",
  "role": "admin",
  "joinedAt": <serverTimestamp>
}
```

#### 🔸 Subcolección: `/circles/{circleId}/posts`
Publicaciones internas (reemplaza el concepto de “posts” globales).
```json
{
  "id": "post_001",
  "authorId": "user_789",
  "text": "Anyone going to the jazz festival this weekend?",
  "eventRef": "/events/E001",
  "createdAt": <serverTimestamp>
}
```

#### 🔸 Subcolección: `/circles/{circleId}/chat`
Mensajes en tiempo real dentro del grupo.
```json
{
  "id": "message_001",
  "senderId": "user_789",
  "message": "Let's meet there at 8pm!",
  "createdAt": <serverTimestamp>
}
```

---

### 🏷️ Colección general: `/categories`

Catálogo de categorías utilizadas para eventos, círculos e intereses de usuario.  
Este contenido es **quemado y multilenguaje**.

```json
{
  "id": "music",
  "name": { "en": "Music", "es": "Música" },
  "color": "#A855F7",
  "icon": "music",
  "status": "active",
  "createdAt": <serverTimestamp>
}
```

---

### ⚙️ Colección opcional: `/app_metadata`

Configuraciones globales del sistema.

```json
{
  "version": "1.0.0",
  "minSupportedVersion": "0.9.0",
  "maintenance": false,
  "supportedLanguages": ["en", "es"],
  "createdAt": <serverTimestamp>
}
```

---

## 🔗 Relaciones Principales

| Relación | Descripción |
|-----------|--------------|
| `/users/{id}/events → /events/{eventId}` | Registra la participación de un usuario en un evento. |
| `/users/{id}/circles → /circles/{circleId}` | Conecta al usuario con los círculos donde participa. |
| `/users/{id}/categories → /categories/{categoryId}` | Define los intereses del usuario. |
| `/circles/{circleId}/posts.eventRef → /events/{eventId}` | Permite enlazar publicaciones con eventos. |
| `/events/{eventId}/comments.userId → /users/{id}` | Asocia comentarios con el autor correspondiente. |

---

## 🌍 Soporte Multilenguaje

Los campos multilenguaje solo se aplican a **datos estáticos o predefinidos**, como:
- `categories.name`
- `events.name`
- `events.description`

No se usa en contenido dinámico (comentarios, mensajes, etc.).

Formato estándar:
```json
"name": { "en": "Music", "es": "Música" }
```

---

## 🧠 Notas de Diseño

- Cada documento incluye `createdAt` y `status` para control y trazabilidad.
- Las **subcolecciones son opcionales**, se crean solo cuando hay datos.
- La estructura está optimizada para:
  - Consultas rápidas por usuario, evento o círculo.
  - Filtro por categorías (intereses).
  - Soporte de tiempo real con Firestore listeners.

---

## 🧉 Ejemplo de árbol de rutas

```
/users
  /{userId}
    /events
    /circles
    /categories
/events
  /{eventId}
    /comments
    /chat
    /media
/circles
  /{circleId}
    /members
    /posts
    /chat
/categories
  /{categoryId}
/app_metadata
  /{docId}
```

---

## ✅ Ventajas del Modelo

- Estructura modular, escalable y sin redundancias.
- Soporta feed social **contextualizado** dentro de círculos.
- Permite búsquedas personalizadas por ubicación e intereses.
- Compatible con una arquitectura híbrida (Firestore + PostgreSQL opcional).
- Facilita sincronización y análisis de datos sin romper la jerarquía lógica.

---


