import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import hu.ait.betledger.MainApplication
import hu.ait.betledger.data.BetDAO
import hu.ait.betledger.data.BetItem
import hu.ait.betledger.data.ResolutionStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


data class BetItemFilter(
    var query: String? = null,
    var party1: String? = null,
    var party2: String? = null,
    var before: String? = null,
    var after: String? = null,
    var status: ResolutionStatus? = null
)

class BetListViewModel(
    private val betDAO: BetDAO
) : ViewModel() {

    fun getAllBets(): Flow<List<BetItem>> {
        return betDAO.getAllBets()
    }

    fun getAllBetsFiltered(
        filter: BetItemFilter
    ): Flow<List<BetItem>> {
        return betDAO.getAllBetsFiltered(
            filter.query, filter.party1, filter.party2, filter.before, filter.after, filter.status
        )
    }

    fun getResolvedBetsNum(): Int {
        return 0 // You should update this method to return the count of resolved bets
    }

    fun addBet(betItem: BetItem) {
        viewModelScope.launch {
            betDAO.insert(betItem)
        }
    }

    fun removeBet(betItem: BetItem) {
        viewModelScope.launch {
            betDAO.delete(betItem)
        }
    }

    fun clearAllBets() {
        viewModelScope.launch {
            betDAO.deleteAllBets()
        }
    }

    fun changeBetResolutionStatus(betItem: BetItem, value: ResolutionStatus) {
        val newBetItem = betItem.copy(resolutionStatus = value)
        viewModelScope.launch {
            betDAO.update(newBetItem)
        }
    }

    fun editBetItem(betEdited: BetItem) {
        viewModelScope.launch {
            betDAO.update(betEdited)
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MainApplication)
                BetListViewModel(betDAO = application.database.betDAO())
            }
        }
    }
}