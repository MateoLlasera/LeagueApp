package com.canonicalexamples.leagueApp.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.canonicalexamples.leagueApp.R
import com.canonicalexamples.leagueApp.databinding.ActivityServerPanelBinding
import com.canonicalexamples.leagueApp.databinding.FragmentServerSelectionBinding
import com.canonicalexamples.leagueApp.viewmodels.ServerListViewModel

class ServerSelectionFragment : Fragment() {

    private lateinit var binding: FragmentServerSelectionBinding
    private val viewModel: ServerListViewModel by viewModels()

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
        /*binding.recyclerViewServers.setOnClickListener {
            viewModel.serverButtonClicked()
        }*/

        viewModel.navigate.observe(viewLifecycleOwner) { navigate ->
            if (navigate) {
                findNavController().navigate(R.id.action_serverSelectionFragment_to_leagueActivity)
            }
        }
        /*view.findViewById<Button>(R.id.button_first).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }*/
    }
}
