package app.itmaster.mobile.coffeemasters.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.itmaster.mobile.coffeemasters.data.DataManager
import app.itmaster.mobile.coffeemasters.data.Product
import app.itmaster.mobile.coffeemasters.ui.theme.Alternative1
import app.itmaster.mobile.coffeemasters.ui.theme.Alternative2
import app.itmaster.mobile.coffeemasters.ui.theme.Alternative3

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderPage(dataManager: DataManager) {
    Column {
        OrderPageBox {
            if (dataManager.cart.isEmpty()) {
                Text(
                    text = "No items have been added yet",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    color = Alternative1
                )
            } else {
                LazyColumn {
                    item {
                        OrderPageBoxTitle("ITEMS")
                    }
                    itemsIndexed(dataManager.cart) { index, item ->
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

        if (dataManager.cart.isNotEmpty()) {
            OrderPageBox {
                Column {
                    OrderPageBoxTitle("NAME")
                    OutlinedTextField(
                        value = "",
                        onValueChange = {/* TODO */ },
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
