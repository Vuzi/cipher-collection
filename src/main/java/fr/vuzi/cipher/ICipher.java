package fr.vuzi.cipher;

import java.io.File;
import java.io.IOException;

/**
 * Cipher common interface
 */
public interface ICipher {
    void encode(File message, File key, File encodedMessage) throws IOException;
    void decode(File encodedMessage, File key, File decodedMessage) throws IOException;

    void generateKey(File key) throws IOException;
}