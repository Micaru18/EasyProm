package com.akipa.ui.carrito

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.akipa.database.plato_en_carrito.PlatoEnCarrito
import com.akipa.databinding.FragmentCarritoBinding

class CarritoFragment : Fragment(), OnClickItemsCarrito {

    private lateinit var binding: FragmentCarritoBinding
    private val viewModel: CarritoViewModel by viewModels(factoryProducer = {
        CarritoViewModel.CarritoViewModelFactory(application = requireActivity().application)
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCarritoBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        val adapter = CarritoAdapter(this)
        binding.listaCarrito.adapter = adapter

        return binding.root
    }

    override fun onAgregarClick(platoEnCarrito: PlatoEnCarrito) {
        viewModel.incrementarCantidadPlato(platoEnCarrito)
    }

    override fun onReducirClick(platoEnCarrito: PlatoEnCarrito) {
        viewModel.reducirCantidadPlato(platoEnCarrito)
    }

    override fun onEliminarClick(platoEnCarrito: PlatoEnCarrito) {
        viewModel.quitarPlato(platoEnCarrito)
    }

}