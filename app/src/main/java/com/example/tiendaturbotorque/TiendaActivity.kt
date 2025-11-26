package com.example.tiendaturbotorque

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tiendaturbotorque.databinding.ActiviyTiendaBinding

class TiendaActivity : AppCompatActivity() {

    private lateinit var binding: ActiviyTiendaBinding

    // Carrito real
    private val carrito = mutableListOf<ProductoCarrito>()
    private lateinit var carritoAdapter: CarritoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActiviyTiendaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Lista de productos de ejemplo
        val productos = listOf(
            Producto("Llantas RR Sport 19\"", 249.99, R.drawable.ic_launcher_foreground),
            Producto("Llanta Xtreme", 230.00, R.drawable.ic_launcher_foreground),
            Producto("Llanta Deportiva Z 20\"", 310.00, R.drawable.ic_launcher_foreground)
        )

        val adapter = ProductosAdapter(productos) { producto ->
            añadirAlCarrito(producto)
        }

        binding.recyclerProductos.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerProductos.adapter = adapter

        // Configurar RecyclerView del carrito
        carritoAdapter = CarritoAdapter(
            lista = carrito,
            onCantidadCambiada = {
                actualizarTotal()
            },
            onEliminar = { productoCarrito ->
                eliminarDelCarrito(productoCarrito)
            }
        )

        binding.recyclerCarrito.layoutManager = LinearLayoutManager(this)
        binding.recyclerCarrito.adapter = carritoAdapter

        // Configurar botón pagar
        binding.btnPagar.setOnClickListener {
            realizarCompra()
        }
    }

    private fun añadirAlCarrito(producto: Producto) {
        // Buscar si el producto ya existe en el carrito
        val itemExistente = carrito.find { it.producto.nombre == producto.nombre }

        if (itemExistente != null) {
            // Si ya existe, aumentar la cantidad
            itemExistente.cantidad++
        } else {
            // Si no existe, añadir nuevo item
            carrito.add(ProductoCarrito(producto, cantidad = 1))
        }

        // Notificar al adapter y actualizar UI
        carritoAdapter.notifyDataSetChanged()
        actualizarTotal()

        Toast.makeText(this, "Añadido: ${producto.nombre}", Toast.LENGTH_SHORT).show()
    }

    private fun actualizarTotal() {
        // Calcular el total sumando los subtotales de cada item
        val total = carrito.sumOf { it.subtotal() }

        // Calcular la cantidad total de items
        val cantidadTotal = carrito.sumOf { it.cantidad }

        // Actualizar UI
        binding.valueTotal.text = "%.2f €".format(total)
        binding.btnPagar.text = "COMPRAR ($cantidadTotal)"
    }

    private fun eliminarDelCarrito(productoCarrito: ProductoCarrito) {
        carrito.remove(productoCarrito)
        carritoAdapter.notifyDataSetChanged()
        actualizarTotal()

        Toast.makeText(this, "Eliminado: ${productoCarrito.producto.nombre}", Toast.LENGTH_SHORT).show()
    }

    private fun realizarCompra() {
        if (carrito.isEmpty()) {
            Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
        } else {
            val total = carrito.sumOf { it.subtotal() }
            Toast.makeText(
                this,
                "Procesando compra de %.2f €".format(total),
                Toast.LENGTH_LONG
            ).show()

            // Aquí iría la lógica de navegación al checkout, etc.
        }
    }
}
