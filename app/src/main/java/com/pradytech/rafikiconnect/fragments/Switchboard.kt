package com.pradytech.rafikiconnect.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.pradytech.rafikiconnect.R
import com.pradytech.rafikiconnect.databinding.FragmentSwitchboardBinding
import com.pradytech.rafikiconnect.viewmodelz.Prefviewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Switchboard : Fragment(R.layout.fragment_switchboard) {
    private var _binding: FragmentSwitchboardBinding?= null
    private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        _binding = FragmentSwitchboardBinding .bind(view)

        binding.lottieAnimationView.imageAssetsFolder="assets"
        binding.lottieAnimationView.setAnimation("finance.json")

        binding.lo.setOnClickListener {
            val x= bundleOf(
                "type" to "lenders" ,
            )
            Navigation.findNavController(requireView()).navigate(R.id.action_switchboard_to_login,x)
        }
        binding.bo.setOnClickListener {
            val x= bundleOf(
                "type" to "borrowers" ,
            )
            Navigation.findNavController(requireView()).navigate(R.id.action_switchboard_to_login,x)
        }



    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}