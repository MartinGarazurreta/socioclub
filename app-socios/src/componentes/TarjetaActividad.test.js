import { fireEvent, render, screen } from '@testing-library/react';
import TarjetaActividad from './TarjetaActividad';

// Actividad de ejemplo; cada prueba cambia solo lo que necesita (por ejemplo, el cupo)
const yoga = {
  id: 8,
  nombre: 'Yoga',
  disciplina: 'Bienestar',
  dias: 'Mar y Jue',
  horario: '9:00',
  edadMinima: 16,
  edadMaxima: 99,
  profesor: 'Inés Arguello',
  cuotaMensual: 17000,
  cupoDisponible: 3,
};

// jest.fn() crea funciones "espía": registran si se las llamó y con qué datos
function renderizarTarjeta(actividad) {
  const onVerDetalle = jest.fn();
  const onInscribirse = jest.fn();
  render(<TarjetaActividad actividad={actividad} onVerDetalle={onVerDetalle} onInscribirse={onInscribirse} />);
  return { onVerDetalle, onInscribirse };
}

describe('TarjetaActividad', () => {
  test('muestra el nombre, la disciplina, la cuota y el cupo de la actividad', () => {
    renderizarTarjeta(yoga);

    expect(screen.getByText('Yoga')).toBeInTheDocument();
    expect(screen.getByText('Bienestar')).toBeInTheDocument();
    expect(screen.getByText(/17\.000/)).toBeInTheDocument();
    expect(screen.getByText('3 lugares disponibles')).toBeInTheDocument();
  });

  test('con cupo 0 muestra "Sin cupo" y el botón Inscribirme queda deshabilitado', () => {
    const { onInscribirse } = renderizarTarjeta({ ...yoga, cupoDisponible: 0 });

    expect(screen.getByText('Sin cupo')).toBeInTheDocument();
    const boton = screen.getByRole('button', { name: 'Inscribirme' });
    expect(boton).toBeDisabled();

    // Aunque se haga clic, un botón deshabilitado no dispara el evento
    fireEvent.click(boton);
    expect(onInscribirse).not.toHaveBeenCalled();
  });

  test('con cupo 0 la tarjeta tiene la clase sin-cupo (se ve en gris)', () => {
    renderizarTarjeta({ ...yoga, cupoDisponible: 0 });
    expect(screen.getByRole('article')).toHaveClass('sin-cupo');
  });

  test('al hacer clic en Inscribirme avisa al componente padre con el id de la actividad', () => {
    const { onInscribirse } = renderizarTarjeta(yoga);

    fireEvent.click(screen.getByRole('button', { name: 'Inscribirme' }));

    expect(onInscribirse).toHaveBeenCalledTimes(1);
    expect(onInscribirse).toHaveBeenCalledWith(8);
  });

  test('al hacer clic en Ver detalle avisa al componente padre con el id de la actividad', () => {
    const { onVerDetalle } = renderizarTarjeta(yoga);

    fireEvent.click(screen.getByRole('button', { name: 'Ver detalle' }));

    expect(onVerDetalle).toHaveBeenCalledWith(8);
  });
});
