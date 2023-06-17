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

//classe che gestisce il fragment del profilo utente
class Profile_Fragment:Fragment() {
    companion object {

        var usernameUtente :String =""
    }

    private lateinit var binding:ProfileLayoutBinding
    private lateinit var dbManager: DBManager
    @SuppressLint("Range")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=ProfileLayoutBinding.inflate(inflater)
        dbManager = DBManager(requireContext())
        dbManager.open()
        val cursor=dbManager.getUser()

        if (cursor.count!=0) {  //se l'utente ha effettuato l'accesso
            val username = cursor.getString(cursor.getColumnIndex("username"))
            val nome = cursor.getString(cursor.getColumnIndex("nome"))
            val cognome = cursor.getString(cursor.getColumnIndex("cognome"))
            val qr = cursor.getString(cursor.getColumnIndex("qr"))

            binding.nome.text = nome.toString()
            binding.cognome.text=cognome.toString()
            binding.username.text=username.toString()
            usernameUtente=username.toString()
            //qrcode
            val code = qr.toString() // Codice da convertire in QR Code
            val bitmap = generateQRCode(code) //genero il qrCode dal nome utente
            binding.imageView3.setImageBitmap(bitmap) //imposto il qrCode come immagine profilo
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
    private fun generateQRCode(code: String): Bitmap? { //Funzione che si occupa di generare il qrCode da una stringa
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