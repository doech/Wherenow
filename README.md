# 📱 WhereNow

Aplicación Android desarrollada en **Kotlin con Jetpack Compose** para la clase de Programación de Plataformas Móviles 
El objetivo de WhereNow es permitir a los usuarios crear una cuenta, y en base a un quiz, poder recomendar eventos ocurriendo cerca del usuario para que este pueda involucrarse en actividades sociales y tener círculos personalizados con amigos, conocidos o personas con los mismos intereses.

---

## 👥 Equipo de desarrollo
**Grupo #5 - coordinadora: Alejandra Sierra**

- **Alejandro Pérez** – signup / Auth  
- **Emily Góngora** – Login / Auth  
- **Esteban De la Peña** – Location Access / Auth
- **Martín Villatoro** – Quiz  
- **Camila Sandoval** – Confirmación / Diseño de pantallas y logo
- **Alejandra Sierra** - Confirmación / NavComposable y NavRoutes 

---

## 📝 Descripción general

WhereNow guía al usuario a través de un flujo de pantallas que van desde el **registro/login** hasta la **selección de intereses**, utilizando un sistema de navegación basado en **NavHost** y rutas centralizadas.  
La aplicación también implementa **buenas prácticas**, como el uso de `strings.xml` para los textos y un ícono personalizado en el launcher.

---

## 🖼️ Pantallas implementadas

- **AuthScreen**  
  - Tabs para Login y Sign Up en una sola pantalla.  
  - Validación básica de email y password.  
  - Uso de `strings.xml` para textos.  

- **LocationScreen**  
  - Explicación de beneficios de compartir ubicación.  
  - Botón principal “Allow Location Access” y opción “Skip for now”.  
  - Navega hacia el quiz.  

- **QuizScreen**  
  - Selección de intereses mediante `LazyVerticalGrid`.  
  - Cada interés representado con tarjetas interactivas.  
  - Botón “Continue” activo solo cuando se eligen al menos 3 categorías.  

- **ConfirmationScreen**  
  - Resumen de intereses seleccionados.  
  - Explicación de los próximos pasos.  
  - Botones de navegación para volver atrás o continuar.  

---

## ⏳ Pantallas pendientes / Mejoras

- Validación avanzada de login y signup (contra base de datos o API).  
- Implementación de “Forgot Password”.    
- Persistencia de intereses seleccionados (Room / DataStore).
- Pantalla de eventos
- Pantalla de círculos

---
