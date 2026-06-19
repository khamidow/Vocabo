package uz.mobiler.gita.gitadictionary.data.source.dao

import androidx.room.Dao
import androidx.room.Query
import uz.mobiler.gita.gitadictionary.data.source.entity.DictionaryEntity

@Dao
interface DictionaryDao {

    @Query("SELECT * FROM dictionary")
    fun getAllWords(): List<DictionaryEntity>

    @Query("SELECT * FROM dictionary WHERE dictionary.english LIKE '%'||:query||'%'")
    fun getResultByQuery(query: String): List<DictionaryEntity>

    @Query("UPDATE dictionary SET is_favourite = :number WHERE id = :id")
    fun editFavouriteById(id: Int, number: Int)

    @Query("SELECT * FROM dictionary WHERE id = :id")
    fun getItemById(id:Int) : DictionaryEntity
}