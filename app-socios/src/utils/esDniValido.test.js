import { esDniValido } from './esDniValido';

describe('esDniValido (RN7)', () => {
  test('acepta un DNI de 8 dígitos', () => {
    expect(esDniValido('35123987')).toBe(true);
  });

  test('acepta un DNI de 7 dígitos', () => {
    expect(esDniValido('1234567')).toBe(true);
  });

  test('rechaza un DNI con puntos', () => {
    expect(esDniValido('35.123.987')).toBe(false);
  });

  test('rechaza un DNI con letras', () => {
    expect(esDniValido('35123A87')).toBe(false);
  });

  test('rechaza un DNI de 6 dígitos (demasiado corto)', () => {
    expect(esDniValido('123456')).toBe(false);
  });

  test('rechaza un DNI de 9 dígitos (demasiado largo)', () => {
    expect(esDniValido('123456789')).toBe(false);
  });

  test('rechaza un campo vacío o un valor que no es texto', () => {
    expect(esDniValido('')).toBe(false);
    expect(esDniValido(null)).toBe(false);
    expect(esDniValido(35123987)).toBe(false);
  });
});
