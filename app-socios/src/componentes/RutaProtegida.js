import { useContext } from 'react';
import { Navigate } from 'react-router-dom';
import { SocioContext } from '../contexto/SocioContext';

// Si nadie ingresó con su DNI, las pantallas del socio redirigen al ingreso
function RutaProtegida({ children }) {
  const { socio } = useContext(SocioContext);
  if (!socio) {
    return <Navigate to="/" replace />;
  }
  return children;
}

export default RutaProtegida;
