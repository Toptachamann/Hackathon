package test.back.message;

import org.json.JSONObject;

import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageDecoder implements Decoder.Text<Message> {

    @Override
    public void init(final EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public Message decode(final String textMessage) {
        System.out.println(textMessage);
        JSONObject jsonObject = new JSONObject(textMessage);
        Message message = new Message(jsonObject.getString("author"), new SimpleDateFormat("HH:mm:ss").format(new Date()).toString(), jsonObject.getString("text"));
        return message;
    }

    @Override
    public boolean willDecode(final String s) {
        return true;
    }

}