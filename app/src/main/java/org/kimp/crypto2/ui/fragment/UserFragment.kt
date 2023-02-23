package org.kimp.crypto2.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import org.kimp.crypto2.databinding.FragmentUserLayoutBinding

@AndroidEntryPoint
class UserFragment: Fragment() {
    private lateinit var binding: FragmentUserLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserLayoutBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }
}