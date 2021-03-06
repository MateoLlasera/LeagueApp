package com.canonicalexamples.leagueApp.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.canonicalexamples.leagueApp.app.LeagueAppApp
import com.canonicalexamples.leagueApp.databinding.FragmentServerSelectionBinding
import com.canonicalexamples.leagueApp.viewmodels.ServerListViewModel
import com.canonicalexamples.leagueApp.viewmodels.ServerListViewModelFactory

class ServerSelectionFragment : Fragment() {

    private lateinit var binding: FragmentServerSelectionBinding
    private val viewModel: ServerListViewModel by viewModels{
        val app = activity?.application as LeagueAppApp
        ServerListViewModelFactory(app.database)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentServerSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //viewModel = ViewModelProvider(this).get(ServerListViewModel::class.java)

        binding.recyclerViewServers.adapter = ServerListAdapter(viewModel)

        viewModel.navigate.observe(viewLifecycleOwner) { navigate ->
            if (navigate) {
                val returnIntent: Intent = Intent()
                returnIntent.putExtra("host_name", viewModel.serverSelected.value)

                activity?.setResult(Activity.RESULT_OK, returnIntent)
                activity?.finish()
            }
        }
    }
}
