// 18000 -> "$ 18.000"
export function formatearPesos(monto) {
  return '$ ' + Math.round(monto).toLocaleString('es-AR');
}
