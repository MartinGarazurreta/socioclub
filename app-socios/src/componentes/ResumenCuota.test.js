import { render, screen } from '@testing-library/react';
import ResumenCuota from './ResumenCuota';

describe('ResumenCuota', () => {
  test('con 2 actividades muestra la bonificación del 10 % y el total con descuento', () => {
    const actividades = [
      { id: 8, nombre: 'Yoga', cuotaMensual: 17000 },
      { id: 4, nombre: 'Natación', cuotaMensual: 26000 },
    ];
    render(<ResumenCuota actividades={actividades} />);

    expect(screen.getByText(/Bonificación por 2 actividades: 10 %/)).toBeInTheDocument();
    expect(screen.getByText('$ 38.700')).toBeInTheDocument();
  });
});
