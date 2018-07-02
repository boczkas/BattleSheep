package pl.jakubowskiprzemyslaw.tajgertim.controllers;

import com.rabbitmq.client.GetResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.jakubowskiprzemyslaw.tajgertim.models.Player;
import pl.jakubowskiprzemyslaw.tajgertim.queues.MessageQueueConnector;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

@Controller
public class ResultController {

    private final MessageQueueConnector messageQueueConnector;

    public ResultController(MessageQueueConnector messageQueueConnector) {
        this.messageQueueConnector = messageQueueConnector;
    }

    @GetMapping(value = "/opponent", produces = "text/plain")
    public @ResponseBody
    String getOpponentName(HttpServletRequest request) throws IOException, TimeoutException {

        Player player = getPlayerFromSession(request);

        String playerQueueName = player.obtainQueueName();
        String opponentName = getOpponentNameFromQueue(playerQueueName);

        return opponentName;
    }

    private String getOpponentNameFromQueue(String playerQueueName) throws IOException, TimeoutException {
        Optional<GetResponse> response = Optional.empty();
        messageQueueConnector.connectToQueue(playerQueueName);

        while (!response.isPresent()) {
            response = Optional.ofNullable(messageQueueConnector.get(playerQueueName));
        }

        System.out.println(new String(response.get().getBody(), "UTF-8")); // TODO: 16.07.2018 to be replaced with logger
        //        messageQueueConnector.closeConnection(); //TODO: 13.07.2018 - connection is not closed, because rzuca bledami

        return new String(response.get().getBody(), "UTF-8");
    }

    private Player getPlayerFromSession(HttpServletRequest request) {
        return (Player) request.getSession().getAttribute("player");
    }
}