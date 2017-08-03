package ac.at.tuwien.infosys.visp.dataProvider.util;

import ac.at.tuwien.infosys.visp.dataProvider.entities.EndpointConfiguration;
import org.springframework.stereotype.Service;

@Service
public class EndpointConfigurationService {

    private String host;
    private String name = "visp";
    private String password = "visp";
    private String slackchannel = "evaluation";
    private String slacktoken = "";

    public void storeData(EndpointConfiguration conf) {
        this.host = conf.getHost();
        this.name = conf.getName();
        this.password = conf.getPassword();
        this.slackchannel = conf.getSlackchannel();
        this.slacktoken = conf.getSlacktoken();
    }

    public EndpointConfiguration getConfiguration() {
        return new EndpointConfiguration(host, name, password, slackchannel, slacktoken);
    }

}

