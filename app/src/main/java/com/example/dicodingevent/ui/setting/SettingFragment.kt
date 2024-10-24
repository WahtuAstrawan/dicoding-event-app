package com.example.dicodingevent.ui.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.dicodingevent.databinding.FragmentSettingBinding
import com.example.dicodingevent.ui.DailyRemainderWorker
import com.example.dicodingevent.ui.ViewModelFactory
import java.util.concurrent.TimeUnit

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null

    private val binding get() = _binding!!
    private lateinit var workManager: WorkManager
    private lateinit var periodicWorkRequest: PeriodicWorkRequest

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val settingViewModel: SettingViewModel by viewModels { factory }
        workManager = WorkManager.getInstance(requireActivity())

        settingViewModel.getThemeSetting().observe(viewLifecycleOwner) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            binding.switchDarkmode.isChecked = isDarkModeActive
        }

        binding.switchDarkmode.setOnCheckedChangeListener { _, isChecked ->
            settingViewModel.saveThemeSetting(isChecked)
        }

        settingViewModel.getNotificationSetting()
            .observe(viewLifecycleOwner) { isNotificationActive ->
                binding.switchNotification.isChecked = isNotificationActive
            }

        binding.switchNotification.setOnCheckedChangeListener { _, isChecked ->
            settingViewModel.saveNotificationSetting(isChecked)
            if (isChecked) {
                startPeriodicTask()
            } else {
                cancelPeriodicTask()
            }
        }
    }

    private fun startPeriodicTask() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        periodicWorkRequest =
            PeriodicWorkRequest.Builder(DailyRemainderWorker::class.java, 1, TimeUnit.DAYS)
                .setConstraints(constraints).addTag(WORKER_TAG).build()
        workManager.enqueueUniquePeriodicWork(
            "DailyRemainder", ExistingPeriodicWorkPolicy.KEEP, periodicWorkRequest
        )
        workManager.getWorkInfoByIdLiveData(periodicWorkRequest.id)
            .observe(viewLifecycleOwner) { workInfo ->
                if (workInfo != null) {
                    if (workInfo.state == WorkInfo.State.CANCELLED) {
                        Toast.makeText(
                            requireActivity(),
                            "Daily Remainder Deactivated",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (workInfo.state == WorkInfo.State.FAILED) {
                        Toast.makeText(
                            requireActivity(),
                            "Daily Remainder Failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }

    companion object{
        private const val WORKER_TAG = "DAILY_REMAINDER"
    }

    private fun cancelPeriodicTask() {
        workManager.cancelAllWorkByTag(WORKER_TAG)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}