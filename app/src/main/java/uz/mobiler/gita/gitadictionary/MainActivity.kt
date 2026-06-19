package uz.mobiler.gita.gitadictionary

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import uz.mobiler.gita.gitadictionary.adapters.DictionaryAdapter
import uz.mobiler.gita.gitadictionary.data.source.MyAppDatabase
import uz.mobiler.gita.gitadictionary.data.source.entity.DictionaryEntity
import uz.mobiler.gita.gitadictionary.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var binding: ActivityMainBinding

    private val db = MyAppDatabase.getInstance()
    private val allWordsList = mutableListOf<DictionaryEntity>()
    private val adapter by lazy { DictionaryAdapter() }
    private val handler by lazy { Handler(Looper.getMainLooper()) }
    private val REQ_CODE_SPEECH_INPUT = 777
    private var tts: TextToSpeech? = null
    private val viewModel: MyViewModel by viewModels<MyViewModelImpl>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.listLiveData.observe(this, object : Observer<List<DictionaryEntity>> {
            override fun onChanged(value: List<DictionaryEntity>) {
                allWordsList.clear()
                allWordsList.addAll(value)
            }
        })
        viewModel.speakLiveData.observe(this, object : Observer<Boolean> {
            override fun onChanged(value: Boolean) {
                if (value) {
                    promptSpeechInput()
                }
            }
        })
        viewModel.searchLiveData.observe(this, object : Observer<String> {
            override fun onChanged(value: String) {
                handler.removeCallbacksAndMessages(null)
                if (value == "") {
                    adapter.mySubmitList(allWordsList, value)
                } else {
                    handler.postDelayed({
                        Log.d("TTT", "in post delayed scope")
                        adapter.mySubmitList(
                            allWordsList,
                            value
                        )
                    }, 900)
                }
            }
        })
        viewModel.speechLiveData.observe(this, object : Observer<String> {
            override fun onChanged(value: String) {
                if (value != "") speakOut(value)
            }
        })

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter.submitList(allWordsList)

        adapter.setFavouriteEditListener { item, pos ->
            viewModel.favChange(item)
            adapter.submitList(allWordsList)
            for (i in allWordsList.indices) {
                if (allWordsList[i].id == item.id) {
                    adapter.notifyItemChanged(i)
                    break
                }
            }
        }
        tts = TextToSpeech(this, this)
        adapter.setSpeechBtnListener { item, pos ->
            viewModel.speech(item.english)
        }

        binding.speakImg.setOnClickListener {
            viewModel.speak()
        }

        binding.edSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                viewModel.search(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })
    }

    fun promptSpeechInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")

        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt))

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                this, "Sorry! Your device doesn\\'t support speech input",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQ_CODE_SPEECH_INPUT) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val message = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                binding.edSearch.setText(message!![0]!!)
            }
        }
    }

    fun updateResults(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
        binding.edSearch.setText(s)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language not supported!")
            } else {

            }
        }
    }

    private fun speakOut(etSpeak: String) {
        val text = etSpeak
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    public override fun onDestroy() {

        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }

}