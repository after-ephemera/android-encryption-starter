package jkjensen.me.keystoredemoapp

import android.annotation.TargetApi
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey


class AndroidKeyStore {
    private val keyStore: KeyStore = createAndroidKeyStore()

    private fun createAndroidKeyStore(): KeyStore {
        val keyStore:KeyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        return keyStore
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun initializeGeneratorForAlias(generator: KeyPairGenerator, alias: String) {
        val builder = KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
//             Check for strongbox support (9.0 and later)
//            .setIsStrongBoxBacked(true)
        generator.initialize(builder.build())
    }

    fun getKeyPair(alias: String): KeyPair? {
        val privateKey = keyStore.getKey(alias, null) as PrivateKey?
        val publicKey = keyStore.getCertificate(alias)?.publicKey

        return if (privateKey != null && publicKey != null) {
            KeyPair(publicKey, privateKey)
        } else {
            null
        }
    }

    fun removeAndroidKeyStoreKey(alias: String) = keyStore.deleteEntry(alias)

    fun createAndroidKeyStoreAsymmetricKey(alias: String): KeyPair {
        val generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore")

        initializeGeneratorForAlias(generator, alias)
        return generator.generateKeyPair()
    }
}