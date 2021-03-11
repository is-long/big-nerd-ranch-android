package com.bignerdranch.android.beatbox

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

// let VM comms w/ layout file when change occurs by implementing Observable()
class SoundViewModel(private val beatBox: BeatBox): BaseObservable() {
    fun onButtonClicked() {
        sound?.let {
            beatBox.play(it)
        }
    }

    var sound: Sound? = null
        set(sound) {
            field = sound
            // call each time property value change
            // the binding class then reruns to repopulate view
            notifyChange()

            // use this one if only specific (not all) value changes
            // notifyPropertyChanged()
        }

    @get:Bindable
    val title: String?
        get() = sound?.name
}