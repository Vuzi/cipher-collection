package fr.vuzi;

import fr.vuzi.cipher.*;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        try {
            ICipher cipher = new VigenereCipher();

            cipher.generateKey(new File("key.txt"));

            //((TranspositionCipher) cipher).readKey(new File("key.txt"));

            cipher.encode(new File("msg.txt"), new File("key.txt"), new File("encoded.txt"));
            cipher.decode(new File("encoded.txt"), new File("key.txt"), new File("msg2.txt"));

            /*
            ICipher cipher = new CaesarCipher();

            cipher.generateKey(new File("key.txt"));

            cipher.encode(new File("msg.txt"), new File("key.txt"), new File("encoded.txt"));
            cipher.decode(new File("encoded.txt"), new File("key.txt"), new File("msg2.txt"));*/
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
        try {
            testSubstitutionCipher();
            testSubstitutionCipher2();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

/*
        String textFreq = "";

        // Generate the target frequencies
        try {
            Stream<String> lines = Files.lines(Paths.get("C:\\Users\\vuzi\\Desktop\\texte.txt"), Charset
                    .defaultCharset());
            textFreq = String.join("", lines.collect(Collectors.toList())).toLowerCase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String text = ("The feudal system of industry, in which industrial production was " +
                "monopolized by closed guilds, now no longer suffices for the growing wants " +
                "of the new markets. The manufacturing system took its place. The " +
                "guild-masters were pushed aside by the manufacturing middle class; division " +
                "of labor between the different corporate guilds vanished in the face of " +
                "division of labor in each single workshop." +
                "Meantime, the markets kept ever growing, the demand ever rising. Even " +
                "manufacturers no longer sufficed. Thereupon, steam and machinery " +
                "revolutionized industrial production. The place of manufacture was taken by " +
                "the giant, MODERN INDUSTRY; the place of the industrial middle class by " +
                "industrial millionaires, the leaders of the whole industrial armies, the " +
                "modern bourgeois." +
                "Modern industry has established the world market, for which the discovery of " +
                "America paved the way. This market has given an immense development to " +
                "commerce, to navigation, to communication by land. This development has, in " +
                "turn, reacted on the extension of industry; and in proportion as industry, " +
                "commerce, navigation, railways extended, in the same proportion the " +
                "bourgeoisie developed, increased its capital, and pushed into the background " +
                "every class handed down from the Middle Ages.").toLowerCase();

        SubstitutionCipherOld cipher = new SubstitutionCipherOld();
        String key = cipher.generateKey(null);

        String encodedMessage = cipher.encode(text, key);

        /*String keyGuessed = cipher.guessKey(encodedMessage,
                Utils.getFrequencies(Utils.filter(textFreq, SubstitutionCipherOld.alphabet)));
        String keyGuessed = cipher.guessKey(encodedMessage);

        System.out.println(key + "\n" + keyGuessed);

        System.out.println("Text : \n" + text + "\n");
        System.out.println("Text : \n" + cipher.decode(encodedMessage, keyGuessed));
        System.out.println();
        System.out.println("Text entropy : " + Utils.getEntropy(text));
        System.out.println("Text entropy : " + Utils.getEntropy(cipher.decode(encodedMessage, keyGuessed)));*/
    }

    /*
    public static void testSubstitutionCipher2() throws IOException {
        System.out.println("Substitution cipher 2:");

        String message = "hello world !! how are you today ?";

        SubstitutionCipherOld cipher = new SubstitutionCipherOld();

        // Get the key
        String key = cipher.generateKey(null);

        System.out.println("Key : " + key);

        // Write the key
        cipher.writeKey(key, new File("key"));

        // Read the key
        String readKey = cipher.readKey(new File("key"));

        assert(readKey.equals(key));

        // Encode the message
        System.out.println("Message : " + message);

        String encodedMessage = cipher.encode(message, key);

        System.out.println("Message encoded : " + encodedMessage);

        // Write the message
        cipher.writeMessage(message, new File("message"));

        // Read message
        String readMessage = cipher.readMessage(new File("message"));

        assert(readMessage.equals(message));

        // Decode the message
        String decodedMessage = cipher.decode(encodedMessage, key);

        System.out.println("Message decoded : " + decodedMessage);

        assert(message.equals(decodedMessage));
    }

    public static void testSubstitutionCipher() {
        System.out.println("Substitution cipher:");

        String message = "hello world !! how are you today ?";

        SubstitutionCipherOld cipher = new SubstitutionCipherOld();

        // Get the key
        String key = cipher.generateKey(null);

        System.out.println("Key : " + key);

        // Encode the message
        System.out.println("Message : " + message);

        String encodedMessage = cipher.encode(message, key);

        System.out.println("Message encoded : " + encodedMessage);

        // Decode the message
        String decodedMessage = cipher.decode(encodedMessage, key);

        System.out.println("Message decoded : " + decodedMessage);

        assert(message.equals(decodedMessage));
    }*/

}
