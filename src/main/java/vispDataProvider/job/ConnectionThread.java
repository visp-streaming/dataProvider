package vispDataProvider.job;


import entities.Message;
import vispDataProvider.dataSender.MessageSender;

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
