package com.example.mymaterialdesign.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.ChangeImageTransform
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.view.*
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
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

    var isFlag = false
    var isFlagDate = false
    var duration = 1000L

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

        loadData()
        searchWiki()
        increaseImage()
        viewAnimation()

    }

    private fun viewAnimation() {
        binding.fab.setOnClickListener {
            isFlagDate = !isFlagDate
            if (isFlagDate) {
                ObjectAnimator.ofFloat(binding.plusImageview, View.ROTATION, 0f, 675f)
                    .setDuration(duration).start()
                ObjectAnimator.ofFloat(binding.optionOneContainer, View.TRANSLATION_Y, -700f)
                    .setDuration(duration).start()
                ObjectAnimator.ofFloat(binding.transparentBackground, View.ALPHA, 0.9f)
                    .setDuration(duration).start()

                binding.optionOneContainer.animate().alpha(1f).setDuration(duration).setListener(
                    object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            binding.optionOneContainer.isClickable = true
                        }
                    }
                )
            } else {
                ObjectAnimator.ofFloat(binding.plusImageview, View.ROTATION, 675f, 0f)
                    .setDuration(duration).start()
                ObjectAnimator.ofFloat(binding.optionOneContainer, View.TRANSLATION_Y, 0f)
                    .setDuration(duration).start()
                ObjectAnimator.ofFloat(binding.transparentBackground, View.ALPHA, 0f)
                    .setDuration(duration).start()

                binding.optionOneContainer.animate().alpha(0f).setDuration(duration).setListener(
                    object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            binding.optionOneContainer.isClickable = false
                        }
                    }
                )
            }
        }
    }

    private fun increaseImage() {
        binding.imageView.setOnClickListener {
            isFlag = !isFlag

            val params = it.layoutParams as ConstraintLayout.LayoutParams

            val transitionSet = TransitionSet()
            val changeBounds = ChangeBounds()
            val changeImageTransform = ChangeImageTransform()

            transitionSet.addTransition(changeBounds)
            transitionSet.addTransition(changeImageTransform)

            TransitionManager.beginDelayedTransition(binding.root, transitionSet)
            if (isFlag) {
                params.height = ConstraintLayout.LayoutParams.MATCH_PARENT
                binding.imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            } else {
                params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
                binding.imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
            }
            it.layoutParams = params
        }
    }

    private fun searchWiki() {
        binding.textInputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${binding.inputSearch.text.toString()}")
            })
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadData() {
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