package com.anton.voicetombola

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast
import java.io.File
import java.util.Locale

class MainBridge(private val context: Context, private val webView: WebView) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech = TextToSpeech(context, this)
    private var mediaPlayer: MediaPlayer? = null

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.ITALIAN
        } else {
            Log.e("MainBridge", "Inizializzazione TTS fallita")
        }
    }

    @JavascriptInterface
    fun speak(text: String, lang: String) {
        val locale = if (lang == "en-US") Locale.US else Locale.ITALIAN
        tts.language = locale
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    @JavascriptInterface
    fun playAudio(fileName: String) {
        try {
            stopAudio()
            
            // Verifichiamo se esiste una versione personalizzata in internal storage
            val pureName = fileName.substringAfterLast("/")
            val customFile = File(File(context.filesDir, "audio"), pureName)
            
            mediaPlayer = MediaPlayer()
            if (customFile.exists()) {
                mediaPlayer?.setDataSource(customFile.absolutePath)
            } else {
                val descriptor = context.assets.openFd(fileName)
                mediaPlayer?.setDataSource(descriptor.fileDescriptor, descriptor.startOffset, descriptor.length)
                descriptor.close()
            }
            
            mediaPlayer?.prepare()
            mediaPlayer?.start()
        } catch (e: Exception) {
            Log.e("MainBridge", "Errore riproduzione audio: $fileName", e)
        }
    }

    @JavascriptInterface
    fun getCustomTitle(n: Int): String? {
        val prefs = context.getSharedPreferences("CustomSmorfia", Context.MODE_PRIVATE)
        return prefs.getString("title_$n", null)
    }

    @JavascriptInterface
    fun stopAudio() {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        } catch (e: Exception) {
            Log.e("MainBridge", "Errore stopAudio", e)
        }
    }

    @JavascriptInterface
    fun checkAndStartListening() {
        (context as? Activity)?.let { activity ->
            if (activity is MainActivity) activity.checkAndStartListening()
            else if (activity is FamilyActivity) activity.checkAndStartListening()
        }
    }

    @JavascriptInterface
    fun startListening() {
        checkAndStartListening()
    }

    @JavascriptInterface
    fun openFamily() {
        val intent = Intent(context, FamilyActivity::class.java)
        context.startActivity(intent)
    }

    @JavascriptInterface
    fun openModSmorfia() {
        val intent = Intent(context, ModSmorfiaActivity::class.java)
        context.startActivity(intent)
    }

    @JavascriptInterface
    fun isSmorfiaCustomized(): Boolean {
        val prefs = context.getSharedPreferences("CustomSmorfia", Context.MODE_PRIVATE)
        return prefs.all.isNotEmpty()
    }

    @JavascriptInterface
    fun restoreSmorfiaFromBackup() {
        try {
            // 1. Ripristina file audio da backup (asset audio -> internal storage audio)
            val audioDir = File(context.filesDir, "audio")
            if (!audioDir.exists()) audioDir.mkdirs()

            val assetManager = context.assets
            val originalFiles = assetManager.list("audio") ?: arrayOf()

            for (fileName in originalFiles) {
                if (fileName.endsWith(".mp3")) {
                    assetManager.open("audio/$fileName").use { input ->
                        File(audioDir, fileName).outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                }
            }

            // 2. Cancella i titoli personalizzati (SharedPreferences)
            val prefs = context.getSharedPreferences("CustomSmorfia", Context.MODE_PRIVATE)
            prefs.edit().clear().apply()

            // 3. Feedback
            (context as? Activity)?.runOnUiThread {
                Toast.makeText(context, "Smorfia Ripristinata!", Toast.LENGTH_SHORT).show()
                webView.reload()
            }
        } catch (e: Exception) {
            Log.e("MainBridge", "Errore nel ripristino: ${e.message}")
        }
    }

    @JavascriptInterface
    fun showAdAndReload() {
        (context as? Activity)?.runOnUiThread {
            webView.reload()
        }
    }
    
    fun onDestroy() {
        tts.stop()
        tts.shutdown()
        stopAudio()
    }
}
