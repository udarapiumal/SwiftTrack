package com.example.cms.Client;

import com.example.cms.generated.*;
import jakarta.xml.ws.BindingProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@Component
public class CmsSoapClient {

    private final CmsPort cmsPort;

    public CmsSoapClient(@Value("${cms.soap.endpoint.url:http://localhost:8081/services/CmsService}") String endpointUrl) {
        CmsService service = new CmsService();
        this.cmsPort = service.getCmsPort();

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

    public SubmitOrderResponse submitOrder(String orderId, String clientId, String items) {
        SubmitOrderRequest request = new SubmitOrderRequest();
        request.setOrderId(orderId);
        request.setClientId(clientId);
        request.setItems(items);
        return cmsPort.submitOrder(request);
    }

    public CancelOrderResponse cancelOrder(String orderId) {
        CancelOrderRequest request = new CancelOrderRequest();
        request.setOrderId(orderId);
        return cmsPort.cancelOrder(request);
    }

    public GetClientDetailsResponse getClientDetails(String clientId) {
        GetClientDetailsRequest request = new GetClientDetailsRequest();
        request.setClientId(clientId);
        return cmsPort.getClientDetails(request);
    }
}
