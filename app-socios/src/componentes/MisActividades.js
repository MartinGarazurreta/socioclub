import { useContext, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { SocioContext } from '../contexto/SocioContext';
import { darDeBaja, mensajeDeError, obtenerInscripciones } from '../servicios/api';
import ResumenCuota from './ResumenCuota';

// Ruta "/mis-actividades": inscripciones activas, baja y cuota estimada (RN6)
function MisActividades() {
  const { socio } = useContext(SocioContext);
  const [inscripciones, setInscripciones] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [mensaje, setMensaje] = useState(null);
  const [recargas, setRecargas] = useState(0);

  // Se piden las inscripciones al mostrar la pantalla y después de cada baja (recargas)
  useEffect(() => {
    setCargando(true);
    obtenerInscripciones(socio.id)
      .then((datos) => setInscripciones(datos))
      .catch((e) => setMensaje({ tipo: 'error', texto: mensajeDeError(e) }))
      .finally(() => setCargando(false));
  }, [socio.id, recargas]);

  async function manejarBaja(inscripcion) {
    if (!window.confirm(`¿Seguro que querés darte de baja de ${inscripcion.actividad.nombre}?`)) {
      return;
    }
    try {
      await darDeBaja(inscripcion.id);
      setMensaje({ tipo: 'exito', texto: `Te diste de baja de ${inscripcion.actividad.nombre}.` });
      setRecargas(recargas + 1);
    } catch (e) {
      setMensaje({ tipo: 'error', texto: mensajeDeError(e) });
    }
  }

  if (cargando) {
    return <p>Cargando tus actividades…</p>;
  }

  const actividades = inscripciones.map((inscripcion) => inscripcion.actividad);

  return (
    <section>
      <h2 className="titulo-pagina">Mis actividades</h2>

      {socio.estadoCuota === 'MOROSO' && (
        <p className="alerta alerta-error">
          Tu cuota figura como impaga. Podés ver y darte de baja de tus actividades, pero para
          anotarte en otras tenés que regularizar en secretaría.
        </p>
      )}

      {mensaje && <p className={`alerta alerta-${mensaje.tipo}`}>{mensaje.texto}</p>}

      {inscripciones.length === 0 ? (
        <div className="panel">
          <p>Todavía no estás inscripto/a en ninguna actividad.</p>
          <Link to="/actividades" className="boton">Ver actividades</Link>
        </div>
      ) : (
        <div className="mis-actividades">
          <ul className="lista-inscripciones">
            {inscripciones.map((inscripcion) => (
              <li key={inscripcion.id} className="panel inscripcion">
                <div>
                  <h3>{inscripcion.actividad.nombre}</h3>
                  <p>{inscripcion.actividad.dias} · {inscripcion.actividad.horario} h · {inscripcion.actividad.profesor}</p>
                </div>
                <button type="button" className="boton boton-peligro" onClick={() => manejarBaja(inscripcion)}>
                  Darme de baja
                </button>
              </li>
            ))}
          </ul>
          <ResumenCuota actividades={actividades} />
        </div>
      )}
    </section>
  );
}

export default MisActividades;
