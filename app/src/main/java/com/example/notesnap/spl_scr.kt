package com.example.notesnap

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.widget.Button
import android.widget.Toast

class spl_scr : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spl_scr)

        fun isInternetAvailable(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
        }

// Usage example:
        if (isInternetAvailable(this)) {
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, login::class.java)
                startActivity(intent)
                finish()
            }, 3000)
            // Internet is available, proceed with your logic
        } else {
            // Internet is not available, show a toast
            showNoInternetDialog(this)
            Toast.makeText(
                this,
                "Your internet is off, please check your connection.",
                Toast.LENGTH_SHORT
            ).show()
        }


    }

    private fun showNoInternetDialog(splScr: spl_scr) {
        val noInternetDialog = NoInternetDialog(this)
        noInternetDialog.show()


    }
    class NoInternetDialog(context: Context) : Dialog(context) {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_no_internet)

            val okButton = findViewById<Button>(R.id.okButton)
            okButton.setOnClickListener {
                dismiss()
            }
        }
    }
}



