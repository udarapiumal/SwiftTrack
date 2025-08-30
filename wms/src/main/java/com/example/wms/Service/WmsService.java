package com.example.wms.Service;

import com.example.wms.Dto.PackageUpdate;
import com.example.wms.Messaging.PackageUpdatePublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WmsService {

    private final PackageUpdatePublisher publisher;

    public WmsService(PackageUpdatePublisher publisher) {
        this.publisher = publisher;
    }

    /**
     * Called when CMS order is created
     * Publishes "RECEIVED" updates for all packages in the order
     */
    public void processOrder(String orderId, List<String> items) {
        for (int i = 0; i < items.size(); i++) {
            String packageId = orderId + "-PKG" + (i + 1);
            PackageUpdate update = new PackageUpdate(packageId, orderId, "RECEIVED", LocalDateTime.now());
            publisher.publishUpdate(update);

            try { Thread.sleep(200); } catch (InterruptedException ignored) {}
        }
    }
}
