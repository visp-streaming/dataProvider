package vispDataProvider.util;

import org.springframework.stereotype.Service;
import vispDataProvider.entities.EndpointConfiguration;

@Service
public class EndpointConfigurationService {

    private String uri;
    private String name = "visp";
    private String password = "visp";

    public void storeData(EndpointConfiguration conf) {
        this.uri = conf.getUri();
        this.name = conf.getName();
        this.password = conf.getPassword();
    }

    public EndpointConfiguration getConfiguration() {
        return new EndpointConfiguration(uri, name, password);
    }

}

