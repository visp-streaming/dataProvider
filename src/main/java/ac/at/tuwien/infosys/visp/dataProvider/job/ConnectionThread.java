package ac.at.tuwien.infosys.visp.dataProvider.job;


import ac.at.tuwien.infosys.visp.common.Message;
import ac.at.tuwien.infosys.visp.dataProvider.dataSender.MessageSender;

public class ConnectionThread implements Runnable {

    private MessageSender service;
    private Message message;
    private String queue;

    public ConnectionThread(MessageSender service, Message message, String queue) {
        this.service = service;
        this.message = message;
        this.queue = queue;
    }

    @Override
    public void run() {
        service.sendMessage(message, queue);
    }
}
