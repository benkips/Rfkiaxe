package com.pradytech.rafikiconnect.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.pradytech.rafikiconnect.R
import com.pradytech.rafikiconnect.databinding.FragmentLoginBinding
import com.pradytech.rafikiconnect.networks.Resource
import com.pradytech.rafikiconnect.utils.handleApiError
import com.pradytech.rafikiconnect.utils.isValidEmail
import com.pradytech.rafikiconnect.utils.showmessages
import com.pradytech.rafikiconnect.utils.visible
import com.pradytech.rafikiconnect.viewmodelz.Loginviewmodel
import com.pradytech.rafikiconnect.viewmodelz.Prefviewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Login : Fragment(R.layout.fragment_login) {
    private var _binding: FragmentLoginBinding?= null
    private val binding get() = _binding!!
    private lateinit var email:String
    private  lateinit var pass:String
    private  lateinit var switchb:String
    private  lateinit var tkn:String

    private  val viewmodel by viewModels<Loginviewmodel>()
    private  val pviewmodel by viewModels<Prefviewmodel>()

    val LendersTOPIC="lenders"
    val BorrowersTOPIC="Borrowers"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding .bind(view)
        binding.pgbar.visible(false)
        switchb= arguments?.getString("type")!!

        //notification token
        FirebaseMessaging.getInstance().getToken()
        // confirm notification
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
             tkn = task.result.toString()


        })
        viewmodel.notiResponse.observe(viewLifecycleOwner, Observer {
            binding.pgbar.visible(it is Resource.Loading)
            when (it) {
                is Resource.Success -> {
                    if(it.value[0].response.contains("success")){
                        val s:String=it.value[0].response
                        requireView().showmessages("Welcome","login was $s",
                            { cachenow() })
                    }else{
                        requireView().showmessages("Error",it.value[0].response)
                    }
                }
                is Resource.Failure -> handleApiError(it) {
                }
            }
        })

        binding.loginbtn.setOnClickListener {v->
            email = binding.email.editText!!.text.toString()
            pass = binding.passwrd.editText!!.text.toString()
            if(email.isEmpty()){
                binding.email.setError("Email cannot be empty")
            }else if (!isValidEmail(email)) {
                binding.email.setError("please  enter a valid email")
            }else if(pass.isEmpty()){
                binding.passwrd.setError("please enter a valid password")
            }else{

                if (switchb != null) {
                    vdate(email,pass,switchb)
                }

            }
        }
        binding.fgtpss.setOnClickListener {
            val x= bundleOf(
                "type" to "borrowers",
            )
            Navigation.findNavController(requireView()).navigate(R.id.action_login_to_forgtpass,x)
        }
        binding.createone.setOnClickListener {
        if (switchb == "lenders") {
            Navigation.findNavController(requireView()).navigate(R.id.action_login_to_lenderRegistration,)
        }else{
            Navigation.findNavController(requireView()).navigate(R.id.action_login_to_registration)
        }
        }
    }
    private fun  vdate(em:String,pass:String,rtype:String){
        viewmodel.validatenow(em,pass,rtype)
    }
    private  fun  cachenow() {
        if(switchb.equals("borrowers")){
            FirebaseMessaging.getInstance().subscribeToTopic(BorrowersTOPIC)
            FirebaseMessaging.getInstance().unsubscribeFromTopic(LendersTOPIC)
        }else{
            FirebaseMessaging.getInstance().subscribeToTopic(LendersTOPIC)
            FirebaseMessaging.getInstance().unsubscribeFromTopic(BorrowersTOPIC)
        }
        pviewmodel.saveToDataStore(email,switchb,tkn)

        val x= bundleOf(
            "web" to "https://rafiki.axecredits.com/android/account.php" ,
        )
        Navigation.findNavController(requireView()).navigate(R.id.action_login_to_wvinfo,x)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}