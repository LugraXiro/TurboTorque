package com.example.tiendaturbotorque

import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Colores del tema
val VerdePrincipal = Color(0xFF8BC34A)
val RojoPrecio = Color(0xFFD32F2F)
val GrisClaro = Color(0xFFF0F0F0)
val GrisTexto = Color(0xFF757575)
val Negro = Color(0xFF333333)
val Blanco = Color(0xFFFFFFFF)
val LineaDivisoria = Color(0xFFEEEEEE)

class DetalleProductoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Recibir datos del producto
        val nombreProducto = intent.getStringExtra("PRODUCTO_NOMBRE") ?: ""
        val precioProducto = intent.getDoubleExtra("PRODUCTO_PRECIO", 0.0)
        val imagenProducto = intent.getIntExtra("PRODUCTO_IMAGEN", R.drawable.ic_launcher_foreground)

        setContent {
            DetalleProductoScreen(
                nombre = nombreProducto,
                precio = precioProducto,
                imagenRes = imagenProducto,
                onBackPressed = { finish() },
                onAddToCart = { cantidad ->
                    // Devolver resultado con el producto y cantidad
                    val resultIntent = android.content.Intent().apply {
                        putExtra("PRODUCTO_NOMBRE", nombreProducto)
                        putExtra("PRODUCTO_PRECIO", precioProducto)
                        putExtra("PRODUCTO_IMAGEN", imagenProducto)
                        putExtra("CANTIDAD", cantidad)
                    }
                    setResult(RESULT_OK, resultIntent)
                    finish()
                }
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(
    nombre: String,
    precio: Double,
    imagenRes: Int,
    onBackPressed: () -> Unit,
    onAddToCart: (Int) -> Unit
) {
    var cantidad by remember { mutableIntStateOf(1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Detalle del Producto",
                        color = Blanco
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás",
                            tint = Blanco
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VerdePrincipal
                )
            )
        },
        bottomBar = {
            BottomBar(
                cantidad = cantidad,
                onCantidadChange = { cantidad = it },
                onAddToCart = { onAddToCart(cantidad) }
            )
        }
    ) { paddingValues ->
        ContenidoProducto(
            modifier = Modifier.padding(paddingValues),
            nombre = nombre,
            precio = precio,
            imagenRes = imagenRes
        )
    }
}
@Composable
fun ContenidoProducto(
    modifier: Modifier = Modifier,
    nombre: String,
    precio: Double,
    imagenRes: Int
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(GrisClaro)
    ) {
        // IMAGEN DEL PRODUCTO
        Image(
            painter = painterResource(id = imagenRes),
            contentDescription = nombre,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(GrisClaro)
                .padding(16.dp),
            contentScale = ContentScale.Fit
        )

        // CARD CON INFORMACIÓN
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Blanco),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                // HEADER: Nombre + Precio
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = nombre,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Negro,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "%.2f €".format(precio),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = RojoPrecio
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 16.dp),
                    color = LineaDivisoria
                )

                // DESCRIPCIÓN
                Text(
                    text = "Descripción",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Negro
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Experimenta el máximo rendimiento y un estilo inigualable con estas llantas. " +
                            "Diseñadas para vehículos de alta gama, ofrecen un agarre excepcional en seco y mojado, " +
                            "y una respuesta de dirección precisa.",
                    fontSize = 14.sp,
                    color = GrisTexto,
                    lineHeight = 21.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ESPECIFICACIONES
                Text(
                    text = "Especificaciones",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Negro
                )
                Spacer(modifier = Modifier.height(8.dp))

                EspecRow("Marca", "TurboTorque")
                EspecRow("Modelo", "RR Sport")
                EspecRow("Diámetro", "19\"")
                EspecRow("Ancho", "8.5J")
                EspecRow("Material", "Aleación de Aluminio")
                EspecRow("Color", "Gris Antracita")
            }
        }
    }
}
@Composable
fun EspecRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = GrisTexto
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Negro
        )
    }
}
@Composable
fun BottomBar(
    cantidad: Int,
    onCantidadChange: (Int) -> Unit,
    onAddToCart: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Blanco,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // SELECTOR DE CANTIDAD
            Row(
                modifier = Modifier
                    .background(Blanco, shape = MaterialTheme.shapes.medium)
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { if (cantidad > 1) onCantidadChange(cantidad - 1) }
                ) {
                    Text(
                        text = "−",
                        fontSize = 24.sp,
                        color = Negro
                    )
                }
                Text(
                    text = cantidad.toString(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Negro,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                IconButton(
                    onClick = { onCantidadChange(cantidad + 1) }
                ) {
                    Text(
                        text = "+",
                        fontSize = 24.sp,
                        color = Negro
                    )
                }
            }

            // BOTÓN AÑADIR AL CARRITO
            Button(
                onClick = onAddToCart,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdePrincipal
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "Añadir al Carrito",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}