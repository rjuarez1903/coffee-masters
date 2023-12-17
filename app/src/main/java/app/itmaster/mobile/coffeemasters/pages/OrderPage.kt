package app.itmaster.mobile.coffeemasters.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.itmaster.mobile.coffeemasters.data.DataManager
import app.itmaster.mobile.coffeemasters.data.Product
import app.itmaster.mobile.coffeemasters.ui.theme.Alternative1
import app.itmaster.mobile.coffeemasters.ui.theme.Alternative2
import app.itmaster.mobile.coffeemasters.ui.theme.Alternative3
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OrderPage(dataManager: DataManager) {
    var clientName by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        LazyColumn {
            if (dataManager.cart.isNotEmpty()) {
                item {
                    OrderPageBox {
                        Column {
                            OrderPageBoxTitle("ITEMS")
                            Column {
                                dataManager.cart.forEachIndexed { index, item ->
                                    run {
                                        OrderDetail(
                                            product = item.product,
                                            quantity = item.quantity,
                                            productName = item.product.name,
                                            price = item.product.price,
                                            showDivider = index < dataManager.cart.size - 1,
                                            onRemove = { product -> dataManager.cartRemove(product) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                item {
                    OrderPageBox {
                        Column {
                            OrderPageBoxTitle("NAME")
                            OutlinedTextField(
                                value = clientName,
                                onValueChange = { clientName = it },
                                label = { Text("Name for order", color = Alternative1) },
                                shape = RoundedCornerShape(24.dp),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = Alternative1,
                                    unfocusedBorderColor = Alternative1
                                ),
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                            )
                        }
                    }
                    if (clientName != "") {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            Spacer(Modifier.weight(1f))
                            Button(
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Alternative1,
                                    contentColor = Color.White
                                ),
                                onClick = {
                                    handleOrderClick(
                                        dataManager,
                                        coroutineScope,
                                        snackbarHostState
                                    )
                                }
                            ) {
                                Text("Order")
                            }
                        }
                        SnackbarHost(
                            hostState = snackbarHostState,
                        )

                    }
                }
            } else {
                item {
                    OrderPageBox {
                        Text(
                            text = "No items have been added yet",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            color = Alternative1
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OrderDetail(
    product: Product,
    quantity: Int,
    productName: String,
    price: Double,
    showDivider: Boolean,
    onRemove: (Product) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "${quantity}X",
            fontSize = 12.sp,
            color = Alternative1,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = productName, modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
        )
        Text(
            text = "$${String.format("%.2f", price)}",
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        IconButton(
            onClick = { onRemove(product) },
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Delete",
                tint = Alternative1
            )
        }
    }
    Spacer(Modifier.height(24.dp))
    if (showDivider) {
        Divider(
            thickness = 1.dp,
            color = Alternative2,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

fun handleOrderClick(
    dataManager: DataManager,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    dataManager.clearCart()
    coroutineScope.launch {
        snackbarHostState.showSnackbar("Your order will be ready soon!")
    }
}

@Composable
fun OrderPageBox(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .shadow(elevation = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .background(Alternative3)
    ) {
        content()
    }
}

@Composable
fun OrderPageBoxTitle(title: String) {
    Text(
        text = title,
        modifier = Modifier.padding(16.dp),
        color = Alternative1
    )
}
