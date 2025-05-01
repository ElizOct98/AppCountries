package com.sperez.appcountries

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.sperez.appcountries.intents.CountriesIntent
import com.sperez.appcountries.intents.CountriesSate
import com.sperez.appcountries.repository.DataBaseCountries
import com.sperez.appcountries.ui.theme.AppCountriesTheme
import com.sperez.appcountries.viewModel.CountriesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalGlideComposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: CountriesViewModel = viewModels<CountriesViewModel>().value

        val job =
        lifecycleScope.launch(Dispatchers.IO) {
            val dataBaseCountries = Room.databaseBuilder(
                context = baseContext,
                klass = DataBaseCountries::class.java,
                name = "countries"
            ).build()

            viewModel.initDatabase(dataBaseCountries)
        }
        job.invokeOnCompletion { viewModel.dispatchEvent(CountriesIntent.GetCountries) }

        enableEdgeToEdge()
        setContent {
            val state = remember { viewModel.currentState }
            AppCountriesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    when(val current = state.value){
                        is CountriesSate.Loading -> {
                            Box( modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                                CircularProgressIndicator(
                                    modifier = Modifier.width(64.dp).align(Alignment.Center),
                                    color = MaterialTheme.colorScheme.secondary,
                                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                                )
                            }
                        }
                        is CountriesSate.Display -> {
                            LazyColumn(
                                modifier = Modifier.padding(innerPadding)
                            ) {
                                items(current.list, itemContent = {
                                    Row(modifier = Modifier.padding(start = 15.dp, top = 30.dp)) {
                                        GlideImage(
                                            model = it.image,
                                            contentDescription = it.name
                                        )
                                        Column(modifier = Modifier.padding(start = 15.dp)) {
                                            Text(it.name, fontSize = 20.sp)
                                            Text(it.capital)
                                        }
                                    }
                                })
                            }
                        }
                        is CountriesSate.Error -> {
                            Box(Modifier.padding(innerPadding).fillMaxSize()) {
                                Column(modifier = Modifier.align(Alignment.Center),
                                    horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("There was an error, please retry.")
                                    IconButton(onClick = { viewModel.dispatchEvent(CountriesIntent.GetCountries) }) {
                                        Icon(Icons.Default.Refresh, "refresh icon")
                                    }
                                }
                            }
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}