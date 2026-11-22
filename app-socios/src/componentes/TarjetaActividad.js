import PropTypes from 'prop-types';
import { formatearPesos } from '../utils/formatearPesos';
import './TarjetaActividad.css';

// Muestra una actividad. Recibe todo por props: no sabe nada de la API ni del socio.
function TarjetaActividad({ actividad, onVerDetalle, onInscribirse }) {
  const sinCupo = actividad.cupoDisponible === 0;

  return (
    // Clase aplicada dinámicamente: si no hay cupo, la tarjeta se ve en gris
    <article className={sinCupo ? 'tarjeta-actividad sin-cupo' : 'tarjeta-actividad'}>
      <div className="tarjeta-cabecera">
        <h3>{actividad.nombre}</h3>
        <span className="tarjeta-disciplina">{actividad.disciplina}</span>
      </div>

      <ul className="tarjeta-datos">
        <li>{actividad.dias} · {actividad.horario} h</li>
        <li>De {actividad.edadMinima} a {actividad.edadMaxima} años</li>
        <li>Profe: {actividad.profesor}</li>
      </ul>

      <p className="tarjeta-cuota">{formatearPesos(actividad.cuotaMensual)} <small>por mes</small></p>

      {/* Renderizado condicional del cupo */}
      {sinCupo ? (
        <p className="etiqueta-cupo etiqueta-sin-cupo">Sin cupo</p>
      ) : (
        <p className="etiqueta-cupo">
          {actividad.cupoDisponible} {actividad.cupoDisponible === 1 ? 'lugar disponible' : 'lugares disponibles'}
        </p>
      )}

      <div className="tarjeta-botones">
        <button type="button" className="boton boton-secundario" onClick={() => onVerDetalle(actividad.id)}>
          Ver detalle
        </button>
        <button type="button" className="boton" disabled={sinCupo} onClick={() => onInscribirse(actividad.id)}>
          Inscribirme
        </button>
      </div>
    </article>
  );
}

TarjetaActividad.propTypes = {
  actividad: PropTypes.shape({
    id: PropTypes.number.isRequired,
    nombre: PropTypes.string.isRequired,
    disciplina: PropTypes.string.isRequired,
    dias: PropTypes.string.isRequired,
    horario: PropTypes.string.isRequired,
    edadMinima: PropTypes.number.isRequired,
    edadMaxima: PropTypes.number.isRequired,
    profesor: PropTypes.string.isRequired,
    cuotaMensual: PropTypes.number.isRequired,
    cupoDisponible: PropTypes.number.isRequired,
  }).isRequired,
  onVerDetalle: PropTypes.func.isRequired,
  onInscribirse: PropTypes.func.isRequired,
};

export default TarjetaActividad;
