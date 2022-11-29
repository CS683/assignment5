package edu.bu.projectportal.datalayer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="projects")
data class Project(
    var title: String,
    var authors: String,
    var links: String,
    var favorite:Boolean) {
    @PrimaryKey(autoGenerate = true)
    var id:Int =0
}