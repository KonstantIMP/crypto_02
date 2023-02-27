package org.kimp.crypto2.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import dagger.hilt.android.AndroidEntryPoint
import org.kimp.crypto2.crypto.CryptResult
import org.kimp.crypto2.databinding.ViewCryptoModuleBinding
import java.util.Locale

@AndroidEntryPoint
class CryptoView(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {
    private val binding: ViewCryptoModuleBinding

    var cryptoListener: CryptoListener? = null

    init {
        binding = ViewCryptoModuleBinding.inflate(
            LayoutInflater.from(context), this, true
        )

        binding.encryptBtn.setOnClickListener {
            cryptoListener?.encrypt(
                binding.editText.text.toString().lowercase(Locale.ROOT)
            )?.apply {
                binding.editText.setText(message)
            }
        }
        binding.decryptBtn.setOnClickListener {
            cryptoListener?.decrypt(
                binding.editText.text.toString().lowercase(Locale.ROOT)
            )?.apply {
                binding.editText.setText(message)
            }
        }
    }

    interface CryptoListener {
        fun encrypt(text: String): CryptResult?
        fun decrypt(text: String): CryptResult?
    }
}
