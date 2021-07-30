package com.pradytech.rafikiconnect.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.pradytech.rafikiconnect.R
import com.pradytech.rafikiconnect.databinding.FragmentRegistrationBinding
import com.pradytech.rafikiconnect.utils.isValidEmail
import com.pradytech.rafikiconnect.utils.isValidMobile
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Registration : Fragment(R.layout.fragment_registration) {
    private var _binding: FragmentRegistrationBinding?= null
    private val binding get() = _binding!!

    private lateinit var email:String
    private  lateinit var phone:String
    private  lateinit var idnumber:String
    private  lateinit var occupation:String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding =  FragmentRegistrationBinding.bind(view)


        binding.nxtlbtn.setOnClickListener {
            email = binding.bemail.editText!!.text.toString()
            phone = binding.bphone.editText!!.text.toString()
            idnumber = binding.bidno.editText!!.text.toString()
            occupation = binding.bocupation.editText!!.text.toString()

            bborrower(email, phone, idnumber, occupation)
        }

    }
    private fun bborrower(e: String, p: String, i: String, o: String) {
        if (e.isEmpty()) {
            binding.bemail.setError("email cannot be  empty")
            binding.bemail.editText!!.requestFocus()
        } else if (p.isEmpty()) {
            binding.bphone.setError("phone cannot be  empty")
            binding.bphone.editText!!.requestFocus()
        } else if (i.isEmpty()) {
            binding.bidno.setError("idnumber cannot be  empty")
            binding.bidno.editText!!.requestFocus()
        }else if (o.isEmpty()) {
            binding.bocupation.setError("Occupation cannot be  empty")
            binding.bocupation.editText!!.requestFocus()
        } else {
            if (!isValidMobile(p) || p.length != 10 || !p.startsWith("07")) {
                binding.bphone.setError("phone number is invalid")
                binding.bphone.editText!!.requestFocus()
            } else if (!isValidEmail(e)) {
                binding.bemail.setError("please  enter a valid email")
                binding.bemail.editText!!.requestFocus()
            }else if (i.toInt()<6) {
                binding.bidno.setError("invalid id number ")
                binding.bidno.editText!!.requestFocus()
            } else {
                val x= bundleOf(
                    "email" to e,
                    "phone" to p,
                    "idnumber" to i,
                    "occupation" to o,
                    "type" to "borrowers",
                )

                Navigation.findNavController(requireView()).navigate(R.id.action_registration_to_locationsz,x)
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}