package ru.bimlibik.pictureswitcher.ui.author_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Router
import org.koin.android.ext.android.inject
import ru.bimlibik.pictureswitcher.databinding.FragmentAuthorProfileBinding

class AuthorProfileFragment : Fragment() {

    private val router by inject<Router>()

    companion object {
        private const val ARGS_LINK_KEY = "AuthorProfileFragmentLinkKey"
        private const val ARGS_NAME_KEY = "AuthorProfileFragmentNameKey"

        fun newInstance(link: String, authorName: String): AuthorProfileFragment {
            return AuthorProfileFragment().also { fragment ->
                fragment.arguments = bundleOf(
                    ARGS_LINK_KEY to link,
                    ARGS_NAME_KEY to authorName
                )
            }
        }
    }

    private lateinit var viewDataBinding: FragmentAuthorProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentAuthorProfileBinding
            .inflate(inflater, container, false)
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolbar()
        setupWebview()
    }

    private fun setupWebview() {
        viewDataBinding.webview.webViewClient = WebViewClient()
        viewDataBinding.webview.loadUrl(arguments?.getString(ARGS_LINK_KEY) ?: "")
    }

    private fun setupToolbar() {
        viewDataBinding.toolbarTitle.text = arguments?.getString(ARGS_NAME_KEY)
        viewDataBinding.toolbar.setNavigationOnClickListener {
            router.exit()
        }
    }
}