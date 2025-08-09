package dev.sertan.android.dfycase.domain.repository

import dev.sertan.android.dfycase.util.State
import kotlinx.coroutines.flow.Flow

internal interface UserRepository {

    fun login(email: String, pass: String): Flow<State<Unit>>

    fun register(email: String, pass: String): Flow<State<Unit>>

    suspend fun updateFCMToken(token: String)

}
