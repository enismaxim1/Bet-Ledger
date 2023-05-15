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

class BetListViewModel(
    private val betDAO: BetDAO
) : ViewModel() {

    fun getAllBets(): Flow<List<BetItem>> {
        return betDAO.getAllBets()
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
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MainApplication)
                BetListViewModel(betDAO = application.database.betDAO())
            }
        }
    }
}