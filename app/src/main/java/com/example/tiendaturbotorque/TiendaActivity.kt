package com.example.tiendaturbotorque

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tiendaturbotorque.databinding.ActiviyTiendaBinding
import androidx.activity.result.contract.ActivityResultContracts

class TiendaActivity : AppCompatActivity() {

    private lateinit var binding: ActiviyTiendaBinding

    // Carrito real
    private val carrito = mutableListOf<ProductoCarrito>()
    private lateinit var carritoAdapter: CarritoAdapter

    // Launcher para recibir resultado del detalle
    private val detalleProductoLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.let { data ->
                val nombre = data.getStringExtra("PRODUCTO_NOMBRE") ?: return@let
                val precio = data.getDoubleExtra("PRODUCTO_PRECIO", 0.0)
                val imagenRes = data.getIntExtra("PRODUCTO_IMAGEN", 0)
                val cantidad = data.getIntExtra("CANTIDAD", 1)

                // Crear producto y añadir al carrito con la cantidad especificada
                val producto = Producto(nombre, precio, imagenRes)
                añadirAlCarritoConCantidad(producto, cantidad)
            }
        }
    }

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

        val adapter = ProductosAdapter(
            lista = productos,
            onAñadirCarrito = { producto ->
                añadirAlCarrito(producto)
            },
            onVerDetalle = { _, intent ->
                detalleProductoLauncher.launch(intent)
            }
        )

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

    private fun añadirAlCarritoConCantidad(producto: Producto, cantidad: Int) {
        // Buscar si el producto ya existe en el carrito
        val itemExistente = carrito.find { it.producto.nombre == producto.nombre }

        if (itemExistente != null) {
            // Si ya existe, aumentar la cantidad
            itemExistente.cantidad += cantidad
        } else {
            // Si no existe, añadir nuevo item con la cantidad especificada
            carrito.add(ProductoCarrito(producto, cantidad = cantidad))
        }

        // Notificar al adapter y actualizar UI
        carritoAdapter.notifyDataSetChanged()
        actualizarTotal()

        Toast.makeText(
            this,
            "Añadido: $cantidad x ${producto.nombre}",
            Toast.LENGTH_SHORT
        ).show()
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
