package com.bignerdranch.android.photogallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class PhotoGalleryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_gallery)

        // if bundle is null, fresh launch, i.e. no fragment
        val isFragmentContainerEmpty = savedInstanceState == null

        // FM automatically creates & adds hosted fragment back to activity after config
        // change or sys-init process death
        if (isFragmentContainerEmpty) {
            // add only if it's empty
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, PhotoGalleryFragment.newInstance())
                .commit()
        }
    }
}