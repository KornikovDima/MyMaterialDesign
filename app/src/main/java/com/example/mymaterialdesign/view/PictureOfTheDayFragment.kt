package com.example.mymaterialdesign.view

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.mymaterialdesign.R
import com.example.mymaterialdesign.databinding.FragmentPictureOfTheDayBinding
import com.example.mymaterialdesign.viewmodel.AppState
import com.example.mymaterialdesign.viewmodel.PictureOfTheDayViewModel
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate


class PictureOfTheDayFragment : Fragment() {

    private var _binding: FragmentPictureOfTheDayBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPictureOfTheDayBinding.inflate(inflater, container, false)

        return binding.root
    }

    val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getLiveData().observe(viewLifecycleOwner) { appState ->
            renderData(appState)
        }
        viewModel.sendRequest(LocalDate.now().toString())

        binding.chipBefore.setOnClickListener {
            viewModel.sendRequest(
                LocalDate.now()
                    .minusDays(2).toString()
            )

        }

        binding.chipYesterday.setOnClickListener {
            viewModel.sendRequest(
                LocalDate.now()
                    .minusDays(1).toString()
            )
        }

        binding.chipToday.setOnClickListener { viewModel.sendRequest(LocalDate.now().toString()) }

        binding.textInputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${binding.inputSearch.text.toString()}")
            })
        }
    }

    private fun renderData(appState: AppState?) {
        when (appState) {
            is AppState.Error -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                Snackbar.make(requireView(), "Не удалось загрузить данные", Snackbar.LENGTH_LONG)
                    .show()
            }
            AppState.Loading -> {
                binding.mainFragmentLoadingLayout.visibility = View.VISIBLE
            }
            is AppState.Success -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                binding.imageView.load(appState.pictureOfTheDayResponseData.url) {
                    lifecycle(this@PictureOfTheDayFragment)
                    error(R.drawable.ic_load_error_vector)
                    placeholder(R.drawable.ic_no_photo_vector)
                    crossfade(true)
                }

                binding.bsl.bottomSheetDescriptionHeader.text =
                    appState.pictureOfTheDayResponseData.title
                binding.bsl.bottomSheetDescription.text =
                    appState.pictureOfTheDayResponseData.explanation

            }
            else -> {}
        }
    }


    companion object {
        fun newInstance() = PictureOfTheDayFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}