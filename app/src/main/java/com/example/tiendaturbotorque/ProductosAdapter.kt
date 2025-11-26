package com.example.tiendaturbotorque

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tiendaturbotorque.databinding.ItemProductoTiendaBinding

class ProductosAdapter(
    private val lista: List<Producto>,
    private val onAñadirCarrito: (Producto) -> Unit
) : RecyclerView.Adapter<ProductosAdapter.ProductoViewHolder>() {

    inner class ProductoViewHolder(val binding: ItemProductoTiendaBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val binding = ItemProductoTiendaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = lista[position]

        with(holder.binding) {
            // Mostrar imagen del producto
            imgProducto.setImageResource(producto.imagenRes)

            // Mostrar nombre y precio
            txtNombreProducto.text = producto.nombre
            txtPrecioProducto.text = "%.2f €".format(producto.precio)

            // Click en botón añadir
            btnAnadirCarrito.setOnClickListener {
                onAñadirCarrito(producto)
            }
        }
    }

    override fun getItemCount() = lista.size
}