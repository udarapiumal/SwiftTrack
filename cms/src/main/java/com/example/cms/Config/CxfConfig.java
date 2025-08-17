package com.example.cms.Config;

import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.cms.endpoint.CmsSoapEndpoint;

@Configuration
public class CxfConfig {

    @Autowired
    private Bus bus;

    @Bean
    public EndpointImpl cmsEndpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, new CmsSoapEndpoint());
        endpoint.publish("/CmsService");
        return endpoint;
    }
}