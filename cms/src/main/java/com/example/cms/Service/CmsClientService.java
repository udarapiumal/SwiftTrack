package com.example.cms.Service;

import com.example.cms.Client.CmsSoapClient;
import com.example.cms.generated.GetOrderStatusResponse;
import org.springframework.stereotype.Service;

@Service
public class CmsClientService {
    private final CmsSoapClient cmsSoapClient;

    public CmsClientService(CmsSoapClient cmsSoapClient){
        this.cmsSoapClient = cmsSoapClient;
    }

    public String getOrderStatus(String orderId){
        GetOrderStatusResponse response = cmsSoapClient.getOrderStatus(orderId);
        return response.getStatus();
    }
}