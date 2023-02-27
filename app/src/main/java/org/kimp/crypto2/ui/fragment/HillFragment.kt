package org.kimp.crypto2.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import org.kimp.crypto2.crypto.CryptResult
import org.kimp.crypto2.crypto.HillCipher
import org.kimp.crypto2.databinding.FragmentHillLayoutBinding
import org.kimp.crypto2.ui.view.CryptoView

@AndroidEntryPoint
class HillFragment: Fragment(), CryptoView.CryptoListener {
    private lateinit var binding: FragmentHillLayoutBinding
    private val cryptor = HillCipher()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHillLayoutBinding.inflate(
            inflater, container, false
        )

        binding.cryptoView.cryptoListener = this

        return binding.root
    }

    override fun encrypt(text: String): CryptResult? {
        return if (binding.keyView.hasErrors) null
            else cryptor.encrypt(text, binding.keyView.getEnteredMatrix())
    }

    override fun decrypt(text: String): CryptResult? {
        return if (binding.keyView.hasErrors) null
            else cryptor.decrypt(text, binding.keyView.getEnteredMatrix())
    }
}