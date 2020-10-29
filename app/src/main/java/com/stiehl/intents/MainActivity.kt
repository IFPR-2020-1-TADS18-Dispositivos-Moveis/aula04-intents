package com.stiehl.intents

import android.Manifest
import android.app.SearchManager
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.AlarmClock
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        const val PICK_IMAGE = 111
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btShowAlarms.setOnClickListener { showAlarms() }
        btOpenURL.setOnClickListener { openURL() }
        btCall.setOnClickListener { call("41999994444") }
        btSendEmail.setOnClickListener { sendEmail("test@test.com", "Hello", "World") }
        btSearch.setOnClickListener { searchWeb(txtSearch.text.toString()) }
        btSendSMS.setOnClickListener { sendSMS("41999998888", "Hello world.") }
        btPickImage.setOnClickListener { pickImage() }

        btShare.setOnClickListener { share(txtShare.text.toString()) }
    }

    private fun showAlarms() {
        val intent = Intent(AlarmClock.ACTION_SHOW_ALARMS)
        startActivity(intent)
    }

    private fun openURL() {
        val uri = Uri.parse("https://paranagua.ifpr.edu.br")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun call(number: String) {
        Dexter.withContext(this)
            .withPermission(Manifest.permission.CALL_PHONE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    callExecute(number)
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {}
                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                }
            })
            .check()
    }

    private fun callExecute(number: String) {
        val uri = Uri.parse("tel:$number")
        val intent = Intent(Intent.ACTION_CALL, uri)
        startActivity(intent)
    }

    private fun sendEmail(to: String, title: String, body: String) {
        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_SUBJECT, title)
            putExtra(Intent.EXTRA_TEXT, body)
            putExtra(Intent.EXTRA_EMAIL, to)
            type = "message/rfc822"
        }
        startActivity(emailIntent)
    }

    private fun searchWeb(query: String) {
        val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
            putExtra(SearchManager.QUERY, query)
        }
        startActivity(intent)
    }

    private fun sendSMS(to: String, message: String) {
        val uri = Uri.parse("sms:$to")
        val intent = Intent(Intent.ACTION_SENDTO, uri).apply {
            putExtra("sms_body", message)
        }
        startActivity(intent)
    }

    private fun pickImage() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imImage.setImageBitmap(imageBitmap)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun share(text: String) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(intent, null)
        startActivity(shareIntent)
    }
}