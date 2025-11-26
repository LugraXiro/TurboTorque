package com.example.tiendaturbotorque

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tiendaturbotorque.databinding.ItemCarritoTiendaBinding

class CarritoAdapter(
    private val lista: MutableList<ProductoCarrito>,
    private val onCantidadCambiada: () -> Unit,  // Callback para actualizar el total
    private val onEliminar: (ProductoCarrito) -> Unit  // Callback para eliminar
) : RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    inner class CarritoViewHolder(val binding: ItemCarritoTiendaBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val binding = ItemCarritoTiendaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CarritoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        val item = lista[position]

        with(holder.binding) {
            // Mostrar datos del producto
            imgProducto.setImageResource(item.producto.imagenRes)
            txtNombre.text = item.producto.nombre
            txtPrecio.text = "%.2f €".format(item.producto.precio)
            etCajaCantidad.setText(item.cantidad.toString())

            // Listener para cambio de cantidad
            etCajaCantidad.setOnEditorActionListener { _, _, _ ->
                val nuevaCantidad = etCajaCantidad.text.toString().toIntOrNull() ?: 1
                if (nuevaCantidad > 0) {
                    item.cantidad = nuevaCantidad
                    onCantidadCambiada()
                }
                true
            }

            // Por si acaso, también detectar cambios al perder el foco
            etCajaCantidad.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    val nuevaCantidad = etCajaCantidad.text.toString().toIntOrNull() ?: 1
                    if (nuevaCantidad > 0) {
                        item.cantidad = nuevaCantidad
                        onCantidadCambiada()
                    }
                }
            }
        }
    }

    override fun getItemCount() = lista.size
}