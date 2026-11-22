import { createContext, useState } from 'react';

// Contexto del socio que ingresó. Lo necesitan varias pantallas (encabezado, catálogo,
// detalle, mis actividades); con el contexto no hace falta pasarlo por props de
// componente en componente.
export const SocioContext = createContext(null);

const CLAVE = 'socioClub.socio';

// sessionStorage (objeto del navegador) guarda el socio mientras la pestaña esté abierta:
// así, si se recarga la página, no hay que volver a ingresar el DNI.
function leerSocioGuardado() {
  try {
    const guardado = window.sessionStorage.getItem(CLAVE);
    return guardado ? JSON.parse(guardado) : null;
  } catch (e) {
    return null;
  }
}

export function SocioProvider({ children }) {
  const [socio, setSocio] = useState(leerSocioGuardado);

  function ingresar(socioEncontrado) {
    setSocio(socioEncontrado);
    window.sessionStorage.setItem(CLAVE, JSON.stringify(socioEncontrado));
  }

  function salir() {
    setSocio(null);
    window.sessionStorage.removeItem(CLAVE);
  }

  return (
    <SocioContext.Provider value={{ socio, ingresar, salir }}>
      {children}
    </SocioContext.Provider>
  );
}
