package com.udacity.project4.authentication

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.udacity.project4.locationreminders.RemindersActivity

/**
 * Creating a base launcher activity with no UI to avoid the brief screen glitch when checking for
 * a logged in user from the [AuthenticationActivity]
 */
class LauncherActivity : AppCompatActivity() {

    private val viewModel by viewModels<AuthenticationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeAuthenticationState()
    }

    /**
     * Check if there is a firebase user currently logged in, and navigate accordingly
     */
    private fun observeAuthenticationState() {
        viewModel.authenticationState.observe(this, { authenticationState ->
            when (authenticationState) {
                AuthenticationViewModel.AuthenticationState.AUTHENTICATED -> navigateToReminderActivity()
                else -> navigateToAuthenticationActivity()
            }
        })
    }

    private fun navigateToReminderActivity() {
        Intent(this, RemindersActivity::class.java).apply {
            startActivity(this)
        }
        finish()
    }

    private fun navigateToAuthenticationActivity() {
        Intent(this, AuthenticationActivity::class.java).apply {
            startActivity(this)
        }
        finish()
    }
}