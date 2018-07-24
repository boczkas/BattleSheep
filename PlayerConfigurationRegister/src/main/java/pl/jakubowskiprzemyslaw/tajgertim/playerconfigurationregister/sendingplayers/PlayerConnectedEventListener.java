package pl.jakubowskiprzemyslaw.tajgertim.playerconfigurationregister.sendingplayers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.jakubowskiprzemyslaw.tajgertim.models.configuration.PlayerConfiguration;
import pl.jakubowskiprzemyslaw.tajgertim.models.confirmation.Confirmation;
import pl.jakubowskiprzemyslaw.tajgertim.models.confirmation.PlayerConfigurationConfirmation;
import pl.jakubowskiprzemyslaw.tajgertim.queues.Queues;
import pl.jakubowskiprzemyslaw.tajgertim.services.QueueService;

@Component
class PlayerConnectedEventListener implements ApplicationListener<PlayerConnectedEvent> {

    private final QueueService queueService;
    private int counter = 0;

    @Autowired
    PlayerConnectedEventListener(QueueService queueService) {
        this.queueService = queueService;
    }

    @Override
    public void onApplicationEvent(PlayerConnectedEvent event) {
        PlayerConfiguration playerConfiguration = event.getPlayerConfiguration();
        queueService.sendObjectToQueue(Queues._6BoardHandlerPlayerQueue, playerConfiguration);
        counter++;
        if (counter == 2) {
            Confirmation playersRegistered = new PlayerConfigurationConfirmation();
            queueService.sendObjectToQueue(Queues._5GameReadyValidationQueue, playersRegistered);
            counter = 0;
        }
    }
}
