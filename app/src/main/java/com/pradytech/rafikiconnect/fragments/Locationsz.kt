package com.pradytech.rafikiconnect.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.pradytech.rafikiconnect.R
import com.pradytech.rafikiconnect.databinding.FragmentLocationszBinding
import com.pradytech.rafikiconnect.networks.Resource
import com.pradytech.rafikiconnect.utils.handleApiError
import com.pradytech.rafikiconnect.utils.safeInt
import com.pradytech.rafikiconnect.utils.showmessages
import com.pradytech.rafikiconnect.utils.visible
import com.pradytech.rafikiconnect.viewmodelz.Loginviewmodel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class Locationsz : Fragment(R.layout.fragment_locationsz) {
    private var _binding: FragmentLocationszBinding?= null
    private val binding get() = _binding!!
    private lateinit  var sub: ArrayList<String>
    private lateinit  var sadapter: ArrayAdapter<String>

    private lateinit  var email: String
    private lateinit  var phone: String
    private lateinit  var idno: String
    private lateinit  var occupation: String
    private lateinit  var category: String
    private lateinit  var type: String

    private lateinit  var county: String
    private lateinit  var scounty: String

    private lateinit  var switchb: String

    private  val viewmodel by viewModels<Loginviewmodel>()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding =  FragmentLocationszBinding.bind(view)
        sub = ArrayList()
        binding.pgbar.visible(false)

        binding.lottieAnimationView.imageAssetsFolder="assets"
        binding.lottieAnimationView.setAnimation("location.json")

       switchb= arguments?.getString("type").toString()
       email= arguments?.getString("email").toString()
       phone= arguments?.getString("phone").toString()
       idno= arguments?.getString("idnumber").toString()



        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.countyarray, android.R.layout.simple_spinner_item
        )

        viewmodel.notiResponse.observe(viewLifecycleOwner,{
            binding.pgbar.visible(it is Resource.Loading)
            when (it) {
                is Resource.Success -> {
                    if(it.value[0].response.contains("success")){
                        val s:String=it.value[0].response
                        requireView().showmessages("Success","Registration was $s",
                            { movetologin() })
                    }else{
                        requireView().showmessages("Error",it.value[0].response)
                    }
                }
                is Resource.Failure -> handleApiError(it) {
                }
            }
        })



        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        binding.countySpinner.setAdapter(adapter)


        binding.countySpinner.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val citem = parent?.getItemAtPosition(position)
                county = citem as String
                splitcounty(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        binding.subcountySpinner.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val sbitem = parent?.getItemAtPosition(position)
                scounty = sbitem as String
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        binding.savebtn.setOnClickListener {

            if(switchb.equals("lenders")){
                category= arguments?.getString("category").toString()
                viewmodel.lreg(email, requireContext().safeInt(phone)!!, requireContext().safeInt(idno)!!, category,county,scounty,switchb)
            }else{
                occupation= arguments?.getString("occupation").toString()
                viewmodel.breg(email, requireContext().safeInt(phone)!!, requireContext().safeInt(idno)!!, occupation,county,scounty,switchb)
            }
        }


    }

    private fun movetologin() {
        val x= bundleOf(
            "type" to switchb ,
        )
        Navigation.findNavController(requireView()).navigate(R.id.action_locationsz_to_login,x)
    }

    private fun splitcounty(p: Int) {
        sub.clear()
        val value = resources.getStringArray(R.array.subcounty)[p]
        val array = value.split("\\,".toRegex()).toTypedArray()
        val length = array.size
        for (i in 0 until length) {
            sub.add(array[i])
        }
        sadapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sub)
        sadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.subcountySpinner.setAdapter(sadapter)
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}