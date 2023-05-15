package hu.ait.betledger

import android.app.Application
import hu.ait.betledger.data.AppDatabase

class MainApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}