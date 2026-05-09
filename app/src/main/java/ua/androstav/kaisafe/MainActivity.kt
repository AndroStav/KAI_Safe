package ua.androstav.kaisafe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ua.androstav.kaisafe.databinding.ActivityMainBinding
import ua.androstav.kaisafe.utils.FileLogger

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Налаштування перехоплення критичних помилок (крашів)
        setupGlobalExceptionHandler()

        // 2. Логування запуску програми
        FileLogger.log(this, "LIFECYCLE", "App Started (onCreate)")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_map, R.id.navigation_contacts
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onDestroy() {
        // 3. Логування закриття програми
        FileLogger.log(this, "LIFECYCLE", "App Stopped (onDestroy)")
        super.onDestroy()
    }

    private fun setupGlobalExceptionHandler() {
        val oldHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            // Логування критичної помилки перед вильотом
            FileLogger.log(this, "ERROR", "CRASH: ${throwable.message}\nStacktrace: ${throwable.stackTraceToString()}")
            oldHandler?.uncaughtException(thread, throwable)
        }
    }
}