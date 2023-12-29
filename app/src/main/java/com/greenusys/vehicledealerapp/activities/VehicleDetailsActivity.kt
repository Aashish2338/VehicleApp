package com.greenusys.vehicledealerapp.activities

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment.getExternalStorageDirectory
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.greenusys.vehicledealerapp.R
import com.greenusys.vehicledealerapp.contracts.DeleteVehicleContract
import com.greenusys.vehicledealerapp.contracts.VehicleDetailsContract
import com.greenusys.vehicledealerapp.models.ResendOtpResponse
import com.greenusys.vehicledealerapp.models.VehicleDetailsResponse
import com.greenusys.vehicledealerapp.presenter.DeleteVehiclePresenter
import com.greenusys.vehicledealerapp.presenter.VehicleDetailsPresenter
import com.greenusys.vehicledealerapp.utilities.UserSession
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


class VehicleDetailsActivity : AppCompatActivity(), VehicleDetailsContract.View,
    DeleteVehicleContract.View, View.OnClickListener {

    private lateinit var mContext: Context
    private lateinit var userSession: UserSession
    private lateinit var backDashboardImg: ImageView
    private lateinit var rcImg: ImageView
    private lateinit var hypoImg: ImageView
    private lateinit var insuRanceImg: ImageView
    private lateinit var othersImg: ImageView
    private lateinit var vehicleRC_Tv: TextView
    private lateinit var vehicleHypo_Tv: TextView
    private lateinit var vehicleInsu_Tv: TextView
    private lateinit var othersDocs_Tv: TextView
    private lateinit var okayGotIt_btn: AppCompatButton
    private lateinit var delete_btn: AppCompatButton
    private lateinit var share_btn: AppCompatButton
    private lateinit var hypoCard: CardView
    private lateinit var insuranceCard: CardView
    private lateinit var otherCard: CardView
    private lateinit var vehicleDetailsPresenter: VehicleDetailsPresenter
    private lateinit var deleteVehiclePresenter: DeleteVehiclePresenter
    private lateinit var dialog: Dialog
    private var strVehicleId: String? = ""
    private var tokenKey: String? = ""
    private var bankNoc: String? = ""
    private var insurance: String? = ""
    private var othersDocs: String? = ""

    private val baseUrl: String = "http://testnode.shivshankartransport.xyz/"
//    private val baseUrl: String = "http://192.168.1.13:5000/"
//    private val baseUrl: String = "https://wheelo.onrender.com/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_details)
        mContext = this
        userSession = UserSession(mContext)
        vehicleDetailsPresenter = VehicleDetailsPresenter(this)
        deleteVehiclePresenter = DeleteVehiclePresenter(this)

        getLayoutUiIdFind()

        backDashboardImg.setOnClickListener(this)
        okayGotIt_btn.setOnClickListener(this)
        delete_btn.setOnClickListener(this)
        share_btn.setOnClickListener(this)

        tokenKey = "Bearer " + userSession.getServiceKeyToken()
        strVehicleId = userSession.getVehicleId()

        println("details for vehicle :- $tokenKey, $strVehicleId")
        vehicleDetailsPresenter.requestVehicleListDataFromServer(tokenKey, strVehicleId)

//        savepdf()
//        createPdf(tokenKey!!)
    }

    private fun getLayoutUiIdFind() {
        try {
            backDashboardImg = findViewById<ImageView>(R.id.backDashboardImg) as ImageView
            rcImg = findViewById<ImageView>(R.id.rcImg) as ImageView
            hypoImg = findViewById<ImageView>(R.id.hypoImg) as ImageView
            insuRanceImg = findViewById<ImageView>(R.id.insuRanceImg) as ImageView
            othersImg = findViewById<ImageView>(R.id.othersImg) as ImageView
            vehicleRC_Tv = findViewById<TextView>(R.id.vehicleRC_Tv) as TextView
            vehicleHypo_Tv = findViewById<TextView>(R.id.vehicleHypo_Tv) as TextView
            vehicleInsu_Tv = findViewById<TextView>(R.id.vehicleInsu_Tv) as TextView
            othersDocs_Tv = findViewById<TextView>(R.id.othersDocs_Tv) as TextView
            okayGotIt_btn = findViewById<AppCompatButton>(R.id.okayGotIt_btn) as AppCompatButton
            delete_btn = findViewById<AppCompatButton>(R.id.delete_btn) as AppCompatButton
            share_btn = findViewById<AppCompatButton>(R.id.share_btn) as AppCompatButton
            hypoCard = findViewById<CardView>(R.id.hypoCard) as CardView
            insuranceCard = findViewById<CardView>(R.id.insuranceCard) as CardView
            otherCard = findViewById<CardView>(R.id.otherCard) as CardView

        } catch (exp: Exception) {
            exp.stackTrace
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.backDashboardImg -> {
                onBackPressedDispatcher.onBackPressed()
            }

            R.id.okayGotIt_btn -> {
                onBackPressedDispatcher.onBackPressed()
            }

            R.id.delete_btn -> {
                try {
                    val alert = AlertDialog.Builder(mContext)
                    alert.setTitle("Delete")
                    alert.setMessage("Are you sure you want to delete this vehicle data?")
                    alert.setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
                        deleteVehiclePresenter.requestDeleteVehicleDataFromServer(
                            tokenKey, strVehicleId
                        )
                    })

                    alert.setNegativeButton("No", DialogInterface.OnClickListener { _, _ ->
                        Toast.makeText(
                            mContext, "Thanks for save the data here!", Toast.LENGTH_SHORT
                        ).show()
                    })

                    val alertDialog = alert.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                    alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).isAllCaps = false
                    alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).isAllCaps = false
                } catch (exp: Exception) {
                    exp.stackTrace
                }
            }

            R.id.share_btn -> {
                val shareText =
                    "Vehicle details information \nRc Number :- " + vehicleRC_Tv.text.toString() + ",\nBank NOC :- $bankNoc," + "\nInsurance validity :- $insurance," + "\nOther Docs :- $othersDocs"
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "$shareText")
                    type = "text/plain"
                }
                try {
                    startActivity(sendIntent)
                } catch (anfe: ActivityNotFoundException) {
                    anfe.stackTrace
                }
            }
        }
    }

    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
    }

    override fun showProgress() {
        dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.google_progress_bar_item)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    override fun hideProgress() {
        dialog.cancel()
        dialog.dismiss()
    }

    override fun getDeleteVehicleData(resendOtpResponse: ResendOtpResponse?) {
        try {
            if (resendOtpResponse?.success.equals("vehicle deleted")) {
                Toast.makeText(
                    mContext, "You have been successfully deleted vehicle data!", Toast.LENGTH_SHORT
                ).show()
                startActivity(
                    Intent(
                        mContext, DashboardActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                )
                this@VehicleDetailsActivity.finish()
            } else {
                Toast.makeText(
                    mContext,
                    "Vehicle data not deleted, please try after some time.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (exp: Exception) {
            exp.stackTrace
        }
    }

    override fun getVehicleDetailsData(vehicleDetailsResponse: VehicleDetailsResponse?) {
        try {
            if (vehicleDetailsResponse?.vehicle != null) {
                if (vehicleDetailsResponse.vehicle?.Id != null) {
                    strVehicleId = vehicleDetailsResponse.vehicle?.Id
                } else {
                    strVehicleId = ""
                }

                if (vehicleDetailsResponse.vehicle?.vehicleNumber != null) {
                    vehicleRC_Tv.text = vehicleDetailsResponse.vehicle?.vehicleNumber
                } else {
                    vehicleRC_Tv.text = "N/A"
                }

                if (vehicleDetailsResponse.vehicle?.hypotheticationRC != null) {
                    vehicleHypo_Tv.text =
                        vehicleDetailsResponse.vehicle?.hypotheticationRC.toString()
                    bankNoc = vehicleDetailsResponse.vehicle?.hypotheticationRC.toString()
                } else if (vehicleDetailsResponse.vehicle?.vehicleNumber != null) {
                    vehicleHypo_Tv.text = vehicleDetailsResponse.vehicle?.vehicleNumber
                } else {
                    vehicleHypo_Tv.text = "N/A"
                }

                if (vehicleDetailsResponse.vehicle?.insuranceValid != null) {
                    vehicleInsu_Tv.text = vehicleDetailsResponse.vehicle?.insuranceValid.toString()
                    insurance = vehicleDetailsResponse.vehicle?.insuranceValid.toString()
                } else if (vehicleDetailsResponse.vehicle?.chassisNumber != null) {
                    vehicleInsu_Tv.text = vehicleDetailsResponse.vehicle?.chassisNumber
                } else {
                    vehicleInsu_Tv.text = "N/A"
                }

                if (vehicleDetailsResponse.vehicle?.otherDocument != null) {
                    othersDocs_Tv.text = vehicleDetailsResponse.vehicle?.otherDocument.toString()
                    othersDocs = vehicleDetailsResponse.vehicle?.otherDocument.toString()
                } else {
                    othersDocs_Tv.text = "N/A"
                }

                if (vehicleDetailsResponse.vehicle?.rcImage != null) {
                    val strVehicleRC: String? = vehicleDetailsResponse.vehicle?.rcImage.toString()
//                    val newVehicleRC: String? = strVehicleRC?.replace("public\\images\\", "")
                    val newVehicleRC: String? = strVehicleRC?.replace("public/images/", "")
                    println("Replace Vehicle RC Number Images :- $baseUrl$newVehicleRC")
                    Glide.with(mContext).load(baseUrl + newVehicleRC).apply(
                        RequestOptions.diskCacheStrategyOf(
                            DiskCacheStrategy.ALL
                        )
                    ).into(rcImg)
                } else {
                    rcImg.setImageResource(R.drawable.no_image)
                }

                if (vehicleDetailsResponse.vehicle?.BankNOCImage != null) {
                    hypoCard.visibility = View.VISIBLE
                    val strBankNOCImage: String? =
                        vehicleDetailsResponse.vehicle?.BankNOCImage.toString()
//                    val newBankNOCImage: String? = strBankNOCImage?.replace("public\\images\\", "")
                    val newBankNOCImage: String? = strBankNOCImage?.replace("public/images/", "")
                    println("Replace Vehicle Bank NOC Images :- $baseUrl$newBankNOCImage")
                    Glide.with(mContext).load(baseUrl + newBankNOCImage).apply(
                        RequestOptions.diskCacheStrategyOf(
                            DiskCacheStrategy.ALL
                        )
                    ).into(hypoImg)
                } else {
                    hypoCard.visibility = View.GONE
                    hypoImg.setImageResource(R.drawable.no_image)
                }

                if (vehicleDetailsResponse.vehicle?.insuranceImage != null) {
                    insuranceCard.visibility = View.VISIBLE
                    val strInsuranceImage: String? =
                        vehicleDetailsResponse.vehicle?.insuranceImage.toString()
                    val newInsuranceImage: String? =
//                        strInsuranceImage?.replace("public\\images\\", "")
                        strInsuranceImage?.replace("public/images/", "")
                    println("Replace Vehicle Insurance Images :- $baseUrl$newInsuranceImage")
                    Glide.with(mContext).load(baseUrl + newInsuranceImage).apply(
                        RequestOptions.diskCacheStrategyOf(
                            DiskCacheStrategy.ALL
                        )
                    ).into(insuRanceImg)
                } else {
                    insuranceCard.visibility = View.GONE
                    insuRanceImg.setImageResource(R.drawable.no_image)
                }

                if (vehicleDetailsResponse.vehicle?.otherDocumentImage != null) {
                    otherCard.visibility = View.VISIBLE
                    val strOtherDocumentImage: String? =
                        vehicleDetailsResponse.vehicle?.otherDocumentImage.toString()
                    val newOtherDocumentImage: String? =
//                        strOtherDocumentImage?.replace("public\\images\\", "")
                        strOtherDocumentImage?.replace("public/images/", "")
                    println("Replace Vehicle Other Details Images :- $baseUrl$newOtherDocumentImage")
                    Glide.with(mContext).load(baseUrl + newOtherDocumentImage).apply(
                        RequestOptions.diskCacheStrategyOf(
                            DiskCacheStrategy.ALL
                        )
                    ).into(othersImg)
                } else {
                    otherCard.visibility = View.GONE
                    othersImg.setImageResource(R.drawable.no_image)
                }
            } else {
                Toast.makeText(mContext, "Something went wrong!", Toast.LENGTH_SHORT).show()
            }
        } catch (exp: Exception) {
            exp.stackTrace
        }
    }

    override fun onResponseFailure(throwable: Throwable?) {
        Toast.makeText(mContext, throwable.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun createPdf(textToPdf: String) {
        val document = PdfDocument()
        var pageInfo: PdfDocument.PageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
        var page: PdfDocument.Page = document.startPage(pageInfo)
        var canvas = page.canvas
        var paint = Paint()
        paint.color = Color.RED
        canvas.drawCircle(50F, 50F, 30F, paint)
        paint.color = Color.BLACK
        canvas.drawText(textToPdf, 80F, 50F, paint)
        //canvas.drawt
        document.finishPage(page)

        // Create Page 2
        pageInfo = PdfDocument.PageInfo.Builder(300, 600, 2).create()
        page = document.startPage(pageInfo)
        canvas = page.canvas
        paint = Paint()
        paint.color = Color.BLUE
        canvas.drawCircle(100F, 100F, 100F, paint)
        document.finishPage(page)

        val directory_path = getExternalStorageDirectory().path + "/mypdf/"
        val file = File(directory_path)
        if (!file.exists()) {
            file.mkdirs()
        }
        val targetPdf = directory_path + "test-2.pdf"
        val filePath = File(targetPdf)
        try {
            document.writeTo(FileOutputStream(filePath))
            Toast.makeText(mContext, "Done", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            Log.e("main", "error $e")
            Toast.makeText(mContext, "Something wrong: $e", Toast.LENGTH_LONG).show()
        }

        document.close()
        //isPrinting = false
    }

    private fun savePdf() {
        try {
            val doc = Document()
            val mfile: String = SimpleDateFormat(
                "yyyyMMdd_HHmmss", Locale.getDefault()
            ).format(System.currentTimeMillis())
            val filepath: String = "${getExternalStorageDirectory()}/$mfile.pdf"
            println("File name -: $String")
            val smallBold = com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN)
//            val smallBold =
//                com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 12, BOLD)
            PdfWriter.getInstance(doc, FileOutputStream(filepath))
            doc.open()
            val mtext: String = vehicleRC_Tv.text.toString()
            doc.addAuthor("Wheeloo")
            doc.add(Paragraph(mtext, smallBold))
            doc.close()
            Toast.makeText(mContext, "$mfile.pdf is saved to $filepath", Toast.LENGTH_SHORT).show()

        } catch (exp: Exception) {
            exp.stackTrace
        }
    }

    private fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            Log.e("src", src!!)
            val url = URL(src)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            val myBitmap = BitmapFactory.decodeStream(input)
            Log.e("Bitmap", "returned")
            myBitmap
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("Exception", e.message.toString())
            null
        }
    }
}