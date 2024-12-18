package com.example.gaiaprototypev2

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeActivity : AppCompatActivity() {

    private lateinit var graphiqueProduction: LineChart
    private lateinit var graphiqueConsommation: LineChart
    private lateinit var graphiqueProfit: LineChart
    private lateinit var requeteQueue: RequestQueue
    private var numClient: String? = null
    private lateinit var texteChargement: TextView
    private lateinit var conteneurGraphique: LinearLayout
    private lateinit var enTeteProduction: TextView
    private lateinit var enTeteConsommation: TextView
    private lateinit var enTeteProfit: TextView
    private lateinit var iconeMeteo: ImageView
    private lateinit var texteTemperature: TextView
    private lateinit var clientLocalisation: FusedLocationProviderClient
    private val handler = Handler(Looper.getMainLooper())
    private val intervalleMiseAJour = 60000L
    private lateinit var texteDCE: TextView
    private lateinit var texteEEC: TextView
    private lateinit var texteENS: TextView
    private lateinit var nomClient: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        graphiqueProduction = findViewById(R.id.chart_production)
        graphiqueConsommation = findViewById(R.id.chart_consumption)
        graphiqueProfit = findViewById(R.id.chart_profit)
        texteChargement = findViewById(R.id.loading_text)
        conteneurGraphique = findViewById(R.id.chart_container)
        enTeteProduction = findViewById(R.id.production_header)
        enTeteConsommation = findViewById(R.id.consumption_header)
        enTeteProfit = findViewById(R.id.profit_header)
        iconeMeteo = findViewById(R.id.weather_icon)
        texteTemperature = findViewById(R.id.temperature_text)
        nomClient = findViewById(R.id.nom)
        texteDCE = findViewById(R.id.text_dce)
        texteEEC = findViewById(R.id.text_eec)
        texteENS = findViewById(R.id.text_ens)
        requeteQueue = Volley.newRequestQueue(this)

        numClient = intent.getStringExtra("numClient")

        clientLocalisation = LocationServices.getFusedLocationProviderClient(this)

        if (numClient != null) {
            recupererDonnees(numClient!!)
            recupererMeteo()
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            demarrerMiseAJour()
        }

        initialiserNavigation()
        mettreAJourOngletSelectionne(R.id.nav_accueil)
    }

    override fun onResume() {
        super.onResume()
        numClient?.let {
            recupererDonnees(it)
            recupererMeteo()
        }
    }

    private fun initialiserNavigation() {
        val navAccueil = findViewById<LinearLayout>(R.id.nav_accueil)
        val navStats = findViewById<LinearLayout>(R.id.nav_stats)
        val navNotifications = findViewById<LinearLayout>(R.id.nav_notifications)
        val navReglages = findViewById<LinearLayout>(R.id.nav_reglages)

        navAccueil.setOnClickListener {
            mettreAJourOngletSelectionne(R.id.nav_accueil)
        }
        navStats.setOnClickListener {
            mettreAJourOngletSelectionne(R.id.nav_stats)
            val intent = Intent(this, Statistiquess::class.java)
            startActivity(intent)
        }
        navNotifications.setOnClickListener {
            mettreAJourOngletSelectionne(R.id.nav_notifications)
            val intent = Intent(this, Notifications::class.java)
            startActivity(intent)
        }
        navReglages.setOnClickListener {
            mettreAJourOngletSelectionne(R.id.nav_reglages)
            val intent = Intent(this, Reglages::class.java)
            startActivity(intent)
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

    private fun demarrerMiseAJour() {
        handler.post(object : Runnable {
            override fun run() {
                numClient?.let { recupererDonnees(it) }
                recupererMeteo()
                handler.postDelayed(this, intervalleMiseAJour)
            }
        })
    }

    private fun recupererMeteo() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        clientLocalisation.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val latitude = it.latitude
                val longitude = it.longitude
                // Local http://10.0.2.2:9200/ ou Public https://gaiaval-8d365896b316.herokuapp.com/
                // NGROK https://7b42-193-32-126-238.ngrok-free.app/ (changer)
                val url = "https://gaiaval-8d365896b316.herokuapp.com/api/meteo?lat=$latitude&lon=$longitude"

                val requeteJson = JsonObjectRequest(Request.Method.GET, url, null,
                    { reponse ->
                        val codeIcone = reponse.getString("icon_url").split("/").last().split("@").first()
                        val nomIcone = when (codeIcone) {
                            "01d" -> "sund"
                            "01n" -> "sunnight"
                            "02d" -> "fewcloudsday"
                            "02n" -> "fewcloudsnight"
                            "03d" -> "scatteredcloudsday"
                            "03n" -> "scatteredcloudsnight"
                            "04d" -> "brokencloudsday"
                            "04n" -> "brokencloudsnight"
                            "09d" -> "showerrainnday"
                            "09n" -> "showerrainnight"
                            "10d" -> "rainday"
                            "10n" -> "rainnight"
                            "11d" -> "thunderstormday"
                            "11n" -> "thunderstormnight"
                            "13d" -> "snowday"
                            "13n" -> "snownight"
                            "50d" -> "mistday"
                            "50n" -> "mistnight"
                            else -> "default_icon"
                        }
                        val temperature = reponse.getDouble("temperature")

                        texteTemperature.text = "${temperature}°C"
                        val resourceId = resources.getIdentifier(nomIcone, "drawable", packageName)
                        if (resourceId != 0) {
                            iconeMeteo.setImageResource(resourceId)
                        } else {
                            iconeMeteo.setImageResource(R.drawable.default_icon)
                        }
                    },
                    { erreur ->
                        erreur.printStackTrace()
                    }
                )

                Volley.newRequestQueue(this).add(requeteJson)
            }
        }
    }
    private fun recupererDonnees(numClient: String) {
        afficherChargement()
        // Local http://10.0.2.2:9200/ ou Public https://gaiaval-8d365896b316.herokuapp.com/
        // NGROK https://7b42-193-32-126-238.ngrok-free.app/ (changer sur RunServer+Ngrok)
        val url = "https://gaiaval-8d365896b316.herokuapp.com/api/donnees?numClient=$numClient"

        val requeteJson = JsonObjectRequest(Request.Method.GET, url, null,
            { reponse ->
                val tableauDonnees = reponse.getJSONArray("data")

                val entreesDCE = ArrayList<Entry>()
                val entreesTCEAM = ArrayList<Entry>()
                val entreesTCEAMB = ArrayList<Entry>()
                val entreesTCEAV = ArrayList<Entry>()
                val entreesENS = ArrayList<Entry>()
                val entreesEEC = ArrayList<Entry>()
                val valeursX = ArrayList<Long>()

                val formatDate = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US)

                var valeurRecenteDCE = 0f
                var valeurRecenteEEC = 0f
                var valeurRecenteENS = 0f
                var dateRecente: Long = 0

                var nom = ""
                var prenom = ""

                for (i in 0 until tableauDonnees.length()) {
                    val objetDonnees = tableauDonnees.getJSONObject(i)
                    val dateStr = objetDonnees.getString("Timestamp")
                    val date = formatDate.parse(dateStr)
                    val index = i.toFloat()
                    date?.let { valeursX.add(it.time) }
                    val dce = objetDonnees.getDouble("DCE").toFloat()
                    val tceam = objetDonnees.getDouble("TCEAM").toFloat()
                    val tceamb = objetDonnees.getDouble("TCEAMB").toFloat()
                    val tceav = objetDonnees.getDouble("TCEAV").toFloat()
                    val ens = objetDonnees.getDouble("ENS").toFloat()
                    val eec = objetDonnees.getDouble("EEC").toFloat()

                    nom = objetDonnees.getString("Nom")
                    prenom = objetDonnees.getString("Prenom")

                    entreesDCE.add(Entry(index, dce))
                    entreesTCEAM.add(Entry(index, tceam))
                    entreesTCEAMB.add(Entry(index, tceamb))
                    entreesTCEAV.add(Entry(index, tceav))
                    entreesENS.add(Entry(index, ens))
                    entreesEEC.add(Entry(index, eec))

                    if (date != null && date.time > dateRecente) {
                        dateRecente = date.time
                        valeurRecenteDCE = dce
                        valeurRecenteEEC = eec
                        valeurRecenteENS = ens
                    }
                }
                valeursX.sort()

                val dceDataSet = LineDataSet(entreesDCE, "Débit Circuit Eau")
                val tceamDataSet = LineDataSet(entreesTCEAM, "Température en Amont")
                val tceambDataSet = LineDataSet(entreesTCEAMB, "Température en Amont B")
                val tceavDataSet = LineDataSet(entreesTCEAV, "Température en Aval")
                val ensDataSet = LineDataSet(entreesENS, "Ensoleillement")
                val eecDataSet = LineDataSet(entreesEEC, "Efficacité Eau Chaude")

                texteDCE.text = "${valeurRecenteDCE} L/min"
                texteEEC.text = "${valeurRecenteEEC} %"
                texteENS.text = "${valeurRecenteENS} kWh"
                nomClient.text = "$nom $prenom"
                configurerGraphique(graphiqueProduction, valeursX, getCouleurVert(), dceDataSet)
                configurerGraphique(graphiqueConsommation, valeursX, getCouleurRouge(), tceamDataSet, tceambDataSet, tceavDataSet)
                configurerGraphique(graphiqueProfit, valeursX, ColorTemplate.getHoloBlue(), ensDataSet, eecDataSet)

                afficherGraphiques()
            },
            { erreur ->
                erreur.printStackTrace()
                afficherGraphiques() // Masquer chargement même en cas d'erreur
            }
        )

        requeteQueue.add(requeteJson)
    }

    private fun getCouleurVert(): Int {
        return Color.rgb(61, 168, 92)
    }

    private fun getCouleurRouge(): Int {
        return Color.rgb(229, 57, 53)
    }

    private fun configurerGraphique(graphique: LineChart, valeursX: ArrayList<Long>, couleur: Int, vararg ensemblesDeDonnees: LineDataSet) {
        ensemblesDeDonnees.forEach { ensembleDeDonnees ->
            ensembleDeDonnees.color = couleur
            ensembleDeDonnees.setCircleColor(couleur)
            ensembleDeDonnees.lineWidth = 1f
            ensembleDeDonnees.circleRadius = 4f
            ensembleDeDonnees.setDrawCircleHole(false)
            ensembleDeDonnees.valueTextSize = 10f
            ensembleDeDonnees.setDrawFilled(true)
            ensembleDeDonnees.fillColor = couleur
            ensembleDeDonnees.valueTextColor = Color.WHITE
        }

        val donnees = LineData(*ensemblesDeDonnees)
        graphique.data = donnees

        graphique.xAxis.valueFormatter = object : ValueFormatter() {
            private val formatDatePlein = SimpleDateFormat("YYYY-MM-dd", Locale.FRANCE)
            private val formatDateCompact = SimpleDateFormat("HH:mm", Locale.FRANCE)

            override fun getFormattedValue(value: Float): String {
                val index = value.toInt()
                return if (index >= 0 && index < valeursX.size) {
                    val tempsActuel = System.currentTimeMillis()
                    val date = Date(valeursX[index])
                    if (tempsActuel - date.time < 24 * 60 * 60 * 1000) {
                        formatDateCompact.format(date)
                    } else {
                        formatDatePlein.format(date)
                    }
                } else {
                    ""
                }
            }
        }
        graphique.xAxis.labelRotationAngle = -25f
        graphique.xAxis.textSize = 10f
        graphique.setBackgroundColor(Color.DKGRAY)
        graphique.setGridBackgroundColor(Color.BLACK)
        graphique.setDrawGridBackground(true)
        graphique.setDrawBorders(true)

        val axeX = graphique.xAxis
        axeX.position = XAxis.XAxisPosition.BOTTOM
        axeX.textColor = Color.WHITE
        axeX.setDrawGridLines(false)

        val axeGauche = graphique.axisLeft
        axeGauche.textColor = Color.WHITE
        axeGauche.setDrawGridLines(true)

        val axeDroite = graphique.axisRight
        axeDroite.isEnabled = false

        graphique.description.isEnabled = false
        graphique.setTouchEnabled(true)
        graphique.isDragEnabled = true
        graphique.setScaleEnabled(true)
        graphique.setPinchZoom(true)
        graphique.legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        graphique.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        graphique.legend.textColor = Color.WHITE
        graphique.invalidate()
    }

    private fun afficherChargement() {
        texteChargement.visibility = View.VISIBLE
        conteneurGraphique.visibility = View.GONE
    }

    private fun afficherGraphiques() {
        texteChargement.visibility = View.GONE
        conteneurGraphique.visibility = View.VISIBLE
    }
}
