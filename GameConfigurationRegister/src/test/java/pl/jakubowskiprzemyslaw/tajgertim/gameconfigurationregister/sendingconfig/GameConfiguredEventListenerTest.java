package pl.jakubowskiprzemyslaw.tajgertim.gameconfigurationregister.sendingconfig;

import org.testng.annotations.Test;
import pl.jakubowskiprzemyslaw.tajgertim.models.confirmation.Confirmation;
import pl.jakubowskiprzemyslaw.tajgertim.models.confirmation.GameConfigurationConfirmation;
import pl.jakubowskiprzemyslaw.tajgertim.queues.Queues;
import pl.jakubowskiprzemyslaw.tajgertim.services.QueueService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@Test
public class GameConfiguredEventListenerTest {


    public void correctObjectInGameConfiguration_SendsToQueueGameConfiguration_AndCallsEventListener() {
        QueueService queueService = mock(QueueService.class);
        GameConfiguredEventListener gameConfigurationEventListener = new GameConfiguredEventListener(queueService);

        GameConfiguredEvent event = mock(GameConfiguredEvent.class);
        gameConfigurationEventListener.onApplicationEvent(event);

        Confirmation gameConfigured = new GameConfigurationConfirmation();
        verify(queueService).sendObjectToQueue(Queues._5GameReadyValidationQueue, gameConfigured);
    }
}
