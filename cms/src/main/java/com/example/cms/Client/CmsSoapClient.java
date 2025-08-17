package com.example.cms.Client;

import com.example.cms.generated.CmsService;
import com.example.cms.generated.CmsPort;
import com.example.cms.generated.GetOrderStatusRequest;
import com.example.cms.generated.GetOrderStatusResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.xml.ws.BindingProvider;
import java.util.Map;

@Component
public class CmsSoapClient {

    private final CmsPort cmsPort;

    public CmsSoapClient(@Value("${cms.soap.endpoint.url:http://localhost:8081/services/CmsService}") String endpointUrl) {
        // Initialize the service and get the port
        CmsService service = new CmsService();
        this.cmsPort = service.getCmsPort();

        // Configure the endpoint URL
        BindingProvider bindingProvider = (BindingProvider) cmsPort;
        Map<String, Object> requestContext = bindingProvider.getRequestContext();
        requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointUrl);

        // Set timeouts
        requestContext.put("com.sun.xml.ws.connect.timeout", 30000);
        requestContext.put("com.sun.xml.ws.request.timeout", 60000);
    }

    public GetOrderStatusResponse getOrderStatus(String orderId) {
        GetOrderStatusRequest request = new GetOrderStatusRequest();
        request.setOrderId(orderId);
        return cmsPort.getOrderStatus(request);
    }
}