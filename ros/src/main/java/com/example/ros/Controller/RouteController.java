package com.example.ros.Controller;

import com.example.ros.Dto.RouteRequest;
import com.example.ros.Dto.RouteResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ros")
public class RouteController {

    @PostMapping("/optimize")
    public RouteResponse optimizeRoute(@RequestBody RouteRequest request) {
        // Simulate route optimization logic
        RouteResponse response = new RouteResponse();
        response.setDriverId(request.getDriverId());

        StringBuilder route = new StringBuilder("Optimized route for addresses: ");
        if (request.getAddresses() != null && !request.getAddresses().isEmpty()) {
            route.append(String.join(" -> ", request.getAddresses()));
        } else {
            route.append("No addresses provided");
        }

        response.setRoute(route.toString());
        return response;
    }
}
