# VallaGest 🏢📡

## ¿Qué es VallaGest?
**VallaGest** es una solución móvil integral diseñada para la gestión y control de alquiler de vallas publicitarias. La aplicación permite administrar de manera eficiente el inventario de vallas, su ubicación, estado de disponibilidad y el registro de pagos, optimizando la operatividad de las empresas de publicidad exterior.

## ¿Cómo funciona?
La aplicación utiliza una arquitectura **Offline-First**, lo que garantiza su funcionamiento incluso sin conexión a internet:
1.  **Almacenamiento Local:** Todos los datos se registran primero en una base de datos local (Room).
2.  **Sincronización Inteligente:** Al detectar una conexión estable, el sistema sincroniza automáticamente los datos locales con un servidor central (API REST).
3.  **Gestión de Pagos:** Incluye un flujo para registrar pagos por transferencia bancaria (con carga de comprobantes) y simulaciones de pago con tarjeta.

## Tecnologías Utilizadas
* **Lenguaje:** [Kotlin](https://kotlinlang.org/)
* **Interfaz de Usuario:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material Design 3)
* **Persistencia Local:** Room Database
* **Red y API:** Retrofit 2 & OkHttp
* **Inyección de Dependencias:** Hilt / Dagger
* **Arquitectura:** Clean Architecture + MVVM (Model-View-ViewModel)

## Video de Presentación
Puedes ver el funcionamiento detallado de la aplicación en el siguiente enlace:
🎬 [Ver presentación de VallaGest en YouTube](https://youtu.be/q4BnjQhvn2Q?si=WgecuW7CoRAyL1s0) 

## Imagenes de la app 

<img width="720" height="1600" alt="WhatsApp Image 2026-04-15 at 19 03 33" src="https://github.com/user-attachments/assets/d014e27b-93bb-4835-a4d4-03f83af0006c" /> 

<img width="720" height="1600" alt="WhatsApp Image 2026-04-15 at 19 03 33 (1)" src="https://github.com/user-attachments/assets/1f49d52a-57a4-42c1-9664-111017ba3e6d" />

<img width="1080" height="2400" alt="WhatsApp Image 2026-04-15 at 19 03 33 (2)" src="https://github.com/user-attachments/assets/9a31fe56-14c2-4e5b-a23c-c81405f90920" /> 

<img width="1080" height="2400" alt="WhatsApp Image 2026-04-15 at 19 03 33 (3)" src="https://github.com/user-attachments/assets/74102b8b-b6e1-4bcb-8a88-0ad463c07c81" /> 

<img width="1080" height="2400" alt="WhatsApp Image 2026-04-15 at 19 03 33 (4)" src="https://github.com/user-attachments/assets/51276879-8641-4d6e-acbd-db76eb239822" />

 <img width="720" height="1600" alt="WhatsApp Image 2026-04-15 at 19 03 33 (5)" src="https://github.com/user-attachments/assets/6e1bff1d-af35-4033-9869-fdb976dcdde2" />

<img width="1080" height="2400" alt="WhatsApp Image 2026-04-15 at 19 03 33 (6)" src="https://github.com/user-attachments/assets/e76b9a2d-21db-418f-91f9-20fc409bc53e" />

---
**Desarrollado por:**
**Luis Angel Gabriel Morillo**
Estudiante de Ingeniería de Sistemas - UCNE 🇩🇴
