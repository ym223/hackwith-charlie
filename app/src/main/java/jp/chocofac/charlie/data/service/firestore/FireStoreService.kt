package jp.chocofac.charlie.data.service.firestore

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.chocofac.charlie.data.service.location.FireStoreRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FireStoreService {

    @Singleton
    @Provides
    fun provideFirebaseFireStoreProvider(): FirebaseFirestore {
        return Firebase.firestore
    }

    @Singleton
    @Provides
    fun provideFirebaseAuthProvider(): FirebaseAuth {
        return Firebase.auth
    }

    @Singleton
    @Provides
    fun provideFireStoreRepositoryProvider(
        db: FirebaseFirestore,
        auth: FirebaseAuth
    ): FireStoreRepository {
        return FireStoreRepository(
            db,
            auth
        )
    }
}