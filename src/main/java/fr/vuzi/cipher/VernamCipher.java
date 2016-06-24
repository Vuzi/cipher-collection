package fr.vuzi.cipher;

import java.io.*;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Vernam cipher, using binary xor operation to encoded and decode. The key should be the same length of the
 * message to encode, or any remaining byte of the message will be ignored and not outputted.
 */
public class VernamCipher implements ICipher {

    @Override
    public void encode(File message, File keyFile, File encodedMessage) throws IOException {
        InputStream keyReader = new BufferedInputStream(new FileInputStream(keyFile));
        InputStream messageReader = new BufferedInputStream(new FileInputStream(message));
        OutputStream messageWriter = new BufferedOutputStream(new FileOutputStream(encodedMessage));

        int c, k;
        while(((k = keyReader.read()) >= 0) && ((c = messageReader.read()) >= 0)) {
            messageWriter.write(c ^ k);
        }

        keyReader.close();
        messageReader.close();
        messageWriter.close();
    }

    @Override
    public void decode(File encodedMessage, File key, File decodedMessage) throws IOException {
        encode(encodedMessage, key, decodedMessage);
    }

    int keySize = 0;

    public int getKeySize() {
        return keySize;
    }

    public void setKeySize(int keySize) {
        this.keySize = keySize;
    }

    @Override
    public void generateKey(File keyFile) throws IOException {
        Random random = new SecureRandom();

        // Write the key
        OutputStream keyWriter = new BufferedOutputStream(new FileOutputStream(keyFile));
        for(int i = 0; i < keySize; i++) {
            keyWriter.write((byte) random.nextInt()); // Random value
        }
        keyWriter.close();
    }
}
