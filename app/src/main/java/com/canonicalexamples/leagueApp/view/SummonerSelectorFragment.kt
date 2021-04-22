package com.canonicalexamples.leagueApp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.canonicalexamples.leagueApp.app.LeagueAppApp
import com.canonicalexamples.leagueApp.databinding.ActivityServerPanelBinding
import com.canonicalexamples.leagueApp.databinding.FragmentSummonerSelectorBinding
import com.canonicalexamples.leagueApp.viewmodels.SummonerFinderViewModel
import com.canonicalexamples.leagueApp.viewmodels.SummonerFinderViewModelFactory

class SummonerSelectorFragment : Fragment() {

    private lateinit var binding: FragmentSummonerSelectorBinding

    private val viewModel: SummonerFinderViewModel by viewModels{
        val app = activity?.application as LeagueAppApp
        SummonerFinderViewModelFactory(app.database)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSummonerSelectorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewSummoners.adapter = SummonerListAdapter(viewModel, this.context)

        /*viewModel.navigate.observe(viewLifecycleOwner) { navigate ->
            if (navigate) {
                val returnIntent: Intent = Intent()
                returnIntent.putExtra("host_name", viewModel.serverSelected.value)

                activity?.setResult(Activity.RESULT_OK, returnIntent)
                activity?.finish()
            }
        }*/
    }

}
