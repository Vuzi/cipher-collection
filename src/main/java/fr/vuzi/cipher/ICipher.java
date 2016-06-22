package fr.vuzi.cipher;

import java.io.File;
import java.io.IOException;

public interface ICipher {
    void encode(File message, File key, File encodedMessage) throws IOException;
    void decode(File encodedMessage, File key, File decodedMessage) throws IOException;

    void generateKey(File key) throws IOException;

    //K readKey(File f) throws IOException;
    //void writeKey(K key, File f) throws IOException;

    //M readMessage(File f) throws IOException;
    //void writeMessage(M message, File f) throws IOException;
}