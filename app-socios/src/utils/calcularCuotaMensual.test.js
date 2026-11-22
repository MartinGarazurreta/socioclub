import { calcularCuotaMensual, obtenerDescuento } from './calcularCuotaMensual';

// Actividades de ejemplo con las cuotas reales del club (sección 3.3)
const yoga = { id: 8, nombre: 'Yoga', cuotaMensual: 17000 };
const natacion = { id: 4, nombre: 'Natación', cuotaMensual: 26000 };
const funcional = { id: 7, nombre: 'Funcional adultos', cuotaMensual: 22000 };

describe('calcularCuotaMensual (RN6)', () => {
  test('sin actividades la cuota es 0', () => {
    expect(calcularCuotaMensual([])).toBe(0);
  });

  test('con 1 actividad se paga la cuota completa, sin descuento', () => {
    expect(calcularCuotaMensual([yoga])).toBe(17000);
  });

  test('con 2 actividades se aplica 10 % de descuento', () => {
    // (17.000 + 26.000) * 0,90 = 38.700
    expect(calcularCuotaMensual([yoga, natacion])).toBe(38700);
  });

  test('con 3 actividades se aplica 15 % de descuento', () => {
    // (17.000 + 26.000 + 22.000) * 0,85 = 55.250
    expect(calcularCuotaMensual([yoga, natacion, funcional])).toBe(55250);
  });

  test('si recibe null o undefined devuelve 0 en vez de fallar', () => {
    expect(calcularCuotaMensual(null)).toBe(0);
    expect(calcularCuotaMensual(undefined)).toBe(0);
  });
});

describe('obtenerDescuento', () => {
  test('devuelve el porcentaje según la cantidad de actividades', () => {
    expect(obtenerDescuento(0)).toBe(0);
    expect(obtenerDescuento(1)).toBe(0);
    expect(obtenerDescuento(2)).toBe(10);
    expect(obtenerDescuento(3)).toBe(15);
  });
});
