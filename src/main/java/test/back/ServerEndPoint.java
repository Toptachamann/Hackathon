package test.back;

import test.back.message.Message;
import test.back.message.MessageDecoder;
import test.back.message.MessageEncoder;

import static java.lang.String.format;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint(value = "/chat", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class ServerEndPoint {
//    static Set<Session> chatRoomUsers = Collections.synchronizedSet(new HashSet<>());

//    @OnOpen
//    public void handleOpen(Session userSession) throws IOException, EncodeException {
//       chatRoomUsers.add(userSession);
//    }
//
//    @OnClose
//    public void handleClose(Session userSession) throws IOException, EncodeException {
//        chatRoomUsers.remove(userSession);
//        String leftMessage = format("%s left the chat room.", (String) userSession.getUserProperties().get("username"));
//        for (Session user : chatRoomUsers) {
//            Message message = new Message("Server",new SimpleDateFormat("HH:mm:ss").format(new Date()).toString(), leftMessage);
//            user.getBasicRemote().sendObject(message); //Message to JSON
//        }
//    }

//    @OnMessage
//    public void handleMessage(Message message/*JSON to Message*/, Session userSession) throws IOException, EncodeException {
//        String username = (String) userSession.getUserProperties().get("username");
//        if (username == null) {
//            userSession.getUserProperties().put("username", message.getAuthor());
//        }
//
//        for (Session user : chatRoomUsers) {
//            if (!userSession.getId().equals(user.getId())) { // do not resend the message to its author
//                user.getBasicRemote().sendObject(message); //Message to JSON
//            }
//        }
//    }
}
