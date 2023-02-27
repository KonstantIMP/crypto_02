package org.kimp.crypto2.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import org.kimp.crypto2.crypto.CryptResult
import org.kimp.crypto2.databinding.FragmentRecurrentHillLayoutBinding
import org.kimp.crypto2.ui.view.CryptoView
import org.kimp.crypto2.ui.view.KeyView

@AndroidEntryPoint
class RecurrentHillFragment: Fragment(), CryptoView.CryptoListener {
    private lateinit var binding: FragmentRecurrentHillLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecurrentHillLayoutBinding.inflate(
            inflater, container, false
        )

        binding.key1.sizeChangedListener = object: KeyView.KeySizeChangedListener {
            override fun keySizeChanged(from: Int, to: Int) {
                binding.key2.migrateMatrixSize(from, to, false)
            }
        }
        binding.key2.sizeChangedListener = object: KeyView.KeySizeChangedListener {
            override fun keySizeChanged(from: Int, to: Int) {
                binding.key1.migrateMatrixSize(from, to, false)
            }

        }
        binding.cryptoView.cryptoListener = this

        return binding.root
    }

    override fun encrypt(text: String): CryptResult? {
        if (binding.key1.hasErrors || binding.key2.hasErrors) return null
        return null
    }

    override fun decrypt(text: String): CryptResult? {
        if (binding.key1.hasErrors || binding.key2.hasErrors) return null
        return null
    }
}