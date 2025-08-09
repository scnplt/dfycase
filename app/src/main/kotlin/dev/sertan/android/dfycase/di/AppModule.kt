package dev.sertan.android.dfycase.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sertan.android.dfycase.BuildConfig
import dev.sertan.android.dfycase.data.repository.FileRepositoryImpl
import dev.sertan.android.dfycase.data.repository.UserRepositoryImpl
import dev.sertan.android.dfycase.domain.repository.FileRepository
import dev.sertan.android.dfycase.domain.repository.UserRepository
import dev.sertan.android.dfycase.util.FCMTokenManager
import javax.inject.Singleton

/**
 * Dependency injection module for providing application-wide dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage =
        Firebase.storage(BuildConfig.FIREBASE_BUCKET_URL)

    @Provides
    @Singleton
    fun provideUserRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore,
        tokenManager: FCMTokenManager
    ): UserRepository = UserRepositoryImpl(auth, firestore, tokenManager)

    @Provides
    @Singleton
    fun provideFileRepository(
        auth: FirebaseAuth,
        storage: FirebaseStorage
    ): FileRepository = FileRepositoryImpl(auth, storage)
}