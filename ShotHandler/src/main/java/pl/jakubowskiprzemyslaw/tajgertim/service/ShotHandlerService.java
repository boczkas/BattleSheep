package pl.jakubowskiprzemyslaw.tajgertim.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import pl.jakubowskiprzemyslaw.tajgertim.models.coordinates.FieldStatus;
import pl.jakubowskiprzemyslaw.tajgertim.models.playeraction.PlayerAction;
import pl.jakubowskiprzemyslaw.tajgertim.models.playeraction.action.Shot;
import pl.jakubowskiprzemyslaw.tajgertim.models.shoot.PlayerShootCoordinate;
import pl.jakubowskiprzemyslaw.tajgertim.models.shoot.PlayerShootResult;
import pl.jakubowskiprzemyslaw.tajgertim.models.shoot.ShootResult;
import pl.jakubowskiprzemyslaw.tajgertim.services.QueueService;

@Service
public class ShotHandlerService {

    private final QueueService queueService;

    public ShotHandlerService(QueueService queueService) {
        this.queueService = queueService;
    }

    @RabbitListener(queues = "ShotHandlerPlayerShotQueueTest")
    public void listenOnShotHandlerPlayerShotQueue(PlayerAction shotAction) {
        System.out.println("Received message" + shotAction);
        Shot shot = (Shot) shotAction.getAction();

        queueService.sendObjectToQueue("BoardHandlerShotQueryQueueTest", new PlayerShootCoordinate(shotAction.getPlayer(),shot.getCoordinate()));
    }


    @RabbitListener(queues = "ShotHandlerFieldStatusQueueTest")
    public void listenOnShotHandlerFieldStatusQueue(FieldStatus fieldStatus) {
        System.out.println("Received message" + fieldStatus);
        queueService.sendObjectToQueue("JudgePlayerShootResultQueueTest", new PlayerShootResult(fieldStatus.getPlayer(),ShootResult.HIT));
    }
}