package com.pradytech.rafikiconnect.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.pradytech.rafikiconnect.R
import com.pradytech.rafikiconnect.databinding.FragmentLenderRegistrationBinding
import com.pradytech.rafikiconnect.utils.isValidEmail
import com.pradytech.rafikiconnect.utils.isValidMobile
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class LenderRegistration : Fragment(R.layout.fragment_lender_registration) {
    private lateinit var ladapter: ArrayAdapter<String>
    private var _binding: FragmentLenderRegistrationBinding?= null
    private val binding get() = _binding!!
    private lateinit var cat: ArrayList<String>

    private lateinit var email: String
    private lateinit var phone: String
    private lateinit var idno: String
    private lateinit var category: String
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding =  FragmentLenderRegistrationBinding.bind(view)
        cat=ArrayList()
        cat.add("Individual")
        cat.add("Company")

        ladapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, cat)

        ladapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.lendingSpinner.adapter=ladapter

        binding.lendingSpinner.onItemSelectedListener=object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                val litem = parent?.getItemAtPosition(position)
                category = litem as String
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // write code to perform some action
            }
        }




        binding.nxtlenbtn.setOnClickListener {

            email = binding.lemail.editText!!.text.toString()
            phone = binding.lphone.editText!!.text.toString()
            idno= binding.lidno.editText!!.text.toString()

            lborrower(email, phone, idno, category)

        }

    }
    private fun lborrower(e: String, p: String, i: String, c: String) {
        if (e.isEmpty()) {
            binding.lemail.setError("email cannot be  empty")
            binding.lemail.editText!!.requestFocus()
        } else if (p.isEmpty()) {
            binding.lphone.setError("phone cannot be  empty")
            binding.lphone.editText!!.requestFocus()
        } else if (i.isEmpty()) {
            binding.lidno.setError("idnumber cannot be  empty")
            binding.lidno.editText!!.requestFocus()
        } else {
            if (!isValidMobile(p) || p.length != 10 || !p.startsWith("07")) {
                binding.lphone.setError("phone number is invalid")
                binding.lphone.editText!!.requestFocus()
            } else if (!isValidEmail(e)) {
                binding.lemail.setError("please  enter a valid email")
                binding.lemail.editText!!.requestFocus()
            }else if (i.toInt()<6) {
                binding.lidno.setError("invalid id number ")
                binding.lidno.editText!!.requestFocus()
            }  else {
                val x= bundleOf(
                    "email" to e,
                    "phone" to p,
                    "idnumber" to i,
                    "category" to c,
                    "type" to "lenders"
                )
                Navigation.findNavController(requireView()).navigate(R.id.action_lenderRegistration_to_locationsz,x)

            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}