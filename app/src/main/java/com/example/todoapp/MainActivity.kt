package com.example.todoapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isGone
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MainActivity : AppCompatActivity() {
    companion object{
        const val LOGIN = "loginOrSignUp"
        const val DARK_MODE = "dark_mode"
    }
    private var bandera = true
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()

        /*CoroutineScope(Dispatchers.IO).launch {
            getSettings().filter { bandera }.collect{ preferences ->
                runOnUiThread {
                    //changeModeView(preferences.mode)
                    changeRegister(preferences.login)
                }

                bandera = false
            }

        }*/
        //isDarkMode = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES



    }

    private fun initUI() {

        binding.tvSignUp.setOnClickListener{
            bandera = true
            CoroutineScope(Dispatchers.IO).launch{
                saveOptions(LOGIN, false)

            }
        }
        binding.tvLogin.setOnClickListener{
            bandera = true
            CoroutineScope(Dispatchers.IO).launch{
                saveOptions(LOGIN, true)

            }
        }
        /*binding.imgMode.setOnClickListener {
            bandera = true
            CoroutineScope(Dispatchers.IO).launch{
                val mode: Boolean = verifyImage(binding.imgMode, R.drawable.ic_dark_mode)
                saveOptions(DARK_MODE, mode)

            }
        }*/
        binding.btnIngresarLogin.setOnClickListener {
            intent = Intent(this,TodoListActivity::class.java)
            startActivity(intent)
        }
        binding.btnRegistrarse.setOnClickListener {
            intent = Intent(this,TodoListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun verifyImage(imageView: ImageView, drawableId: Int): Boolean {
        // Obtener el Drawable del ImageView
        val imageViewDrawable = imageView.drawable

        // Obtener el Drawable de la imagen en la carpeta drawable
        val contextDrawable = AppCompatResources.getDrawable(imageView.context, drawableId)

        // Comparar ambos Drawables
        return imageViewDrawable?.constantState == contextDrawable?.constantState


    }

    private suspend fun saveOptions(key: String, value: Boolean){
        dataStore.edit { prefereces ->
            prefereces[booleanPreferencesKey(key)] = value

        }
    }
    /*private fun changeModeView(isDarkMode: Boolean) {
        if (isDarkMode) {
            binding.imgMode.setImageResource(R.drawable.ic_light_mode)
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
            delegate.applyDayNight()
        } else {
            binding.imgMode.setImageResource(R.drawable.ic_dark_mode)
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
            delegate.applyDayNight()
        }
    }*/


    private fun changeRegister(band: Boolean) {
        if (band){
            binding.tvContentLogin.isGone = false
            binding.tvContentSingUp.isGone = true
        }else{
            binding.tvContentLogin.isGone = true
            binding.tvContentSingUp.isGone = false
        }
    }

    private fun getSettings(): Flow<Configures> {
        return  dataStore.data.map { preferences ->
            Configures(
                login = preferences[booleanPreferencesKey(LOGIN)] ?: false,
                mode = preferences[booleanPreferencesKey(DARK_MODE)] ?: false,
            )
        }
    }


}