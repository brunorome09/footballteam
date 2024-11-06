# FootballTeam App

Este repositorio contiene el código fuente de la aplicación **FootballTeam App**, que utiliza Docker para facilitar su despliegue y administración.

## Comandos para levantar el Docker

1. **Construir la imagen de Docker:**

   ```bash
   docker build -t footballteam-app:latest .
   ```

2. **Ejecutar el contenedor:**

   ```bash
   docker run -p 9090:9090 footballteam-app:latest
   ```

   Este comando expone la aplicación en el puerto `9090`.

## Documentación

Una vez que la aplicación esté en ejecución, puedes acceder a la documentación general de la API a través de Swagger en la siguiente URL:

[http://localhost:9090/swagger-ui/index.html#](http://localhost:9090/swagger-ui/index.html#)
