package ac.at.tuwien.infosys.visp.dataProvider.util;

import ac.at.tuwien.infosys.visp.dataProvider.entities.EndpointConfiguration;
import org.springframework.stereotype.Service;

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

