package vispDataProvider.job;


import entities.Message;
import vispDataProvider.dataSender.MessageSender;

public class ConnectionThread implements Runnable {

    private MessageSender service;
    private Message message;

    public ConnectionThread(MessageSender service, Message message) {
        this.service = service;
        this.message = message;
    }

    @Override
    public void run() {
        service.sendMessage(message);
    }
}
