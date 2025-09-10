

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.michael.kmp.playground.firestore.FetchTriggerRepository
import org.michael.kmp.playground.firestore.FlagsViewModel

val flagsModule = module {
    single { Firebase.firestore }
    single<SharedPreferences> {
        androidContext().getSharedPreferences("flags_repo", Context.MODE_PRIVATE)
    }
    single { FetchTriggerRepository(db = get(), prefs = get(), docPath = "configs/appFlags_dev_android") }

    viewModel { FlagsViewModel(app = androidApplication(), repo = get()) }


}

val firebaseModule = module {
    // Instancia única de Firestore
    single<FirebaseFirestore> { Firebase.firestore }
}