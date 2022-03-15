package com.example.android.investaxchange.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.example.android.investaxchange.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val navView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val appBarConfiguration: AppBarConfiguration =
            AppBarConfiguration.Builder(R.id.favorite, R.id.market, R.id.home, R.id.settings, R.id.user).build()

        setupActionBarWithNavController(this, navController, appBarConfiguration)
        setupWithNavController(navView, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            navView.visibility =
                when (destination.id) {
                    R.id.market -> View.VISIBLE
                    R.id.home -> View.VISIBLE
                    R.id.favorite -> View.VISIBLE
                    R.id.settings -> View.VISIBLE
                    R.id.user -> View.VISIBLE
                    else -> View.GONE
                }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}