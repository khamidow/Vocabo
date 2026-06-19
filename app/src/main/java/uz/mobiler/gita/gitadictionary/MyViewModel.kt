package uz.mobiler.gita.gitadictionary

import androidx.lifecycle.LiveData
import androidx.room.Entity
import uz.mobiler.gita.gitadictionary.data.source.entity.DictionaryEntity

interface MyViewModel {
    val speakLiveData: LiveData<Boolean>
    val speechLiveData: LiveData<String>
    val searchLiveData: LiveData<String>
    val listLiveData: LiveData<MutableList<DictionaryEntity>>
    fun speak()
    fun speech(word:String)
    fun search(searchWord: String)
    fun favChange(item: DictionaryEntity)

}