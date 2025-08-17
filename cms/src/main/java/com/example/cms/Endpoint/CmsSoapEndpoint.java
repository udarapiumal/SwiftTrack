package com.example.cms.endpoint;

import com.example.cms.generated.GetOrderStatusRequest;
import com.example.cms.generated.GetOrderStatusResponse;
import com.example.cms.generated.CmsPort;
import org.springframework.stereotype.Service;

import jakarta.jws.WebService;

@WebService(
        endpointInterface = "com.example.cms.generated.CmsPort",
        serviceName = "CmsService",
        portName = "CmsPort",
        targetNamespace = "http://example.com/cms"
)
@Service
public class CmsSoapEndpoint implements CmsPort {

    @Override
    public GetOrderStatusResponse getOrderStatus(GetOrderStatusRequest request) {
        GetOrderStatusResponse response = new GetOrderStatusResponse();
        response.setStatus("Order " + request.getOrderId() + " is COMPLETED");
        return response;
    }
}