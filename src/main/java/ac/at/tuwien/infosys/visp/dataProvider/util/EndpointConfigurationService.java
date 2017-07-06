package ac.at.tuwien.infosys.visp.dataProvider.util;

import ac.at.tuwien.infosys.visp.dataProvider.entities.EndpointConfiguration;
import org.springframework.stereotype.Service;

@Service
public class EndpointConfigurationService {

    private String host;
    private String name = "visp";
    private String password = "visp";

    public void storeData(EndpointConfiguration conf) {
        this.host = conf.getHost();
        this.name = conf.getName();
        this.password = conf.getPassword();
    }

    public EndpointConfiguration getConfiguration() {
        return new EndpointConfiguration(host, name, password);
    }

}

