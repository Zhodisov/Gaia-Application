package com.example.gaiaprototypev2

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var carteNomUtilisateur: CardView
    private lateinit var carteNumClient: CardView
    private lateinit var boutonAuth: Button
    private lateinit var lienRecuperation: TextView
    private lateinit var champNomUtilisateur: TextInputEditText
    private lateinit var champNumClient: TextInputEditText
    private lateinit var fileRequetes: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        carteNomUtilisateur = findViewById(R.id.username_card)
        carteNumClient = findViewById(R.id.client_number_card)
        boutonAuth = findViewById(R.id.auth_button)
        lienRecuperation = findViewById(R.id.recovery_link)
        champNomUtilisateur = findViewById(R.id.username_edit_text)
        champNumClient = findViewById(R.id.client_number_edit_text)

        fileRequetes = Volley.newRequestQueue(this)
        boutonAuth.setOnClickListener {
            authentifier()
        }
        lienRecuperation.setOnClickListener {
            recupererCompte()
        }

        verifierConnexionLocale()
    }

    private fun verifierConnexionLocale() {
        val sharedPref = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
        val nomUtilisateur = sharedPref.getString("username", null)
        val numClient = sharedPref.getString("clientNumber", null)

        if (nomUtilisateur != null && numClient != null) {
            carteNomUtilisateur.visibility = View.GONE
            carteNumClient.visibility = View.GONE
            boutonAuth.visibility = View.GONE
            lienRecuperation.visibility = View.GONE
            Toast.makeText(this, "Chargement...", Toast.LENGTH_SHORT).show()
            authentifier(nomUtilisateur, numClient)
        }
    }

    private fun authentifier() {
        val nomUtilisateur = champNomUtilisateur.text.toString()
        val numClient = champNumClient.text.toString()

        if (nomUtilisateur.isEmpty() || numClient.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            return
        }

        authentifier(nomUtilisateur, numClient)
    }

    private fun authentifier(nomUtilisateur: String, numClient: String) {
        val json = JSONObject().apply {
            put("Nom", nomUtilisateur)
            put("numClient", numClient)
        }
        // Local http://10.0.2.2:9200/ ou Public https://gaiaval-8d365896b316.herokuapp.com/
        // NGROK https://7b42-193-32-126-238.ngrok-free.app/ (changer)
        val url = "https://gaiaval-8d365896b316.herokuapp.com/login"
        boutonAuth.isEnabled = false
        boutonAuth.setBackgroundColor(ContextCompat.getColor(this, R.color.gray))

        val requeteJson = JsonObjectRequest(Request.Method.POST, url, json,
            { response ->
                val status = response.getBoolean("status")
                if (status) {
                    Toast.makeText(this, "Authentification réussie", Toast.LENGTH_SHORT).show()
                    val sharedPref = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putString("username", nomUtilisateur)
                        putString("clientNumber", numClient)
                        apply()
                    }
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra("numClient", numClient)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Identifiants invalides", Toast.LENGTH_SHORT).show()
                    carteNomUtilisateur.visibility = View.VISIBLE
                    carteNumClient.visibility = View.VISIBLE
                    boutonAuth.visibility = View.VISIBLE
                    lienRecuperation.visibility = View.VISIBLE
                }
                boutonAuth.isEnabled = true
                boutonAuth.setBackgroundColor(ContextCompat.getColor(this, R.color.login_sh))
            },
            { error ->
                Log.e("MainActivity", "Erreur reçue: ${error.message}")
                Toast.makeText(this, "Échec de connexion au serveur", Toast.LENGTH_SHORT).show()
                boutonAuth.isEnabled = true
                boutonAuth.setBackgroundColor(ContextCompat.getColor(this, R.color.login_sh))
                carteNomUtilisateur.visibility = View.VISIBLE
                carteNumClient.visibility = View.VISIBLE
                boutonAuth.visibility = View.VISIBLE
                lienRecuperation.visibility = View.VISIBLE
            }
        )
        fileRequetes.add(requeteJson)
    }

    private fun recupererCompte() {
        Toast.makeText(this, "Récupération de compte cliquée", Toast.LENGTH_SHORT).show()
    }
}
