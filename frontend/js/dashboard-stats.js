// Creamos una función asíncrona para pedir los datos a Java
async function cargarDatosDelDashboard() {
    try {
        // Hacemos la petición al endpoint de tu Javalin
        const respuesta = await fetch('http://localhost:7070/api/v1/dashboard-stats-con-historico');
        
        if (!respuesta.ok) {
            throw new Error('Error en la respuesta del servidor Java');
        }

        // Convertimos la respuesta de Java en un objeto JSON
        const DASHBOARD_STATS = await respuesta.json();

        // AQUÍ llamas a la función que ya tenías para pintar los gráficos en el HTML
        // pasándole los datos REALES que acaba de escupir tu backend.
        pintarGraficos(DASHBOARD_STATS); // (Sustituye 'pintarGraficos' por el nombre de la función que uses en tu código)

    } catch (error) {
        console.error("❌ Error conectando con el backend:", error);
    }
}

// Ejecutamos la función en cuanto cargue la página
document.addEventListener('DOMContentLoaded', cargarDatosDelDashboard);