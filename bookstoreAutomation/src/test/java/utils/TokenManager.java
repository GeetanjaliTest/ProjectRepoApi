package utils;

import java.io.*;
import java.util.Properties;

public class TokenManager {
    private static final String FILE_PATH = "src/test/resources/token.properties";

    public static void saveToken(String accessToken, String tokenType) {
        Properties props = new Properties();
        props.setProperty("access_token", accessToken);
        props.setProperty("token_type", tokenType);

        try (FileOutputStream out = new FileOutputStream(FILE_PATH)) {
            props.store(out, "Auth Token");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getAccessToken() {
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream(FILE_PATH)) {
            props.load(in);
            return props.getProperty("access_token");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getTokenType() {
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream(FILE_PATH)) {
            props.load(in);
            return props.getProperty("token_type");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
