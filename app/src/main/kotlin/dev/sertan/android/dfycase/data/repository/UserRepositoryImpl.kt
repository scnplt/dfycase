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

// TAG for logging
private const val TAG = "TAG_USER_REPO"

// Collection name for users in Firestore
private const val USERS_COL_NAME = "users"

/**
 * Implementation of UserRepository that handles user authentication and management
 * using Firebase Authentication and Firestore.
 * This class provides methods for user login, registration,
 * updating FCM tokens, and logging out.
 *
 * @param auth FirebaseAuth instance for user authentication
 * @param firestore FirebaseFirestore instance for database operations
 * @param fcmTokenManager FCMTokenManager instance for managing FCM tokens
 * @constructor Creates an instance of UserRepositoryImpl
 */
internal class UserRepositoryImpl(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val fcmTokenManager: FCMTokenManager
) : UserRepository {

    /** Firestore collection reference for users. */
    private val usersCollection get() = firestore.collection(USERS_COL_NAME)

    /**
     * Logs in a user with the provided email and password.
     * Emits a loading state while the operation is in progress.
     * On success, emits a success state with Unit.
     * On failure, emits an error state with the exception message.
     *
     * @param email User's email address
     * @param pass User's password
     * @return Flow<State<Unit>> representing the login operation state
     */
    override fun login(email: String, pass: String): Flow<State<Unit>> = flow {
        emit(State.Loading)
        try {
            auth.signInWithEmailAndPassword(email, pass).await()
            fcmTokenManager.token?.let { updateFCMToken(it) }
            emit(State.Success(Unit))
        } catch (e: Exception) {
            emit(State.Error(e.message.toString()))
        }
    }

    /**
     * Registers a new user with the provided email and password.
     * Emits a loading state while the operation is in progress.
     * On success, creates a user document in Firestore and emits a success state with Unit.
     * On failure, emits an error state with the exception message.
     *
     * @param email User's email address
     * @param pass User's password
     * @return Flow<State<Unit>> representing the registration operation state
     */
    override fun register(email: String, pass: String): Flow<State<Unit>> = flow {
        emit(State.Loading)
        try {
            val result = auth.createUserWithEmailAndPassword(email, pass).await()
            val user = result.user ?: throw Exception("User not found")
            val userDocument = usersCollection.document(user.uid)
            val userModel = User(id = user.uid, email = email)
            userDocument.set(userModel).await()
            fcmTokenManager.token?.let { updateFCMToken(it) }
            emit(State.Success(Unit))
        } catch (e: Exception) {
            emit(State.Error(e.message.toString()))
        }
    }

    /**
     * Updates the FCM token for the currently authenticated user.
     * If the user is not authenticated, it stores the token in FCMTokenManager.
     *
     * @param token The FCM token to be updated
     */
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

    /**
     * Logs out the currently authenticated user.
     * Clears the FCM token with FCMTokenManager.
     */
    override suspend fun logout() {
        auth.signOut()
        fcmTokenManager.deleteToken()
    }
}
