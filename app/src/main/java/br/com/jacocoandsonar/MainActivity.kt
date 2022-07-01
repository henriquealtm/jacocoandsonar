package br.com.jacocoandsonar

import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import br.com.jacocoandsonar.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

@Deprecated("...")
class SeeSonarHandleDeprecated

class MainActivity : AppCompatActivity() {

    @VisibleForTesting
    lateinit var binding: ActivityMainBinding

    @VisibleForTesting
    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil
            .setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply {
                vm = viewModel
            }

        observeShowSnackBar()

        print(SeeSonarHandleDeprecated())
    }

    private fun observeShowSnackBar() {
        viewModel.showToastMessageWithId.observe(this, ::showToast)
    }

    fun showToast(toastMessageId: Int?) {
        toastMessageId?.let {
            Snackbar
                .make(binding.root, getString(toastMessageId), Snackbar.LENGTH_LONG)
                .show()
        }
    }

}