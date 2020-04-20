package jp.techacademy.kosuke.autoslideshowapp

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.provider.MediaStore
import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception


class MainActivity : AppCompatActivity() , View.OnClickListener{

    private val PERMISSIONS_REQUEST_CODE = 100
    var mutableList = mutableListOf<Uri>()
    var cnt:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Android 6.0以降の場合
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // パーミッションの許可状態を確認する
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // 許可されている
                getContentsInfo()
            } else {
                // 許可されていないので許可ダイアログを表示する
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSIONS_REQUEST_CODE)
            }
            // Android 5系以下の場合
        } else {
            getContentsInfo()
        }

        Log.d("Kotlintest", cnt.toString())
        Log.d("Kotlintest", mutableList.count().toString())

        next_button.setOnClickListener(this)
        back_button.setOnClickListener(this)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo()
                }
        }
    }

    override fun onClick(v: View) {
        if(v.id == R.id.next_button){
            if(cnt == mutableList.count()-1){
                cnt = 0
                imageView.setImageURI(mutableList[cnt])
                Log.d("Kotlintest", (cnt+1).toString() + "枚目")

            }else{
                cnt++
                imageView.setImageURI(mutableList[cnt])
                Log.d("Kotlintest", (cnt+1).toString() + "枚目")

            }

        }else if (v.id == R.id.back_button) {
            if(cnt == 0){
                cnt = mutableList.count()-1
                imageView.setImageURI(mutableList[cnt])
                Log.d("Kotlintest", (cnt+1).toString() + "枚目")

            }else{
                cnt--
                imageView.setImageURI(mutableList[cnt])
                Log.d("Kotlintest", (cnt+1).toString() + "枚目")
            }

        }
    }

    private fun getContentsInfo() {
        // 画像の情報を取得する
        val resolver = contentResolver
        val cursor = resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
            null, // 項目(null = 全項目)
            null, // フィルタ条件(null = フィルタなし)
            null, // フィルタ用パラメータ
            null // ソート (null ソートなし)
        )

        if (cursor!!.moveToFirst()) {
            // indexからIDを取得し、そのIDから画像のURIを取得する
            val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
            val id = cursor.getLong(fieldIndex)
            val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

            imageView.setImageURI(imageUri)
            Log.d("Kotlintest","firstView")
        }

        if (cursor!!.moveToFirst()) {
            do {
                // indexからIDを取得し、そのIDから画像のURIを取得する
                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor.getLong(fieldIndex)
                val imageUri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                mutableList.add(imageUri)

            } while (cursor.moveToNext())

            cursor.close()
        }
    }
}
