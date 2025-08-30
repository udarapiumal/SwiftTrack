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

    @PostMapping("/order/cancel")
    public String cancelOrder(@RequestParam String orderId){
        return cmsClientService.cancelOrder(orderId);
    }

    @GetMapping("/client/{clientId}")
    public GetClientDetailsResponse getClientDetails(@PathVariable String clientId){
        return cmsClientService.getClientDetails(clientId);
    }
}
