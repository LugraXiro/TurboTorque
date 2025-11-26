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
            txtCantidad.text = item.cantidad.toString()

            // Botón eliminar
            btnEliminar.setOnClickListener {
                if (item.cantidad > 1) {
                    // Si hay más de 1, reducir cantidad
                    item.cantidad--
                    notifyItemChanged(position)
                    onCantidadCambiada()
                } else {
                    // Si solo hay 1, eliminar del carrito
                    onEliminar(item)
                }
            }
        }
    }



    override fun getItemCount() = lista.size
}