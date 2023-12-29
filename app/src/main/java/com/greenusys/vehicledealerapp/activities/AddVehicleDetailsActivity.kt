package com.greenusys.vehicledealerapp.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.cardview.widget.CardView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.greenusys.vehicledealerapp.R
import com.greenusys.vehicledealerapp.contracts.AddVehicleContract
import com.greenusys.vehicledealerapp.models.VehicleResponse
import com.greenusys.vehicledealerapp.presenter.AddVehiclePresenter
import com.greenusys.vehicledealerapp.utilities.UserSession
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class AddVehicleDetailsActivity : AppCompatActivity(), AddVehicleContract.View,
    View.OnClickListener {

    private lateinit var mContext: Context
    private lateinit var userSession: UserSession
    private lateinit var backDashboardImg: ImageView
    private lateinit var rcChassisNumber_Et: EditText
    private lateinit var rDRCYes: AppCompatRadioButton
    private lateinit var rDRCNo: AppCompatRadioButton
    private lateinit var rDInsuranceYes: AppCompatRadioButton
    private lateinit var rDInsuranceNo: AppCompatRadioButton
    private lateinit var otherDocsYes: AppCompatRadioButton
    private lateinit var otherDocsNo: AppCompatRadioButton
    private lateinit var hypoYes: CardView
    private lateinit var insuRanceYes: CardView
    private lateinit var othersYes: CardView
    private lateinit var vehicleRCEngine_Img: ImageView
    private lateinit var hypotheti_Img: ImageView
    private lateinit var insuRance_Img: ImageView
    private lateinit var others_Img: ImageView
    private lateinit var submit_btn: AppCompatButton
    private lateinit var rcEngineImageCapture: TextView
    private lateinit var hypotheticationCapture: TextView
    private lateinit var insuRanceCapture: TextView
    private lateinit var othersCapture: TextView
    private lateinit var galleryCameraDl: RelativeLayout
    private lateinit var addVehiclePresenter: AddVehiclePresenter
    private lateinit var dialog: Dialog
    private var tokenKey: String? = ""
    private var rcNumber: String? = ""
    private var hypotheticalYesNo: Boolean? = false
    private var strHypotheticalYesNo: String? = ""
    private var insuranceYesNo: Boolean? = false
    private var strInsuranceYesNo: String? = ""
    private var othersYesNo: Boolean? = false
    private var strOthersYesNo: String? = ""
    private var vehicleRCImage: String? = ""
    private var strHypotheticalImage: String? = ""
    private var strInsuranceImage: String? = ""
    private var strOtherDocsImage: String? = ""
    private var serialPhotoPath: String? = null
    private var vehicleDocsRCImage: File? = null
    private var hypotheticalImage: File? = null
    private var insuranceImage: File? = null
    private var otherDocsImage: File? = null
    private var photoURI: Uri? = null
    private val REQUEST_CODE: Int = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vehicle_details)
        mContext = this
        userSession = UserSession(mContext)
        addVehiclePresenter = AddVehiclePresenter(this)

        getUiIdInit()

        submit_btn.setOnClickListener(this)
        backDashboardImg.setOnClickListener(this)
        rcEngineImageCapture.setOnClickListener(this)
        rDRCYes.setOnClickListener(this)
        rDRCNo.setOnClickListener(this)
        rDInsuranceYes.setOnClickListener(this)
        rDInsuranceNo.setOnClickListener(this)
        otherDocsYes.setOnClickListener(this)
        otherDocsNo.setOnClickListener(this)
        hypotheticationCapture.setOnClickListener(this)
        insuRanceCapture.setOnClickListener(this)
        othersCapture.setOnClickListener(this)

        tokenKey = "Bearer " + userSession.getServiceKeyToken()

        if (tokenKey == null) {
            tokenKey = "Bearer " + userSession.getServiceKeyToken()
        } else {
            tokenKey = "Bearer " + userSession.getServiceKeyToken()
        }
    }

    private fun getUiIdInit() {
        try {
            backDashboardImg = findViewById<ImageView>(R.id.backDashboardImg) as ImageView
            rcChassisNumber_Et = findViewById<EditText>(R.id.rcChassisNumber_Et) as EditText
            rDRCYes = findViewById<AppCompatRadioButton>(R.id.rDRCYes) as AppCompatRadioButton
            rDRCNo = findViewById<AppCompatRadioButton>(R.id.rDRCNo) as AppCompatRadioButton
            rDInsuranceYes =
                findViewById<AppCompatRadioButton>(R.id.rDInsuranceYes) as AppCompatRadioButton
            rDInsuranceNo =
                findViewById<AppCompatRadioButton>(R.id.rDInsuranceNo) as AppCompatRadioButton
            otherDocsYes =
                findViewById<AppCompatRadioButton>(R.id.otherDocsYes) as AppCompatRadioButton
            otherDocsNo =
                findViewById<AppCompatRadioButton>(R.id.otherDocsNo) as AppCompatRadioButton
            vehicleRCEngine_Img = findViewById<ImageView>(R.id.vehicleRCEngine_Img) as ImageView
            hypotheti_Img = findViewById<ImageView>(R.id.hypotheti_Img) as ImageView
            insuRance_Img = findViewById<ImageView>(R.id.insuRance_Img) as ImageView
            others_Img = findViewById<ImageView>(R.id.others_Img) as ImageView

            submit_btn = findViewById<AppCompatButton>(R.id.submit_btn) as AppCompatButton
            rcEngineImageCapture = findViewById<TextView>(R.id.rcEngineImageCapture) as TextView
            hypotheticationCapture = findViewById<TextView>(R.id.hypotheticationCapture) as TextView
            insuRanceCapture = findViewById<TextView>(R.id.insuRanceCapture) as TextView
            othersCapture = findViewById<TextView>(R.id.othersCapture) as TextView

            galleryCameraDl = findViewById<RelativeLayout>(R.id.galleryCameraDl) as RelativeLayout

            hypoYes = findViewById<CardView>(R.id.hypoYes) as CardView
            insuRanceYes = findViewById<CardView>(R.id.insuRanceYes) as CardView
            othersYes = findViewById<CardView>(R.id.othersYes) as CardView

            hypoYes.visibility = View.GONE
            insuRanceYes.visibility = View.GONE
            othersYes.visibility = View.GONE

        } catch (exp: Exception) {
            exp.stackTrace
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )
        serialPhotoPath = image.absolutePath
        return image
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.submit_btn -> {
                try {

                    lateinit var hypotheticalsImage: MultipartBody.Part
                    lateinit var insurancesImage: MultipartBody.Part
                    lateinit var otherDocumentsImage: MultipartBody.Part

                    if (isFormDataValidate()) {
                        val vehicleRcNumber: RequestBody =
                            RequestBody.create(MediaType.parse("text/plain"), rcNumber)
                        val hypothetical: RequestBody = RequestBody.create(
                            MediaType.parse("text/plain"), hypotheticalYesNo.toString()
                        )
                        val insurance: RequestBody = RequestBody.create(
                            MediaType.parse("text/plain"), insuranceYesNo.toString()
                        )
                        val otherDocs: RequestBody = RequestBody.create(
                            MediaType.parse("text/plain"), othersYesNo.toString()
                        )

                        val requestBodyRcImage: RequestBody = RequestBody.create(
                            MediaType.parse("multipart/form-data"), vehicleDocsRCImage
                        )
                        val rcImage = MultipartBody.Part.createFormData(
                            "rcImage", vehicleDocsRCImage?.name, requestBodyRcImage
                        )

                        if (hypotheticalYesNo == true && insuranceYesNo == true && othersYesNo == true) {
                            val requestBodyHypothetImage: RequestBody = RequestBody.create(
                                MediaType.parse("multipart/form-data"), hypotheticalImage
                            )
                            hypotheticalsImage = MultipartBody.Part.createFormData(
                                "BankNOCImage", hypotheticalImage?.name, requestBodyHypothetImage
                            )

                            val requestBodyInsuranceImage: RequestBody = RequestBody.create(
                                MediaType.parse("multipart/form-data"), insuranceImage
                            )
                            insurancesImage = MultipartBody.Part.createFormData(
                                "insuranceImage", insuranceImage?.name, requestBodyInsuranceImage
                            )

                            val requestBodyOtherDocsImage: RequestBody = RequestBody.create(
                                MediaType.parse("multipart/form-data"), otherDocsImage
                            )
                            otherDocumentsImage = MultipartBody.Part.createFormData(
                                "otherDocumentImage",
                                otherDocsImage?.name,
                                requestBodyOtherDocsImage
                            )
                            addVehiclePresenter.requestAddVehicleDetailsDataFromServer(
                                tokenKey,
                                vehicleRcNumber,
                                hypothetical,
                                insurance,
                                otherDocs,
                                rcImage,
                                hypotheticalsImage,
                                insurancesImage,
                                otherDocumentsImage
                            )
                        } else if (hypotheticalYesNo == true && insuranceYesNo == true) {
                            val requestBodyHypothetImage: RequestBody = RequestBody.create(
                                MediaType.parse("multipart/form-data"), hypotheticalImage
                            )
                            hypotheticalsImage = MultipartBody.Part.createFormData(
                                "BankNOCImage", hypotheticalImage?.name, requestBodyHypothetImage
                            )

                            val requestBodyInsuranceImage: RequestBody = RequestBody.create(
                                MediaType.parse("multipart/form-data"), insuranceImage
                            )
                            insurancesImage = MultipartBody.Part.createFormData(
                                "insuranceImage", insuranceImage?.name, requestBodyInsuranceImage
                            )
                            addVehiclePresenter.requestAddVehicleDetailsDataFromServer(
                                tokenKey,
                                vehicleRcNumber,
                                hypothetical,
                                insurance,
                                otherDocs,
                                rcImage,
                                hypotheticalsImage,
                                insurancesImage,
                                null
                            )
                        } else if (insuranceYesNo == true && othersYesNo == true) {
                            val requestBodyInsuranceImage: RequestBody = RequestBody.create(
                                MediaType.parse("multipart/form-data"), insuranceImage
                            )
                            insurancesImage = MultipartBody.Part.createFormData(
                                "insuranceImage", insuranceImage?.name, requestBodyInsuranceImage
                            )

                            val requestBodyOtherDocsImage: RequestBody = RequestBody.create(
                                MediaType.parse("multipart/form-data"), otherDocsImage
                            )
                            otherDocumentsImage = MultipartBody.Part.createFormData(
                                "otherDocumentImage",
                                otherDocsImage?.name,
                                requestBodyOtherDocsImage
                            )
                            addVehiclePresenter.requestAddVehicleDetailsDataFromServer(
                                tokenKey,
                                vehicleRcNumber,
                                hypothetical,
                                insurance,
                                otherDocs,
                                rcImage,
                                null,
                                insurancesImage,
                                otherDocumentsImage
                            )
                        } else if (hypotheticalYesNo == true) {
                            val requestBodyHypothetImage: RequestBody = RequestBody.create(
                                MediaType.parse("multipart/form-data"), hypotheticalImage
                            )
                            hypotheticalsImage = MultipartBody.Part.createFormData(
                                "BankNOCImage", hypotheticalImage?.name, requestBodyHypothetImage
                            )
                            addVehiclePresenter.requestAddVehicleDetailsDataFromServer(
                                tokenKey,
                                vehicleRcNumber,
                                hypothetical,
                                insurance,
                                otherDocs,
                                rcImage,
                                hypotheticalsImage,
                                null,
                                null
                            )
                        } else if (insuranceYesNo == true) {
                            val requestBodyInsuranceImage: RequestBody = RequestBody.create(
                                MediaType.parse("multipart/form-data"), insuranceImage
                            )
                            insurancesImage = MultipartBody.Part.createFormData(
                                "insuranceImage", insuranceImage?.name, requestBodyInsuranceImage
                            )
                            addVehiclePresenter.requestAddVehicleDetailsDataFromServer(
                                tokenKey,
                                vehicleRcNumber,
                                hypothetical,
                                insurance,
                                otherDocs,
                                rcImage,
                                null,
                                insurancesImage,
                                null
                            )
                        } else if (othersYesNo == true) {
                            val requestBodyOtherDocsImage: RequestBody = RequestBody.create(
                                MediaType.parse("multipart/form-data"), otherDocsImage
                            )
                            otherDocumentsImage = MultipartBody.Part.createFormData(
                                "otherDocumentImage",
                                otherDocsImage?.name,
                                requestBodyOtherDocsImage
                            )
                            addVehiclePresenter.requestAddVehicleDetailsDataFromServer(
                                tokenKey,
                                vehicleRcNumber,
                                hypothetical,
                                insurance,
                                otherDocs,
                                rcImage,
                                null,
                                null,
                                otherDocumentsImage
                            )
                        } else {
                            hypotheticalYesNo = false
                            insuranceYesNo = false
                            othersYesNo = false
                            addVehiclePresenter.requestAddVehicleDetailsDataFromServer(
                                tokenKey,
                                vehicleRcNumber,
                                hypothetical,
                                insurance,
                                otherDocs,
                                rcImage,
                                null,
                                null,
                                null
                            )
                        }
                    }
                } catch (exp: Exception) {
                    exp.stackTrace
                    println("Click error result :- ${exp.message}")
                }
            }

            R.id.backDashboardImg -> {
                onBackPressedDispatcher.onBackPressed()
            }

            R.id.rDRCYes -> {
                rDRCNo.isChecked = false
                rDRCYes.isChecked = true
                hypotheticalYesNo = true
                strHypotheticalYesNo = "true"
                hypoYes.visibility = View.VISIBLE
            }

            R.id.rDRCNo -> {
                rDRCNo.isChecked = true
                rDRCYes.isChecked = false
                hypotheticalYesNo = false
                strHypotheticalYesNo = "false"
                hypoYes.visibility = View.GONE
                val res = resources.getDrawable(R.drawable.no_image)
                hypotheti_Img.setImageDrawable(res)
                hypotheticalImage = null
            }

            R.id.rDInsuranceYes -> {
                rDInsuranceYes.isChecked = true
                rDInsuranceNo.isChecked = false
                insuranceYesNo = true
                strInsuranceYesNo = "true"
                insuRanceYes.visibility = View.VISIBLE
            }

            R.id.rDInsuranceNo -> {
                rDInsuranceNo.isChecked = true
                rDInsuranceYes.isChecked = false
                insuranceYesNo = false
                strInsuranceYesNo = "false"
                insuRanceYes.visibility = View.GONE
                val res = resources.getDrawable(R.drawable.no_image)
                insuRance_Img.setImageDrawable(res)
                insuranceImage = null
            }

            R.id.otherDocsYes -> {
                otherDocsYes.isChecked = true
                otherDocsNo.isChecked = false
                othersYesNo = true
                strOthersYesNo = "true"
                othersYes.visibility = View.VISIBLE
            }

            R.id.otherDocsNo -> {
                otherDocsYes.isChecked = false
                otherDocsNo.isChecked = true
                othersYesNo = false
                strOthersYesNo = "false"
                othersYes.visibility = View.GONE
                val res = resources.getDrawable(R.drawable.no_image)
                others_Img.setImageDrawable(res)
                otherDocsImage = null
            }

            R.id.rcEngineImageCapture -> {
                try {
                    val bottomSheetDialog = BottomSheetDialog(mContext)
                    val layout: View = LayoutInflater.from(mContext).inflate(
                        R.layout.gallary_camera_popup_layout, galleryCameraDl, false
                    )
                    bottomSheetDialog.setContentView(layout)
                    bottomSheetDialog.setCancelable(false)
                    bottomSheetDialog.setCanceledOnTouchOutside(true)
                    bottomSheetDialog.show()

                    val cancelBtn = layout.findViewById<ImageView>(R.id.cancelBtn) as ImageView
                    val galleryCV = layout.findViewById<CardView>(R.id.galleryCV) as CardView
                    val cameraCV = layout.findViewById<CardView>(R.id.cameraCV) as CardView

                    cancelBtn.setOnClickListener(View.OnClickListener {
                        bottomSheetDialog.cancel()
                        bottomSheetDialog.dismiss()
                    })

                    galleryCV.setOnClickListener(View.OnClickListener {
                        bottomSheetDialog.cancel()
                        bottomSheetDialog.dismiss()
                        val gallery =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                        launchGalleryRCActivity.launch(gallery)
                    })

                    cameraCV.setOnClickListener(View.OnClickListener {
                        bottomSheetDialog.cancel()
                        bottomSheetDialog.dismiss()
//                        capturePhoto()
                        val rCCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        launchRCCameraActivity.launch(rCCamera)
                    })
                } catch (exp: Exception) {
                    exp.stackTrace
                }
            }

            R.id.hypotheticationCapture -> {
                try {
                    val bottomSheetDialog = BottomSheetDialog(mContext)
                    val layout: View = LayoutInflater.from(mContext).inflate(
                        R.layout.gallary_camera_popup_layout, galleryCameraDl, false
                    )
                    bottomSheetDialog.setContentView(layout)
                    bottomSheetDialog.setCancelable(false)
                    bottomSheetDialog.setCanceledOnTouchOutside(true)
                    bottomSheetDialog.show()

                    val cancelBtn = layout.findViewById<ImageView>(R.id.cancelBtn) as ImageView
                    val galleryCV = layout.findViewById<CardView>(R.id.galleryCV) as CardView
                    val cameraCV = layout.findViewById<CardView>(R.id.cameraCV) as CardView

                    cancelBtn.setOnClickListener(View.OnClickListener {
                        bottomSheetDialog.cancel()
                        bottomSheetDialog.dismiss()
                    })

                    galleryCV.setOnClickListener(View.OnClickListener {
                        bottomSheetDialog.cancel()
                        bottomSheetDialog.dismiss()
                        val gallery =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                        launchHypotheticalGalleryActivity.launch(gallery)
                    })

                    cameraCV.setOnClickListener(View.OnClickListener {
                        bottomSheetDialog.cancel()
                        bottomSheetDialog.dismiss()
//                        capturePhoto()
                        val cameraHypothetication = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        launchHypotheticalCameraActivity.launch(cameraHypothetication)
                    })
                } catch (exp: Exception) {
                    exp.stackTrace
                }
            }

            R.id.insuRanceCapture -> {
                try {
                    val bottomSheetDialog = BottomSheetDialog(mContext)
                    val layout: View = LayoutInflater.from(mContext).inflate(
                        R.layout.gallary_camera_popup_layout, galleryCameraDl, false
                    )
                    bottomSheetDialog.setContentView(layout)
                    bottomSheetDialog.setCancelable(false)
                    bottomSheetDialog.setCanceledOnTouchOutside(true)
                    bottomSheetDialog.show()

                    val cancelBtn = layout.findViewById<ImageView>(R.id.cancelBtn) as ImageView
                    val galleryCV = layout.findViewById<CardView>(R.id.galleryCV) as CardView
                    val cameraCV = layout.findViewById<CardView>(R.id.cameraCV) as CardView

                    cancelBtn.setOnClickListener(View.OnClickListener {
                        bottomSheetDialog.cancel()
                        bottomSheetDialog.dismiss()
                    })

                    galleryCV.setOnClickListener(View.OnClickListener {
                        bottomSheetDialog.cancel()
                        bottomSheetDialog.dismiss()
                        val gallery =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                        launchInsuranceGalleryActivity.launch(gallery)
                    })

                    cameraCV.setOnClickListener(View.OnClickListener {
                        bottomSheetDialog.cancel()
                        bottomSheetDialog.dismiss()
//                        capturePhoto()
                        val cameraInsurance = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        launchInsuranceCameraActivity.launch(cameraInsurance)
                    })
                } catch (exp: Exception) {
                    exp.stackTrace
                }
            }

            R.id.othersCapture -> {
                try {
                    val bottomSheetDialog = BottomSheetDialog(mContext)
                    val layout: View = LayoutInflater.from(mContext).inflate(
                        R.layout.gallary_camera_popup_layout, galleryCameraDl, false
                    )
                    bottomSheetDialog.setContentView(layout)
                    bottomSheetDialog.setCancelable(false)
                    bottomSheetDialog.setCanceledOnTouchOutside(true)
                    bottomSheetDialog.show()

                    val cancelBtn = layout.findViewById<ImageView>(R.id.cancelBtn) as ImageView
                    val galleryCV = layout.findViewById<CardView>(R.id.galleryCV) as CardView
                    val cameraCV = layout.findViewById<CardView>(R.id.cameraCV) as CardView

                    cancelBtn.setOnClickListener(View.OnClickListener {
                        bottomSheetDialog.cancel()
                        bottomSheetDialog.dismiss()
                    })

                    galleryCV.setOnClickListener(View.OnClickListener {
                        bottomSheetDialog.cancel()
                        bottomSheetDialog.dismiss()
                        val gallery =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                        launchOtherDocsGalleryActivity.launch(gallery)
                    })

                    cameraCV.setOnClickListener(View.OnClickListener {
                        bottomSheetDialog.cancel()
                        bottomSheetDialog.dismiss()
//                        capturePhoto()
                        val cameraOtherDocs = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        launchOtherDocsCameraActivity.launch(cameraOtherDocs)
                    })
                } catch (exp: Exception) {
                    exp.stackTrace
                }
            }
        }
    }

    private fun capturePhoto() {
        if (hypotheticalYesNo == true) {
            println("Hypothetical Yes/No :- $hypotheticalYesNo")
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, REQUEST_CODE)
        } else if (insuranceYesNo == true) {
            println("Insurance Yes/No :- $insuranceYesNo")
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, REQUEST_CODE)
        } else if (othersYesNo == true) {
            println("Other Yes/No :- $othersYesNo")
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, REQUEST_CODE)
        } else {
            println("Vehicle RC :- Yes")
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, REQUEST_CODE)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var bitmap: Bitmap? = null
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE && data != null) {
            bitmap = data.extras?.get("data") as Bitmap
            vehicleRCEngine_Img.setImageBitmap(bitmap)
            val imageUri = getImageUri(mContext, bitmap)
            vehicleDocsRCImage = getPath(imageUri)?.let { File(it) }
            vehicleRCImage = vehicleDocsRCImage.toString()
            if (vehicleRCImage != null) {
                println("Camera RC file image path file :- $vehicleRCImage")
            } else {
                Toast.makeText(
                    mContext, "Please retry for your RC picture!", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private var launchRCCameraActivity = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        try {
            if (result.resultCode == RESULT_OK) {
                val bitmap = result.data?.extras?.get("data") as Bitmap
                vehicleRCEngine_Img.setImageBitmap(bitmap)
                val imageUri = getImageUri(mContext, bitmap)
                vehicleDocsRCImage = getPath(imageUri)?.let { File(it) }
                vehicleRCImage = vehicleDocsRCImage.toString()
                if (vehicleRCImage != null) {
                    println("Camera RC file image path file :- $vehicleRCImage")
                } else {
                    val res = resources.getDrawable(R.drawable.no_image)
                    vehicleRCEngine_Img.setImageDrawable(res)
                    Toast.makeText(
                        mContext, "Please retry for your RC picture!", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } catch (exp: Exception) {
            exp.stackTrace
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private var launchHypotheticalCameraActivity =
        registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            try {
                if (result.resultCode == RESULT_OK) {
                    val bitmap = result.data?.extras?.get("data") as Bitmap
                    hypotheti_Img.setImageBitmap(bitmap)
                    val imageUri = getImageUri(mContext, bitmap)
                    hypotheticalImage = getPath(imageUri)?.let { File(it) }
                    strHypotheticalImage = hypotheticalImage.toString()
                    if (strHypotheticalImage != null) {
                        println("Camera hypothetical file image path file :- $strHypotheticalImage")
                    } else {
                        val res = resources.getDrawable(R.drawable.no_image)
                        hypotheti_Img.setImageDrawable(res)
                        Toast.makeText(
                            mContext,
                            "Please retry for your hypothetical picture!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (exp: Exception) {
                exp.stackTrace
            }
        }

    @SuppressLint("UseCompatLoadingForDrawables")
    private var launchInsuranceCameraActivity = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        try {
            if (result.resultCode == RESULT_OK) {
                val bitmap = result.data?.extras?.get("data") as Bitmap
                insuRance_Img.setImageBitmap(bitmap)
                val imageUri = getImageUri(mContext, bitmap)
                insuranceImage = getPath(imageUri)?.let { File(it) }
                strInsuranceImage = insuranceImage.toString()
                if (strInsuranceImage != null) {
                    println("Camera insurance file image path file :- $strInsuranceImage")
                } else {
                    val res = resources.getDrawable(R.drawable.no_image)
                    insuRance_Img.setImageDrawable(res)
                    Toast.makeText(
                        mContext, "Please retry for your insurance picture!", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } catch (exp: Exception) {
            exp.stackTrace
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private var launchOtherDocsCameraActivity = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        try {
            if (result.resultCode == RESULT_OK) {
                val bitmap = result.data?.extras?.get("data") as Bitmap
                others_Img.setImageBitmap(bitmap)
                val imageUri = getImageUri(mContext, bitmap)
                otherDocsImage = getPath(imageUri)?.let { File(it) }
                strOtherDocsImage = otherDocsImage.toString()
                if (strOtherDocsImage != null) {
                    println("Camera others docs file image path file :- $strOtherDocsImage")
                } else {
                    val res = resources.getDrawable(R.drawable.no_image)
                    others_Img.setImageDrawable(res)
                    Toast.makeText(
                        mContext, "Please retry for your others docs picture!", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } catch (exp: Exception) {
            exp.stackTrace
        }
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver, inImage, "image", null
        )
        return Uri.parse(path)
    }

    private fun isFormDataValidate(): Boolean {
        try {
            rcNumber = rcChassisNumber_Et.text.toString().trim { it <= ' ' }
            if (rcNumber!!.isEmpty()) {
                rcChassisNumber_Et.error = "Enter RC Number!"
                rcChassisNumber_Et.requestFocus()
                return false
            } else if (strHypotheticalYesNo!!.isEmpty()) {
                Toast.makeText(
                    mContext, "Please select Yes/No of hypothetical on RC!", Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (strInsuranceYesNo!!.isEmpty()) {
                Toast.makeText(
                    mContext, "Please select Yes/No of insurance validity!", Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (strOthersYesNo!!.isEmpty()) {
                Toast.makeText(
                    mContext, "Please select Yes/No of other documents!", Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (vehicleRCImage!!.isEmpty()) {
                Toast.makeText(
                    mContext, "Vehicle RC image is mandatory!", Toast.LENGTH_SHORT
                ).show()
                return false
            }
        } catch (exp: Exception) {
            exp.stackTrace
        }
        return true
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private var launchGalleryRCActivity = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        try {
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val imageUri = data?.data
                if (imageUri.toString().contains("image")) {
                    vehicleRCEngine_Img.setImageURI(imageUri)
                    vehicleDocsRCImage = getPath(imageUri)?.let { File(it) }
                    vehicleRCImage = vehicleDocsRCImage.toString()
                    println("Gallery vehicle RC image path file A $vehicleRCImage")
                } else {
                    val res = resources.getDrawable(R.drawable.no_image)
                    vehicleRCEngine_Img.setImageDrawable(res)
                    Toast.makeText(mContext, "Please select image only!", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (exp: Exception) {
            exp.stackTrace
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private var launchHypotheticalGalleryActivity =
        registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            try {
                if (result.resultCode == RESULT_OK) {
                    val data = result.data
                    val imageUri = data?.data
                    if (imageUri.toString().contains("image")) {
                        hypotheti_Img.setImageURI(imageUri)
                        hypotheticalImage = getPath(imageUri)?.let { File(it) }
                        strHypotheticalImage = hypotheticalImage.toString()
                        println("Gallery hypothetical image path file A $strHypotheticalImage")
                    } else {
                        val res = resources.getDrawable(R.drawable.no_image)
                        hypotheti_Img.setImageDrawable(res)
                        Toast.makeText(mContext, "Please select image only!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } catch (exp: Exception) {
                exp.stackTrace
            }
        }

    @SuppressLint("UseCompatLoadingForDrawables")
    private var launchInsuranceGalleryActivity = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        try {
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val imageUri = data?.data
                if (imageUri.toString().contains("image")) {
                    insuRance_Img.setImageURI(imageUri)
                    insuranceImage = getPath(imageUri)?.let { File(it) }
                    strInsuranceImage = insuranceImage.toString()
                    println("Gallery insurance image path file A $strInsuranceImage")
                } else {
                    val res = resources.getDrawable(R.drawable.no_image)
                    insuRance_Img.setImageDrawable(res)
                    Toast.makeText(mContext, "Please select image only!", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (exp: Exception) {
            exp.stackTrace
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private var launchOtherDocsGalleryActivity = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        try {
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val imageUri = data?.data
                if (imageUri.toString().contains("image")) {
                    others_Img.setImageURI(imageUri)
                    otherDocsImage = getPath(imageUri)?.let { File(it) }
                    strOtherDocsImage = otherDocsImage.toString()
                    println("Gallery other docs image path file A $strOtherDocsImage")
                } else {
                    val res = resources.getDrawable(R.drawable.no_image)
                    insuRance_Img.setImageDrawable(res)
                    Toast.makeText(mContext, "Please select image only!", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (exp: Exception) {
            exp.stackTrace
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

    override fun getAddVehicleDetailsData(vehicleResponse: VehicleResponse?) {
        if (vehicleResponse?.newVehicle != null) {
            Toast.makeText(mContext, "Vehicle added successfully!", Toast.LENGTH_SHORT).show()
            setClearAllFields()
        } else {
            Toast.makeText(
                mContext, "Vehicle not added! Please try after sometime.", Toast.LENGTH_SHORT
            ).show()
            setClearAllFields()
        }
    }

    private fun setClearAllFields() {
        try {
            rcChassisNumber_Et.text.clear()
            vehicleRCImage = ""
            hypotheticalYesNo = false
            insuranceYesNo = false
            othersYesNo = false
            startActivity(
                Intent(
                    mContext, DashboardActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            this@AddVehicleDetailsActivity.finish()
        } catch (exp: Exception) {
            exp.stackTrace
        }
    }

    private fun getPath(uri: Uri?): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri!!, projection, null, null, null) ?: return null
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val s = cursor.getString(column_index)
        cursor.close()
        return s
    }

    override fun onResponseFailure(throwable: Throwable?) {
        Toast.makeText(mContext, throwable.toString(), Toast.LENGTH_SHORT).show()
    }
}