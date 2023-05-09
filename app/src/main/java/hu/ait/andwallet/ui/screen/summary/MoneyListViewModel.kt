package hu.ait.andwallet.ui.screen.summary

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import hu.ait.andwallet.ui.screen.MoneyItem
import hu.ait.andwallet.ui.screen.MoneyType


class MoneyListViewModel : ViewModel() {

    private var _moneyList = mutableStateListOf<MoneyItem>()

    fun getList(): List<MoneyItem> {
        return _moneyList
    }

    fun getExpense(): Double {
        var expense = 0.0
        _moneyList.forEach {
            if (it.type == MoneyType.EXPENSE) {
                expense += it.amount
            }
        }
        return expense
    }

    fun getIncome(): Double {
        var income = 0.0
        _moneyList.forEach {
            if (it.type == MoneyType.INCOME) {
                income += it.amount
            }
        }
        return income
    }

    fun add(moneyItem: MoneyItem) {
        _moneyList.add(moneyItem)
    }

    fun removeAt(index: Int) {
        _moneyList.removeAt(index)
    }

    fun clear() {
        _moneyList.clear()
    }
}