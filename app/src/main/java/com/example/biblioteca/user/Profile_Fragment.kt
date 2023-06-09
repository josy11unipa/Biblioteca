package com.example.biblioteca.user

import android.annotation.SuppressLint
import com.example.biblioteca.R
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.example.biblioteca.database.DBManager
import com.example.biblioteca.databinding.ProfileLayoutBinding
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.WriterException
import com.google.zxing.BarcodeFormat


class Profile_Fragment:Fragment() {
    companion object {
        var isLogged: Boolean = false
        var usernameUtente :String =""
    }

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
        //val result = "Area Personale"
        val cursor=dbManager.getUser()

        //setFragmentResult("key", bundleOf("keyBundle" to result))
        if (cursor.count!=0) {
            val username = cursor.getString(cursor.getColumnIndex("username"))
            val nome = cursor.getString(cursor.getColumnIndex("nome"))
            val cognome = cursor.getString(cursor.getColumnIndex("cognome"))
            val qr = cursor.getString(cursor.getColumnIndex("qr"))
            //val isLogged = cursor.getString(cursor.getColumnIndex("flag"))
            //binding.nome.text = "NOME: " + username.toString() //Mod JJ

            binding.nome.text = nome.toString()
            binding.cognome.text=cognome.toString()
            binding.username.text=username.toString()
            usernameUtente=username.toString()

            //qrcode
            val code = username.toString() // Codice da convertire in QR Code
            val bitmap = generateQRCode(code)
            binding.imageView3.setImageBitmap(bitmap)
        }

        binding.logoutButton.setOnClickListener{
           dbManager.delete()
            val manager=parentFragmentManager
            val transaction=manager.beginTransaction()

            transaction.replace(R.id.fragmentMain,Login_Fragment())
            transaction.commit()
            isLogged=false
        }
        return binding.root
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