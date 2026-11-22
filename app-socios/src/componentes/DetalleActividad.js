import { useContext, useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { SocioContext } from '../contexto/SocioContext';
import { inscribir, mensajeDeError, obtenerActividad } from '../servicios/api';
import { formatearPesos } from '../utils/formatearPesos';

// Ruta "/actividades/:id": detalle de la actividad con el botón "Inscribirme"
function DetalleActividad() {
  const { id } = useParams(); // el :id de la URL
  const { socio } = useContext(SocioContext);
  const navigate = useNavigate();
  const [actividad, setActividad] = useState(null);
  const [error, setError] = useState('');
  const [mensaje, setMensaje] = useState(null);

  // Depende de [id]: si cambia la actividad de la URL, se vuelve a pedir
  useEffect(() => {
    obtenerActividad(id)
      .then((datos) => setActividad(datos))
      .catch((e) => setError(mensajeDeError(e)));
  }, [id]);

  async function manejarInscripcion() {
    try {
      await inscribir(socio.id, actividad.id);
      setMensaje({ tipo: 'exito', texto: `¡Listo! Quedaste inscripto/a en ${actividad.nombre}.` });
      setActividad(await obtenerActividad(id)); // cupo actualizado
    } catch (e) {
      setMensaje({ tipo: 'error', texto: mensajeDeError(e) });
    }
  }

  if (error) {
    return (
      <section className="panel">
        <p className="alerta alerta-error">{error}</p>
        <button type="button" className="boton boton-secundario" onClick={() => navigate('/actividades')}>
          Volver a actividades
        </button>
      </section>
    );
  }

  if (!actividad) {
    return <p>Cargando…</p>;
  }

  const sinCupo = actividad.cupoDisponible === 0;

  return (
    <section className="panel detalle">
      <p className="detalle-disciplina">{actividad.disciplina}</p>
      <h2>{actividad.nombre}</h2>

      <dl className="detalle-datos">
        <dt>Días y horario</dt>
        <dd>{actividad.dias} · {actividad.horario} h</dd>
        <dt>Edades</dt>
        <dd>De {actividad.edadMinima} a {actividad.edadMaxima} años</dd>
        <dt>Profesor/a</dt>
        <dd>{actividad.profesor}</dd>
        <dt>Cuota mensual</dt>
        <dd>{formatearPesos(actividad.cuotaMensual)}</dd>
        <dt>Cupo</dt>
        <dd className={sinCupo ? 'texto-sin-cupo' : ''}>
          {sinCupo ? 'Sin cupo' : `${actividad.cupoDisponible} de ${actividad.cupoMaximo} lugares disponibles`}
        </dd>
      </dl>

      {mensaje && <p className={`alerta alerta-${mensaje.tipo}`}>{mensaje.texto}</p>}

      <div className="detalle-botones">
        <button type="button" className="boton boton-secundario" onClick={() => navigate(-1)}>
          Volver
        </button>
        <button type="button" className="boton" disabled={sinCupo} onClick={manejarInscripcion}>
          Inscribirme
        </button>
      </div>
    </section>
  );
}

export default DetalleActividad;
