package fr.vuzi.cipher;

import fr.vuzi.Utils;

import java.io.*;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * Substitution cipher
 */
public class SubstitutionCipher implements ICipher {

    public static String alphabet = "abcdefghijklmnopqrstuvwxyz";

    @Override
    public void encode(File message, File key, File encodedMessage) throws IOException {
        encode(alphabet, readKey(key), message, encodedMessage);
    }

    @Override
    public void decode(File encodedMessage, File key, File decodedMessage) throws IOException {
        encode(readKey(key), alphabet, encodedMessage, decodedMessage);
    }

    /**
     * Encode the provided file to the destination file, using the provided key
     * @param alphabet The alphabet to use
     * @param key The key containing the file
     * @param message The message file
     * @param encodedMessage The destination file
     * @throws IOException
     */
    private void encode(String alphabet, String key, File message, File encodedMessage) throws IOException {
        Reader msgReader = new InputStreamReader(new FileInputStream(message));
        Writer msgWriter = new OutputStreamWriter(new FileOutputStream(encodedMessage));

        // Temp map
        Map<Character, Character> alphabetMap = new HashMap<>();

        for (int i = 0; i < alphabet.length(); i++){
            alphabetMap.put(alphabet.charAt(i), key.charAt(i));
        }

        int c;
        while ((c = msgReader.read()) != -1) {
            msgWriter.write(alphabetMap.containsKey(c) ? (int)alphabetMap.get(c) : c);
        }

        msgWriter.flush();
        msgWriter.close();
        msgReader.close();
    }

    @Override
    public void generateKey(File key) throws IOException {
        // Shuffle the alphabet
        char[] alphArray = alphabet.toCharArray();
        SecureRandom random = new SecureRandom();
        for(int i = 0; i < alphArray.length - 1 ; i++){
            int j = random.nextInt(alphArray.length - i);
            char temp = alphArray[i];
            alphArray[i] = alphArray[i + j];
            alphArray[i + j] = temp;
        }

        // Write the key
        Writer keyWriter = new OutputStreamWriter(new FileOutputStream(key));
        keyWriter.write(alphArray);
        keyWriter.flush();
        keyWriter.close();
    }

    /**
     * Read a key from a file
     * @param key The file containing the key
     * @return The read key
     * @throws IOException
     */
    public String readKey(File key) throws IOException {
        // Get key
        Reader keyReader = new InputStreamReader(new FileInputStream(key));
        char[] k = new char[alphabet.length()];

        keyReader.read(k);
        keyReader.close();

        return new String(k);
    }

    /**
     * Guess the key from the provided frequencies
     * @param message The encoded message to crack
     * @param targetFrequencies The target frequencies
     * @return The guessed key
     * @throws IOException
     */
    public String guessKey(File message, Map<Character, Double> targetFrequencies) throws IOException {
        // Get frequencies
        InputStream messageStream = new FileInputStream(message);
        Map<Character, Double> frequencies = Utils.getFrequencies(alphabet, messageStream);
        messageStream.close();

        // Try to guess the key
        char[] key = new char[alphabet.length()];

        for(int i = 0; i < alphabet.length(); i++) {
            // Target frequency
            Double f = targetFrequencies.get(alphabet.charAt(i));

            char e = '-';
            double diff = Double.MAX_VALUE;

            for(char c : frequencies.keySet()) {
                double newDiff = Math.abs(f - frequencies.get(c));

                if(newDiff < diff) {
                    e = c;
                    diff = newDiff;
                }
            }

            key[i] = e;
        }

        return new String(key);
    }

}
