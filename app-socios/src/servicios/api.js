import axios from 'axios';

// Toda la comunicación con el backend pasa por este archivo.
// Si cambia la dirección del servidor, se cambia solo acá.
const api = axios.create({
  baseURL: 'http://localhost:8080/api',
});

// GET /api/actividades o /api/actividades?disciplina=Fútbol
export async function obtenerActividades(disciplina) {
  const params = disciplina ? { disciplina } : {};
  const respuesta = await api.get('/actividades', { params });
  return respuesta.data;
}

// GET /api/actividades/{id}
export async function obtenerActividad(id) {
  const respuesta = await api.get(`/actividades/${id}`);
  return respuesta.data;
}

// GET /api/socios/dni/{dni}
export async function buscarSocioPorDni(dni) {
  const respuesta = await api.get(`/socios/dni/${dni}`);
  return respuesta.data;
}

// GET /api/socios/{id}/inscripciones (solo las ACTIVAS)
export async function obtenerInscripciones(socioId) {
  const respuesta = await api.get(`/socios/${socioId}/inscripciones`);
  return respuesta.data;
}

// POST /api/inscripciones  { socioId, actividadId }
export async function inscribir(socioId, actividadId) {
  const respuesta = await api.post('/inscripciones', { socioId, actividadId });
  return respuesta.data;
}

// PUT /api/inscripciones/{id}/baja
export async function darDeBaja(inscripcionId) {
  const respuesta = await api.put(`/inscripciones/${inscripcionId}/baja`);
  return respuesta.data;
}

// Arma el mensaje para mostrarle al socio cuando algo sale mal.
// Si el backend respondió (409, 404, 400) trae { "error": "..." } en el cuerpo.
export function mensajeDeError(error) {
  if (error.response && error.response.data && error.response.data.error) {
    return error.response.data.error;
  }
  return 'No se pudo conectar con el servidor. Probá de nuevo en un rato.';
}
