package ac.at.tuwien.infosys.visp.dataProvider.entities;


public class EndpointConfiguration {

    private String host;
    private String name;
    private String password;
    private String slackchannel;
    private String slacktoken;


    public EndpointConfiguration(String host, String name, String password, String slackchannel, String slacktoken) {
        this.host = host;
        this.name = name;
        this.password = password;
        this.slackchannel = slackchannel;
        this.slacktoken = slacktoken;
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

    public String getSlackchannel() {
        return slackchannel;
    }

    public void setSlackchannel(String slackchannel) {
        this.slackchannel = slackchannel;
    }

    public String getSlacktoken() {
        return slacktoken;
    }

    public void setSlacktoken(String slacktoken) {
        this.slacktoken = slacktoken;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
