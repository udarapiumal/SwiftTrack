package com.example.ros.Service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PackageStatusService {

    private final Map<String, Set<String>> pendingPackages = new HashMap<>();

    public synchronized void registerPackages(String orderId, List<String> packageIds) {
        pendingPackages.put(orderId, new HashSet<>(packageIds));
    }

    public synchronized boolean markPackageReady(String orderId, String packageId) {
        Set<String> packages = pendingPackages.get(orderId);
        if (packages != null) {
            packages.remove(packageId);
            if (packages.isEmpty()) {
                pendingPackages.remove(orderId);
                return true;
            }
        }
        return false;
    }
}
