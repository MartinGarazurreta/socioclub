// RN7: el DNI debe tener 7 u 8 dígitos numéricos, sin puntos.
export function esDniValido(dni) {
  if (typeof dni !== 'string') {
    return false;
  }
  return /^\d{7,8}$/.test(dni.trim());
}
