const input = document.getElementById("input");
const productos = document.getElementById("productos");
const seleccionados = document.getElementById("seleccionados").querySelector("tbody");

input.addEventListener("input", () => {
    const texto = input.value.toLowerCase();
    const productosFiltrados = todosProductos.filter(producto => producto.nombre.toLowerCase().includes(texto));
    if(texto != ""){
        displaySearchResults(productosFiltrados);
    } else {
        productos.innerHTML = "";
    }
});

function displaySearchResults(resultados) {
    productos.innerHTML = "";
    resultados.forEach(producto => {
        const resultado = document.createElement("div");
        resultado.textContent = producto.nombre;
        resultado.addEventListener("click", () => addSelectedProduct(producto));
        resultados.appendChild(resultado);
        
    });
}

function addSelectedProduct(producto) {
    const nuevaFila = document.createElement("tr");
    nuevaFila.innerHTML = `
        <td>${producto.nombre}</td>
        <td>${producto.stock}</td>
        <td>${producto.precio}</td>
    `;
    seleccionados.appendChild(nuevaFila);
}

console.log(todosProductos);
