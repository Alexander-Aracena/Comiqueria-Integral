const BASE_URL = "http://localhost:8080";

const appContent = document.getElementById("app-content");

document.addEventListener("DOMContentLoaded", () => {
    const productos = getProductos();
    renderProductos(productos);
});

const getProductos = async () => {
    try {
        const response = await fetch(`${BASE_URL}/api/productos`);
        if (!response.ok) {
            throw new Error(`Error http: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error("Error al obtener los productos:", error);
        return [];
    }
};

function renderProductos(productos) {
    appContent.innerHTML = "";
    const productsSection = document.createElement("div");
    productsSection.className = "products-section";
    appContent.appendChild(productsSection);
    productos.forEach(producto => {
        productsSection.innerHTML = `
            <h2>${producto.nombre}</h2>
            <p>Precio: $${producto.precio}</p>
            <p>Categoría: ${producto.categoria}</p>
        `;
    });
}