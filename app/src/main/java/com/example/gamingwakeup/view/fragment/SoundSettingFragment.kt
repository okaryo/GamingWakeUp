package com.example.gamingwakeup.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamingwakeup.R
import com.example.gamingwakeup.databinding.FragmentSoundSettingBinding
import com.example.gamingwakeup.view.adapter.SoundSettingAdapter
import com.example.gamingwakeup.viewmodel.SoundSettingViewModel

class SoundSettingFragment : Fragment() {
    private lateinit var binding: FragmentSoundSettingBinding
    private val viewModel: SoundSettingViewModel by lazy {
        val arguments = SoundSettingFragmentArgs.fromBundle(requireArguments())
        SoundSettingViewModel.crate(arguments.alarm, requireContext())
    }
    private val soundSettingAdapter: SoundSettingAdapter by lazy {
        SoundSettingAdapter(
            SoundSettingAdapter.OnClickSoundTitleListener { soundTitle ->
                viewModel.onTapSoundTitle(soundTitle)
            },
            SoundSettingAdapter.OnChangeSoundVolumeListener { soundVolume ->
                viewModel.onChangeSoundVolume(soundVolume)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupBinding(inflater, container)
        setupRecyclerView()
        setupToolbar()
        setupAdapter()
        setupSoundTitleObserver()
        setupMediaPlayerObserver()

        return binding.root
    }

    private fun setupBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = FragmentSoundSettingBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.soundSettingRecyclerView
        recyclerView.apply {
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = soundSettingAdapter
        }
    }

    private fun setupAdapter() {
        soundSettingAdapter.apply {
            soundTitles = viewModel.soundTitles.keys.toList()
            soundVolume = viewModel.volume
            notifyDataSetChanged()
        }
    }

    private fun setupSoundTitleObserver() {
        viewModel.selectedSoundTitle.observe(viewLifecycleOwner, Observer {
            soundSettingAdapter.apply {
                selectedSoundTitle = it
                notifyDataSetChanged()
            }
        })
    }

    private fun setupMediaPlayerObserver() {
        viewModel.isSoundPlaying.observe(viewLifecycleOwner, Observer {
            soundSettingAdapter.apply {
                isSoundPlaying = it
                notifyDataSetChanged()
            }
        })
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            title = getString(R.string.toolbar_title_sound_setting)
            setNavigationIcon(R.drawable.ic_arrow_back)
            setNavigationOnClickListener {
                this.findNavController()
                    .navigate(
                        SoundSettingFragmentDirections.actionSoundSettingFragmentToAddEditAlarmFragment(
                            viewModel.currentAlarm()
                        )
                    )
            }
        }
    }
}
