package ru.bimlibik.pictureswitcher.ui.picture_detail

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import org.koin.android.viewmodel.ext.android.viewModel
import ru.bimlibik.pictureswitcher.R
import ru.bimlibik.pictureswitcher.data.Picture
import ru.bimlibik.pictureswitcher.databinding.FragmentPictureDetailBinding
import ru.bimlibik.pictureswitcher.utils.EventObserver
import ru.bimlibik.pictureswitcher.utils.hideKeyboard
import ru.bimlibik.pictureswitcher.utils.setupSnackbar
import ru.bimlibik.pictureswitcher.workers.WallpaperWorker
import timber.log.Timber

class PictureDetailFragment : Fragment() {

    companion object {
        private const val ARGS_KEY = "PictureDetailFragmentKey"

        fun newInstance(picture: Picture): PictureDetailFragment {
            return PictureDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARGS_KEY, picture)
                }
            }
        }
    }

    private val viewModel: PictureDetailViewModel by viewModel()
    private lateinit var viewDataBinding: FragmentPictureDetailBinding

    private val args: Picture?
        get() = arguments?.getParcelable(ARGS_KEY)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.i("Args = $args")
        viewDataBinding = FragmentPictureDetailBinding
            .inflate(inflater, container, false)
            .apply {
                viewModel = this@PictureDetailFragment.viewModel
                item = args
                root.hideKeyboard()
            }
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.lifecycleOwner = this
        viewModel.start(args)
        setupSnackbar()
        setupNavigation()
        setupToolbar()
    }

    private fun setupNavigation() {
        viewModel.wallpaperEvent.observe(viewLifecycleOwner, EventObserver {
            showDialog()
        })
    }

    private fun showDialog() {
        AlertDialog.Builder(requireActivity()).apply {
            setTitle(R.string.label_dialog_title)
            setPositiveButton(R.string.label_ok) { dialog, _ ->
                viewModel.showProgress()
                startWork()
                dialog.dismiss()
            }
            setNegativeButton(R.string.label_cancel) { dialog, _ ->
                dialog.dismiss()
            }
        }.show()
    }

    private fun startWork() {
        val workManager = WorkManager.getInstance(requireContext())

        val url = Data.Builder()
            .putString(WallpaperWorker.IMAGE_URL, args?.getPictureLink())
            .build()

        val request = OneTimeWorkRequestBuilder<WallpaperWorker>()
            .setInputData(url)
            .build()

        workManager.enqueue(request)
        workManager.getWorkInfoByIdLiveData(request.id).observe(viewLifecycleOwner) { info ->
            viewModel.showInfo(info.state)
        }
    }

    private fun setupToolbar() {
        viewDataBinding.toolbar.setNavigationOnClickListener {
            viewModel.goBack()
        }
    }

    private fun setupSnackbar() {
        viewDataBinding.root.setupSnackbar(viewLifecycleOwner, viewModel.snackbarText)
    }
}