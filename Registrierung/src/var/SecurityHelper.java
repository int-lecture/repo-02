package var;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Helper functions to deal with passwords.
 * @author Thomas Smits
 */
public class SecurityHelper {

    /** Number of rounds in hashing the password. */
    public static final int ITERATIONS = 1000;

    /**
     * Hash the given Password.
     *
     * @param password the cleartext password to hash.
     * @return the hashed passworrd.
     *
     * @throws NoSuchAlgorithmException Crypto API issue
     * @throws InvalidKeySpecException Crypto API issue
     */
    public static String hashPassword(String password)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, ITERATIONS, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return ITERATIONS + ":" + encode(salt) + ":" + encode(hash);
    }

    /**
     * Create a random salt with a secure random generator.
     *
     * @return the salt (16 Bytes).
     * @throws NoSuchAlgorithmException Crypto API problems
     */
    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    /**
     * Compare the given password with the hashed password.
     *
     * @param password the password typed in by the user.
     * @param storedPassword the hashed password.
     * @return {@code true} if the passwords match, otherwise {@code false}.
     */
    public static boolean validatePassword(String password,
            String storedPassword) {

        if (password == null || storedPassword == null) {
            return false;
        }

        try {
            String[] parts = storedPassword.split(":");
            int iterations = Integer.parseInt(parts[0]);
            byte[] salt = decode(parts[1]);
            byte[] hash = decode(parts[2]);

            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, hash.length * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] testHash = skf.generateSecret(spec).getEncoded();

            return Arrays.equals(testHash, hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            return false;
        }
    }

    /**
     * Convert the Base64 input to bytes.
     *
     * @param input the input (Base64 encoded).
     * @return the content as bytes.
     */
    private static byte[] decode(String input)  {
        return Base64.getDecoder().decode(input);
    }

    /**
     * Convert bytes to Base 64.
     *
     * @param array the bytes
     * @return the input encoded in Base64.
     */
    private static String encode(byte[] array) {
        return Base64.getEncoder().encodeToString(array);
    }
}