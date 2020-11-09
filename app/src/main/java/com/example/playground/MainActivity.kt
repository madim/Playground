package com.example.playground

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Environment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import java.io.File

private const val DOWNLOAD_URL = "https://app.krisha.kz/data/dogovor-arendy.pdf"
private const val FILENAME = "dogovor-arendy.pdf"
private const val WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1001

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val onCompleteReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action != DownloadManager.ACTION_DOWNLOAD_COMPLETE) return
            val downloadId = intent.extras?.getLong(DownloadManager.EXTRA_DOWNLOAD_ID) ?: return

            val query = DownloadManager.Query()
            query.setFilterById(downloadId)
            val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
            val cursor = manager?.query(query)
            cursor?.use { c ->
                if (!c.moveToFirst()) return

                val status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    val filepath = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
                    shareFile(File(filepath))
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(onCompleteReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(onCompleteReceiver)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val permissionGranted = grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE && permissionGranted) {
            downloadPdf()
        }
    }

    fun onDownloadClicked(view: View) {
        val pdfFile = getPdfFile()
        if (pdfFile.exists()) {
            shareFile(pdfFile)

            return
        }

        if (!checkWriteExternalStoragePermission()) return

        downloadPdf()
    }

    private fun checkWriteExternalStoragePermission(): Boolean {
        val permissionManager = DefaultPermissionManager(this)
        if (!permissionManager.isPermissionGranted(WRITE_EXTERNAL_STORAGE)) {
            permissionManager.askForPermission(
                WRITE_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE_REQUEST_CODE
            )

            return false
        }

        return true
    }

    private fun getPdfFile(): File {
        val documentsDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)

        return File(documentsDir, FILENAME)
    }

    private fun downloadPdf() {
        val downloadManager = getSystemService(DownloadManager::class.java)
        val request = DownloadManager.Request(DOWNLOAD_URL.toUri()).apply {
            allowScanningByMediaScanner()
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS, FILENAME)
        }
        downloadManager.enqueue(request)
    }

    private fun shareFile(file: File) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        val authority = "${BuildConfig.APPLICATION_ID}.fileprovider"
        val contentUri = FileProvider.getUriForFile(this, authority, file)
        intent.setDataAndType(contentUri, "application/pdf")
        intent.putExtra(Intent.EXTRA_STREAM, contentUri);
        startActivity(Intent.createChooser(intent, "Поделиться:"))
    }
}
