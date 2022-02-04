package guru.springframework.sfgjms.listener;

import guru.springframework.sfgjms.config.JmsConfig;
import guru.springframework.sfgjms.model.HelloWorldMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import java.util.UUID;

@Component //comment out to let artemismq docker to pick us messages
public class HelloMessageListener {

    private final JmsTemplate jmsTemplate;

    public HelloMessageListener(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @JmsListener(destination = JmsConfig.MY_QUEUE)
    public void listen(@Payload HelloWorldMessage helloWorldMessage,
                       @Headers MessageHeaders headers,
                       Message message){

        System.out.println("I got a message!!!");
        System.out.println("This is the payload " + helloWorldMessage);
//        throw new RuntimeException("Error Runtime");

    }


//    org.springframework.messaging.Message testing abstraction btw spring Message and JMS Message activemq
    @JmsListener(destination = JmsConfig.MY_SEND_RCV_QUEUE)
    public void listenForHelloSendReceive(@Payload HelloWorldMessage helloWorldMessage,
                       @Headers MessageHeaders headers, Message message,
                                          org.springframework.messaging.Message springMessage) throws JMSException {

        System.out.println("Got message, this is the payload from send HelloSender" + helloWorldMessage);

        HelloWorldMessage payloadMsg = HelloWorldMessage
                .builder()
                .id(UUID.randomUUID())
                .message("World!")
                .build();

  //      jmsTemplate.convertAndSend((Destination) springMessage.getHeaders().get("jms_replyTo"), "got it");

        jmsTemplate.convertAndSend(message.getJMSReplyTo(), payloadMsg );
        //        throw new RuntimeException("Error Runtime");

    }
}
