# Pruebas de SocioClub (Parte E)

Hay cuatro tipos de pruebas:

| Tipo | Herramienta | Dónde está | Cantidad |
|---|---|---|---|
| Unitarias | Jest | `app-socios/src/utils/*.test.js` | 13 |
| De componente | Jest + React Testing Library | `app-socios/src/componentes/*.test.js` | 6 |
| De la API | Postman | `postman/SocioClub.postman_collection.json` | 24 pedidos |
| De sistema | Manual, de punta a punta | Este documento | 9 casos |

## 1. Pruebas unitarias y de componente (Jest)

Se corren desde `app-socios`:

```bash
npm test
```

Para correrlas una sola vez, sin quedarse mirando cambios: `CI=true npm test -- --watchAll=false`.

**Resultado: 4 archivos, 19 pruebas, 19 aprobadas.**

### `calcularCuotaMensual(actividades)` — RN6

| Prueba | Entrada | Esperado |
|---|---|---|
| Sin actividades | `[]` | 0 |
| 1 actividad | Yoga ($ 17.000) | 17.000 |
| 2 actividades: 10 % de descuento | Yoga + Natación ($ 43.000) | 38.700 |
| 3 actividades: 15 % de descuento | Yoga + Natación + Funcional ($ 65.000) | 55.250 |
| Entrada nula | `null`, `undefined` | 0 |
| Porcentaje según cantidad | 0, 1, 2, 3 | 0, 0, 10, 15 |

### `esDniValido(dni)` — RN7

| Prueba | Entrada | Esperado |
|---|---|---|
| 8 dígitos | `"35123987"` | `true` |
| 7 dígitos | `"1234567"` | `true` |
| Con puntos | `"35.123.987"` | `false` |
| Con letras | `"35123A87"` | `false` |
| 6 dígitos | `"123456"` | `false` |
| 9 dígitos | `"123456789"` | `false` |
| Vacío o no es texto | `""`, `null`, `35123987` (número) | `false` |

### `TarjetaActividad` (React Testing Library)

Se usan `render`, `screen` y `fireEvent`. Las funciones `onInscribirse` y `onVerDetalle` se reemplazan por `jest.fn()` para saber si se las llamó.

| Prueba | Qué verifica |
|---|---|
| Muestra los datos | Nombre "Yoga", disciplina, cuota "$ 17.000" y "3 lugares disponibles" |
| Cupo 0 | Muestra "Sin cupo", el botón "Inscribirme" está deshabilitado y un clic no llama a `onInscribirse` |
| Cupo 0, estilo | El `<article>` tiene la clase `sin-cupo` (tarjeta en gris) |
| Clic en "Inscribirme" | Llama a `onInscribirse` una vez, con el id 8 |
| Clic en "Ver detalle" | Llama a `onVerDetalle` con el id 8 |

### `ResumenCuota` (React Testing Library)

| Prueba | Qué verifica |
|---|---|
| 2 actividades | Muestra "Bonificación por 2 actividades: 10 %" y el total "$ 38.700" |

## 2. Pruebas de la API (Postman)

Importar `postman/SocioClub.postman_collection.json` en Postman y correr la colección completa (*Run collection*) con el backend levantado. Cada pedido tiene tests que verifican el código de estado y, en los 409, la regla y el mensaje.

| Carpeta | Casos |
|---|---|
| Actividades | Listar, filtrar por disciplina, detalle, 404, crear (201), crear con datos inválidos (400), modificar, modificar inexistente (404), eliminar con inscriptos (409), eliminar (204), eliminar inexistente (404) |
| Socios | Buscar por DNI, DNI inexistente (404), inscripciones activas, socio inexistente (404) |
| Inscripciones | Inscripción exitosa (201), RN1, RN2, RN3, RN4 y RN5 (409), socio inexistente (404), baja lógica (200), baja inexistente (404) |

**Resultado** (corrida con newman, el ejecutor de Postman por consola, dos veces seguidas): 48 pedidos, 74 assertions, 0 fallas. La colección deja la base como estaba, así que se puede volver a correr.

## 3. Prueba de sistema (de punta a punta)

Cada caso se hace desde la interfaz, como lo haría un socio o la secretaria, y recorre todo el sistema: React o Thymeleaf → controlador → servicio → repositorio → MySQL → respuesta en pantalla.

- **Fecha de ejecución:** 22/11/2026.
- **Datos:** base recién cargada con `data.sql`.
- **Orden:** los casos se corren en el orden de la tabla, porque algunos dependen de los anteriores (por ejemplo, CP2 necesita que CP1 haya llenado Yoga).

| N.º | Caso | Pasos | Resultado esperado | Resultado obtenido | Estado |
|---|---|---|---|---|---|
| CP1 | Inscripción exitosa | App de socios: ingresar con DNI 42345987 (Bazán, 29 años) → Actividades → Yoga → "Inscribirme" | Mensaje de confirmación; Yoga pasa de 1 lugar libre a "Sin cupo" | "¡Listo! Quedaste inscripto/a en Yoga." El detalle muestra "Sin cupo". En MySQL queda la inscripción 29 en estado ACTIVA | Aprobado |
| CP2 | RN1: sin cupo | 1) App de socios: ingresar con DNI 39876012 (Maldonado) → Yoga. 2) Secretaría: Inscribir → Maldonado → Yoga → "Inscribir" | 1) Botón "Inscribirme" deshabilitado y etiqueta "Sin cupo". 2) Se rechaza con "La actividad no tiene cupo disponible" | 1) Botón deshabilitado, "Sin cupo". 2) "RN1: La actividad no tiene cupo disponible" | Aprobado |
| CP3 | RN2: inscripción duplicada | App de socios: DNI 35123987 (Moyano) → Natación (ya inscripta) → "Inscribirme" | Se rechaza con "El socio ya está inscripto en esta actividad" | "El socio ya está inscripto en esta actividad" | Aprobado |
| CP4 | RN3: edad fuera de rango | App de socios: DNI 55123456 (Bustamante, 8 años) → Vóley mixto (13 a 60) → "Inscribirme" | Se rechaza indicando el rango permitido | "La edad del socio (8 años) está fuera del rango permitido para Vóley mixto: de 13 a 60 años" | Aprobado |
| CP5 | RN4: socio moroso | App de socios: DNI 20345678 (Agüero, MOROSO) → Fútbol infantil → "Inscribirme" | Se rechaza con "Debe regularizar su cuota en secretaría" | "Debe regularizar su cuota en secretaría". Además, "Mis actividades" muestra el aviso de cuota impaga | Aprobado |
| CP6 | RN5: máximo 3 actividades | App de socios: DNI 35123987 (Moyano, 3 activas) → Ajedrez → "Inscribirme" | Se rechaza con "Alcanzó el máximo de 3 actividades" | "Alcanzó el máximo de 3 actividades" | Aprobado |
| CP7 | Baja lógica y cuota (RF9, RN6) | App de socios: DNI 35123987 → Mis actividades → Yoga → "Darme de baja" | La inscripción pasa a BAJA y la cuota se recalcula de 15 % a 10 % | Total antes: $ 55.250 (3 actividades). Después: "Te diste de baja de Yoga.", total $ 43.200 con "Bonificación por 2 actividades: 10 %". En MySQL, la inscripción 1 quedó en BAJA (no se borró) | Aprobado |
| CP8 | La baja libera el cupo | App de socios: DNI 39876012 (Maldonado) → Yoga → "Inscribirme" | Ahora sí hay 1 lugar y la inscripción se acepta | Antes: "1 de 15 lugares disponibles". Después: "¡Listo! Quedaste inscripto/a en Yoga." y "Sin cupo" | Aprobado |
| CP9 | RN7: DNI inválido | App de socios: ingresar "42.345.987" → "Ingresar" | No se envía; muestra el error junto al campo | "El DNI debe tener 7 u 8 números, sin puntos." y la app se queda en la pantalla de ingreso, sin llamar a la API | Aprobado |

**Resultado: 9 casos, 9 aprobados.**

**Observación sobre CP2:** desde la app de socios no se puede ni intentar la inscripción, porque el botón se deshabilita cuando el cupo es 0. Por eso el rechazo de RN1 del backend se prueba desde la secretaría, que permite elegir una actividad sin cupo, y también con el pedido "RN1 - sin cupo (409)" de Postman. La regla está en `InscripcionService`, así que la valida igual quien sea que haga la inscripción.
