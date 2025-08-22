package com.example.cms.Config;

import com.example.cms.Endpoint.CmsSoapEndpoint;
import com.example.cms.Repository.ClientRepository;
import com.example.cms.Repository.OrderRepository;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CxfConfig {

    @Autowired
    private Bus bus;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Bean
    public EndpointImpl cmsEndpoint(OrderRepository orderRepository, ClientRepository clientRepository) {
        EndpointImpl endpoint = new EndpointImpl(bus, new CmsSoapEndpoint(orderRepository, clientRepository));
        endpoint.publish("/CmsService");
        return endpoint;
    }

}
