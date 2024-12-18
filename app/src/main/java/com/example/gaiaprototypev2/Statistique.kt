package com.example.gaiaprototypev2

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Statistiquess : AppCompatActivity() {

    private lateinit var texteDateSelectionnee: TextView
    private lateinit var texteInfosMeteo: TextView
    private lateinit var texteTemperature: TextView
    private lateinit var textePrecipitation: TextView
    private lateinit var texteTempMax: TextView
    private lateinit var texteTempMin: TextView
    private lateinit var texteSunrise: TextView
    private lateinit var texteSunset: TextView
    private lateinit var texteMoonrise: TextView
    private lateinit var texteMoonset: TextView
    private lateinit var texteMoonPhase: TextView
    private lateinit var texteDewPoint: TextView
    private lateinit var texteWindSpeed: TextView
    private lateinit var texteWindDeg: TextView
    private lateinit var texteWindGust: TextView
    private lateinit var texteClouds: TextView
    private lateinit var textePop: TextView
    private lateinit var texteUvi: TextView
    private lateinit var texteHumidity: TextView
    private lateinit var calendrier: CalendarView
    private lateinit var iconeMeteo: ImageView

    private lateinit var clientLocalisation: FusedLocationProviderClient
    private lateinit var fileRequetes: RequestQueue
    private lateinit var texteProductionEstimee: TextView
    private lateinit var texteRatioPuissance: TextView
    private lateinit var iconeProduction: ImageView

    private val CODE_PERMISSION_LOCALISATION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistique)

        texteDateSelectionnee = findViewById(R.id.tv_selected_date)
        texteInfosMeteo = findViewById(R.id.tv_weather_info)
        texteTemperature = findViewById(R.id.tv_temperature)
        texteTempMax = findViewById(R.id.tv_max_temp)
        texteTempMin = findViewById(R.id.tv_min_temp)

        texteUvi = findViewById(R.id.tv_uvi)
        texteHumidity = findViewById(R.id.tv_humidity)
        calendrier = findViewById(R.id.calendarView)
        iconeMeteo = findViewById(R.id.weather_icon)

        texteProductionEstimee = findViewById(R.id.tv_production_estimated)
        texteRatioPuissance = findViewById(R.id.tv_power_ratio)
        iconeProduction = findViewById(R.id.production_icon)

        clientLocalisation = LocationServices.getFusedLocationProviderClient(this)
        fileRequetes = Volley.newRequestQueue(this)

        val dateActuelle = obtenirDateActuelleFormattee()
        texteDateSelectionnee.text = dateActuelle

        obtenirLocalisationEtMeteo(dateActuelle)

        calendrier.setOnDateChangeListener { _, annee, mois, jour ->
            val dateSelectionnee = "$annee-${mois + 1}-$jour"
            texteDateSelectionnee.text = dateSelectionnee
            obtenirLocalisationEtMeteo(dateSelectionnee)
        }

        verifierPermissionLocalisation()
        initialiserNavigation()
        mettreAJourOngletSelectionne(R.id.nav_stats)
    }

    private fun obtenirDateActuelleFormattee(): String {
        val formatDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateActuelle = Date()
        return formatDate.format(dateActuelle)
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
        }
        navNotifications.setOnClickListener {
            mettreAJourOngletSelectionne(R.id.nav_notifications)
            val intent = Intent(this, Notifications::class.java)
            startActivity(intent)
            finish()
        }
        navReglages.setOnClickListener {
            mettreAJourOngletSelectionne(R.id.nav_reglages)
            val intent = Intent(this, Reglages::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun mettreAJourOngletSelectionne(idSelectionne: Int) {
        val icones = listOf(
            R.id.icon_accueil,
            R.id.icon_stats,
            R.id.icon_notifications,
            R.id.icon_reglages
        )
        val textes = listOf(
            R.id.text_accueil,
            R.id.text_stats,
            R.id.text_notifications,
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
            R.id.nav_reglages -> {
                findViewById<ImageView>(R.id.icon_reglages).setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary))
                findViewById<TextView>(R.id.text_reglages).setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            }
        }
    }

    private fun verifierPermissionLocalisation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), CODE_PERMISSION_LOCALISATION)
        } else {
            obtenirLocalisationEtMeteo(texteDateSelectionnee.text.toString())
        }
    }

    override fun onRequestPermissionsResult(codeRequete: Int, permissions: Array<out String>, resultats: IntArray) {
        super.onRequestPermissionsResult(codeRequete, permissions, resultats)
        if (codeRequete == CODE_PERMISSION_LOCALISATION) {
            if ((resultats.isNotEmpty() && resultats[0] == PackageManager.PERMISSION_GRANTED)) {
                obtenirLocalisationEtMeteo(texteDateSelectionnee.text.toString())
            } else {
                Toast.makeText(this, "Impossible d'obtenir la localisation", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun obtenirLocalisationEtMeteo(dateSelectionnee: String) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        clientLocalisation.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                recupererDonneesMeteo(location.latitude, location.longitude, dateSelectionnee)
            } else {
                Toast.makeText(this, "Impossible de récupérer la localisation", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun recupererDonneesMeteo(latitude: Double, longitude: Double, dateSelectionnee: String) {
        // Local 10.0.2.2:9200 ou Public https://gaiaval-8d365896b316.herokuapp.com/

        val url = "https://gaiaval-8d365896b316.herokuapp.com/api/prevision?lat=$latitude&lon=$longitude&date=$dateSelectionnee"

        val requeteJson = JsonObjectRequest(Request.Method.GET, url, null,
            { reponse ->
                mettreAJourUI(reponse)
            },
            { erreur ->
                val errorMessage = when {
                    erreur.networkResponse != null && erreur.networkResponse.statusCode == 500 -> {
                        "Erreur serveur, veuillez réessayer plus tard"
                    }
                    erreur.networkResponse != null -> {
                        "Erreur réseau: Code ${erreur.networkResponse.statusCode}"
                    }
                    else -> {
                        "Erreur: ${erreur.message}"
                    }
                }
                Toast.makeText(this, "Impossible de récupérer les données: $errorMessage", Toast.LENGTH_SHORT).show()
            }
        )

        fileRequetes.add(requeteJson)
    }
    private fun mettreAJourUI(reponse: JSONObject) {
        Log.d("WeatherData", reponse.toString())  // Ajoutez cette ligne pour déboguer la réponse JSON

        try {
            // Accéder directement aux valeurs de la réponse JSON
            val temperature = reponse.optDouble("temperature", Double.NaN)
            val description = reponse.optString("description", "Pas de description")
            val tempMin = reponse.optDouble("temperature_min", Double.NaN)
            val tempMax = reponse.optDouble("temperature_max", Double.NaN)
            val humidity = reponse.optInt("humidity", -1)
            val uvi = reponse.optDouble("uvi", Double.NaN)
            val sunriseTimestamp = reponse.optLong("sunrise", 0)
            val sunrise = if (sunriseTimestamp != 0L) SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(sunriseTimestamp * 1000)) else "N/A"
            val sunsetTimestamp = reponse.optLong("sunset", 0)
            val sunset = if (sunsetTimestamp != 0L) SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(sunsetTimestamp * 1000)) else "N/A"
            val moonriseTimestamp = reponse.optLong("moonrise", 0)
            val moonrise = if (moonriseTimestamp != 0L) SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(moonriseTimestamp * 1000)) else "N/A"
            val moonsetTimestamp = reponse.optLong("moonset", 0)
            val moonset = if (moonsetTimestamp != 0L) SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(moonsetTimestamp * 1000)) else "N/A"
            val moonPhase = reponse.optDouble("moon_phase", Double.NaN)
            val dewPoint = reponse.optDouble("dew_point", Double.NaN)
            val windSpeed = reponse.optDouble("wind_speed", Double.NaN)
            val windDeg = reponse.optInt("wind_deg", -1)
            val windGust = reponse.optDouble("wind_gust", Double.NaN)
            val clouds = reponse.optInt("clouds", -1)
            val pop = reponse.optDouble("pop", Double.NaN)
            texteTemperature.text = if (!temperature.isNaN()) "Température Moyenne : $temperature°C" else "Température Moyenne : N/A"
            texteTempMin.text = if (!tempMin.isNaN()) "Min : $tempMin°C" else "Min : N/A"
            texteTempMax.text = if (!tempMax.isNaN()) "Max : $tempMax°C" else "Max : N/A"
            texteHumidity.text = if (humidity != -1) "Humidité : $humidity%" else "Humidité : N/A"
            texteInfosMeteo.text = description
            texteUvi.text = if (!uvi.isNaN()) "Ensoleillement : $sunrise" else "Ensoleillement : N/A"

            if (reponse.has("production_estimee")) {
                val production = reponse.getDouble("production_estimee")
                val ratioPuissance = reponse.getDouble("ratio_puissance")

                texteProductionEstimee.text = "Production: $production kWh"
                texteRatioPuissance.text = "Ratio de puissance: $ratioPuissance %"
            }

        } catch (e: Exception) {
            Toast.makeText(this, "Impossible de récupérer les données JSON", Toast.LENGTH_SHORT).show()
            Log.e("WeatherError", "Erreur lors de la mise à jour de l'UI", e)
        }
    }

}
