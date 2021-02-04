package com.akipa.ui.lista_platos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.akipa.entidades.Plato
import com.akipa.network.AkipaAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Nos indicará si la info de los platos se encuentra
 * en alguno de los estados establecidos.
 * CARGANDO -> aún se está obteniendo
 * ERROR -> no se pudo conseguir la info
 * LISTO -> información conseguida y lista para uso
 */
enum class EstadoListadoPlatos { CARGANDO, ERROR, LISTO }

class ListaProductosViewModel : ViewModel() {

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _listadoPlatos = MutableLiveData<List<Plato>>()
    val listadoPlatos: LiveData<List<Plato>>
        get() = _listadoPlatos

    private val _estadoListaPlatos = MutableLiveData<EstadoListadoPlatos>()
    val estadoListaPlatos: LiveData<EstadoListadoPlatos>
        get() = _estadoListaPlatos

    init {
        obtenerListadoPlatos()
    }

    private fun obtenerListadoPlatos() {

        coroutineScope.launch {
            val listadoDeferred = AkipaAPI.retrofitService.obtenerListadoPlatosAsync()
            try {
                _estadoListaPlatos.value = EstadoListadoPlatos.CARGANDO
                val listadoPlatos = listadoDeferred.await()
                _estadoListaPlatos.value = EstadoListadoPlatos.LISTO
                if (listadoPlatos.platos.isNotEmpty()) {
                    _listadoPlatos.value = listadoPlatos.platos
                }
            } catch (e: Exception) {
                _estadoListaPlatos.value = EstadoListadoPlatos.ERROR
                _listadoPlatos.value = ArrayList()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}