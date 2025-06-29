package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BookManager {

    private static final String BOOK_ID_FILE = ConfigManager.getProperty("book.id.path");

    public static void saveBookId(int bookId) {
        try (FileWriter writer = new FileWriter(BOOK_ID_FILE)) {
            writer.write(String.valueOf(bookId));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getBookId() {
        try {
            return Integer.parseInt(Files.readString(Paths.get(BOOK_ID_FILE)).trim());
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return -1; // fallback
        }
    }

    public static int loadBookId() {
        try (BufferedReader reader = new BufferedReader(new FileReader(BOOK_ID_FILE))) {
            return Integer.parseInt(reader.readLine());
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load book ID from file");
        }
    }
}
