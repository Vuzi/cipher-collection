package fr.vuzi.cipher;

import fr.vuzi.Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TranspositionCipher implements ICipher {

    @Override
    public void encode(File message, File keyFile, File encodedMessage) throws IOException {
        encode(readKey(keyFile), message, encodedMessage);
    }

    @Override
    public void decode(File encodedMessage, File keyFile, File decodedMessage) throws IOException {
        encode(readKey(keyFile), encodedMessage, decodedMessage);
    }

    private void encode(int[] key, File message, File encodedMessage) throws IOException {
        char[] buffer = new char[key.length];

        Reader msgReader = new InputStreamReader(new FileInputStream(message));
        Writer msgWriter = new OutputStreamWriter(new FileOutputStream(encodedMessage));

        int read;
        while ((read = msgReader.read(buffer)) != -1) {
            // Need padding
            if(read < buffer.length)
                for(int i = read; i < buffer.length; i++)
                    buffer[i] = ' '; // Padding

            // Transpose...
            for(int i = 0; i < key.length; i++) {
                char tmp = buffer[i];
                buffer[i] = buffer[key[i]];
                buffer[key[i]] = tmp;
            }

            msgWriter.write(buffer);
        }

        msgWriter.close();
        msgReader.close();
    }

    public int keySize = 26;

    public int getKeySize() {
        return keySize;
    }

    public void setKeySize(int keySize) {
        this.keySize = keySize;
    }

    @Override
    public void generateKey(File keyFile) throws IOException {

        int[] key = new int[keySize];

        for(int i = 0; i < key.length; i++)
            key[i] = i;

        // Shuffle the key
        Utils.shuffe(key);

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
