package com.tunahanozatac.cryptojetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tunahanozatac.cryptojetpackcompose.model.CryptoModel
import com.tunahanozatac.cryptojetpackcompose.sevice.CryptoAPI
import com.tunahanozatac.cryptojetpackcompose.ui.theme.CryptoJetpackComposeTheme
import com.tunahanozatac.cryptojetpackcompose.util.Constant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptoJetpackComposeTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val cryptoModels = remember { mutableStateListOf<CryptoModel>() }

    val retrofit = Retrofit.Builder()
        .baseUrl(Constant.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CryptoAPI::class.java)
    val call = retrofit.getData()
    call.enqueue(object : Callback<List<CryptoModel>> {
        override fun onResponse(
            call: Call<List<CryptoModel>>,
            response: Response<List<CryptoModel>>
        ) {
            if (response.isSuccessful) {
                response.body()?.let {
                    cryptoModels.addAll(it)
                }
            }
        }

        override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {
            t.printStackTrace()
        }
    })

    Scaffold(
        topBar = { AppBar() }) {
        CryptoList(cryptoList = cryptoModels)
    }
}

@Composable
fun CryptoList(cryptoList: List<CryptoModel>) {
    LazyColumn(contentPadding = PaddingValues(15.dp)) {
        items(cryptoList) {
            CryptoRow(it)
        }
    }
}

@Composable
fun CryptoRow(cryptoModel: CryptoModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Gray)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = cryptoModel.currency,
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(start = 20.dp)
        )
        Text(
            text = ":",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(start = 20.dp)
        )
        Text(
            text = cryptoModel.price,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(start = 20.dp)
        )
    }
}

@Composable
fun AppBar() {
    TopAppBar(contentPadding = PaddingValues(10.dp)) {
        Text(text = "Retrofit Crypto App")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CryptoJetpackComposeTheme {
        CryptoRow(cryptoModel = CryptoModel("BTC", "1231231"))
    }
}