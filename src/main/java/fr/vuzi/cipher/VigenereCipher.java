package fr.vuzi.cipher;

import java.io.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Vigenere cipher implementation, on 'a' to 'z' character. Any other character will be ignored in both encoded
 * and decode method and outputted directly
 */
public class VigenereCipher implements ICipher {

    public int keySize = 10;

    @Override
    public void encode(File message, File key, File encodedMessage) throws IOException {
        encode(readKey(key), message, encodedMessage);
    }

    @Override
    public void decode(File encodedMessage, File keyFile, File decodedMessage) throws IOException {
        int[] key = readKey(keyFile);

        for(int i = 0; i < key.length; i++)
            key[i] = 26 - key[i];

        encode(key, encodedMessage, decodedMessage);
    }

    private void encode(int[] key, File message, File encodedMessage) throws IOException {
        Reader msgReader = new InputStreamReader(new FileInputStream(message));
        Writer msgWriter = new OutputStreamWriter(new FileOutputStream(encodedMessage));

        int c, i = 0;
        while ((c = msgReader.read()) != -1) {
            if(c >= 'a' && c <= 'z')
                c = ((c - 'a' + key[i % key.length]) % 26) + 'a';
            i++;
            msgWriter.write(c);
        }

        msgWriter.close();
        msgReader.close();
    }

    public int getKeySize() {
        return keySize;
    }

    public void setKeySize(int keySize) {
        this.keySize = keySize;
    }

    @Override
    public void generateKey(File keyFile) throws IOException {
        int[] key = new int[keySize];
        Random random = new SecureRandom();

        for(int i = 0; i < key.length; i++)
            key[i] = random.nextInt(26);

        // Write the key
        Writer keyWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(keyFile)));
        for(int keyElement : key) {
            keyWriter.write(keyElement + "\r\n");
        }
        keyWriter.close();
    }

    /**
     * Read a key from a file
     * @param keyFile The file containing the key
     * @return The read key
     * @throws IOException
     */
    public int[] readKey(File keyFile) throws IOException {
        // Get key
        BufferedReader keyReader = new BufferedReader(new InputStreamReader(new FileInputStream(keyFile)));

        List<Integer> keyTmp = new ArrayList<>();

        String line;
        while((line = keyReader.readLine()) != null) {
            keyTmp.add(Integer.valueOf(line.trim()));
        }

        // Into int array
        int[] key = new int[keyTmp.size()];
        int i = 0;
        for(Integer val : keyTmp)
            key[i++] = val;

        return key;
    }
}
