import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kalmarium.data.repository.UserSettingsRepository
import com.example.kalmarium.data.repository.VasarRepository
import com.example.kalmarium.ui.viewmodel.VasarSzerkesztesViewModel

class VasarSzerkesztesViewModelFactory(
    private val vasarRepository: VasarRepository,
    private val userSettingsRepository: UserSettingsRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VasarSzerkesztesViewModel::class.java)) {
            return VasarSzerkesztesViewModel(
                vasarRepository,
                userSettingsRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
