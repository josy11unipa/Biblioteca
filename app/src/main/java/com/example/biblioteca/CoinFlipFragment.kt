package com.example.biblioteca

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.example.biblioteca.databinding.LoginLayoutBinding

class CoinFlipFragment : Fragment() {

    private lateinit var coinImageView: ImageView
    private lateinit var loginFieldsLayout: LinearLayout
    private lateinit var binding: LoginLayoutBinding
    var username =""
    var password = ""

    var flag=true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.login_layout, container, false)
        coinImageView = view.findViewById(R.id.coinImageView)
        loginFieldsLayout = view.findViewById(R.id.loginFieldsLayout)

        binding=LoginLayoutBinding.inflate(layoutInflater)
        val result = "Login"
        setFragmentResult("key", bundleOf("keyBundle" to result))
        var button=binding.button2


        button.setOnClickListener {
            username = binding.campoUsername.text.toString()
            password = binding.campoPassword.text.toString()
            flipCoin()
        }
        Log.i("CREDENZIALI", "username: $username, password: $password")
        return view
    }

    private fun flipCoin() {
        this.flag=false
        coinImageView.animate().apply {
            duration = 1000

            rotationYBy(180f)
            withEndAction {
                if (coinImageView.rotationY == 180f) {
                    coinImageView.setImageResource(R.drawable.coin_heads)
                    //loginFieldsLayout.visibility = View.VISIBLE
                } else {
                    coinImageView.setImageResource(R.drawable.coin_heads)
                    //loginFieldsLayout.visibility = View.GONE
                }
                coinImageView.rotationY = 0f
            }
        }.start()
    }
}
