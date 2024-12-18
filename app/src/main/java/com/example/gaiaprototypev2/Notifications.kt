package com.example.gaiaprototypev2

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class Notifications : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        initialiserNavigation()
        mettreAJourOngletSelectionne(R.id.nav_notifications)
    }

    private fun initialiserNavigation() {
        val navAccueil = findViewById<LinearLayout>(R.id.nav_accueil)
        val navStats = findViewById<LinearLayout>(R.id.nav_stats)
        val navNotifications = findViewById<LinearLayout>(R.id.nav_notifications)
        val navReglages = findViewById<LinearLayout>(R.id.nav_reglages)

        navAccueil.setOnClickListener {
            mettreAJourOngletSelectionne(R.id.nav_accueil)
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        navStats.setOnClickListener {
            mettreAJourOngletSelectionne(R.id.nav_stats)
            val intent = Intent(this, Statistiquess::class.java)
            startActivity(intent)
            finish()
        }
        navNotifications.setOnClickListener {
            mettreAJourOngletSelectionne(R.id.nav_notifications)
        }
        navReglages.setOnClickListener {
            mettreAJourOngletSelectionne(R.id.nav_reglages)
            val intent = Intent(this, Reglages::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun mettreAJourOngletSelectionne(selectedId: Int) {
        val icons = listOf(
            R.id.icon_accueil,
            R.id.icon_stats,
            R.id.icon_notifications,
            R.id.icon_reglages
        )
        val texts = listOf(
            R.id.text_accueil,
            R.id.text_stats,
            R.id.text_notifications,
            R.id.text_reglages
        )

        icons.forEach { id ->
            findViewById<ImageView>(id).setColorFilter(ContextCompat.getColor(this, R.color.black))
        }
        texts.forEach { id ->
            findViewById<TextView>(id).setTextColor(ContextCompat.getColor(this, R.color.black))
        }

        when (selectedId) {
            R.id.nav_accueil -> {
                findViewById<ImageView>(R.id.icon_accueil).setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary))
                findViewById<TextView>(R.id.text_accueil).setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            }
            R.id.nav_stats -> {
                findViewById<ImageView>(R.id.icon_stats).setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary))
                findViewById<TextView>(R.id.text_stats).setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            }
            R.id.nav_notifications -> {
                findViewById<ImageView>(R.id.icon_notifications).setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary))
                findViewById<TextView>(R.id.text_notifications).setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            }
            R.id.nav_reglages -> {
                findViewById<ImageView>(R.id.icon_reglages).setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary))
                findViewById<TextView>(R.id.text_reglages).setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            }
        }
    }
}
