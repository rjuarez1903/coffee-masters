package app.itmaster.mobile.coffeemasters.data

import android.content.ClipData.Item
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DataManager : ViewModel() {
    var menu: List<Category> by mutableStateOf(listOf())
    var cart: List<ItemInCart> by mutableStateOf(listOf())

    init {
        fetchData()
    }

    fun fetchData() {
        // Ejecuta el getMenu en una corutina (algo así como un thread)
        viewModelScope.launch {
            menu = API.menuService.getMenu()
        }
    }

    fun cartAdd(product: Product) {
        val existingItem = cart.find { it.product == product }
        if (existingItem != null) {
            val updatedCart = cart.toMutableList().apply {
                val index = indexOf(existingItem)
                this[index] = existingItem.copy(quantity = existingItem.quantity + 1)
            }
            cart = updatedCart
        } else {
            cart = cart + ItemInCart(product, 1)
        }
        println(cart)
    }

    fun cartRemove(product: Product) {
        val existingItem = cart.find { it.product == product }
        if (existingItem != null) {
            if (existingItem.quantity > 1) {
                val updatedCart = cart.toMutableList().apply {
                    val index = indexOf(existingItem)
                    this[index] = existingItem.copy(quantity = existingItem.quantity - 1)
                }
                cart = updatedCart
            } else {
                cart = cart - existingItem
            }
        }
    }

}