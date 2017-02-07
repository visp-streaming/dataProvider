package ac.at.tuwien.infosys.visp.dataProvider.dataSender;


import ac.at.tuwien.infosys.visp.common.Message;

public interface MessageSender {

    void sendMessage(Message msg, String queue);

}
