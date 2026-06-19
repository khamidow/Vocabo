package uz.mobiler.gita.gitadictionary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.mobiler.gita.gitadictionary.data.source.MyAppDatabase
import uz.mobiler.gita.gitadictionary.data.source.entity.DictionaryEntity

class MyViewModelImpl : MyViewModel, ViewModel() {
    private val db = MyAppDatabase.getInstance()
    override val speakLiveData = MutableLiveData(false)
    override val speechLiveData = MutableLiveData("")
    override val searchLiveData = MutableLiveData("")
    override val listLiveData = MutableLiveData(db.getDictionaryDao().getAllWords().toMutableList())

    override fun speak() {
        speakLiveData.value = !speakLiveData.value!!
    }

    override fun speech(word: String) {
        speechLiveData.value = word
    }

    override fun search(searchWord: String) {
        if (searchWord==""){
            listLiveData.value = db.getDictionaryDao().getAllWords().toMutableList()
        }else{
            listLiveData.value = db.getDictionaryDao().getResultByQuery(searchWord).toMutableList()
        }
        searchLiveData.value = searchWord
    }

    override fun favChange(item: DictionaryEntity) {
        db.getDictionaryDao()
            .editFavouriteById(item.id, if (item.isFavourite == 0) 1 else 0)
        search(searchLiveData.value.toString())
    }

}