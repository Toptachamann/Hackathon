package test.back.message;

import org.json.JSONObject;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<Message> {

    @Override
    public void init(final EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public String encode(final Message message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("author", message.getAuthor());
        jsonObject.put("date", message.getDate());
        jsonObject.put("text", message.getText());
        return jsonObject.toString();
    }

}