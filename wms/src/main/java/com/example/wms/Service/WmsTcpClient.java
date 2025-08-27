package com.example.wms.Service;

import com.example.wms.Dto.PackageUpdate;
import com.example.wms.Messaging.PackageUpdatePublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct; // <-- fixed import
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.time.LocalDateTime;

@Service
public class WmsTcpClient {

    private final PackageUpdatePublisher publisher;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Replace with WMS TCP host/port
    private final String host = "localhost";
    private final int port = 5555;

    public WmsTcpClient(PackageUpdatePublisher publisher) {
        this.publisher = publisher;
    }

    @PostConstruct
    public void startListening() {
        new Thread(this::listenToWms).start();
    }

    private void listenToWms() {
        try (Socket socket = new Socket(host, port);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String line;
            while ((line = reader.readLine()) != null) {
                // Assume WMS sends JSON string for each package update
                PackageUpdate update = objectMapper.readValue(line, PackageUpdate.class);
                // Add timestamp if not present
                if (update.getTimestamp() == null) update.setTimestamp(LocalDateTime.now());
                publisher.publishUpdate(update);
            }

        } catch (Exception e) {
            System.err.println("❌ WMS TCP Client error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
