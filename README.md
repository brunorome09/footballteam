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

Aquí tienes una actualización de la sección "Documentación" para el README con las indicaciones que deseas agregar:

---

### Documentación
Una vez que la aplicación esté en ejecución, puedes acceder a la documentación general de la API a través de Swagger en la siguiente URL:
[http://localhost:9090/swagger-ui/index.html#](http://localhost:9090/swagger-ui/index.html#)

Desde la documentación de Swagger, podrás ver la estructura de las operaciones `GET`, `PUT`, `POST`, y `DELETE`, así como probar su construcción. Sin embargo, para ejecutarlas, es necesario autenticarse primero.

Para autenticarse:
1. Realiza una solicitud `POST` a la URL `http://localhost:9090/auth/login` con el siguiente usuario y contraseña por defecto:
   ```json
   {
     "username": "test",
     "password": "12345"
   }
   ```

2. La respuesta incluirá un `Bearer Token`, que deberás utilizar en todas las solicitudes autenticadas.

Para realizar acciones:
- Usa `curl` o Postman configurando el `Bearer Token` obtenido en el encabezado `Authorization` de cada solicitud.
  
Con esta configuración, podrás realizar las operaciones de la API de forma segura.
