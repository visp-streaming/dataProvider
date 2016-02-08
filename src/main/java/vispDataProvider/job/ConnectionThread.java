package vispDataProvider.job;


import entities.Message;
import vispDataProvider.service.HelloWorldService;

public class ConnectionThread implements Runnable {

    private HelloWorldService service;
    private Message message;

    public ConnectionThread(HelloWorldService service, Message message) {
        this.service = service;
        this.message = message;
    }

    @Override
    public void run() {
        service.hello(message);
    }
}
