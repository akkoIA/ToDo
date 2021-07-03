package com.example.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_to_do_append.*
import java.util.*

class ToDoAppendActivity : AppCompatActivity() {

    private val realm by lazy {
        Realm.getDefaultInstance()
    }
    var adapter: MyListAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do_append)

        //Intent.getStringExtraでMainActivityからのIDを受け取る

        //RealmでIDを元にデータを引っ張ってきてdataに代入

        // ToDoのリストを表示するAdapterを新しくもう一つ作って設定
        adapter = MyListAdapter(this, object: MyListAdapter.OnItemClickListner{
            override fun onItemClick(item: SaveData) {
                // SecondActivityに遷移するためのIntent
                val intent = Intent(applicationContext, ToDoAppendActivity::class.java)
                // RecyclerViewの要素をタップするとintentによりSecondActivityに遷移する
                // また，要素のidをSecondActivityに渡す
                intent.putExtra("id", item.id)
                startActivity(intent)
            }
        })


    }

    // Activity終了時にralmを終了
    override fun onDestroy() {
        realm.close()
        super.onDestroy()
    }
}
