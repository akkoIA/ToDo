package com.example.todo

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class SaveData(
    @PrimaryKey open var id: String = UUID.randomUUID().toString(),
    open var title: String = "",
    open var content: RealmList<TaskData> = RealmList()
): RealmObject()

open class TaskData(
    @PrimaryKey open var id: String = UUID.randomUUID().toString(),
    open var title:String="",
    open var content:String=""
): RealmObject()