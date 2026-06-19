package uz.mobiler.gita.gitadictionary.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import uz.mobiler.gita.gitadictionary.MainActivity

class LanguageDetailsReceiver(var mSSL: MainActivity) : BroadcastReceiver() {
    var mLanguages: MutableList<String?>?

    init {
        mLanguages = ArrayList<String?>()
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val extras = getResultExtras(true)
        mLanguages = extras.getStringArrayList(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES)
        if (mLanguages == null) {
            mSSL.updateResults("No voice data found.")
        } else {
            var s = "\nQo'llab-quvatlanadigan tillar ro'yxati:\n"
            for (i in mLanguages!!.indices) {
                s += (mLanguages!!.get(i) + ", ")
            }
            s += "\n Jami " + mLanguages!!.size + " ta"
            mSSL.updateResults(s)
        }
    }
}