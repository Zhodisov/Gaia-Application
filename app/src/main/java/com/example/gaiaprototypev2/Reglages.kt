package com.example.gaiaprototypev2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class Reglages : AppCompatActivity() {

    private lateinit var switchNotificationsProduction: Switch
    private lateinit var switchAlertesSecurite: Switch
    private lateinit var switchAccesDistance: Switch
    private lateinit var switchMisesAJourSysteme: Switch
    private lateinit var switchConfidentialite: Switch
    private lateinit var switchNotification: Switch
    private lateinit var switchHistoriqueConnexions: Switch
    private lateinit var spinnerLangue: Spinner
    private lateinit var spinnerTheme: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reglages)

        switchNotificationsProduction = findViewById(R.id.switch_notifications_production)
        switchAlertesSecurite = findViewById(R.id.switch_alertes_securite)
        switchAccesDistance = findViewById(R.id.switch_acces_distance)
        switchMisesAJourSysteme = findViewById(R.id.switch_mises_a_jour_systeme)
        switchConfidentialite = findViewById(R.id.switch_parametres_confidentialite)
        switchNotification = findViewById(R.id.switch_canaux_notification)
        switchHistoriqueConnexions = findViewById(R.id.switch_historique_connexions)

        switchNotificationsProduction.setOnCheckedChangeListener { _, estCoche ->
            if (estCoche) {
            } else {
            }
        }


        initialiserNavigation()
        mettreAJourOngletSelectionne(R.id.nav_reglages)
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
            val intent = Intent(this, Notifications::class.java)
            startActivity(intent)
            finish()
        }

        navReglages.setOnClickListener {
            mettreAJourOngletSelectionne(R.id.nav_reglages)
        }
    }

    private fun mettreAJourOngletSelectionne(idSelectionne: Int) {
        val icones = listOf(
            R.id.icon_accueil,
            R.id.icon_stats,
            R.id.icon_notifications,
            R.id.icon_historique,
            R.id.icon_reglages
        )
        val textes = listOf(
            R.id.text_accueil,
            R.id.text_stats,
            R.id.text_notifications,
            R.id.text_historique,
            R.id.text_reglages
        )

        icones.forEach { id ->
            findViewById<ImageView>(id).setColorFilter(ContextCompat.getColor(this, R.color.black))
        }
        textes.forEach { id ->
            findViewById<TextView>(id).setTextColor(ContextCompat.getColor(this, R.color.black))
        }

        when (idSelectionne) {
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
            R.id.nav_historique -> {
                findViewById<ImageView>(R.id.icon_historique).setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary))
                findViewById<TextView>(R.id.text_historique).setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            }
            R.id.nav_reglages -> {
                findViewById<ImageView>(R.id.icon_reglages).setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary))
                findViewById<TextView>(R.id.text_reglages).setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            }
        }
    }
}
