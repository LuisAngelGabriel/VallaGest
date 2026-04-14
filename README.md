# VallaGest 🏢📡

## ¿Qué es VallaGest?
**VallaGest** es una solución móvil integral diseñada para la gestión y control de alquiler de vallas publicitarias. La aplicación permite administrar de manera eficiente el inventario de vallas, su ubicación, estado de disponibilidad y el registro de pagos, optimizando la operatividad de las empresas de publicidad exterior.

## ¿Cómo funciona?
La aplicación utiliza una arquitectura **Offline-First**, lo que garantiza su funcionamiento incluso sin conexión a internet:
1.  **Almacenamiento Local:** Todos los datos se registran primero en una base de datos local (Room).
2.  **Sincronización Inteligente:** Al detectar una conexión estable, el sistema sincroniza automáticamente los datos locales con un servidor central (API REST).
3.  **Gestión de Pagos:** Incluye un flujo para registrar pagos por transferencia bancaria (con carga de comprobantes) y simulaciones de pago con tarjeta.
4.  **Cero Duplicados:** Implementa una lógica de limpieza de IDs temporales que asegura la integridad de los datos durante la sincronización.

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

---
**Desarrollado por:**
**Luis Angel Gabriel Morillo**
Estudiante de Ingeniería de Sistemas - UCNE 🇩🇴
