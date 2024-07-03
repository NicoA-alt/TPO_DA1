import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpo_da1.ui.data.FilterRepository
import com.example.tpo_da1.ui.domain.FilterSettings
import com.example.tpo_da1.ui.domain.Store
import kotlinx.coroutines.launch

class FilterViewModel(private val repository: FilterRepository) : ViewModel() {

    private val _stores = MutableLiveData<List<Store>>()
    val stores: LiveData<List<Store>> get() = _stores

    private val _filters = MutableLiveData<FilterSettings>()
    val filters: LiveData<FilterSettings> get() = _filters

    fun loadStores() {
        viewModelScope.launch {
            val stores = repository.getStores()
            _stores.value = stores
        }
    }

    fun loadFilters() {
        _filters.value = repository.loadFilters()
    }

    fun saveFilters(filterSettings: FilterSettings) {
        repository.saveFilters(filterSettings)
    }
}