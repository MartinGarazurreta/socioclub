import { useContext } from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import { SocioContext } from '../contexto/SocioContext';
import './Encabezado.css';

function Encabezado() {
  const { socio, salir } = useContext(SocioContext);
  const navigate = useNavigate();

  function manejarSalir() {
    salir();
    navigate('/');
  }

  return (
    <header className="encabezado">
      <div className="encabezado-marca">
        <span className="encabezado-logo" aria-hidden="true">LA</span>
        <div>
          <h1>Mi Club</h1>
          <p>Club Social y Deportivo Los Algarrobos</p>
        </div>
      </div>

      {/* Renderizado condicional: el menú solo aparece si hay un socio que ingresó */}
      {socio && (
        <nav className="encabezado-menu">
          <NavLink to="/actividades">Actividades</NavLink>
          <NavLink to="/mis-actividades">Mis actividades</NavLink>
          <span className="encabezado-socio">Hola, {socio.nombre}</span>
          <button type="button" className="boton boton-secundario" onClick={manejarSalir}>
            Salir
          </button>
        </nav>
      )}
    </header>
  );
}

export default Encabezado;
