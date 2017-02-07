package ac.at.tuwien.infosys.visp.dataProvider.entities;


public class EndpointConfiguration {

    private String uri;
    private String name;
    private String password;


    public EndpointConfiguration(String uri, String name, String password) {
        this.uri = uri;
        this.name = name;
        this.password = password;
    }

    public EndpointConfiguration() {
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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
