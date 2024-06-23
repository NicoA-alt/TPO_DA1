package com.example.tpo_da1.ui.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tpo_da1.ui.data.CheapSharkApi
import com.example.tpo_da1.ui.domain.Deal
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DealsViewModel : ViewModel() {
    private val _deals = MutableLiveData<List<Deal>>()
    val deals: LiveData<List<Deal>> get() = _deals

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.cheapshark.com/api/1.0/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(CheapSharkApi::class.java)

    init {
        fetchDeals()
    }

    private fun fetchDeals() {
        _loading.value = true
        api.getDeals().enqueue(object : Callback<List<Deal>> {
            override fun onResponse(call: Call<List<Deal>>, response: Response<List<Deal>>) {
                if (response.isSuccessful) {
                    _deals.value = response.body()
                }
                _loading.value = false
            }

            override fun onFailure(call: Call<List<Deal>>, t: Throwable) {
                _loading.value = false
                // Manejar errores
            }
        })
    }


    /*
    private val _deals = MutableLiveData<List<Deal>>()
    val deals: LiveData<List<Deal>> get() = _deals

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    init {
        fetchDeals()
    }

    private fun fetchDeals() {
        _loading.value = true

        // Llama a tu API y actualiza _deals y _loading cuando la respuesta llegue
        // Simulación de llamada API con un retraso
        Thread {
            Thread.sleep(10000) // Simula un retraso en la carga
            _deals.postValue(mockDeals())
            _loading.postValue(false)
        }.start()
    }

    private fun mockDeals(): List<Deal> {
        // Devuelve una lista de objetos Deal simulados
        return listOf(
            Deal("BioShock Infinite", "29.99", "59.99", ""),
            Deal("Nombre juego", "0.00", "50.00", "")
        )
    }

     */
}