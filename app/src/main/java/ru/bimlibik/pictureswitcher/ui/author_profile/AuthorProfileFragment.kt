package ru.bimlibik.pictureswitcher.ui.author_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import ru.bimlibik.pictureswitcher.databinding.FragmentAuthorProfileBinding

class AuthorProfileFragment : Fragment() {

    private lateinit var viewDataBinding: FragmentAuthorProfileBinding
    private val args: AuthorProfileFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        viewDataBinding.webview.loadUrl(args.link)
    }

    private fun setupToolbar() {
        viewDataBinding.toolbarTitle.text = args.authorName
        viewDataBinding.toolbar.setNavigationOnClickListener { view ->
            view.findNavController().navigateUp()
        }
    }
}