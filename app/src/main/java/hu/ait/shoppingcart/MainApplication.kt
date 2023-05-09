package hu.ait.shoppingcart

import android.app.Application
import hu.ait.shoppingcart.data.AppDatabase

class MainApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}