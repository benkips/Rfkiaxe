package com.pradytech.rafikiconnect.fragments

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.*
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.firebase.messaging.FirebaseMessaging
import com.pradytech.rafikiconnect.R
import com.pradytech.rafikiconnect.databinding.FragmentWvinfoBinding
import com.pradytech.rafikiconnect.utils.visible
import com.pradytech.rafikiconnect.viewmodelz.Prefviewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class wvinfo : Fragment(R.layout.fragment_wvinfo) {
    private var _binding: FragmentWvinfoBinding? = null
    private val binding get() = _binding!!

    private val pviewmodel by viewModels<Prefviewmodel>()
    private lateinit var email: String
    private lateinit var type: String
    private lateinit var token: String


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentWvinfoBinding.bind(view)
        if (binding.wvvs != null) {
            val url = arguments?.getString("web")


            pviewmodel.readFromDataStore.observe(viewLifecycleOwner, Observer {
                if (it.em != "none") {
                    email = it.em
                    type = it.typ
                    token=it.tkn
                }

            })

            binding.wvvs.settings.javaScriptEnabled = true
            binding.wvvs.webViewClient = WebViewClient()
            binding.wvvs.webChromeClient = WebChromeClient()


            val jsInterface = JavaScriptInterface(requireContext(), pviewmodel,view)
            binding.wvvs.addJavascriptInterface(jsInterface, "JSInterface")
            binding.wvvs.settings.setUseWideViewPort(true)
            binding.wvvs.settings.setLoadWithOverviewMode(true)
            binding.wvvs.settings.setDomStorageEnabled(true)
            binding.wvvs.settings.setDatabaseEnabled(true)
            binding.wvvs.clearHistory()



            binding.wvvs.setOnLongClickListener({ true })
            binding.wvvs.setLongClickable(false)


            if (url != null) {
                binding.wvvs.loadUrl(url)
            }

            binding.wvvs.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    val key = "email"
                    val `val`: String = email

                    val key2 = "type"
                    val `val2`: String = type

                    val key3 = "token"
                    val `val3`: String = token

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        binding.wvvs.evaluateJavascript(
                            "localStorage.setItem('$key','$`val`');",
                            null
                        )
                        binding.wvvs.evaluateJavascript(
                            "localStorage.setItem('$key2','$val2');",
                            null
                        )

                        binding.wvvs.evaluateJavascript(
                            "localStorage.setItem('$key3','$val3');",
                            null
                        )

                    } else {
                        binding.wvvs.loadUrl("javascript:localStorage.setItem('$key','$`val`');")
                        binding.wvvs.loadUrl("javascript:localStorage.setItem('$key2','$`val2`');")
                        binding.wvvs.loadUrl("javascript:localStorage.setItem('$key3','$`val3`');")
                    }
                    try {
                        binding.pgbar.visible(true)
                    } catch (exception: Exception) {
                        exception.printStackTrace()
                    }



                    super.onPageStarted(view, url, favicon)

                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    try {
                        binding.pgbar.visible(false)
                    } catch (exception: Exception) {
                        exception.printStackTrace()
                    }

                    super.onPageFinished(view, url)
                }

                override fun onReceivedError(
                    view: WebView?,
                    errorCode: Int,
                    description: String?,
                    failingUrl: String?
                ) {
                    try {
                        binding.pgbar.visible(false)
                    } catch (exception: Exception) {
                        exception.printStackTrace()
                    }
                    val myerrorpage = "file:///android_asset/android/errorpage.html";
                    binding.wvvs.loadUrl(myerrorpage)
                    /*super.onReceivedError(view, errorCode, description, failingUrl)*/

                }

                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    if ((url.contains(".pdf") || url.contains(".doc") || url.contains(".docx") || url.contains(
                            ".pptx"
                        ) || url.contains(".xlsx") || url.contains(".pub") || url.contains(".rar") || url.contains(
                            ".zip"
                        ) || url.contains(".ppt"))
                    ) {
                        //getting file name from url
                        val filename = url.substring(url.lastIndexOf("/", url.length))
                        //DownloadManager.Request created with url.
                        val request = DownloadManager.Request(Uri.parse(url))
                        //cookie
                        val cookie = CookieManager.getInstance().getCookie(url)
                        //Add cookie and User-Agent to request
                        request.addRequestHeader("Cookie", cookie)
                        //file scanned by MediaScannar
                        request.allowScanningByMediaScanner()
                        request.setDescription("Download file...")
                        //Download is visible and its progress, after completion too.
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        //DownloadManager created
                        val downloadmanager =
                            activity?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                        //Saving file in Download folder
                        request.setDestinationInExternalPublicDir(
                            Environment.DIRECTORY_DOWNLOADS,
                            filename
                        )
                        //download enqued
                        downloadmanager.enqueue(request)

                        Toast.makeText(context, "Downloading file", Toast.LENGTH_SHORT).show()
                    } else if (url.startsWith("tel:")) {
                        val intent = Intent(
                            Intent.ACTION_DIAL,
                            Uri.parse(url)
                        )
                        startActivity(intent)
                    } else if (url.startsWith("mailto:")) {
                        val body = "Body of message."
                        val mail = Intent(Intent.ACTION_SEND)
                        mail.type = "application/octet-stream"
                        mail.putExtra(Intent.EXTRA_EMAIL, arrayOf(url.substring(7)))
                        mail.putExtra(Intent.EXTRA_SUBJECT, "Subject")
                        mail.putExtra(Intent.EXTRA_TEXT, body)
                        startActivity(mail)
                        return true
                    } else {
                        binding.wvvs.loadUrl(url)
                    }
                    return true
                }
            }
            binding.wvvs.setDownloadListener(object : DownloadListener {
                override fun onDownloadStart(
                    url: String, userAgent: String,
                    contentDisposition: String, mimetype: String,
                    contentLength: Long
                ) {

                    //getting file name from url
                    val filename = URLUtil.guessFileName(url, contentDisposition, mimetype)
                    //DownloadManager.Request created with url.
                    val request = DownloadManager.Request(Uri.parse(url))
                    //cookie
                    val cookie = CookieManager.getInstance().getCookie(url)
                    //Add cookie and User-Agent to request
                    request.addRequestHeader("Cookie", cookie)
                    request.addRequestHeader("User-Agent", userAgent)
                    //file scanned by MediaScannar
                    request.allowScanningByMediaScanner()
                    request.setDescription("Download file...")
                    //Download is visible and its progress, after completion too.
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    //DownloadManager created
                    val downloadmanager =
                        activity?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    //Saving file in Download folder
                    request.setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS,
                        filename
                    )
                    //download enqued
                    downloadmanager.enqueue(request)

                    Toast.makeText(context, "Downloading file", Toast.LENGTH_SHORT).show()
                }

            })

            binding.wvvs.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK && event.action == MotionEvent.ACTION_UP && binding.wvvs.canGoBack()) {
                    binding.wvvs.goBack()
                    return@OnKeyListener true
                }
                false
            })
        }
    }

    private class JavaScriptInterface(private val context: Context,
                                      private val viewModel: Prefviewmodel,
                                      private val view: View
    ) {
        private var progressDialog: ProgressDialog? = null

        @JavascriptInterface
        fun toast(toast: String?) {
            Toast.makeText(context, toast, Toast.LENGTH_SHORT).show()
        }

        @JavascriptInterface
        fun logout() {
            viewModel.logout()
            Navigation.findNavController(view).navigate(R.id.login)
        }

        @JavascriptInterface
        fun alert(show: String?) {
            val alert = AlertDialog.Builder(
                context
            )
            alert.setMessage(show)
            alert.setNeutralButton(
                "ok"
            ) { dialog, which -> }
            alert.show()
        }

        @JavascriptInterface
        fun call(no: String) {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$no")
            context.startActivity(intent)
        }

        @JavascriptInterface
        fun email(em: String?) {
            val emailIntent = Intent(
                Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", em, null
                )
            )
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject")
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Body")
            context.startActivity(Intent.createChooser(emailIntent, "Send email..."))
        }

        @JavascriptInterface
        fun showprogress(m: String?) {
            progressDialog = ProgressDialog(context)
            progressDialog!!.setMessage(m)
            progressDialog!!.show()
            progressDialog!!.setCancelable(false)
        }

        @JavascriptInterface
        fun hideprogress() {
            progressDialog!!.dismiss()
        }



    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}