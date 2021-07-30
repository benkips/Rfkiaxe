package com.pradytech.rafikiconnect.fragments

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.pradytech.rafikiconnect.R
import com.pradytech.rafikiconnect.databinding.FragmentLauncherBinding
import com.pradytech.rafikiconnect.viewmodelz.Prefviewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Launcher : Fragment(R.layout.fragment_launcher) {
    private var _binding: FragmentLauncherBinding? = null
    private val binding get() = _binding!!
    private  val pviewmodel by viewModels<Prefviewmodel>()
    private val handler= Handler()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLauncherBinding.bind(view)
    }
    private val runnable= Runnable {
        pviewmodel.readFromDataStore.observe(viewLifecycleOwner, Observer {
            if(it.em!="none"){
                val x= bundleOf(
                    "web" to "https://rafiki.axecredits.com/android/account.php" ,
                )
                Navigation.findNavController(requireView()).navigate(R.id.action_launcher_to_wvinfo,x)
            }else{

                Navigation.findNavController(requireView()).navigate(R.id.action_launcher_to_switchboard)
            }
        })

    }
    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable,1000)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }
}