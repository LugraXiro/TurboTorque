package com.example.tiendaturbotorque

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
                val categoria = data.getStringExtra("PRODUCTO_CATEGORIA") ?: "LLANTAS"
                val cantidad = data.getIntExtra("CANTIDAD", 1)

                // Crear producto y añadir al carrito con la cantidad especificada
                val producto = Producto(nombre, precio, imagenRes, categoria)
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
            Producto("Llantas RR Sport 19\"", 249.99, R.drawable.ic_launcher_foreground, "LLANTAS"),
            Producto("Llanta Xtreme", 230.00, R.drawable.ic_launcher_foreground, "LLANTAS"),
            Producto("Llanta Deportiva Z 20\"", 310.00, R.drawable.ic_launcher_foreground, "LLANTAS"),
            Producto("Neumático Michelin 195/65", 89.99, R.drawable.ic_launcher_foreground, "NEUMÁTICOS"),
            Producto("Neumático Pirelli 205/55", 95.50, R.drawable.ic_launcher_foreground, "NEUMÁTICOS")
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

        binding.recyclerCarrito.layoutManager = LinearLayoutManager(this)
        binding.recyclerCarrito.adapter = carritoAdapter

        configurarSpinner()  // ← AÑADIR ESTA LÍNEA

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

    private fun configurarSpinner() {
        // Crear un ArrayAdapter desde el recurso de arrays.xml
        // Este adapter convierte el array "categorias" en elementos que puede mostrar el Spinner
        ArrayAdapter.createFromResource(
            this,  // Contexto (la Activity actual)
            R.array.categorias,  // El array que creamos en arrays.xml con TODOS, LLANTAS, NEUMÁTICOS
            android.R.layout.simple_spinner_item  // Layout por defecto de Android para el item del Spinner
        ).also { adapter ->
            // Establecer el layout para el menú desplegable (el dropdown)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            // Asignar el adapter al Spinner (esto hace que el Spinner muestre las opciones)
            binding.spinnerCategorias.adapter = adapter
        }

        // Configurar el listener del Spinner (qué hacer cuando el usuario selecciona algo)
        binding.spinnerCategorias.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                // Este metodo se ejecuta cuando el usuario selecciona un item del Spinner
                override fun onItemSelected(
                    parent: AdapterView<*>?,  // El Spinner que lanzó el evento
                    view: View?,  // La vista del item seleccionado
                    position: Int,  // La posición del item (0=TODOS, 1=LLANTAS, 2=NEUMÁTICOS)
                    id: Long  // ID del item (no lo usamos)
                ) {
                    // Obtener el texto del item seleccionado (ej: "LLANTAS")
                    val categoriaSeleccionada = parent?.getItemAtPosition(position).toString()

                    // Llamar al metodo que filtra los productos según la categoría
                    filtrarProductos(categoriaSeleccionada)
                }

                // Este metodo se ejecuta si no hay nada seleccionado (casi nunca pasa)
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // No hacemos nada aquí
                }
            }
    }

    private fun filtrarProductos(categoria: String) {
        // PASO 1: Crear la lista completa de todos los productos disponibles
        // (En el futuro, esto podría venir de una base de datos)
        val todosLosProductos = listOf(
            // Productos de la categoría LLANTAS
            Producto("Llantas RR Sport 19\"", 249.99, R.drawable.ic_launcher_foreground, "LLANTAS"),
            Producto("Llanta Xtreme", 230.00, R.drawable.ic_launcher_foreground, "LLANTAS"),
            Producto("Llanta Deportiva Z 20\"", 310.00, R.drawable.ic_launcher_foreground, "LLANTAS"),

            // Productos de la categoría NEUMÁTICOS
            Producto("Neumático Michelin 195/65", 89.99, R.drawable.ic_launcher_foreground, "NEUMÁTICOS"),
            Producto("Neumático Pirelli 205/55", 95.50, R.drawable.ic_launcher_foreground, "NEUMÁTICOS")
        )

        // PASO 2: Filtrar los productos según la categoría seleccionada
        val productosFiltrados = if (categoria == "TODOS") {
            // Si seleccionó "TODOS", mostrar la lista completa sin filtrar
            todosLosProductos
        } else {
            // Si seleccionó una categoría específica (ej: "LLANTAS"),
            // filtrar solo los productos que tengan esa categoría
            // El .filter { } recorre cada producto y solo deja los que cumplen la condición
            todosLosProductos.filter { it.categoria == categoria }
        }

        // PASO 3: Crear un nuevo adapter con los productos filtrados
        val adapter = ProductosAdapter(
            lista = productosFiltrados,  // Lista filtrada que se mostrará

            // Callback: qué hacer cuando se pulse "Añadir al Carrito"
            onAñadirCarrito = { producto ->
                añadirAlCarrito(producto)  // Llamar al metodo que añade al carrito
            },

            // Callback: qué hacer cuando se pulse en la tarjeta para ver el detalle
            onVerDetalle = { _, intent ->
                detalleProductoLauncher.launch(intent)  // Navegar a la pantalla de detalle
            }
        )

        // PASO 4: Actualizar el RecyclerView con el nuevo adapter
        // Esto hace que el grid de productos se actualice y muestre solo los filtrados
        binding.recyclerProductos.adapter = adapter
    }
}
