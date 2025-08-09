package dev.sertan.android.dfycase.util

/**
 * A sealed class representing the state of a data operation.
 * It can be in one of three states: Success, Error, or Loading.
 *
 * @param T The type of data returned in the Success state.
 * @param message The error message in the Error state.
 */
internal sealed class State<out T> {
    data class Success<T>(val data: T) : State<T>()
    data class Error(val message: String) : State<Nothing>()
    object Loading : State<Nothing>()
}