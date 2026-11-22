# Mi Club — app de socios (Parte D)

App React para que los socios del Club Los Algarrobos ingresen con su DNI, vean las actividades, se inscriban, se den de baja y consulten su cuota mensual estimada.

Creada con create-react-app. Usa React Router (`BrowserRouter`) y Axios.

## Cómo levantarla
1. Levantar antes el backend Spring Boot (`/backend`), que escucha en `http://localhost:8080`.
2. En esta carpeta:
   ```bash
   npm install
   npm start
   ```
3. Abrir `http://localhost:3000`.

## Rutas
| Ruta | Pantalla |
|---|---|
| `/` | Ingreso con DNI |
| `/actividades` | Catálogo con filtro por disciplina y cupo disponible |
| `/actividades/:id` | Detalle de la actividad con el botón "Inscribirme" |
| `/mis-actividades` | Inscripciones activas, "Darme de baja" y cuota mensual estimada (RN6) |

## Organización
- `src/componentes/`: componentes funcionales (`Encabezado`, `Pie`, `Ingreso`, `TarjetaActividad`, `ListaActividades`, `DetalleActividad`, `MisActividades`, `ResumenCuota`, `RutaProtegida`) y sus hojas CSS.
- `src/contexto/SocioContext.js`: el socio que ingresó, compartido con `createContext` / `useContext`.
- `src/servicios/api.js`: toda la comunicación con el backend (Axios).
- `src/utils/`: funciones puras `calcularCuotaMensual` (RN6) y `esDniValido` (RN7).

## DNI de prueba
| DNI | Socio | Para qué sirve |
|---|---|---|
| 35123987 | Moyano, Lucía | Ya tiene 3 actividades (RN5) |
| 20345678 | Agüero, Roberto | Cuota morosa (RN4) |
| 42345987 | Bazán, Rocío | Adulta sin actividades: ocupa el último lugar de Yoga |
| 39876012 | Maldonado, Diego | Adulto sin actividades: rechazado por cupo en Yoga (RN1) |
| 55123456 | Bustamante, Tomás | 8 años, fuera de rango en Vóley (RN3) |
