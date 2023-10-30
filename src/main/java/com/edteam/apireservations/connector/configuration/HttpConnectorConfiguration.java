package com.edteam.apireservations.connector.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
@ConfigurationProperties(prefix = "http-connector") // http-connector en application.yml
public class HttpConnectorConfiguration {

    private HashMap<String, HostConfiguration> hosts;

    public HashMap<String, HostConfiguration> getHosts() {
        return hosts;
    }

    public void setHosts(HashMap<String, HostConfiguration> hosts) {
        this.hosts = hosts;
    }
}
