package com.cleanarch.features.wikientry.presentation


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.cleanarch.app.CleanArchApp
import com.cleanarch.databinding.ActivityMainBinding

/*
 * Copyright (C) 2017 Naresh Gowd Idiga
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

class WikiEntryActivity : AppCompatActivity() {

    private lateinit var wikiEntryViewModel: WikiEntryViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // WikiEntry feature component scope start here
        (application as CleanArchApp).buildWikiEntryComponent()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.submitButton.setOnClickListener(submitButtonOnClickListener)

        wikiEntryViewModel = ViewModelProvider(this)[WikiEntryViewModel::class.java]
        wikiEntryViewModel.wikiEntry.observe(this) {
            Log.d(TAG, "received update for wikiEntry")
            binding.entryDetails.text = it?.extract
        }
        wikiEntryViewModel.showProgress.observe(this) {
            if (it) binding.progressBar.show() else binding.progressBar.hide()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // WikiEntry feature component scope ends here
        (application as CleanArchApp).releaseWikiEntryComponent()
    }

    private val submitButtonOnClickListener = View.OnClickListener { v ->
        val inputMethodManager =
            this@WikiEntryActivity.baseContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
        wikiEntryViewModel.loadWikiEntry(binding.entryTitle.text.toString())
    }

    companion object {
        private val TAG = WikiEntryActivity::class.java.simpleName
    }
}
