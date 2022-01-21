package com.rapaccinim.realtime;

import com.symphony.bdk.core.SymphonyBdk;
import com.symphony.bdk.core.service.datafeed.EventException;
import com.symphony.bdk.core.service.datafeed.RealTimeEventListener;
import com.symphony.bdk.gen.api.model.V4Initiator;
import com.symphony.bdk.gen.api.model.V4Message;
import com.symphony.bdk.gen.api.model.V4MessageSent;
import com.symphony.bdk.gen.api.model.V4SymphonyElementsAction;

import java.util.Map;

class MyOrderListener implements RealTimeEventListener {

    private final SymphonyBdk bdk;

    public MyOrderListener(SymphonyBdk bdk) {
        this.bdk = bdk;
    }

    // override for the message sending
    @Override
    public void onMessageSent(V4Initiator initiator, V4MessageSent event) throws EventException {
        // first of all let's take the message
        V4Message message = event.getMessage();
        // get rid of all the HTML and other tags
        String messageText = message.getMessage().replaceAll("<[^>]*>", "");

        // if the user has prompted an /order command then the bot will reply with a form
        if (messageText.startsWith("/order")) {
            // let's define the form
            String form = "<form id=\"order-form\">";
            form += "<text-field name=\"ticker\" placeholder=\"Ticker\" /><br />";
            form += "<text-field name=\"quantity\" placeholder=\"Quantity\" /><br />";
            form += "<text-field name=\"price\" placeholder=\"Price\" /><br />";
            form += "<button type=\"action\" name=\"order\">Place Order Now</button>";
            form += "</form>";

            // and send back the form in the stream
            bdk.messages().send(message.getStream(), form);
        }
    }

    // override for the Elements Form submission (so we can handle the form submission)
    @Override
    public void onSymphonyElementsAction(
            V4Initiator initiator,
            V4SymphonyElementsAction event) throws EventException {

        // we assume that all the values coming from the form are String
        @SuppressWarnings("unchecked")
        Map<String, String> values = (Map<String, String>) event.getFormValues();

        // then we get the actual individual values
        String ticker = values.get("ticker");
        int quantity = Integer.parseInt(values.get("quantity"));
        int price = Integer.parseInt(values.get("price"));

        // and the bot will send back a template message with cash tag
        String replyMessage = "Order placed for %d of <cash tag=\"%s\"/> @ %d";
        bdk.messages().send(event.getStream(), String.format(replyMessage, quantity, ticker, price));

    }
}
