import { useContext, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { SocioContext } from '../contexto/SocioContext';
import { inscribir, mensajeDeError, obtenerActividades } from '../servicios/api';
import TarjetaActividad from './TarjetaActividad';

// Ruta "/actividades": catálogo con filtro por disciplina y cupo disponible
function ListaActividades() {
  const { socio } = useContext(SocioContext);
  const navigate = useNavigate();
  const [actividades, setActividades] = useState([]);
  const [disciplinas, setDisciplinas] = useState([]);
  const [disciplina, setDisciplina] = useState('');
  const [cargando, setCargando] = useState(true);
  const [mensaje, setMensaje] = useState(null); // { tipo: 'exito' | 'error', texto }
  const [recargas, setRecargas] = useState(0);

  // Arreglo de dependencias vacío: se ejecuta una sola vez, al mostrar la pantalla.
  // Arma la lista de disciplinas para el filtro.
  useEffect(() => {
    obtenerActividades()
      .then((todas) => {
        const unicas = [...new Set(todas.map((a) => a.disciplina))].sort((a, b) => a.localeCompare(b, 'es'));
        setDisciplinas(unicas);
      })
      .catch(() => setDisciplinas([]));
  }, []);

  // Depende de [disciplina, recargas]: se vuelve a pedir la lista a la API cada vez que el socio
  // cambia el filtro, o después de una inscripción (para mostrar el cupo actualizado)
  useEffect(() => {
    setCargando(true);
    obtenerActividades(disciplina)
      .then((datos) => setActividades(datos))
      .catch((e) => setMensaje({ tipo: 'error', texto: mensajeDeError(e) }))
      .finally(() => setCargando(false));
  }, [disciplina, recargas]);

  async function manejarInscripcion(actividadId) {
    try {
      const inscripcion = await inscribir(socio.id, actividadId);
      setMensaje({ tipo: 'exito', texto: `¡Listo! Quedaste inscripto/a en ${inscripcion.actividad.nombre}.` });
      setRecargas(recargas + 1); // vuelve a ejecutar el useEffect: se actualiza el cupo disponible
    } catch (e) {
      // 409: el mensaje de la regla que rechazó la inscripción (sin cupo, duplicado, edad, moroso, máximo)
      setMensaje({ tipo: 'error', texto: mensajeDeError(e) });
    }
  }

  return (
    <section>
      <div className="titulo-pagina">
        <h2>Actividades del club</h2>
        <div className="filtro">
          <label htmlFor="filtro-disciplina">Disciplina</label>
          <select id="filtro-disciplina" value={disciplina} onChange={(e) => setDisciplina(e.target.value)}>
            <option value="">Todas</option>
            {disciplinas.map((d) => (
              <option key={d} value={d}>{d}</option>
            ))}
          </select>
        </div>
      </div>

      {mensaje && <p className={`alerta alerta-${mensaje.tipo}`}>{mensaje.texto}</p>}

      {cargando ? (
        <p>Cargando actividades…</p>
      ) : (
        <div className="grilla-actividades">
          {actividades.map((actividad) => (
            <TarjetaActividad
              key={actividad.id}
              actividad={actividad}
              onVerDetalle={(id) => navigate(`/actividades/${id}`)}
              onInscribirse={manejarInscripcion}
            />
          ))}
        </div>
      )}

      {!cargando && actividades.length === 0 && <p>No hay actividades para esa disciplina.</p>}
    </section>
  );
}

export default ListaActividades;
