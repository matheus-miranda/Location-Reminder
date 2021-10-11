package com.udacity.project4.authentication

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * Observes the current Firebase user. If no user is logged in, it will be null.
 */
class FirebaseUserLiveData : LiveData<FirebaseUser?>() {
    private val firebaseAuth = FirebaseAuth.getInstance()

    // Set the value of this FireUserLiveData object by hooking it up to equal the value of the
    // current FirebaseUser
    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        // Current user
        value = firebaseAuth.currentUser
    }

    // When this object has an active observer, start observing the FirebaseAuth state to see if
    // there is currently a logged in user.
    override fun onActive() {
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    // When this object no longer has an active observer, stop observing the FirebaseAuth state to
    // prevent memory leaks.
    override fun onInactive() {
        firebaseAuth.removeAuthStateListener(authStateListener)
    }
}