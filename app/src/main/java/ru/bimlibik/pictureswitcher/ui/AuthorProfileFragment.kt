package ru.bimlibik.pictureswitcher.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import ru.bimlibik.pictureswitcher.R
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
        val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar_layout)
        val toolbarTitle: TextView = requireActivity().findViewById(R.id.toolbar_title)
        toolbar.setNavigationIcon(R.drawable.ic_close)
        toolbarTitle.text = args.authorName
    }
}