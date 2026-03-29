package voidbreaker.prolog;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.List;

public class HostUpdator {
    public static void main(String[] args) {
         // Detect current IP address
        try {
            String currentIp = getLocalIpAddress();
            System.out.println("Detected current IP: " + currentIp);
            updateConfigJson(currentIp);
            updateMyIni();
            System.out.println("Update process completed successfully.");
        } catch (SocketException e) {
            System.out.println("Error detecting local IP: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("File I/O error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String getLocalIpAddress() throws SocketException {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface ni = networkInterfaces.nextElement();
            Enumeration<InetAddress> inetAddresses = ni.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress ia = inetAddresses.nextElement();
                if (!ia.isLoopbackAddress() && ia instanceof Inet4Address) {
                    System.out.println("Found non-loopback IPv4 address: " + ia.getHostAddress());
                    return ia.getHostAddress();
                }
            }
        }
        System.out.println("No non-loopback IPv4 address found, falling back to 127.0.0.1");
        return "127.0.0.1";
    }

    private static void updateConfigJson(String newIp) throws IOException {
        Path path = Paths.get("C:\\ProgramData\\prolog\\configs\\config.json");
        String defaultJson = "[{\"ip\": \"" + newIp + "\", \"username\": \"prologDBadmin\", \"password\": \"hfpNLHEv44]wQ(oJ\", \"database\": \"prologdb\", \"port\": \"3306\"}]";

        if (!Files.exists(path)) {
            System.out.println("config.json does not exist. Creating directory and file.");
            Files.createDirectories(path.getParent());
            Files.writeString(path, defaultJson, StandardCharsets.UTF_8);
            System.out.println("Created config.json with IP: " + newIp);
        } else {
            System.out.println("config.json exists. Reading content.");
            String content = Files.readString(path, StandardCharsets.UTF_8);
            System.out.println("Original content: " + content);
            String updatedContent = content.replaceAll("\"ip\"\\s*:\\s*\"[^\"]*\"", "\"ip\": \"" + newIp + "\"");
            Files.writeString(path, updatedContent, StandardCharsets.UTF_8);
            System.out.println("Updated config.json with new IP: " + newIp);
            System.out.println("Updated content: " + updatedContent);
        }
    }

    private static void updateMyIni() throws IOException {
        Path path = Paths.get("C:\\xampp\\mysql\\bin\\my.ini");

        if (!Files.exists(path)) {
            System.out.println("my.ini does not exist. No changes made.");
            return;
        }

        System.out.println("my.ini exists. Reading lines.");
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        boolean foundAndUpdated = false;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.startsWith("# bind-address=") && line.contains("127.0.0.1")) {
                lines.set(i, "# bind-address=0.0.0.0");
                foundAndUpdated = true;
                System.out.println("Found and replaced bind-address line at index " + i);
            }
        }

        if (foundAndUpdated) {
            Files.write(path, lines, StandardCharsets.UTF_8);
            System.out.println("Updated my.ini with bind-address=0.0.0.0");
        } else {
            System.out.println("No matching bind-address=127.0.0.1 line found in my.ini. No changes made.");
        }
    }

    public static void setConfigIP(String newIp) throws IOException {
        updateConfigJson(newIp);
    }
}