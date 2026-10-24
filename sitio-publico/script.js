// =========================================================
// script.js - Sitio público del Club Los Algarrobos
// Un solo archivo para las 3 páginas: cada parte revisa si
// los elementos que necesita existen en la página actual.
// (M1 Lectura 3: clases, const/let, funciones; M1 Lectura 4: DOM, eventos, objetos del navegador)
// =========================================================

// ---------- Clase Actividad ----------
class Actividad {
    constructor(nombre, disciplina, dias, horario, edadMinima, edadMaxima, cupo, cuotaMensual, profesor) {
        this.nombre = nombre;
        this.disciplina = disciplina;
        this.dias = dias;
        this.horario = horario;
        this.edadMinima = edadMinima;
        this.edadMaxima = edadMaxima;
        this.cupo = cupo;
        this.cuotaMensual = cuotaMensual;
        this.profesor = profesor;
    }

    // Devuelve true si la edad está dentro del rango de la actividad
    esAptaParaEdad(edad) {
        return edad >= this.edadMinima && edad <= this.edadMaxima;
    }

    // Devuelve la cuota con formato de pesos argentinos, por ejemplo "$ 18.000"
    obtenerCuotaFormateada() {
        return "$ " + this.cuotaMensual.toLocaleString("es-AR");
    }
}

// ---------- Datos iniciales del club (sección 3.3 del enunciado) ----------
const actividades = [
    new Actividad("Fútbol infantil", "Fútbol", "Lun y Mié", "18:00", 6, 12, 40, 18000, "Diego Ferreyra"),
    new Actividad("Vóley mixto", "Vóley", "Mar y Jue", "19:00", 13, 60, 24, 20000, "Laura Ceballos"),
    new Actividad("Básquet juvenil", "Básquet", "Lun y Vie", "19:30", 12, 17, 20, 19000, "Martín Oviedo"),
    new Actividad("Natación", "Natación", "Mar y Jue", "17:00", 5, 70, 30, 26000, "Sofía Bustos"),
    new Actividad("Patín artístico", "Patín", "Mié y Vie", "17:30", 5, 15, 25, 21000, "Carla Peralta"),
    new Actividad("Ajedrez", "Ajedrez", "Sáb", "10:00", 8, 99, 16, 9000, "Raúl Giménez"),
    new Actividad("Funcional adultos", "Fitness", "Lun, Mié y Vie", "20:30", 18, 65, 30, 22000, "Paula Luna"),
    new Actividad("Yoga", "Bienestar", "Mar y Jue", "9:00", 16, 99, 15, 17000, "Inés Arguello")
];

// ---------- Funciones de validación (puras: no tocan el DOM) ----------

// RN7: el DNI debe tener 7 u 8 dígitos numéricos, sin puntos
function esDniValido(dni) {
    return /^\d{7,8}$/.test(dni);
}

function esEmailValido(email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

// Calcula la edad a la fecha de hoy a partir de un texto "aaaa-mm-dd"
function calcularEdad(fechaTexto) {
    const hoy = new Date();
    const nacimiento = new Date(fechaTexto + "T00:00:00");
    let edad = hoy.getFullYear() - nacimiento.getFullYear();
    const todaviaNoCumplio =
        hoy.getMonth() < nacimiento.getMonth() ||
        (hoy.getMonth() === nacimiento.getMonth() && hoy.getDate() < nacimiento.getDate());
    if (todaviaNoCumplio) {
        edad--;
    }
    return edad;
}

// =========================================================
// PÁGINA actividades.html: catálogo y filtros
// =========================================================

// Crea el <article> de una actividad usando el DOM (createElement + appendChild)
function crearTarjeta(actividad) {
    const tarjeta = document.createElement("article");
    tarjeta.classList.add("tarjeta");

    const titulo = document.createElement("h3");
    titulo.textContent = actividad.nombre;

    const disciplina = document.createElement("span");
    disciplina.classList.add("disciplina");
    disciplina.textContent = actividad.disciplina;

    const datos = document.createElement("ul");
    const lineas = [
        "Días: " + actividad.dias + " · " + actividad.horario + " h",
        "Edades: de " + actividad.edadMinima + " a " + actividad.edadMaxima + " años",
        "Profesor/a: " + actividad.profesor,
        "Cupo: " + actividad.cupo + " lugares"
    ];
    for (const texto of lineas) {
        const item = document.createElement("li");
        item.textContent = texto;
        datos.appendChild(item);
    }

    const cuota = document.createElement("p");
    cuota.classList.add("cuota");
    cuota.textContent = actividad.obtenerCuotaFormateada() + " por mes";

    tarjeta.appendChild(titulo);
    tarjeta.appendChild(disciplina);
    tarjeta.appendChild(datos);
    tarjeta.appendChild(cuota);
    return tarjeta;
}

// Borra el catálogo y lo vuelve a dibujar con la lista recibida
function mostrarActividades(lista) {
    const contenedor = document.getElementById("lista-actividades");
    const mensajeVacio = document.getElementById("mensaje-vacio");

    contenedor.innerHTML = "";
    for (const actividad of lista) {
        contenedor.appendChild(crearTarjeta(actividad));
    }

    // Si no quedó ninguna actividad, se muestra el mensaje
    if (lista.length === 0) {
        mensajeVacio.classList.remove("oculto");
    } else {
        mensajeVacio.classList.add("oculto");
    }
}

// Lee los dos filtros y redibuja el catálogo con las actividades que cumplen ambos
function filtrarActividades() {
    const disciplinaElegida = document.getElementById("filtro-disciplina").value;
    const edadTexto = document.getElementById("filtro-edad").value;

    const filtradas = actividades.filter(function (actividad) {
        const cumpleDisciplina = disciplinaElegida === "" || actividad.disciplina === disciplinaElegida;
        const cumpleEdad = edadTexto === "" || actividad.esAptaParaEdad(Number(edadTexto));
        return cumpleDisciplina && cumpleEdad;
    });

    mostrarActividades(filtradas);
}

function iniciarCatalogo() {
    const contenedor = document.getElementById("lista-actividades");
    if (contenedor === null) {
        return; // no estamos en actividades.html
    }

    mostrarActividades(actividades);

    // Eventos registrados con addEventListener (no con atributos onchange en el HTML)
    document.getElementById("filtro-disciplina").addEventListener("change", filtrarActividades);
    document.getElementById("filtro-edad").addEventListener("input", filtrarActividades);
    document.getElementById("form-filtros").addEventListener("reset", function () {
        // El evento reset ocurre antes de que se limpien los campos, por eso se muestra la lista completa
        mostrarActividades(actividades);
    });
}

// =========================================================
// PÁGINA preinscripcion.html: validación del formulario
// =========================================================

// Muestra el mensaje de error junto al campo y lo marca en rojo
function mostrarError(idCampo, mensaje) {
    document.getElementById(idCampo).classList.add("invalido");
    document.getElementById("error-" + idCampo).textContent = mensaje;
}

function limpiarError(idCampo) {
    document.getElementById(idCampo).classList.remove("invalido");
    document.getElementById("error-" + idCampo).textContent = "";
}

// Valida todos los campos; devuelve true si el formulario está correcto
function validarFormulario() {
    let esValido = true;

    const nombre = document.getElementById("nombre").value.trim();
    const apellido = document.getElementById("apellido").value.trim();
    const dni = document.getElementById("dni").value.trim();
    const fechaNacimiento = document.getElementById("fecha-nacimiento").value;
    const email = document.getElementById("email").value.trim();
    const nombreActividad = document.getElementById("actividad").value;

    const campos = ["nombre", "apellido", "dni", "fecha-nacimiento", "email", "actividad"];
    for (const campo of campos) {
        limpiarError(campo);
    }

    if (nombre === "") {
        mostrarError("nombre", "Ingresá tu nombre.");
        esValido = false;
    }

    if (apellido === "") {
        mostrarError("apellido", "Ingresá tu apellido.");
        esValido = false;
    }

    if (dni === "") {
        mostrarError("dni", "Ingresá tu DNI.");
        esValido = false;
    } else if (!esDniValido(dni)) {
        mostrarError("dni", "El DNI debe tener 7 u 8 números, sin puntos.");
        esValido = false;
    }

    if (email === "") {
        mostrarError("email", "Ingresá tu email.");
        esValido = false;
    } else if (!esEmailValido(email)) {
        mostrarError("email", "El email no tiene un formato válido (ejemplo: nombre@mail.com).");
        esValido = false;
    }

    if (nombreActividad === "") {
        mostrarError("actividad", "Elegí una actividad.");
        esValido = false;
    }

    if (fechaNacimiento === "") {
        mostrarError("fecha-nacimiento", "Ingresá tu fecha de nacimiento.");
        esValido = false;
    } else {
        const edad = calcularEdad(fechaNacimiento);
        if (edad < 0) {
            mostrarError("fecha-nacimiento", "La fecha no puede ser posterior a hoy.");
            esValido = false;
        } else if (nombreActividad !== "") {
            // Aviso de edad usando el método de la clase (la regla definitiva, RN3, la valida el backend)
            const actividad = actividades.find(function (a) {
                return a.nombre === nombreActividad;
            });
            if (!actividad.esAptaParaEdad(edad)) {
                mostrarError("fecha-nacimiento",
                    "Con " + edad + " años no podés anotarte en " + actividad.nombre +
                    " (de " + actividad.edadMinima + " a " + actividad.edadMaxima + " años).");
                esValido = false;
            }
        }
    }

    return esValido;
}

function iniciarFormulario() {
    const formulario = document.getElementById("form-preinscripcion");
    if (formulario === null) {
        return; // no estamos en preinscripcion.html
    }

    formulario.addEventListener("submit", function (evento) {
        evento.preventDefault(); // evita que el formulario recargue la página
        if (validarFormulario()) {
            formulario.classList.add("oculto");
            document.getElementById("mensaje-exito").classList.remove("oculto");
        }
    });

    // Cuando el usuario corrige un campo, se borra su error
    const campos = ["nombre", "apellido", "dni", "fecha-nacimiento", "email", "actividad"];
    for (const campo of campos) {
        document.getElementById(campo).addEventListener("input", function () {
            limpiarError(campo);
        });
    }

    // Objeto history (M1 Lectura 4): vuelve a la página anterior.
    // Si se entró directo a esta página no hay "anterior", entonces usamos location para ir al inicio.
    document.getElementById("boton-volver").addEventListener("click", function () {
        if (window.history.length > 1) {
            window.history.back();
        } else {
            window.location.href = "index.html";
        }
    });
}

// ---------- Punto de entrada ----------
// El script se carga con "defer", así que el HTML ya está listo cuando esto se ejecuta
iniciarCatalogo();
iniciarFormulario();
