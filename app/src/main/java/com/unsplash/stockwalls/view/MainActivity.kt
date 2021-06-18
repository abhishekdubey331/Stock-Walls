package com.unsplash.stockwalls.view

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.unsplash.stockwalls.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel.fetchUsers()

        lifecycleScope.launchWhenStarted {
            mainViewModel.photoFetchEvent.collect { event ->
                when (event) {

                    is MainViewModel.PhotoFetchEvent.Success -> {
                        binding.progressBar.isVisible = false
                        binding.hiltTextView.setTextColor(Color.BLACK)
                        binding.hiltTextView.text = event.unsplashPhoto?.first()?.id
                    }

                    is MainViewModel.PhotoFetchEvent.Failure -> {
                        binding.progressBar.isVisible = false
                        binding.hiltTextView.setTextColor(Color.RED)
                        binding.hiltTextView.text = event.errorText
                    }

                    is MainViewModel.PhotoFetchEvent.Loading -> {
                        binding.progressBar.isVisible = true
                    }

                    else -> Unit
                }
            }
        }
    }
}