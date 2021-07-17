package com.example.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_task_append.*
import java.util.*

class TaskAppendActivity : AppCompatActivity() {

    private val realm by lazy {
        // by lazyは多用すると地獄を見るのでRealmの宣言以外には使わないこと．特にfindViewById()はby lazyで設定すると落ちる原因になる
        Realm.getDefaultInstance()
    }

    // onCreate(), onResume()で利用するためクラス内に記述


    var data: SaveData? = null

    var id :String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_append)
        Realm.init(this)

        //Intent.getStringExtraでMainActivityからのIDを受け取る
        id=intent.getStringExtra("id")

        //RealmでIDを元にデータを引っ張ってきてdataに代入
        if(id==null){
            finish()
        }else{
            val item = realm.where(TaskData::class.java).equalTo("id", id).findFirst()

            if(item!=null){
                findViewById<EditText>(R.id.textView3).setText(item.title)
                findViewById<EditText>(R.id.textView4).setText(item.content)
            }
        }



        floatingActionButton2.setOnClickListener() {
            if (textView3.text != null) {
                finish()
            }
        }
    }

    override fun onPause() {
        realm.executeTransaction {
            val item = realm.where(TaskData::class.java).equalTo("id", id).findFirst()
            item?.title = findViewById<EditText>(R.id.textView3).text.toString()
            item?.content = findViewById<EditText>(R.id.textView4).text.toString()
        }
        super.onPause()
    }
    override fun onResume() {
        super.onResume()
        // IDから取得したSaveDataの中のToDoDataをセットする
        //adapter?.setList(data?.content)
    }

    // Activity終了時にRealmを終了すること
    override fun onDestroy() {
        realm.close()
        super.onDestroy()
    }

//    // データ作成関数
//    fun createSaveData(title: String, content: String, details: String, icon: Int){
//        realm.executeTransaction {
//            val item = it.createObject(SaveData::class.java, UUID.randomUUID().toString())
//            item.title = title
//            // item.content = content
//            // item.details = details
//            //item.icon = icon
//        }
//    }
//
//    // Realmの全データを削除する関数のサンプル
//    fun deleteAll(){
//        realm.executeTransaction { realm.deleteAll() }
//    }
//
//    // Realmの特定のデータを削除する関数のサンプル
//    fun delete(id: String){
//        realm.executeTransaction { it.where(SaveData::class.java).equalTo("id", id).findFirst()?.deleteFromRealm() }
//    }
}
