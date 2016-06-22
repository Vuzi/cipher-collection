package fr.vuzi.cipher;

import fr.vuzi.Utils;

import java.io.*;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CaesarCipher implements ICipher {
    @Override
    public void encode(File message, File key, File encodedMessage) throws IOException {
        encode(readKey(key), message, encodedMessage);
    }

    @Override
    public void decode(File encodedMessage, File key, File decodedMessage) throws IOException {
        encode(26 - readKey(key), encodedMessage, decodedMessage);
    }

    private void encode(int key, File message, File encodedMessage) throws IOException {
        Reader msgReader = new InputStreamReader(new FileInputStream(message));
        Writer msgWriter = new OutputStreamWriter(new FileOutputStream(encodedMessage));

        int c;
        while ((c = msgReader.read()) != -1) {
            if(c >= 'a' && c <= 'z')
                c = ((c - 'a' + key) % 26) + 'a';
            msgWriter.write(c);
        }

        msgWriter.close();
        msgReader.close();
    }

    public int guessKey(File dictionary, File message) throws IOException {
        BufferedReader msgReader = new BufferedReader(new InputStreamReader(new FileInputStream(dictionary)));

        int dec = -1, maxCount = Integer.MIN_VALUE;

        // Try each keys
        for(int i = 0; i < 26; i++) {
            File tmp = new File("temp.txt");
            encode(i, message, tmp);

            List<String> words = Arrays.asList(Utils.readFile(tmp).split(" ")).stream().
                    map(String::trim).collect(Collectors.toList());

            String line;
            int count = 0;

            while((line = msgReader.readLine()) != null) {
                for(String word : words)
                    count += word.toLowerCase().equals(line.trim().toLowerCase()) ? 1 : 0; // If word found...
            }

            // Max match found
            if(count > maxCount) {
                maxCount = count;
                dec = i;
            }

            msgReader.reset();
        }

        return dec;
    }

    /**
     * Read a key from a file
     * @param key The file containing the key
     * @return The read key
     * @throws IOException
     */
    public Integer readKey(File key) throws IOException {
        // Get key
        Reader keyReader = new InputStreamReader(new FileInputStream(key));
        char[] k = new char[512];

        keyReader.read(k);
        keyReader.close();

        return Integer.valueOf(new String(k).trim());
    }

    @Override
    public void generateKey(File key) throws IOException {
        // Write the key
        Writer keyWriter = new OutputStreamWriter(new FileOutputStream(key));
        keyWriter.write(String.valueOf(new SecureRandom().nextInt(26)));
        keyWriter.flush();
        keyWriter.close();
    }
}
