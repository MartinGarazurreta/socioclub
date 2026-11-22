import { useContext, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { SocioContext } from '../contexto/SocioContext';
import { buscarSocioPorDni } from '../servicios/api';
import { esDniValido } from '../utils/esDniValido';

// Ruta "/": el socio escribe su DNI y la app lo busca en la API
function Ingreso() {
  const { ingresar } = useContext(SocioContext);
  const navigate = useNavigate();
  const [dni, setDni] = useState('');
  const [error, setError] = useState('');
  const [cargando, setCargando] = useState(false);

  async function manejarEnvio(evento) {
    evento.preventDefault();
    // RN7: se valida antes de llamar al backend
    if (!esDniValido(dni)) {
      setError('El DNI debe tener 7 u 8 números, sin puntos.');
      return;
    }
    setError('');
    setCargando(true);
    try {
      const socio = await buscarSocioPorDni(dni.trim());
      ingresar(socio);
      navigate('/actividades');
    } catch (e) {
      if (e.response && e.response.status === 404) {
        setError('No encontramos un socio con ese DNI. Consultá en secretaría.');
      } else {
        setError('No se pudo conectar con el servidor. Probá de nuevo en un rato.');
      }
    } finally {
      setCargando(false);
    }
  }

  return (
    <section className="panel ingreso">
      <h2>Ingresá con tu DNI</h2>
      <p>Mirá a qué actividades estás anotado, cuánto pagás por mes y anotate en otras.</p>

      <form onSubmit={manejarEnvio} noValidate>
        <label htmlFor="dni">DNI (sin puntos)</label>
        <input
          id="dni"
          type="text"
          inputMode="numeric"
          maxLength={10}
          value={dni}
          onChange={(e) => setDni(e.target.value)}
          className={error ? 'campo-con-error' : ''}
          placeholder="Ej.: 35123987"
        />
        {error && <p className="mensaje-error-campo">{error}</p>}

        <button type="submit" className="boton" disabled={cargando}>
          {cargando ? 'Buscando…' : 'Ingresar'}
        </button>
      </form>
    </section>
  );
}

export default Ingreso;
