# SocioClub — Club Social y Deportivo Los Algarrobos

Trabajo Práctico Integrador de **Taller de Construcción de Software** (Universidad Siglo 21).

Sistema web para el Club Los Algarrobos, que hoy maneja las inscripciones a mano. Tiene tres caras:

| Carpeta | Qué es | Para quién | Tecnología |
|---|---|---|---|
| [`/sitio-publico`](sitio-publico) | Sitio institucional con catálogo de actividades y formulario de preinscripción | Público en general | HTML, CSS y JavaScript |
| [`/backend`](backend) | Backoffice de la secretaría y API REST | Secretaría del club / app de socios | Spring Boot, Thymeleaf, JPA, MySQL |
| [`/app-socios`](app-socios) | App "Mi Club": ingreso con DNI, inscripción, baja y cuota mensual | Socios | React (create-react-app) y Axios |

También están:
- [`/postman`](postman): colección de Postman con todos los endpoints de la API.
- [`/docs`](docs): las pruebas ([`pruebas.md`](docs/pruebas.md)), la [bitácora de IA](docs/bitacora-ia.md) y el [informe técnico en PDF](docs/TPI_SocioClub_GrupoN.pdf). El PDF se genera desde [`docs/informe/informe.html`](docs/informe/informe.html) con *Imprimir → Guardar como PDF* en Chrome.

## Integrantes

| Integrante | Usuario de GitHub |
|---|---|
| Martín Garazurreta | [@MartinGarazurreta](https://github.com/MartinGarazurreta) |
| Jerenuas Peres | — |
| _(completar)_ | — |
| _(completar)_ | — |

## Requisitos

| Programa | Versión usada | Para qué |
|---|---|---|
| Java (JDK) | 25 | Backend |
| MySQL | 8 o superior | Base de datos |
| Node.js y npm | Node 18 o superior | App React |
| Postman | Cualquiera | Probar la API (opcional) |

Maven no hace falta instalarlo: el proyecto trae el *wrapper* (`mvnw`).

## 1. Crear la base de datos

Con MySQL andando, entrar como `root`:

```bash
mysql -u root -p
```

Y crear la base y el usuario del TP:

```sql
CREATE DATABASE socioclub CHARACTER SET utf8mb4;
CREATE USER 'socioclub'@'localhost' IDENTIFIED BY 'socioclub';
GRANT ALL PRIVILEGES ON socioclub.* TO 'socioclub'@'localhost';
FLUSH PRIVILEGES;
```

Para usar otro usuario o contraseña hay que cambiarlos en [`backend/src/main/resources/application.properties`](backend/src/main/resources/application.properties).

**Las tablas no se crean a mano.** Al arrancar el backend, Hibernate las crea a partir de las entidades (`ddl-auto=update`). Después Spring ejecuta [`data.sql`](backend/src/main/resources/data.sql), que carga los datos iniciales:
- las 8 actividades de la sección 3.3 del enunciado;
- 22 socios de prueba, entre ellos 2 morosos;
- 28 inscripciones.

El script usa `INSERT IGNORE`, así que se puede arrancar el backend muchas veces sin duplicar datos.

Para volver la base a cero (por ejemplo, antes de una demo):

```sql
DROP DATABASE socioclub;
CREATE DATABASE socioclub CHARACTER SET utf8mb4;
```

## 2. Levantar el backend

```bash
cd backend
./mvnw spring-boot:run
```

En Windows: `mvnw.cmd spring-boot:run`. Si en Mac o Linux da "permiso denegado", usar `sh mvnw spring-boot:run`.

Queda escuchando en `http://localhost:8080`:

| Dirección | Qué es |
|---|---|
| `http://localhost:8080/` | Backoffice de la secretaría (Thymeleaf) |
| `http://localhost:8080/actividades` | ABM de actividades e inscriptos por actividad |
| `http://localhost:8080/socios` | Socios: listado paginado, búsqueda por apellido, alta, edición y ficha |
| `http://localhost:8080/inscripciones/nueva` | Inscribir a un socio (aplica RN1 a RN5) |
| `http://localhost:8080/api/...` | API REST (ver la tabla de endpoints más abajo) |

## 3. Levantar la app React

Con el backend ya andando, en otra terminal:

```bash
cd app-socios
npm install
npm start
```

Se abre en `http://localhost:3000`. La API permite pedidos desde ese origen (CORS).

## 4. Abrir el sitio público

No necesita servidor: alcanza con abrir [`sitio-publico/index.html`](sitio-publico/index.html) en el navegador. También se puede usar la extensión Live Server de VS Code.

## Usuarios y DNI de prueba

La app de socios no pide contraseña: se ingresa con el DNI. El backoffice tampoco tiene usuario, porque Spring Security no forma parte de la materia.

| DNI | Socio | Edad | Situación | Para probar |
|---|---|---|---|---|
| `42345987` | Rocío Bazán | 29 | Al día, sin actividades | Inscripción exitosa: ocupa el último lugar de Yoga |
| `39876012` | Diego Maldonado | 32 | Al día, sin actividades | **RN1**: Yoga sin cupo (después de inscribir a Bazán) |
| `35123987` | Lucía Moyano | 36 | Al día, 3 actividades | **RN2** (ya está en Natación), **RN5** (máximo 3) y baja |
| `55123456` | Tomás Bustamante | 8 | Al día | **RN3**: fuera de rango en Vóley mixto (13 a 60) |
| `20345678` | Roberto Agüero | 58 | **Moroso** | **RN4**: debe regularizar la cuota |
| `50876543` | Joaquín Herrera | 15 | **Moroso** | RN4 con un menor |
| `42.345.987` | — | — | — | **RN7**: la app rechaza el DNI con puntos sin llamar a la API |

Yoga arranca con 14 de 15 lugares ocupados para poder mostrar RN1.

## Reglas de negocio

| Regla | Qué dice | Dónde está |
|---|---|---|
| RN1 | No se inscribe si la actividad no tiene cupo | `InscripcionService` (backend) |
| RN2 | Un socio no puede estar dos veces en la misma actividad | `InscripcionService` |
| RN3 | La edad del socio tiene que estar dentro del rango de la actividad | `InscripcionService` |
| RN4 | Un socio moroso no puede inscribirse | `InscripcionService` |
| RN5 | Máximo 3 actividades activas por socio | `InscripcionService` |
| RN6 | Descuento de 10 % con 2 actividades y de 15 % con 3 o más | `calcularCuotaMensual.js` (app React) |
| RN7 | El DNI tiene 7 u 8 números, sin puntos | `esDniValido.js` (app React) y `script.js` (sitio público) |

Las reglas RN1 a RN5 están en la capa de servicio, así que las respetan por igual la secretaría (Thymeleaf), la API y la app React.

## Endpoints de la API

| Verbo | Ruta | Qué hace | Respuestas |
|---|---|---|---|
| GET | `/api/actividades` | Lista las actividades con su cupo disponible. Acepta `?disciplina=Fútbol` | 200 |
| GET | `/api/actividades/{id}` | Detalle de una actividad | 200 / 404 |
| POST | `/api/actividades` | Crea una actividad | 201 / 400 |
| PUT | `/api/actividades/{id}` | Modifica una actividad | 200 / 400 / 404 |
| DELETE | `/api/actividades/{id}` | Elimina una actividad sin inscriptos activos | 204 / 404 / 409 |
| GET | `/api/socios/dni/{dni}` | Busca un socio por DNI (ingreso a la app) | 200 / 404 |
| GET | `/api/socios/{id}/inscripciones` | Inscripciones activas de un socio | 200 / 404 |
| POST | `/api/inscripciones` | Inscribe a un socio. Cuerpo: `{ "socioId": 21, "actividadId": 8 }` | 201 / 400 / 404 / 409 |
| PUT | `/api/inscripciones/{id}/baja` | Baja lógica: el estado pasa a BAJA | 200 / 404 |

Cuando una regla rechaza la inscripción, la respuesta es un 409 con este cuerpo:

```json
{ "error": "La actividad no tiene cupo disponible", "regla": "RN1" }
```

## Pruebas

| Tipo | Cómo se corre | Resultado |
|---|---|---|
| Unitarias y de componente (Jest + React Testing Library) | `cd app-socios` y `npm test` | 19 de 19 aprobadas |
| API (Postman) | Importar [`postman/SocioClub.postman_collection.json`](postman/SocioClub.postman_collection.json) y correr la colección con el backend andando | 24 pedidos, 0 fallas |
| Sistema (de punta a punta) | Seguir la tabla de [`docs/pruebas.md`](docs/pruebas.md) con la base recién cargada | 9 de 9 casos aprobados |

La colección de Postman deja la base como estaba, así que se puede correr varias veces.

## Problemas frecuentes

| Problema | Causa | Solución |
|---|---|---|
| `Communications link failure` al arrancar el backend | MySQL está apagado | Prenderlo (`brew services start mysql` en Mac, o desde *Servicios* en Windows) |
| `Access denied for user 'socioclub'` | Falta crear el usuario | Correr el paso 1 |
| La app React muestra "No se pudo conectar con el servidor" | El backend no está andando | Levantarlo (paso 2) |
| `Port 8080 was already in use` | Ya hay otra instancia del backend | Cerrar la otra terminal o el otro proceso |
