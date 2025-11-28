package com.example.tiendaturbotorque

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tiendaturbotorque.databinding.ItemProductoTiendaBinding
import android.content.Intent

class ProductosAdapter(
    private val lista: List<Producto>,
    private val onAñadirCarrito: (Producto) -> Unit,
    private val onVerDetalle: (Producto, android.content.Intent) -> Unit
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

            // Click en la tarjeta completa para ver detalle
            tarjeta2.setOnClickListener {
                val intent = Intent(holder.itemView.context, DetalleProductoActivity::class.java).apply {
                    putExtra("PRODUCTO_NOMBRE", producto.nombre)
                    putExtra("PRODUCTO_PRECIO", producto.precio)
                    putExtra("PRODUCTO_IMAGEN", producto.imagenRes)
                    putExtra("PRODUCTO_CATEGORIA", producto.categoria)
                }
                onVerDetalle(producto, intent)
            }
        }
    }

    override fun getItemCount() = lista.size
}