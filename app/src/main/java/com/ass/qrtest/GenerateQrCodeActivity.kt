package com.ass.qrtest

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.ass.qrtest.helper.EncryptionHelper
import com.ass.qrtest.helper.QRCodeHelper
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

import com.spartons.qrcodegeneratorreader.models.UserObject
import kotlinx.android.synthetic.main.activity_generate_qr_code.*

class GenerateQrCodeActivity : AppCompatActivity() {

    companion object {

        fun getGenerateQrCodeActivity(callingClassContext: Context) = Intent(callingClassContext, GenerateQrCodeActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_qr_code)
        generateQrCodeButton.setOnClickListener {
            if (checkEditText()) {
                hideKeyboard()
                val user = UserObject(
                    fullName = fullNameEditText.text.toString(),
                    phoneno = Integer.parseInt(phonenoEditText.text.toString()) ,
                    address = addressEditText.text.toString() )
                val serializeString = Gson().toJson(user)
                val encryptedString = EncryptionHelper.getInstance().encryptionString(serializeString).encryptMsg()
                setImageBitmap(encryptedString)
            }




            generateQrCodeButtonOtherScanner.setOnClickListener(View.OnClickListener {

                val name = fullNameEditText.text.toString()
                val phno = phonenoEditText.text.toString()
                val address = addressEditText.text.toString()

                val writer = QRCodeWriter()
                try {
                    val bitMatrix = writer.encode("Name  :$name\nPhone No  :$phno\nAddress : $address", BarcodeFormat.QR_CODE, 512, 512)
                    val width = bitMatrix.width
                    val height = bitMatrix.height
                    val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                    for (x in 0 until width) {
                        for (y in 0 until height) {
                            bmp.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
                        }
                    }
                    qrCodeImageView.setImageBitmap(bmp)

                } catch (e: WriterException) {
                    e.printStackTrace()
                }

            })



        }





    }

    private fun setImageBitmap(encryptedString: String?) {
        val bitmap = QRCodeHelper.newInstance(this).setContent(encryptedString).setErrorCorrectionLevel(ErrorCorrectionLevel.Q).setMargin(2).qrcOde
        qrCodeImageView.setImageBitmap(bitmap)
    }

    /**
     * Hides the soft input keyboard if it is shown to the screen.
     */

    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun checkEditText(): Boolean {
        val view : View

        if (TextUtils.isEmpty(fullNameEditText.text.toString())) {
            Toast.makeText(this, "name field cannot be empty!", Toast.LENGTH_SHORT).show()
            return false
        } else if (TextUtils.isEmpty(phonenoEditText.text.toString())) {
            Toast.makeText(this, "phone no field cannot be empty!", Toast.LENGTH_SHORT).show()
            return false
        }else if(TextUtils.isEmpty(addressEditText.text.toString())) {
            Toast.makeText(this, "address field cannot be empty!", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }


}
