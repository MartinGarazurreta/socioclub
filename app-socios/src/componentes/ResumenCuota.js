import PropTypes from 'prop-types';
import { calcularCuotaMensual, obtenerDescuento } from '../utils/calcularCuotaMensual';
import { formatearPesos } from '../utils/formatearPesos';
import './ResumenCuota.css';

// RN6: muestra la cuota mensual estimada según las actividades activas
function ResumenCuota({ actividades }) {
  const subtotal = actividades.reduce((suma, a) => suma + a.cuotaMensual, 0);
  const descuento = obtenerDescuento(actividades.length);
  const total = calcularCuotaMensual(actividades);

  return (
    <aside className="resumen-cuota">
      <h3>Tu cuota mensual estimada</h3>
      <ul>
        {actividades.map((actividad) => (
          <li key={actividad.id}>
            <span>{actividad.nombre}</span>
            <span>{formatearPesos(actividad.cuotaMensual)}</span>
          </li>
        ))}
      </ul>
      {descuento > 0 && (
        <p className="resumen-descuento">
          Bonificación por {actividades.length} actividades: {descuento} % (−{formatearPesos(subtotal - total)})
        </p>
      )}
      <p className="resumen-total">
        <span>Total</span>
        <strong>{formatearPesos(total)}</strong>
      </p>
      {actividades.length === 1 && (
        <p className="resumen-ayuda">Con 2 actividades tenés 10 % de descuento y con 3, 15 %.</p>
      )}
    </aside>
  );
}

ResumenCuota.propTypes = {
  actividades: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.number.isRequired,
      nombre: PropTypes.string.isRequired,
      cuotaMensual: PropTypes.number.isRequired,
    })
  ).isRequired,
};

export default ResumenCuota;
