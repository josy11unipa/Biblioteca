package com.example.biblioteca

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.biblioteca.databinding.ProfileLayoutBinding
import com.google.zxing.integration.android.IntentIntegrator

class Profile_Fragment:Fragment() {
    private lateinit var binding:ProfileLayoutBinding
    var risult:String=""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=ProfileLayoutBinding.inflate(inflater)

        binding.button.setOnClickListener {
            val scanner=IntentIntegrator.forSupportFragment(this)
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            scanner.initiateScan()
        }
        return binding.root
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data)
        if(resultCode==Activity.RESULT_OK){
            if(result.contents==null){
                Toast.makeText(this.requireContext(),"cancelled",Toast.LENGTH_SHORT).show()
                Log.i("QRCODE","${result.contents}")
            }else{
                Toast.makeText(this.requireContext(),"scanned ->"+result.contents,Toast.LENGTH_SHORT).show()
                risult=result.contents.toString()
                binding.button.text=risult
            }
        }

    }
}