package app.komiteku.base

import android.app.Application
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import app.komiteku.data.local.SessionSerializer
import app.komiteku.data.remote.ApiService
import app.komiteku.data.repo.AuthRepo
import app.komiteku.data.repo.MainRepo
import app.komiteku.ui.AuthViewModel
import app.komiteku.ui.MainViewModel
import app.komiteku.ui.PaymentViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.dsl.module

class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val networkModule = module {
            single { ApiService.getApiMethod() }
        }

        val dataStoreModule = module {
            single { PreferenceDataStoreFactory.create { androidContext().preferencesDataStoreFile("UserSessionInfo") } }
            single { SessionSerializer }
        }

        val repositoryModule = module {
            single { AuthRepo(get(), get()) }
            single { MainRepo(get(), get()) }
        }

        val viewModelModule = module {
            viewModel { AuthViewModel(get()) }
            viewModel { MainViewModel(get()) }
            viewModelOf(::PaymentViewModel)
        }

        startKoin {
            androidContext(this@BaseApp)
            modules(
                listOf(
                    networkModule,
                    dataStoreModule,
                    repositoryModule,
                    viewModelModule
                )
            )
        }
    }
}
