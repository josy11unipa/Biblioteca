package com.example.biblioteca.user

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.biblioteca.databinding.ProfileLayoutBinding
import com.google.zxing.integration.android.IntentIntegrator
import androidx.fragment.app.setFragmentResult
import com.example.biblioteca.R
import com.example.biblioteca.database.DBManager
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter


class Profile_Fragment:Fragment() {
    private lateinit var binding:ProfileLayoutBinding
    private lateinit var dbManager: DBManager
    var risult:String=""

    @SuppressLint("Range")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=ProfileLayoutBinding.inflate(inflater)
        dbManager = DBManager(requireContext())
        dbManager.open()
        val result = "Area Personale"
        val cursor=dbManager.getUser()

        setFragmentResult("key", bundleOf("keyBundle" to result))
        if (cursor.count!=0) {
            val username = cursor.getString(cursor.getColumnIndex("username"))
            val type = cursor.getString(cursor.getColumnIndex("type"))
            val qr = cursor.getString(cursor.getColumnIndex("qr"))
            binding.nome.text = "NOME: " + username.toString()
            //qrcode
            val code = username.toString() // Codice da convertire in QR Code
            val bitmap = generateQRCode(code)
            binding.imageView3.setImageBitmap(bitmap)
        }

        binding.button.setOnClickListener {
            val scanner=IntentIntegrator.forSupportFragment(this)
            scanner.setDesiredBarcodeFormats("qrCode")
            scanner.initiateScan()
        }

        binding.logoutButton.setOnClickListener{
           dbManager.delete()
            val manager=parentFragmentManager
            val transaction=manager.beginTransaction()

            transaction.replace(R.id.fragmentMain,Login_Fragment())
            transaction.commit()

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
    private fun generateQRCode(code: String): Bitmap? {
        val qrCodeWriter = QRCodeWriter()
        try {
            val bitMatrix: BitMatrix = qrCodeWriter.encode(code, BarcodeFormat.QR_CODE, 200, 200)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            return bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return null
    }
}