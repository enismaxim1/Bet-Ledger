package hu.ait.shoppingcart.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import hu.ait.shoppingcart.MainApplication
import hu.ait.shoppingcart.data.ShoppingDAO
import hu.ait.shoppingcart.data.ShoppingItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
class ShoppingListViewModel(
    private val shoppingDAO: ShoppingDAO
) : ViewModel() {
    fun getAllShoppingList(): Flow<List<ShoppingItem>> {
        return shoppingDAO.getAllShopping()
    }

    fun getAllShoppingNum(): Int {
        //return _todoList.size
        return 0
    }
    fun getImportantShoppingNum(): Int {
        return 0
        //return _todoList.count { it.priority == TodoPriority.HIGH }
    }
    fun addShoppingList(shoppingItem: ShoppingItem) {
        viewModelScope.launch {
            shoppingDAO.insert(shoppingItem)
        }
    }

    fun removeShoppingItem(shoppingItem: ShoppingItem) {
        viewModelScope.launch {
            shoppingDAO.delete(shoppingItem)
        }
    }

    fun clearAllShopping() {
        viewModelScope.launch {
            shoppingDAO.deleteAllShopping()
        }
    }

    fun changeShoppingState(shoppingItem: ShoppingItem, value: Boolean) {
        // it will recompose the item only if we create a new object
        // we do this with the copy, so the database ID will not be changed,
        // but it will replace a whole row in the table so the Flow will emit a new state
        // that will cause a recomposition
        val newShoppingItem = shoppingItem.copy()
        newShoppingItem.status = value

        viewModelScope.launch {
            shoppingDAO.update(newShoppingItem)
        }
    }

    fun editShoppingItem(shoppingEdited: ShoppingItem) {
        viewModelScope.launch {
            shoppingDAO.update(shoppingEdited)
        }
    }


    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MainApplication)
                ShoppingListViewModel(shoppingDAO = application.database.shoppingDAO())
            }
        }
    }
}


