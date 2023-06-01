package com.example.biblioteca.bibliotecario

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.biblioteca.CustomCaptureActivity
import com.example.biblioteca.databinding.LibrarianLayoutBinding
import com.google.zxing.integration.android.IntentIntegrator

class Librarian_Fragment : Fragment() {
    private lateinit var binding: LibrarianLayoutBinding
    var value:String=""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LibrarianLayoutBinding.inflate(inflater)

        binding.qrAccesso.setOnClickListener {
            val scanner= IntentIntegrator.forSupportFragment(this)
            scanner.setCaptureActivity(CustomCaptureActivity::class.java) // Imposta la tua activity personalizzata
            scanner.setDesiredBarcodeFormats("qrCode")
            scanner.initiateScan()
        }

        binding.qrConsegna.setOnClickListener {
            val scanner= IntentIntegrator.forSupportFragment(this)
            scanner.setCaptureActivity(CustomCaptureActivity::class.java) // Imposta la tua activity personalizzata
            scanner.setDesiredBarcodeFormats("qrCode")
            scanner.initiateScan()
        }
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data)
        if(resultCode== Activity.RESULT_OK){
            if(result.contents==null){
                Toast.makeText(this.requireContext(),"cancelled", Toast.LENGTH_SHORT).show()
                Log.i("QRCODE","${result.contents}")
            }else{
                Toast.makeText(this.requireContext(),"scanned ->"+result.contents, Toast.LENGTH_SHORT).show()
                value=result.contents.toString()
                if(value =="user"){
                    val dialogBuilder = android.app.AlertDialog.Builder(context)
                    dialogBuilder.setTitle("Esito scansione")
                    dialogBuilder.setMessage("Approvato")
                    dialogBuilder.setPositiveButton("OK"){
                            _, _ ->
                    }
                    val dialog = dialogBuilder.create()
                    dialog.show()
                    }else{
                        val dialogBuilder = android.app.AlertDialog.Builder(context)
                        dialogBuilder.setTitle("Esito scansione")
                        dialogBuilder.setMessage("Negato")
                        dialogBuilder.setPositiveButton("OK"){
                                _, _ ->
                        }
                        val dialog = dialogBuilder.create()
                        dialog.show()
                    }


            }
        }
    }
}