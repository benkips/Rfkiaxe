package com.pradytech.rafikiconnect.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.pradytech.rafikiconnect.R
import com.pradytech.rafikiconnect.databinding.FragmentForgtpassBinding
import com.pradytech.rafikiconnect.networks.Resource
import com.pradytech.rafikiconnect.utils.handleApiError
import com.pradytech.rafikiconnect.utils.isValidEmail
import com.pradytech.rafikiconnect.utils.showmessages
import com.pradytech.rafikiconnect.utils.visible
import com.pradytech.rafikiconnect.viewmodelz.Loginviewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class forgtpass : Fragment(R.layout.fragment_forgtpass) {
    private var _binding: FragmentForgtpassBinding?= null
    private val binding get() = _binding!!
    private  lateinit var switchb:String

    private lateinit var email:String
    private  val viewmodel by viewModels<Loginviewmodel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding =  FragmentForgtpassBinding.bind(view)
        binding.pgbar.visible(false)
        binding.lottieAnimationView.imageAssetsFolder="assets"
        binding.lottieAnimationView.setAnimation("forgotpass.json")

        switchb= arguments?.getString("type")!!

        binding.resetbtn.setOnClickListener {
            email = binding.email.editText!!.text.toString()
            if(email.isEmpty()){
                binding.email.setError("please enter a valid email")
            } else if (!isValidEmail(email)) {
                binding.email.setError("please  enter a valid email")
                binding.email.editText!!.requestFocus()
            } else {
                if (switchb != null) {
                    viewmodel.fgtpas(email,switchb)
                }
            }

        }

        viewmodel.notiResponse.observe(viewLifecycleOwner, Observer {
            binding.pgbar.visible(it is Resource.Loading)
            when (it) {
                is Resource.Success -> {
                    if(it.value[0].response.contains("success")){
                        val s:String=it.value[0].response
                        requireView().showmessages("Success","check your email we sent " +
                                "the password",
                            { null })
                    }else{
                        requireView().showmessages("Error",it.value[0].response)
                    }
                }
                is Resource.Failure -> handleApiError(it) {
                }
            }
        })
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}