package com.sperez.appcountries

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.sperez.appcountries.intents.CountriesSate
import com.sperez.appcountries.ui.theme.AppCountriesTheme
import com.sperez.appcountries.viewModel.CountriesViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalGlideComposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: CountriesViewModel = viewModels<CountriesViewModel>().value
            //val listCountries = remember { viewModel.countriesList}
            val state = remember { viewModel.currentState }
            AppCountriesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    when(val current = state.value){
                        is CountriesSate.Loading -> {

                        }

                        is CountriesSate.Display -> {
                            LazyColumn(
                                modifier = Modifier.padding(innerPadding)
                            ) {
                                items(current.list, itemContent = {
                                    Row(modifier = Modifier.padding(start = 15.dp, top = 30.dp)) {
                                        GlideImage(
                                            model = it.image.png,
                                            contentDescription = it.name.common
                                        )
                                        Column(modifier = Modifier.padding(start = 15.dp)) {
                                            Text(it.name.common, fontSize = 20.sp)
                                            Text(it.capital.first())
                                        }
                                    }
                                })
                            }
                        }
                        is CountriesSate.Error -> {}
                        else -> {}
                    }

                }
            }
        }
    }
}
