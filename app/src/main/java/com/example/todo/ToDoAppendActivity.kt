package com.example.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.activity_to_do_append.*
import kotlinx.android.synthetic.main.activity_to_do_append.view.*
import java.util.*

class ToDoAppendActivity : AppCompatActivity() {

    private val realm by lazy {
        Realm.getDefaultInstance()
    }

    var id :String?=null

    var adapter: TodoAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do_append)


        // ToDoのリストを表示するAdapterを新しくもう一つ作って設定
        adapter = TodoAdapter(this, object: TodoAdapter.OnItemClickListner{
            override fun onItemClick(item: TaskData) {
                // SecondActivityに遷移するためのIntent
                val intent = Intent(applicationContext, TaskAppendActivity::class.java)
                // RecyclerViewの要素をタップするとintentによりSecondActivityに遷移する
                // また，要素のidをSecondActivityに渡す
                intent.putExtra("id", item.id)
                startActivity(intent)
            }
        })

        // RecyclerViewの変数を作成してレイアウトを決定し，上で作ったadapterをセット
        val recyclerView: RecyclerView = findViewById(R.id.RecyclerView1)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        //Intent.getStringExtraでMainActivityからのIDを受け取る
        id=intent.getStringExtra("id")

        //RealmでIDを元にデータを引っ張ってきてdataに代入
        if(id==null){
            id = UUID.randomUUID().toString()
            realm.executeTransaction {
                val item = it.createObject(SaveData::class.java, id)
            }
        }else{
            val item = realm.where(SaveData::class.java).equalTo("id", id).findFirst()

            if(item!=null){
                findViewById<EditText>(R.id.editText).setText(item.title)
                adapter?.setList(item.content.toList())
            }
        }

        findViewById<Button>(R.id.button).setOnClickListener {
            val taskId = UUID.randomUUID().toString()
            realm.executeTransaction {
                val task = it.createObject(TaskData::class.java, taskId)

                // 作ったアイテムをSaveDataのcontentにAdd
                val item = realm.where(SaveData::class.java).equalTo("id", id).findFirst()
                item?.content?.add(task)

                val intent = Intent(applicationContext, TaskAppendActivity::class.java)
                intent.putExtra("id", taskId)
                startActivity(intent)


            }


        }


        findViewById<FloatingActionButton>(R.id.floatingActionButton3).setOnClickListener {
            finish()
        }

    }

    override fun onResume() {
        super.onResume()
        Log.d("id", id.toString())
        val data = realm.where(SaveData::class.java).equalTo("id", id).findFirst() ?: return

        for(item in data.content){
            Log.d("item", item.title)
        }
        adapter?.setList(data.content.toList())
    }

    override fun onPause() {
        realm.executeTransaction {
            val item = realm.where(SaveData::class.java).equalTo("id", id).findFirst()
            item?.title = findViewById<EditText>(R.id.editText).text.toString()
        }
        super.onPause()
    }

    // Activity終了時にralmを終了
    override fun onDestroy() {
        realm.close()
        super.onDestroy()
    }
}
