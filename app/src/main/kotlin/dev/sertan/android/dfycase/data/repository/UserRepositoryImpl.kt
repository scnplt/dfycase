package dev.sertan.android.dfycase.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dev.sertan.android.dfycase.data.model.User
import dev.sertan.android.dfycase.domain.repository.UserRepository
import dev.sertan.android.dfycase.util.FCMTokenManager
import dev.sertan.android.dfycase.util.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val TAG = "TAG_USER_REPO"
private const val USERS_COL_NAME = "users"

internal class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val fcmTokenManager: FCMTokenManager
) : UserRepository {

    private val usersCollection get() = firestore.collection(USERS_COL_NAME)

    override fun login(email: String, pass: String): Flow<State<Unit>> = flow {
        emit(State.Loading)
        try {
            auth.signInWithEmailAndPassword(email, pass).await()
            fcmTokenManager.token?.let {
                updateFCMToken(it)
                fcmTokenManager.deleteToken()
            }
            emit(State.Success(Unit))
        } catch (e: Exception) {
            emit(State.Error(e.message.toString()))
        }
    }

    override fun register(email: String, pass: String): Flow<State<Unit>> = flow {
        emit(State.Loading)
        try {
            val result = auth.createUserWithEmailAndPassword(email, pass).await()
            val user = result.user ?: throw Exception("User not found")
            val userDocument = usersCollection.document(user.uid)
            val userModel = User(id = user.uid, email = email)
            userDocument.set(userModel).await()
            fcmTokenManager.token?.let {
                updateFCMToken(it)
                fcmTokenManager.deleteToken()
            }
            emit(State.Success(Unit))
        } catch (e: Exception) {
            emit(State.Error(e.message.toString()))
        }
    }

    override suspend fun updateFCMToken(token: String) {
        try {
            val userId = auth.currentUser?.uid
            if (userId == null) {
                fcmTokenManager.token = token
                return
            }

            usersCollection
                .document(userId)
                .update(User::fcmToken.name, token)
                .await()
        } catch (e: Exception) {
            Log.d(TAG, "Error updating FCM token: ${e.message}")
        }
    }
}
