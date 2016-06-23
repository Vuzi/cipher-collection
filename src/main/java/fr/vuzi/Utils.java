package fr.vuzi;

import java.io.*;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static String readFile(File f) throws IOException {
        FileInputStream fis = new FileInputStream(f);
        byte[] data = new byte[(int) f.length()];
        fis.read(data);
        fis.close();

        return new String(data, "UTF-8");
    }

    public static void writeFile(String content, File f) throws IOException {
        if(f.exists())
            f.delete();
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(content.getBytes());
        fos.close();
    }

    public static String filter(String s, String filter) {

        for(int i = 0; i < s.length(); i++) {
            if(!filter.contains(s.charAt(i) + ""))
                s = s.replace(s.charAt(i) + "", "");
        }
        return s;
    }

    public final static double[] englishFrequencies =
            {0.08167, 0.01492, 0.02782, 0.04253, 0.12702, 0.02228,
             0.02015, 0.06094, 0.06966, 0.00153, 0.00772, 0.04025,
             0.02406, 0.06749, 0.07507, 0.01929, 0.00095, 0.05987,
             0.06327, 0.09056, 0.02758, 0.00978, 0.02360, 0.00150,
             0.01974,0.00074};

    public static double getEntropy(String str) {

        double res = 0;

        for(char c : str.toCharArray()) {
            if ('a' <= c && 'z' >= c)
                res += -Math.log(englishFrequencies[c - 'a']);
        }

        return res;
    }

    public static Map<Character, Double> getFrequencies(String alphabet, InputStream stream) throws IOException {
        Reader msgReader = new InputStreamReader(stream);

        // Calculate frequencies
        Map<Character, Integer> occurrences = new HashMap<>();

        for(char c : alphabet.toCharArray()) {
            occurrences.put(c, 0);
        }

        int c, size = 0;
        while ((c = msgReader.read()) != -1) {
            Integer occurrence = occurrences.get((char) c);
            if(occurrence != null)
                occurrences.put((char) c, occurrence + 1);
            size++;
        }

        Map<Character, Double> frequencies = new HashMap<>();

        for(Map.Entry<Character, Integer> entry : occurrences.entrySet())
            frequencies.put(entry.getKey(), (double)entry.getValue() / size);

        msgReader.reset();

        return frequencies;
    }

    public static Map<Character, Double> getFrequencies(String s) {

        Map<Character, Integer> occ = new HashMap<>();

        for(char c : s.toCharArray()) {
            if (occ.containsKey(c))
                occ.put(c, occ.get(c) + 1);
            else
                occ.put(c, 1);
        }

        Map<Character, Double> freq = new HashMap<>();

        for(char c : occ.keySet()) {
            freq.put(c, (double)occ.get(c) / s.length());
        }

        return freq;
    }

    public static <T> T[] shuffle(T[] toShuffle) {

        SecureRandom random = new SecureRandom();
        for(int i = 0; i < toShuffle.length - 1 ; i++){
            int j = random.nextInt(toShuffle.length - i);
            T temp = toShuffle[i];
            toShuffle[i] = toShuffle[i + j];
            toShuffle[i + j] = temp;
        }

        return toShuffle;
    }

    public static char[] shuffle(char[] toShuffle) {

        SecureRandom random = new SecureRandom();
        for(int i = 0; i < toShuffle.length - 1 ; i++){
            int j = random.nextInt(toShuffle.length - i);
            char temp = toShuffle[i];
            toShuffle[i] = toShuffle[i + j];
            toShuffle[i + j] = temp;
        }

        return toShuffle;
    }

    public static int[] shuffle(int[] toShuffle) {

        SecureRandom random = new SecureRandom();
        for(int i = 0; i < toShuffle.length - 1 ; i++){
            int j = random.nextInt(toShuffle.length - i);
            int temp = toShuffle[i];
            toShuffle[i] = toShuffle[i + j];
            toShuffle[i + j] = temp;
        }

        return toShuffle;
    }

    public static int[] invert(int[] toInvert) {
        for(int i = 0; i < toInvert.length / 2; i++) {
            int temp = toInvert[i];
            toInvert[i] = toInvert[toInvert.length - i - 1];
            toInvert[toInvert.length - i - 1] = temp;
        }

        return toInvert;
    }
}