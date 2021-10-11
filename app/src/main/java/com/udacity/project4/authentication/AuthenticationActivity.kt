package com.udacity.project4.authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.udacity.project4.R
import com.udacity.project4.databinding.ActivityAuthenticationBinding
import com.udacity.project4.locationreminders.RemindersActivity
import timber.log.Timber

/**
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */
class AuthenticationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthenticationBinding
    private lateinit var loginRequest: ActivityResultLauncher<Intent>
    private val viewModel by viewModels<AuthenticationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_authentication)

        observeAuthenticationState()
        getResultFromLoginRequest()
        bindListeners()
    }

    /**
     * Check if there is a firebase user currently logged in, then navigate to [RemindersActivity]
     */
    private fun observeAuthenticationState() {
        viewModel.authenticationState.observe(this, { authenticationState ->
            when (authenticationState) {
                AuthenticationViewModel.AuthenticationState.AUTHENTICATED -> navigateToReminderActivity()
                else -> Timber.i("No authenticated user")
            }
        })
    }

    private fun navigateToReminderActivity() {
        Intent(this, RemindersActivity::class.java).apply {
            startActivity(this)
        }
        finish()
    }

    /**
     * Get the response from the login Intent to find out if user was able to sign in/up successfully
     */
    private fun getResultFromLoginRequest() {
        loginRequest =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                val response = IdpResponse.fromResultIntent(result.data)

                if (result.resultCode == Activity.RESULT_OK) {
                    Timber.i("Successfully signed in user " + FirebaseAuth.getInstance().currentUser?.displayName + "!")
                    finish() // called because we are observing for an authenticated user
                } else {
                    Timber.i("Unsuccessful: " + response?.error?.message)
                }
            }
    }

    private fun bindListeners() {
        binding.btnLogin.setOnClickListener { firebaseLoginFlow() }
    }

    /**
     * Create account and sign in using FirebaseUI, using email or Google
     */
    private fun firebaseLoginFlow() {
        // Give users the option to sign in with e-mail or Google
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        // Create Intent to be passed on to loginRequest launcher
        val loginIntent = Intent(
            AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.map)
                .build()
        )
        loginRequest.launch(loginIntent)
    }
}
