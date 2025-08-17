package com.example.cms.Controller;

import com.example.cms.Service.CmsClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CmsController {

    private final CmsClientService cmsClientService;

    public CmsController(CmsClientService cmsClientService){
        this.cmsClientService = cmsClientService;
    }

    @GetMapping("/cms/order/{orderId}")
    public String getOrderStatus(@PathVariable String orderId){
        return cmsClientService.getOrderStatus(orderId);
    }
}
