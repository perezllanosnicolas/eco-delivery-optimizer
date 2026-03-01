// Variable global para que envio.html pueda leer los datos
let ECO_OPCIONES = null;

async function cargarOpcionesEco() {
    try {
        // Hacemos la petición a tu API recién descomentada
        const respuesta = await fetch('http://localhost:7070/api/v1/eco-opciones');
        
        if (!respuesta.ok) {
            throw new Error('Error en la respuesta del servidor Java');
        }

        // Guardamos los datos reales calculados por tu Optimizador
        ECO_OPCIONES = await respuesta.json();

        // Llamamos a la función que pintará las opciones en la web
        if (typeof inicializarEnvioEco === 'function') {
            inicializarEnvioEco();
        }

    } catch (error) {
        console.error("❌ Error conectando con el backend de eco-opciones:", error);
    }
}

// Ejecutamos la petición al cargar la página
document.addEventListener('DOMContentLoaded', cargarOpcionesEco);