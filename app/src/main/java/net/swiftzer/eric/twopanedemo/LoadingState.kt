package net.swiftzer.eric.twopanedemo

/**
 * Represent backend server loading state.
 */
enum class LoadingState {
    /** Request is sent and wait for response. */
    LOADING,
    /** Success response is received */
    SUCCESS,
    /** Error response is received or the request is failed */
    FAIL
}
