package com.ass.qrtest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*


class FirstActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firstActivityGenerateButton.setOnClickListener {
            startActivity(GenerateQrCodeActivity.getGenerateQrCodeActivity(this))
        }
        firstActivityScanButton.setOnClickListener {
            startActivity(ScanQrCodeActivity.getScanQrCodeActivity(this))
        }
    }
}
