package com.example.cms.Endpoint;

import com.example.cms.Service.CmsClientService;
import com.example.cms.generated.*;
import jakarta.jws.WebService;
import org.springframework.stereotype.Service;

@WebService(
        endpointInterface = "com.example.cms.generated.CmsPort",
        serviceName = "CmsService",
        portName = "CmsPort",
        targetNamespace = "http://example.com/cms"
)
@Service
public class CmsSoapEndpoint implements CmsPort {

    private final CmsClientService cmsClientService;

    public CmsSoapEndpoint(CmsClientService cmsClientService) {
        this.cmsClientService = cmsClientService;
    }

    @Override
    public SubmitOrderResponse submitOrder(SubmitOrderRequest request) {
        String status = cmsClientService.submitOrder(
                request.getOrderId(),
                request.getClientId(),
                request.getItems()
        );
        SubmitOrderResponse response = new SubmitOrderResponse();
        response.setStatus(status);
        return response;
    }

    @Override
    public GetOrderStatusResponse getOrderStatus(GetOrderStatusRequest request) {
        String status = cmsClientService.getOrderStatus(request.getOrderId());
        GetOrderStatusResponse response = new GetOrderStatusResponse();
        response.setStatus(status);
        return response;
    }

    @Override
    public CancelOrderResponse cancelOrder(CancelOrderRequest request) {
        String status = cmsClientService.cancelOrder(request.getOrderId());
        CancelOrderResponse response = new CancelOrderResponse();
        response.setStatus(status);
        return response;
    }

    @Override
    public GetClientDetailsResponse getClientDetails(GetClientDetailsRequest request) {
        return cmsClientService.getClientDetails(request.getClientId());
    }
}
