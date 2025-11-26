package com.example.tiendaturbotorque

data class ProductoCarrito(
    val producto: Producto,
    var cantidad: Int = 1  // Por defecto 1 unidad
) {
    // Metodo para calcular el subtotal de este producto
    fun subtotal(): Double = producto.precio * cantidad
}