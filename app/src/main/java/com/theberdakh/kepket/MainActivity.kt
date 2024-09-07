package com.theberdakh.kepket

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.theberdakh.kepket.data.local.LocalPreferences
import com.theberdakh.kepket.presentation.screens.allorders.AllOrdersScreen
import com.theberdakh.navigation.NavigationExtensions.addFragment
import com.theberdakh.kepket.presentation.screens.login.LoginScreen
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private val localPreferences by inject<LocalPreferences>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val firstFragment = if (localPreferences.getLoginStatus()) {
            AllOrdersScreen.newInstance()
        } else {
            LoginScreen.newInstance()
        }

        supportFragmentManager.addFragment(R.id.main, firstFragment)


    }
}
