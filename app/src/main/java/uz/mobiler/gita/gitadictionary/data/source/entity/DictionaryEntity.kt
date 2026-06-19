package uz.mobiler.gita.gitadictionary.data.source.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dictionary")
data class DictionaryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val english: String,
    val type: String,
    val transcript: String,
    val uzbek: String,
    val countable: String,
    @ColumnInfo(name = "is_favourite")
    val isFavourite: Int
)