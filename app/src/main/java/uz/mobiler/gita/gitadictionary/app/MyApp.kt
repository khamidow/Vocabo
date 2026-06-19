package uz.mobiler.gita.gitadictionary.app

import android.app.Application
import uz.mobiler.gita.gitadictionary.data.source.MyAppDatabase

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        MyAppDatabase.init(this)
    }
}