package com.example.cms.Config;

import com.example.cms.Endpoint.CmsSoapEndpoint;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CxfConfig {

    private final Bus bus;
    private final CmsSoapEndpoint cmsSoapEndpoint;

    public CxfConfig(Bus bus, CmsSoapEndpoint cmsSoapEndpoint) {
        this.bus = bus;
        this.cmsSoapEndpoint = cmsSoapEndpoint;
    }

    @Bean
    public EndpointImpl cmsEndpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, cmsSoapEndpoint);
        endpoint.publish("/CmsService");  // Exposed at /services/CmsService
        return endpoint;
    }
}
