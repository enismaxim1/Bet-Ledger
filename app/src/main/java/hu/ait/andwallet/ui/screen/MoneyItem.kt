package hu.ait.andwallet.ui.screen

data class MoneyItem (
    val title: String,
    val amount: Double,
    var type: MoneyType
)

enum class MoneyType {
    EXPENSE, INCOME;
}