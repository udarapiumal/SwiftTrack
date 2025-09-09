package com.example.cms.Controller;

import com.example.cms.Service.CmsClientService;
import com.example.cms.generated.GetClientDetailsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cms")
public class CmsController {

    private final CmsClientService cmsClientService;

    public CmsController(CmsClientService cmsClientService){
        this.cmsClientService = cmsClientService;
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<String> getOrderStatus(@PathVariable String orderId){
        String status = cmsClientService.getOrderStatus(orderId);
        if(status == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Order not found: " + orderId);
        }
        return ResponseEntity.ok(status);
    }

    @PostMapping("/order/submit")
    public String submitOrder(@RequestParam String orderId,
                              @RequestParam String clientId,
                              @RequestParam String items){
        return cmsClientService.submitOrder(orderId, clientId, items);
    }
    @GetMapping("/client/{clientId}/orders")
    public ResponseEntity<?> getClientOrders(@PathVariable String clientId) {
        try {
            return ResponseEntity.ok(cmsClientService.getOrdersForClient(clientId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No orders found for client: " + clientId);
        }
    }


    @PostMapping("/order/cancel")
    public String cancelOrder(@RequestParam String orderId){
        return cmsClientService.cancelOrder(orderId);
    }

    @GetMapping("/client/{clientId}")
    public GetClientDetailsResponse getClientDetails(@PathVariable String clientId){
        return cmsClientService.getClientDetails(clientId);
    }

    // ✅ NEW: Create client directly (called by signup service)
    @PostMapping("/client/create")
    public ResponseEntity<String> createClient(@RequestParam String clientId,
                                               @RequestParam String clientName,
                                               @RequestParam(required = false) String email,
                                               @RequestParam(required = false) String phone) {
        boolean success = cmsClientService.createClient(clientId, clientName, email, phone);
        if (success) {
            return ResponseEntity.ok("Client created successfully with ID: " + clientId);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create client with ID: " + clientId);
        }
    }
}
