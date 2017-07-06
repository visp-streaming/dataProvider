package ac.at.tuwien.infosys.visp.dataProvider.entities;


public class EndpointConfiguration {

    private String host;
    private String name;
    private String password;


    public EndpointConfiguration(String host, String name, String password) {
        this.host = host;
        this.name = name;
        this.password = password;
    }

    public EndpointConfiguration() {
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
