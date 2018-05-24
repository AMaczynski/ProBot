import java.io.BufferedReader;
import java.io.IOException;
import com.restfb.*;
import com.restfb.types.send.IdMessageRecipient;
import com.restfb.types.send.Message;
import com.restfb.types.send.SendResponse;
import com.restfb.types.webhook.WebhookEntry;
import com.restfb.types.webhook.WebhookObject;
import com.restfb.types.webhook.messaging.MessagingItem;

@javax.servlet.annotation.WebServlet(name = "Servlet")
public class ProBot extends javax.servlet.http.HttpServlet {
    private String accesToken = "EAAFZAOHwkoYYBALM6aMhu5lEwRS1VgnAYMdBm589yv3I9BN29ZBqXW74780ljIPUAoO2mUgfQ4uOsxSkGpiFFZCh3N6XAZAqAz0U262wvyDJh5Y6ln132vTP6l2suzO3hz3nfBrYvyyIPbEpCcZCpgSvQKUBGIAL9nSiTCILX8wZDZD";
    private String verifyToken = "bot123";

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        StringBuffer stringBuffer = new StringBuffer();
        BufferedReader bufferedReader = request.getReader();
        String line = "";
        while ((line = bufferedReader.readLine()) != null){
            stringBuffer.append(line);
        }
        JsonMapper mapper = new DefaultJsonMapper();
        WebhookObject webhookObject = mapper.toJavaObject(stringBuffer.toString(), WebhookObject.class);

        for (WebhookEntry entry: webhookObject.getEntryList()){
            if (entry.getMessaging() != null){
                for (MessagingItem messagingItem : entry.getMessaging()){
                    String senderId = messagingItem.getSender().getId();
                    IdMessageRecipient recipient = new IdMessageRecipient(senderId);

                    if(messagingItem.getMessage() != null && messagingItem.getMessage().getText() != null){
                        sendMessage(recipient, new Message("Hi"));
                    }
                }
            }
        }
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String hubToken = request.getParameter("hub.verify_toekn");
        String hubChallenge = request.getParameter("hub.challenge");

        if(verifyToken.equals(hubToken)){
            response.getWriter().write(hubChallenge);
            response.getWriter().flush();
            response.getWriter().close();
        } else {
            response.getWriter().write("incorrect token");
        }
    }

    void sendMessage(IdMessageRecipient recipient, Message message){
        FacebookClient pageClient = new DefaultFacebookClient(accesToken, Version.VERSION_2_6);

        SendResponse resp = pageClient.publish("me/messages", SendResponse.class,
                Parameter.with("recipient", recipient), // the id or phone recipient
                Parameter.with("message", message)); // one of the messages from above
    }
}
