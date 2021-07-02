package com.example.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_task_append.*

class TaskAppendActivity : AppCompatActivity() {

    val realm:Realm= Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_append)
        Realm.init(this)

        floatingActionButton2.setOnClickListener() {
            if (textView3.text != null) {
                val intent = Intent(this, ToDoAppendActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
