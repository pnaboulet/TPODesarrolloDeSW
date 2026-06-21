Nombre del proyecto: 501 management
Integrantes del grupo: Pablo Naboulet, Valentín Maraulo y Joaquin Marolda.
Descripción breve del sistema:
    Realizamos un sistema de gestión operativa para barrios cerrados, diseñado para centralizar y optimizar la administración de residentes, unidades funcionales, visitantes, proveedores y solicitudes de mantenimiento. El proyecto busca solucionar la falta de trazabilidad y desorganización operativa mediante una arquitectura moderna, modular y escalable, utilizando tecnologías como Spring Boot y PostgreSQL, garantizando una administración eficiente y una comunicación clara entre todos los actores involucrados.
Instrucciones para ejecutar el proyecto:
    Gracias a Docker contenerizamos el codigo y ahora es portable a cualquiera que tenga instalado el Docker engine. Simplemente se ejecuta el comando "docker compose up" y el proyecto se levanta en el puerto 8080. 
Patrones aplicados: 
    State
    Observer
    Strategy
    Facade
    Factory
    Builder
Principios SOLID aplicados: 
    Single Responsibility Principle
    Open/Closed Principle
    Dependency Inversion Principle
Patrones GRASP aplicados:
    Information Expert
    Creator
    Controller
    Polimorphism
    Low Coupling
    High Cohesion
Distribución de tareas:
    Valentín Maraulo:
        - Diseño de la jerarquía y persistencia de Personas.
        - Implementación del módulo de Control de Accesos (Patrón Strategy).
        - Configuración de base de datos y dockerización del entorno de desarrollo.

    Joaquin Marolda:
        - Desarrollo del módulo de Reclamos y Tareas de Mantenimiento (Patrón State).
        - Implementación del patrón Facade para unificar la capa de servicios.
        - Desarrollo de endpoints de API e integración de controladores.

    Pablo Naboulet:
        - Desarrollo del sistema de Notificaciones asíncronas (Patrón Observer).
        - Implementación de la creación de roles mediante fábricas (Patrón Factory).
        - Diseño de validaciones de negocio e inicialización de datos.

    Colaboración general (QA):
        - Todos los integrantes participaron de manera flexible y colaborativa en el control de calidad (QA), pruebas funcionales y testing del sistema.