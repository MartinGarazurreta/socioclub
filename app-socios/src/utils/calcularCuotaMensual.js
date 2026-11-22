// RN6: la cuota mensual estimada es la suma de las cuotas de las actividades activas,
// con 10 % de descuento si tiene 2 actividades y 15 % si tiene 3.

// Porcentaje de descuento según la cantidad de actividades
export function obtenerDescuento(cantidadActividades) {
  if (cantidadActividades >= 3) {
    return 15;
  }
  if (cantidadActividades === 2) {
    return 10;
  }
  return 0;
}

// Función pura: recibe la lista de actividades y devuelve el total a pagar
export function calcularCuotaMensual(actividades) {
  if (!actividades || actividades.length === 0) {
    return 0;
  }
  const subtotal = actividades.reduce((suma, actividad) => suma + actividad.cuotaMensual, 0);
  const descuento = obtenerDescuento(actividades.length);
  return Math.round(subtotal * (100 - descuento) / 100);
}
