// FileIntegrityChecker.java

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Scanner;

public class FileIntegrityChecker {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("1. Generate file hash");
        System.out.println("2. Verify file hash");
        System.out.print("Choice: ");
        int choice = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter file path: ");
        String path = sc.nextLine();

        try {
            String hash = getFileChecksum(path);
            if (choice == 1) {
                try (FileOutputStream fos = new FileOutputStream(path + ".sha256")) {
                    fos.write(hash.getBytes());
                }
                System.out.println("SHA-256 hash saved to " + path + ".sha256");
            } else if (choice == 2) {
                System.out.print("Enter stored SHA-256 hash: ");
                String storedHash = sc.nextLine().trim();
                if (storedHash.equalsIgnoreCase(hash)) {
                    System.out.println("✅ File is intact. Hash matches.");
                } else {
                    System.out.println("❌ WARNING: File has been modified!");
                    System.out.println("Current hash: " + hash);
                }
            } else {
                System.out.println("Invalid choice.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static String getFileChecksum(String filePath) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        FileInputStream fis = new FileInputStream(filePath);

        byte[] byteArray = new byte[4096];
        int bytesCount;
        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        }
        fis.close();

        byte[] bytes = digest.digest();

        // Convert to hex string
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
