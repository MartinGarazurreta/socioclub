import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import { SocioProvider } from './contexto/SocioContext';
import Encabezado from './componentes/Encabezado';
import Pie from './componentes/Pie';
import Ingreso from './componentes/Ingreso';
import ListaActividades from './componentes/ListaActividades';
import DetalleActividad from './componentes/DetalleActividad';
import MisActividades from './componentes/MisActividades';
import RutaProtegida from './componentes/RutaProtegida';
import './App.css';

function App() {
  return (
    // SocioProvider envuelve toda la app: cualquier componente puede leer el socio con useContext
    <SocioProvider>
      <BrowserRouter>
        <Encabezado />
        <main className="contenido">
          <Routes>
            <Route path="/" element={<Ingreso />} />
            <Route path="/actividades" element={<RutaProtegida><ListaActividades /></RutaProtegida>} />
            <Route path="/actividades/:id" element={<RutaProtegida><DetalleActividad /></RutaProtegida>} />
            <Route path="/mis-actividades" element={<RutaProtegida><MisActividades /></RutaProtegida>} />
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </main>
        <Pie />
      </BrowserRouter>
    </SocioProvider>
  );
}

export default App;
