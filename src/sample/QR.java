package sample;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;

import com.google.zxing.common.ByteMatrix;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import Tools.TOTP;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

public class QR {

    public static String getRandomSecretKey() {
        SecureRandom secureRandom = new SecureRandom(); // Cryptographically Random Number Generator (RNG)
        byte[] bytes = new byte[20];
        secureRandom.nextBytes(bytes);
        Base32 base32 = new Base32();
        String SecretKey = base32.encodeToString(bytes);
        // make the secret key more human-readable by lower-casing and
        // inserting spaces between each group of 4 characters
        return SecretKey.toLowerCase().replaceAll("(.{4})(?=.{4})", "$1 ");
    }

    /**
     * @param SecretKey Base32 encoded secret key (may have optional whitespace)
     * @param userid The user's account name. e.g. an email address or a username
     * @param issuer The organization managing this account
     * @see https://github.com/google/google-authenticator/wiki/Key-Uri-Format
     **/
    public static String getGoogleAuthenticatorBarCode(String SecretKey, String userid, String issuer) {
        String normalizedBase32Key = SecretKey.replace(" ", "").toUpperCase();
        try { //Encoding URL
            return "otpauth://totp/"
                    + URLEncoder.encode(issuer + ":" + userid, "UTF-8").replace("+", "%20")
                    + "?secret=" + URLEncoder.encode(normalizedBase32Key, "UTF-8").replace("+", "%20")
                    + "&issuer=" + URLEncoder.encode(issuer, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void createQRCode(String barCodeData, String filePath, int height, int width)
            throws WriterException, IOException {
        ByteMatrix matrix = new MultiFormatWriter().encode(barCodeData, BarcodeFormat.QR_CODE,
                width, height);
        try (FileOutputStream out = new FileOutputStream(filePath)) {
            MatrixToImageWriter.writeToStream(matrix, "png", out);
        }
    }

    public static String TOTPCode(String SecretKey) {
        String normalizedBase32Key = SecretKey.replace(" ", "").toUpperCase();
        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(normalizedBase32Key);
        String hexKey = Hex.encodeHexString(bytes);
        long Time = (System.currentTimeMillis() / 1000) / 30;
        String HexTime = Long.toHexString(Time);
        return TOTP.generateTOTP(hexKey, HexTime, "6");
    }

    public static void main(String[] args) throws Exception {
        // required for generating the PNG file on a server with no graphics hardware
        System.setProperty("java.awt.headless", "true");

        String SecretKey = getRandomSecretKey();
        String BarCode = getGoogleAuthenticatorBarCode(SecretKey, "email@example.com", "github.com/Novartus");
        String Tmp_Dir = System.getProperty("java.io.tmpdir");
        if (!Tmp_Dir.endsWith(File.separator)) {
            Tmp_Dir += File.separator;
        }
        String QR_Path = Tmp_Dir + "2FA-QR-Code.png";
        createQRCode(BarCode, QR_Path, 400, 400);

        System.out.println("\n Configure the Google Authenticator App by scanning the following QR code image:\n");
        System.out.println(QR_Path + "\n");
        System.out.println("or by manually entering the secret key:\n");
        System.out.println(SecretKey + "\n");
        System.out.println("Then verify that the 6 digit codes generated by Google Authenticator\n"
                            + "are synchronized with the following :\n");

        String lastCode = null;
        while (true) {
            String code = TOTPCode(SecretKey);
            if (!code.equals(lastCode)) {
                System.out.println(code);
            }
            lastCode = code;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {};
        }
    }
}