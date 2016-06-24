package fr.vuzi.cipher;

import fr.vuzi.Utils;

import java.io.*;
import java.security.SecureRandom;
import java.util.*;

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

    /**
     * Encode the provided message
     *
     * @param key Key to use
     * @param message Message to encode
     * @param encodedMessage Message to decode to
     * @throws IOException
     */
    private void encode(int[] key, File message, File encodedMessage) throws IOException {
        Reader msgReader = new InputStreamReader(new FileInputStream(message));
        Writer msgWriter = new OutputStreamWriter(new FileOutputStream(encodedMessage));

        int c, i = 0;
        while ((c = msgReader.read()) != -1) {
            if(c >= 'a' && c <= 'z') {
                c = ((c - 'a' + key[i % key.length]) % 26) + 'a';
                i++;
            } else if(c >= 'A' && c <= 'Z') {
                c = ((c - 'A' + key[i % key.length]) % 26) + 'A';
                i++;
            }
            msgWriter.write(c);
        }

        msgWriter.close();
        msgReader.close();
    }

    /**
     * Guess the key size based on the provided text
     *
     * @param text Text use to guess the key
     * @return The key size
     * @throws IOException
     */
    public int guessKeySize(String text) throws IOException {
        Map<String, List<Integer>> groups = new HashMap<>();
        int maxKeySize = 26;

        for(int size = 2; size <= text.length() / 2; size++) {
            boolean cont = false;
            for(int offset = 0; offset < text.length() - size; offset++, cont = false) {
                String toSearch = text.substring(offset, size + offset);

                if(groups.containsKey(toSearch))
                    continue;

                // Add to groups
                List<Integer> indexes = new ArrayList<>();

                int searchOffset = offset;
                while(searchOffset < text.length() - size) {
                    int foundIndex = text.indexOf(toSearch, searchOffset);

                    if(foundIndex < 0)
                        break;

                    indexes.add(foundIndex);

                    searchOffset = foundIndex + size;
                    cont = true;
                }

                // Add to map
                groups.put(toSearch, indexes);

                if(!cont)
                    break;
            }
        }

        int[] values = new int[maxKeySize];
        for(Map.Entry<String, List<Integer>> entry : groups.entrySet()) {
            if(entry.getValue().size() <= 1)
                continue;

            int globalOffset = entry.getValue().get(0);

            for(int i = 1; i < entry.getValue().size(); i++) {
                int difference = entry.getValue().get(i) - globalOffset;

                for(int j = values.length - 1; j >= 2; j--) {
                    if((difference % j) == 0) {
                        values[j]++;
                    }
                }

                globalOffset += difference + entry.getKey().length();
            }
        }

        int max = Integer.MIN_VALUE, value = 0;
        for (int i = 0; i < values.length; i++) {
            if(values[i] > max) {
                max = values[i];
                value = i;
            }
        }

        return value;
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
        writeKey(key, keyFile);
    }

    private void writeKey(int[] key, File keyFile) throws IOException {
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

    /**
     * Guess the key based on the provided text
     *
     * @param textFile File used to guess the key
     * @param keyFile Where to write the key
     * @throws IOException
     */
    public void guessKey(File textFile, File keyFile) throws IOException {
        String text = Utils.readFile(textFile).toLowerCase().replaceAll("[^a-z]+", "");
        int keySize = guessKeySize(text);

        int[] key = new int[keySize];

        for (int i = 0; i < keySize; i++) {
            Map<Character, Integer> frequencies = new HashMap<>();

            for (int j = i; j < text.length(); j += keySize) {
                char c = text.charAt(j);
                Integer frequency = frequencies.get(c);

                frequencies.put(c, frequency != null ? frequency + 1 : 1);
            }

            char c = '-';
            int max = Integer.MIN_VALUE;
            for (Map.Entry<Character, Integer> entry : frequencies.entrySet()) {
                if(entry.getValue() > max) {
                    max = entry.getValue();
                    c = entry.getKey();
                }
            }

            int dec = c - 'e';
            while(dec < 0)
                dec += 26;

            key[i] = dec;
        }

        writeKey(key, keyFile);
    }

}
