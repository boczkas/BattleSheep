package pl.jakubowskiprzemyslaw.tajgertim.playingstatemachine;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pl.jakubowskiprzemyslaw.tajgertim.event.PlayerActionEvent;
import pl.jakubowskiprzemyslaw.tajgertim.event.PlayerActionEventListener;
import pl.jakubowskiprzemyslaw.tajgertim.models.coordinates.Coordinate;
import pl.jakubowskiprzemyslaw.tajgertim.models.player.Player;
import pl.jakubowskiprzemyslaw.tajgertim.models.playeraction.PlayerAction;
import pl.jakubowskiprzemyslaw.tajgertim.models.playeraction.action.Action;
import pl.jakubowskiprzemyslaw.tajgertim.models.playeraction.action.Move;
import pl.jakubowskiprzemyslaw.tajgertim.models.playeraction.action.Shot;
import pl.jakubowskiprzemyslaw.tajgertim.queues.Queues;
import pl.jakubowskiprzemyslaw.tajgertim.services.QueueService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@Test
public class PSMEventListenerTest {

    private PlayerActionEventListener eventListener;
    private QueueService queueService;


    @BeforeMethod
    public void setUp() {
        queueService = mock(QueueService.class);
        eventListener = new PlayerActionEventListener(queueService);
    }

    public void shotObjectAsPlayerAction_SendsPlayerShot_ToQueue10() {
        Player player = new Player("", "");
        Coordinate coordinate = new Coordinate(1, 1);
        Action shot = new Shot(coordinate);
        PlayerAction action = new PlayerAction(player, shot);
        PlayerActionEvent event = new PlayerActionEvent(this, action);

        eventListener.onApplicationEvent(event);

        verify(queueService).sendObjectToQueue(Queues._10ShotHandlerPlayerShotQueue, action);
    }

    public void moveObjectAsPlayerAction_SendsPlayerMove_ToQueue11() {
        Player player = new Player("", "");
        Action move = new Move();
        PlayerAction action = new PlayerAction(player, move);
        PlayerActionEvent event = new PlayerActionEvent(this, action);

        eventListener.onApplicationEvent(event);

        verify(queueService).sendObjectToQueue(Queues._11MoveHandlerPlayerMoveQueue, action);
    }
}
