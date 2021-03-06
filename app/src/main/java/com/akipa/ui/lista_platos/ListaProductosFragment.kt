package com.akipa.ui.lista_platos

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.akipa.R
import com.akipa.databinding.FragmentListaplatosBinding
import com.akipa.utils.Constantes

class ListaProductosFragment : Fragment() {

    private val viewModel: ListaProductosViewModel by viewModels()
    private lateinit var binding: FragmentListaplatosBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentListaplatosBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = ListaProductosAdapter(ProductoListener { plato ->
            findNavController()
                .navigate(
                    ListaProductosFragmentDirections.actionListaProductosFragmentToDetallePlatoFragment(
                        plato
                    )
                )
        })
        binding.productosLista.adapter = adapter

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.obtenerListadoPlatos()
            binding.swipeRefresh.isRefreshing = false
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // Para aparecer el menu hacía el fragment del inicio de sesión
        // Eventualmente lo usaremos para hacerlo desaparecer tmb
        // O almenos eso creo yo :thinking:
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        if (Constantes.personalAkipaLogueado != null) {
            menu.findItem(R.id.inicioSesionFragment).isVisible = false
            menu.findItem(R.id.perfilFragment).isVisible = true
        } else {
            menu.findItem(R.id.inicioSesionFragment).isVisible = true
            menu.findItem(R.id.perfilFragment).isVisible = false
        }
    }
}