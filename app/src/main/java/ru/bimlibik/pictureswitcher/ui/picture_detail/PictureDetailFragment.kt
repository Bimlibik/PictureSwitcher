package ru.bimlibik.pictureswitcher.ui.picture_detail

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import org.koin.android.viewmodel.ext.android.viewModel
import ru.bimlibik.pictureswitcher.R
import ru.bimlibik.pictureswitcher.databinding.FragmentPictureDetailBinding
import ru.bimlibik.pictureswitcher.utils.EventObserver
import ru.bimlibik.pictureswitcher.utils.setupSnackbar
import ru.bimlibik.pictureswitcher.workers.WallpaperWorker

class PictureDetailFragment : Fragment() {

    private val viewModel: PictureDetailViewModel by viewModel()
    private lateinit var viewDataBinding: FragmentPictureDetailBinding

    private val args: PictureDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentPictureDetailBinding
            .inflate(inflater, container, false)
            .apply {
                viewModel = this@PictureDetailFragment.viewModel
                item = args.picture
            }
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.lifecycleOwner = this
        viewModel.start(args.picture)
        setupSnackbar()
        setupNavigation()
        setupToolbar()
    }

    private fun setupNavigation() {
        viewModel.authorProfileEvent.observe(viewLifecycleOwner, EventObserver {
            val action = PictureDetailFragmentDirections
                .actionPictureDetailFragmentToAuthorProfileFragment(
                    args.picture.getAuthorProfileLink(),
                    args.picture.author?.name
                )
            findNavController().navigate(action)
        })

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
            .putString(WallpaperWorker.IMAGE_URL, args.picture.getPictureLink())
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
        viewDataBinding.toolbar.setNavigationOnClickListener { view ->
            view.findNavController().navigateUp()
        }
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, viewModel.snackbarText)
    }
}