package fr.vuzi.cipher;

import fr.vuzi.Utils;

import java.io.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Homophonic substitution cipher. This cipher only handle a to z lower case char, and will ignore any other
 * character. Those characters are flagged and will be outputted flagged but not encoded. The other character
 * specified in the alphabet will be encoded with 0 to 127 ASCII value
 */
public class HomophonicSubstitutionCipher implements ICipher {

    /**
     * Encoded alphabet
     */
    private static String alphabet = "abcdefghijklmnopqrstuvwxyz";

    /**
     * Key size
     */
    private static int key_size = 128;

    @Override
    public void encode(File message, File keyFile, File encodedMessage) throws IOException {
        Map<Character, List<Integer>> key = readKey(keyFile);
        Map<Character, Integer> keyState = new HashMap<>();

        for(Map.Entry<Character, List<Integer>> entry : key.entrySet()) {
            keyState.put(entry.getKey(), 0);
        }

        InputStream msgReader = new FileInputStream(message);
        OutputStream msgWriter = new FileOutputStream(encodedMessage);

        int c;
        while ((c = msgReader.read()) != -1) {
            if(key.containsKey((char) c)) {
                Integer i = keyState.get((char)c);
                List<Integer> substitutions = key.get((char)c);

                // Update state
                keyState.put((char) c, i + 1);

                // Get the substituted char
                c = substitutions.get(i % substitutions.size());
            } else
                c = c | 128;

            msgWriter.write(c);
        }

        msgWriter.close();
        msgReader.close();
    }

    @Override
    public void decode(File encodedMessage, File keyFile, File decodedMessage) throws IOException {

        Map<Character, List<Integer>> key = readKey(keyFile);
        Map<Character, Integer> keyState = new HashMap<>();

        for(Map.Entry<Character, List<Integer>> entry : key.entrySet()) {
            keyState.put(entry.getKey(), 0);
        }

        InputStream msgReader = new FileInputStream(encodedMessage);
        OutputStream msgWriter = new FileOutputStream(decodedMessage);

        int c;
        while ((c = msgReader.read()) != -1) {
            char decodedChar = (char)c; // Default : use the found char

            if((c & 128) != 128) {
                // Try to find the decoded char
                for (Map.Entry<Character, List<Integer>> vals : key.entrySet()) {
                    Integer i = keyState.get(vals.getKey());
                    List<Integer> substitutions = key.get(vals.getKey());

                    // Get the substituted char
                    char cSubstitute = (char) substitutions.get(i % substitutions.size()).intValue();

                    if (c == cSubstitute) {
                        // If the substituted char is found :
                        keyState.put(vals.getKey(), i + 1);
                        decodedChar = vals.getKey();
                    }
                }
            } else
                decodedChar = (char)(c ^ 128);

            msgWriter.write(decodedChar);
        }

        msgWriter.close();
        msgReader.close();
    }

    /**
     * Read the key form the provided file.
     *
     * @param keyFile The key file
     * @return The read key
     * @throws IOException
     */
    public Map<Character, List<Integer>> readKey(File keyFile) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(keyFile)));

        // Create the key
        Map<Character, List<Integer>> key = new HashMap<>();

        String line;

        while((line = reader.readLine()) != null) {
            String[] lineValues = line.split("=");

            if(lineValues.length != 2 || lineValues[0].isEmpty())
                continue; // Ignore

            char c = lineValues[0].charAt(0);
            List<Integer> vals = new ArrayList<>();

            for(String val : lineValues[1].split(";"))
                vals.add(Integer.valueOf(val.trim()));

            key.put(c, vals);
        }

        reader.close();

        return key;
    }

    @Override
    public void generateKey(File keyFile) throws IOException {

        // A key is an hashmap attributing to each char a list of possible
        // char output from 0 to 127. The number of values for each char
        // is based on the frequency of the letter in the language

        // Create the alphabet
        char[] keyAlphabet = new char[key_size];
        for(char i = 0; i < key_size; i++)
            keyAlphabet[i] = i;

        // Shuffle the alphabet
        Utils.shuffe(keyAlphabet);

        // Create the key
        Map<Character, List<Integer>> key = new HashMap<>();

        int keyAlphabetIndex = 0;
        for(char c : alphabet.toCharArray()) {
            // Guess the number of possible char based on the frequency
            int charNumber = (int) Math.floor(Utils.englishFrequencies[c - 'a'] * key_size);

            if(charNumber <= 0)
                charNumber = 1; // Minimum is 1 value

            List<Integer> charValues = new ArrayList<>();

            for(int i = 0; i < charNumber && keyAlphabetIndex < keyAlphabet.length; i++, keyAlphabetIndex++) {
                charValues.add((int) keyAlphabet[keyAlphabetIndex]); // From the key alphabet
            }

            key.put(c, charValues);
        }

        // Write the key
        Writer keyWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(keyFile)));
        for(Map.Entry<Character, List<Integer>> entry : key.entrySet()) {
            // Join the int values of the replacement chars
            String values = String.join(";", entry.getValue().stream().map(String::valueOf).collect(Collectors.toList()));
            // Write them
            keyWriter.write(entry.getKey() + "=" + values + "\r\n");
        }

        keyWriter.close();
    }
}
