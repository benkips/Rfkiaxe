package com.pradytech.rafikiconnect

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.messaging.FirebaseMessaging
import com.pradytech.rafikiconnect.databinding.ActivityMainBinding
import com.pradytech.rafikiconnect.utils.showPermissionRequestExplanation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.M)
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    val TOPIC="Alerts"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val navController = findNavController(R.id.nav_host_fragment_content_index)
      /*  appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.mainfragment
            )
        )*/
        toolbar.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.launcher) {
                toolbar.visibility = View.GONE
            }else  if(destination.id == R.id.switchboard) {
                toolbar.visibility = View.GONE
            }else  if(destination.id == R.id.login) {
                toolbar.visibility = View.GONE
            }else if(destination.id == R.id.registration){
                toolbar.visibility = View.GONE
            }else  if(destination.id == R.id.wvinfo) {
                toolbar.visibility = View.GONE
            }else  if(destination.id == R.id.lenderRegistration) {
                toolbar.visibility = View.GONE
            }else  if(destination.id == R.id.locationsz) {
                toolbar.visibility = View.GONE
            }else  if(destination.id == R.id.forgtpass) {
                toolbar.visibility = View.GONE
            }else{
                toolbar.visibility = View.VISIBLE
            }
        }

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (!granted) {
                    Toast.makeText(this, "Storage Permission NOT Granted", Toast.LENGTH_SHORT).show()
                    requestStoragePermission()
                }
            }
        requestStoragePermission()
    }
    //asking for permission
    private fun requestStoragePermission(){
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                // The permission is granted
                // you can go with the flow that requires permission here
            }
            shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                // This case means user previously denied the permission
                // So here we can display an explanation to the user
                // That why exactly we need this permission
                showPermissionRequestExplanation(
                    getString(R.string.write_storage),
                    getString(R.string.permission_request)
                ) { requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) }
            }
            else -> {
                // Everything is fine you can simply request the permission
                requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }

}