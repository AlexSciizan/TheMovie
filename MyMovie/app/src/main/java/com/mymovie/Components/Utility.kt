package com.mymovie.Components

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import android.os.AsyncTask
import android.text.TextWatcher
import android.text.Editable
import com.koushikdutta.ion.Ion
import android.app.Activity
import android.app.Dialog
import android.content.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.PorterDuff
import android.webkit.MimeTypeMap
import kotlin.Throws
import android.provider.MediaStore
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.mymovie.R
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.view.animation.AccelerateInterpolator
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.lang.NumberFormatException
import java.nio.charset.StandardCharsets
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import java.util.concurrent.ExecutionException

class Utility {
    fun findKey(sharedPreferences: SharedPreferences, value: String): String? {
        for ((key, value1) in sharedPreferences.all) {
            if (value == value1) {
                return key
            }
        }
        return null
    }

    @SuppressLint("CommitPrefEdits")
    fun removeKeyAt(sf: SharedPreferences, idx: Int) {
        val allPrefs = sf.all
        val keys: Set<String> = allPrefs.keys
        var loop = 0
        for (key in keys) {
            if (loop == idx) {
                sf.edit().remove(key).apply()
            } else {
                Logd("hapus", key + " " + Objects.requireNonNull(allPrefs[key]).toString())
            }
            loop++
        }
    }

    fun getAllSf(sf: SharedPreferences) {
        val allPrefs = sf.all
        val keys: Set<String> = allPrefs.keys
        for (key in keys) {
            Logd("open", key + " " + Objects.requireNonNull(allPrefs[key]).toString())
        }
    }

    fun saveImageViewToDrawable(context: Context?, Icon: Array<ImageView>, drawableIcon: Array<Drawable?>) {
        val IconTemp = arrayOfNulls<ImageView>(Icon.size)
        for (iconIndex in Icon.indices) {
            IconTemp[iconIndex] = ImageView(context)
            IconTemp[iconIndex] = Icon[iconIndex]
            drawableIcon[iconIndex] = Icon[iconIndex].background
        }
    }

    fun getMyWidth(percent: Int, context: Context): Int {
        val dm = DisplayMetrics()
        (context as AppCompatActivity).windowManager.defaultDisplay.getMetrics(dm)
        val width = dm.widthPixels
        Logd("getMyWidth", percent.toString() + " * " + width + " " + percent * width)
        Logd("==", "" + percent * width / 100)
        return percent * width / 100
    }

    fun getMyHeight(percent: Int, context: Context): Int {
        val dm = DisplayMetrics()
        (context as AppCompatActivity).windowManager.defaultDisplay.getMetrics(dm)
        val height = dm.heightPixels
        Logd("getMyHeight", percent.toString() + " * " + height + " " + percent * height)
        Logd("==", "" + percent * height / 100)
        return percent * height / 100
    }

    internal abstract class AsyncTaskDialog : AsyncTask<Void?, Void?, Void?> {
        var dialog: Dialog? = null

        @SuppressLint("StaticFieldLeak")
        var activity: AppCompatActivity? = null
        var finish = false

        constructor() {}
        constructor(dialogQ: Dialog?, activityQ: AppCompatActivity?) {
            dialog = dialogQ
            activity = activityQ
        }

        override fun onPreExecute() {
            super.onPreExecute()
            dialog!!.show()
        }

        override fun doInBackground(vararg params: Void?): Void? {
            dialog!!.context.startActivity(Intent(dialog!!.context, activity!!.javaClass))
            finish = true
            return null
        }

        override fun onPostExecute(unused: Void?) {
            if (finish) {
                dialog!!.dismiss()
            }
            super.onPostExecute(unused)
        }
    }

    fun setTimer(et_timer: EditText) {
        val s_time = arrayOfNulls<String>(1)
        et_timer.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable) {
                et_timer.removeTextChangedListener(this)
                try {
                    s_time[0] = s.toString()
                    val int_val: Int
                    if (s_time[0]!!.contains(":")) {
                        s_time[0] = s_time[0]!!.replace(":".toRegex(), "")
                    }
                    int_val = s_time[0]!!.toInt()
                    val df = NumberFormat.getInstance(Locale.US) as DecimalFormat
                    df.applyPattern("##,##")
                    //setting text after format to EditText
                    et_timer.setText(df.format(int_val.toLong()).replace(",".toRegex(), ":"))
                    et_timer.setSelection(et_timer.text.length)
                    if (et_timer.text.length == 5) {
                        val a = et_timer.text.toString().split(":").toTypedArray()
                        if (a[1].toInt() >= 60) {
                            a[1] = "00"
                        }
                        et_timer.setText(a[0] + a[1])
                        //                        sf.edit().putString("detik", "" + 60 * Integer.parseInt(a[0]) + Integer.parseInt(a[1])).apply();
                    }
                } catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                }
                et_timer.addTextChangedListener(this)
            }
        })
    }

    companion object {
        private const val TAG = " \t "
        fun DialogUpdate(context: Context) {
            val dialog = AlertDialog.Builder(context)
                    .setTitle("New Quiz version is available on Play Store")
                    .setMessage("Download your Quiz apps in Play Store.")
                    .setPositiveButton("Update") { dialog1: DialogInterface?, which: Int ->
                        try {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.packageName)))
                        } catch (anfe: ActivityNotFoundException) {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + context.packageName)))
                        }
                    }.create()
            dialog.setCancelable(false)
            dialog.show()
        }

        fun getVersion(context: Context): String {
            var d = ""
            var s = ""
            try {
                if (cekKoneksi(context, false, true)) {
                    d = Ion.with(context)
                            .load("https://play.google.com/store/apps/details?id=" + context.packageName + "&hl=en")
                            .setHeader("Content-Type", "application/x-www-form-urlencoded")
                            .setHeader("Accept", "application/json")
                            .asString().get()
                    val idx = d.indexOf("Current Version</div><span class=\"htlgb\"><div class=\"IQ1z0d\"><span class=\"htlgb\">")
                    var siku = 1
                    if (idx != -1) {
                        var i = idx
                        while (i < idx + 100) {
                            if (d[i] == '>') {
                                siku++
                            }
                            if (siku == 5) {
                                s += "" + d[i + 1]
                                if (d[i] == '<') {
                                    s = s.replace("</", "")
                                    i = idx + 100
                                }
                            }
                            i++
                        }
                    }
                }
            } catch (e: ExecutionException) {
                e.printStackTrace()
                Toast.makeText(context, "Anda belum terhubung dengan Jaringan", Toast.LENGTH_LONG).show()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            Logd(TAG, "versi barunya $s")
            return s
        }

        fun deleteSharedPreference(context: Context, name: String?) {
            /*cara 1*/ //PreferenceManager.getDefaultSharedPreferences(context).edit().clear().apply();
            /*cara 2*/
            context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().clear().apply()
        }

        fun deleteAllSharedPreferenceWO(context: Context) {
            val root = context.filesDir.parentFile
            val sharedPreferencesFileNames = File(root, "shared_prefs").list()!!
            for (fileName in sharedPreferencesFileNames) {
                if (11 == fileName.length) {
                    Logd("TAG", "file name sf : " + fileName + " " + fileName.length)
                    context.getSharedPreferences(fileName.replace(".xml", ""), Context.MODE_PRIVATE).edit().clear().apply()
                }
            }
        }

        fun isTablet(activity: Activity): Boolean {
            val metrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(metrics)
            val xInches = metrics.widthPixels / metrics.xdpi
            val yInches = metrics.heightPixels / metrics.ydpi
            val diagonalInches = Math.sqrt((xInches * xInches + yInches * yInches).toDouble())
            return diagonalInches >= 7
        }

        fun encodeTobase64(image: Bitmap): String {
            val baos = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val b = baos.toByteArray()
            return Base64.encodeToString(b, Base64.DEFAULT)
        }

        fun dpToPx(dp: Int, context: Context): Int {
            val density = context.resources.displayMetrics.density
            return Math.round(dp.toFloat() * density)
        }

        fun decodeBase64(input: String?): Bitmap {
            val decodedByte = Base64.decode(input, 0)
            return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)
        }

        fun SaveUrltoBase64(context: Context?, url: String?) {
            // encodeTobase64(Ion.with(context).load(url).setTimeout(30_000).asBitmap().get());
        }

        fun inputStringToEditText(editText: EditText, string: String) {
            if (!string.contains("null") || !string.isEmpty()) {
                editText.setText(string)
            }
        }

        fun inputStringToTextView(textView: TextView, string: String) {
            if (!string.contains("null") || !string.isEmpty()) {
                textView.text = string
            }
        }

        fun minimizeActivity(context: Context) {
            val i = Intent(Intent.ACTION_MAIN)
            i.addCategory(Intent.CATEGORY_HOME)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(i)
        } /*namaFile = /storage/sdcard1/folder/file.pdf*/

        fun openFile(c: Context, namaFile: String?) {
            val myMime = MimeTypeMap.getSingleton()
            val newIntent = Intent(Intent.ACTION_VIEW)
            val mimeType = myMime.getMimeTypeFromExtension("pdf")
            val filePath = File(namaFile) /*+"//DIR//"*/
            newIntent.setDataAndType(Uri.fromFile(filePath), mimeType)
            newIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            try {
                c.startActivity(newIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(c, "No handler for this type of file.", Toast.LENGTH_LONG).show()
            }
        }

        @Throws(IOException::class)
        fun getMyImage(context: Context?, path: String?): Bitmap? {
            //bitmap = Ion.with(context).load("http://192.168.20.20/images/produk/medium/ABS-JG-AA%2001-58539433b03f9.jpg")
            //.setTimeout(30_000).withBitmap().asBitmap().get();
            return null as Bitmap?
        }

        fun getResizedBitmap(bm: Bitmap, newWidth: Int): Bitmap {
            val width = bm.width
            val height = bm.height
            val scaleWidth = newWidth.toFloat() / width
            val aspectRatio = bm.width / bm.height.toFloat()
            val newheight = Math.round(newWidth / aspectRatio)
            val scaleHeight = newheight.toFloat() / height
            val matrix = Matrix()
            matrix.postScale(scaleWidth, scaleHeight)
            val resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false)
            bm.recycle()
            return resizedBitmap
        }

        fun scanMedia(c: Context, path: String?) {
            val file = File(path)
            val uri = Uri.fromFile(file)
            val scanFileIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri)
            c.sendBroadcast(scanFileIntent)
        }

        @Deprecated("")
        fun getPathQ(uri: Uri?, activity: Activity): String {
            val projection = arrayOf(MediaStore.MediaColumns.DATA)
            val cursor = activity.managedQuery(uri, projection, null, null, null)
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        }

        @JvmStatic
        fun setupEventViewPager(viewPager: ViewPager, btn: Array<TextView?>) {
            viewPager.addOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
                @Deprecated("")
                override fun onPageSelected(position: Int) {
                    btn[position]?.setBackgroundResource(R.drawable.round_rec_background_button_white1)
                    for (i in btn.indices) {
                        if (i != position) btn[i]?.setBackgroundResource(R.drawable.round_rec_background_button_white2)
                    }
                } /*((ScrollView)((LinearLayout)viewPager.getChildAt(0)).getChildAt(0)).setScrollY(0);*/

                override fun onPageScrollStateChanged(state: Int) {
                    if (state != ViewPager.SCROLL_STATE_IDLE) {
                        val childCount = viewPager.childCount
                        for (i in 0 until childCount) viewPager.getChildAt(i).setLayerType(View.LAYER_TYPE_NONE, null)
                    }
                }
            })
        }

        @JvmStatic
        fun setupButtonGenre(vp: ViewPager, btn: TextView, posisi_btn: Int) {
            btn.setOnClickListener { v: View? -> vp.currentItem = posisi_btn }
        }

        fun writeImage(bitmap: Bitmap, destPath: String?, quality: Int) {
            var isFileCreated = false
            try {
                try {
                    val file = File(destPath)
                    if (file.exists()) {
                        file.delete()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                try {
                    val file = File(destPath)
                    if (!file.exists()) {
                        if (!Objects.requireNonNull(file.parentFile).exists()) {
                            file.parentFile.mkdirs()
                        }
                        isFileCreated = file.createNewFile()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                if (isFileCreated) {
                    var out: FileOutputStream? = FileOutputStream(destPath)
                    if (bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)) {
                        out!!.flush()
                        out.close()
                        out = null
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        fun dp2px(context: Context, value: Float): Int {
            val scale = context.resources.displayMetrics.densityDpi.toFloat()
            return (value * (scale / 160) + 0.5f).toInt()
        }

        fun getJSONFromAsset(context: Context, nama_file: String?): String? {
            var json: String? = null
            json = try { /*"nama_file.json"*/
                val `is` = context.assets.open(nama_file!!)
                val size = `is`.available()
                val buffer = ByteArray(size)
                `is`.read(buffer)
                `is`.close()
                String(buffer, StandardCharsets.UTF_8)
            } catch (ex: IOException) {
                ex.printStackTrace()
                return null
            }
            return json
        }

        /*  Logd(TAG, "1 loop i " + i);
                    Logd(TAG, "je:" + je);
                    Logd(TAG, "jo:" + jo_str);
                    Logd(TAG, "cari:" + arrS[i]);*/

        fun getJSONFromAPI(context: Context?): String {
            val s = ""
            // s = Ion.with(context).load("https://api.myjson.com/bins/gsq5w").setTimeout(30_000).asJsonObject().get().toString();
            Logd(TAG, "getJSONFromAPI: $s")
            return s
        }

        fun inFromRightAnimation(): Animation {
            val inFromRight: Animation = TranslateAnimation(
                    Animation.RELATIVE_TO_PARENT, +1.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f)
            inFromRight.duration = 500
            inFromRight.interpolator = AccelerateInterpolator()
            return inFromRight
        }

        fun outToLeftAnimation(): Animation {
            val outtoLeft: Animation = TranslateAnimation(
                    Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, -1.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f)
            outtoLeft.duration = 500
            outtoLeft.interpolator = AccelerateInterpolator()
            return outtoLeft
        }

        fun inFromLeftAnimation(): Animation {
            val inFromLeft: Animation = TranslateAnimation(
                    Animation.RELATIVE_TO_PARENT, -1.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f)
            inFromLeft.duration = 500
            inFromLeft.interpolator = AccelerateInterpolator()
            return inFromLeft
        }

        fun outToRightAnimation(): Animation {
            val outtoRight: Animation = TranslateAnimation(
                    Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, +1.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f)
            outtoRight.duration = 500
            outtoRight.interpolator = AccelerateInterpolator()
            return outtoRight
        }

        fun dismissDialog(isActivityShow: Boolean, dialogLoading: Dialog) {
            val handler = Handler()
            val detik17 = intArrayOf(0)
            val timer = Timer()
            timer.scheduleAtFixedRate(object : TimerTask() {
                @SuppressLint("DefaultLocale")
                override fun run() {
                    handler.post {
                        if (isActivityShow) {
                            dialogLoading.dismiss()
                            if (detik17[0] == 17) {
                                timer.cancel()
                            }
                            detik17[0]++
                        }
                    }
                }
            }, 1000, 1000)
        }

        fun DialogLoading(context: Context): Dialog {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCanceledOnTouchOutside(false)
            dialog.setContentView(R.layout.dialog_loading)
            val window = dialog.window!!
            window.setBackgroundDrawableResource(android.R.color.transparent)
            val progressBar = dialog.findViewById<ProgressBar>(R.id.progress_bar)
            progressBar.indeterminateDrawable.setColorFilter(context.resources.getColor(R.color.biru), PorterDuff.Mode.SRC_ATOP)
            val lp = window.attributes
            //This makes the dialog take up the full width
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.MATCH_PARENT
            lp.x = 0
            lp.y = 0
            lp.gravity = Gravity.CENTER
            window.attributes = lp
            window.setGravity(Gravity.CENTER)
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            return dialog
        }

        fun ListSf(context: Context): List<String> {
            val list: MutableList<String> = ArrayList()
            val root = context.filesDir.parentFile
            val sf_names = File(root, "shared_prefs").list()
            var s = ""
            val i = 0
            assert(sf_names != null)
            for (fileName in sf_names!!) {
                s = fileName.replace(".xml", "")
                if (s.startsWith("quiz") && !Objects.requireNonNull(context.getSharedPreferences(s, 0).getString("id", ""))?.isEmpty()!!) {
                    list.add(s)
                    Logd("TAG", "list sf $s")
                }
            }
            return list
        }

        fun Logd(x: String?, y: String?) {
            Log.d(x, y!!)
        }

        @JvmStatic
        fun cekKoneksi(context: Context, show_snackbar: Boolean, show_toast: Boolean): Boolean {
            var konek = false /*TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);*/
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager /*NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);*/ /*NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);*/ /*if (wifiNetwork != null && wifiNetwork.isConnected()){*/ /*konek = true;*/ /*Logd("koneksi ", "cekKoneksi wifi " + konek);*/ /*} else if (mobileNetwork != null && mobileNetwork.isConnected()){*/ /*konek = true;Logd("koneksi ", "cekKoneksi hp " + konek);}NetworkInfo activeNetwork = cm.getActiveNetworkInfo();if (activeNetwork != null && activeNetwork.isConnected()){konek = true;Logd("koneksi ", "cekKoneksi jaringan aktif " + konek);}*/
            val isWifiConnected = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.isConnectedOrConnecting
            val is3gConnected = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.isConnectedOrConnecting
            val activeNetworkInfo = cm.activeNetworkInfo
            try {
                Ion.with(context).load("https://www.google.com").asString().get()
                if ((isWifiConnected || is3gConnected) && activeNetworkInfo != null && activeNetworkInfo.isConnected) konek = true
            } catch (e: InterruptedException) {
                e.printStackTrace()
                if (show_snackbar) {
                    noInternetSnackbar(context).show()
                } else if (show_toast) {
                    Toast.makeText(context, "Anda belum terhubung dengan Internet", Toast.LENGTH_SHORT).show()
                }
                return false
            } catch (e: ExecutionException) {
                e.printStackTrace()
                if (show_snackbar) {
                    noInternetSnackbar(context).show()
                } else if (show_toast) {
                    Toast.makeText(context, "Anda belum terhubung dengan Internet", Toast.LENGTH_SHORT).show()
                }
                return false
            }
            return konek
        }

        fun noInternetSnackbar(context: Context): Snackbar {
            val my_view = (context as AppCompatActivity).findViewById<View>(android.R.id.content)
            val snackbar = Snackbar.make(my_view, "No Connection Internet", Snackbar.LENGTH_INDEFINITE)
            snackbar.setAction("X") { snackbar.dismiss() }
            return snackbar
        }
    }
}

