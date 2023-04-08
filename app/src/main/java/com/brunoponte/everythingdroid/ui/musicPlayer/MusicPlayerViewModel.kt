package com.brunoponte.everythingdroid.ui.musicPlayer

import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.brunoponte.everythingdroid.ApplicationClass
import com.brunoponte.everythingdroid.domain.musicPlayer.model.Song
import com.brunoponte.everythingdroid.helpers.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.io.BufferedInputStream
import java.io.File
import java.lang.Exception
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class MusicPlayerViewModel
@Inject
constructor(
    val applicationClass: ApplicationClass
) : AndroidViewModel(applicationClass) {

    val songs: MutableLiveData<List<Song>> = MutableLiveData(listOf())

    init {
        songs.value = Constants.DefaultSongs

        viewModelScope.launch {
            fillDownloadedSongPaths()
        }
    }

    fun onPlay(song: Song) {
        val songList = mutableListOf<Song>()
        songs.value?.forEach {
            val songCopy = it.copy()
            if (it == song) { songCopy.isPlaying = true }

            songList.add(songCopy)
        }

        songs.value = songList
    }

    fun onPause(song: Song) {
        val songList = mutableListOf<Song>()
        songs.value?.forEach {
            val songCopy = it.copy()
            if (it == song) { songCopy.isPlaying = false }

            songList.add(songCopy)
        }

        songs.value = songList
    }

    fun onDownload(song: Song) {
        viewModelScope.launch {
            val songList1 = mutableListOf<Song>()
            songs.value?.forEach {
                val songCopy = it.copy()
                if (it.id == song.id) { songCopy.isDownloading = true }

                songList1.add(songCopy)
            }

            songs.value = songList1

            val savedPath = downloadSong(song)

            val songList2 = mutableListOf<Song>()
            songs.value?.forEach {
                val songCopy = it.copy()
                if (it.id == song.id) {
                    songCopy.isDownloading = false
                    songCopy.savedPath = savedPath
                }

                songList2.add(songCopy)
            }

            songs.value = songList2
        }
    }

    private suspend fun downloadSong(song: Song) : String {
        return withContext(Dispatchers.IO) {
            val filename = "song_${song.id}.mp3"

            val file = File(filename)
            if(file.exists()) {
                return@withContext filename
            }

            try {
                val url  = URL(song.url)
                val connection = url.openConnection()
                connection.connect()
                val inputStream = BufferedInputStream(url.openStream())
                val outputStream = getApplication<ApplicationClass>().applicationContext.openFileOutput(
                    filename,
                    Context.MODE_PRIVATE)
                val data = ByteArray(1024)
                var count = inputStream.read(data)
                var total = count
                while (count != -1) {
                    outputStream.write(data, 0, count)
                    count = inputStream.read(data)
                    total += count
                }
                outputStream.flush()
                outputStream.close()
                inputStream.close()

                Log.d(MusicPlayerViewModel::class.java.simpleName, "Finished saving $filename to internal storage")

                return@withContext filename
            } catch (e: Exception) {
                Log.d(MusicPlayerViewModel::class.java.simpleName, e.message.toString())
                return@withContext ""
            }
        }
    }

    private suspend fun fillDownloadedSongPaths() {
        // TODO: When saving songs in cache, this method won't be necessary, since this data will be saved persistently

        val fileList = applicationClass.applicationContext.fileList().filter {
            it.contains(".mp3")
        }

        val songList = mutableListOf<Song>()
        songs.value?.forEach {
            val songCopy = it.copy()
            val targetFilename = "song_${songCopy.id}.mp3"
            if (fileList.contains(targetFilename)) { songCopy.savedPath = targetFilename }
            songList.add(songCopy)
        }

        songs.value = songList
    }
}