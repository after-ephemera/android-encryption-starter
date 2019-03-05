package jkjensen.me.keystoredemoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import jkjensen.me.keystoredemoapp.CipherHelper.Companion.TRANSFORMATION_ASYMMETRIC
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    // Creates Android Key Store and provides manage functions
    private val keyStore = AndroidKeyStore()

    private var encryptedData = "first encrypted string"
    private var decryptedData = "nothing decrypted yet"
    private var data = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        keyStore.createAndroidKeyStoreAsymmetricKey("MASTER_KEY")

        val masterKey = keyStore.getKeyPair("MASTER_KEY")

        val cipherHelper = CipherHelper(TRANSFORMATION_ASYMMETRIC)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        encryptText.doOnTextChanged { text, start, count, after ->
            data = text.toString()
        }
        encryptButton.setOnClickListener {
            encryptedData = cipherHelper.encrypt(data, masterKey?.public)
            encryptedText.text = encryptedData
        }
        decryptButton.setOnClickListener {
            decryptedData = cipherHelper.decrypt(encryptedData, masterKey?.private)
            decryptText.text = decryptedData
        }
    }
}
