package pl.jakubowskiprzemyslaw.tajgertim.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import pl.jakubowskiprzemyslaw.tajgertim.models.coordinates.FieldState;
import pl.jakubowskiprzemyslaw.tajgertim.models.coordinates.FieldStatus;
import pl.jakubowskiprzemyslaw.tajgertim.models.playeraction.PlayerAction;
import pl.jakubowskiprzemyslaw.tajgertim.models.playeraction.action.Shot;
import pl.jakubowskiprzemyslaw.tajgertim.models.shoot.PlayerShootCoordinate;
import pl.jakubowskiprzemyslaw.tajgertim.models.shoot.PlayerShootResult;
import pl.jakubowskiprzemyslaw.tajgertim.models.shoot.ShootResult;
import pl.jakubowskiprzemyslaw.tajgertim.queues.Queues;
import pl.jakubowskiprzemyslaw.tajgertim.services.QueueService;

@Service
public class ShotHandlerService {
    private final QueueService queueService;

    public ShotHandlerService(QueueService queueService) {
        this.queueService = queueService;
    }

    @RabbitListener(queues = "ShotHandlerPlayerShotQueueTest")  // 10
    public void listenOnShotHandlerPlayerShotQueue(PlayerAction shotAction) {
        System.out.println("Received message" + shotAction);
        Shot shot = (Shot) shotAction.getAction();

        queueService.sendObjectToQueue(Queues._12BoardHandlerShotQueryQueue, new PlayerShootCoordinate(shotAction.getPlayer(), shot.getCoordinate()));
    }

    @RabbitListener(queues = "ShotHandlerFieldStatusQueueTest") // 17
    public void listenOnShotHandlerFieldStatusQueue(FieldStatus fieldStatus) {
        System.out.println("Received message" + fieldStatus);

        FieldState fieldState = fieldStatus.getFieldState();

        if (fieldState.equals(FieldState.NOT_HIT_MAST)) {
            queueService.sendObjectToQueue(Queues._15JudgePlayerShootResultQueue, new PlayerShootResult(fieldStatus.getPlayer(), ShootResult.HIT));
        } else {
            queueService.sendObjectToQueue(Queues._15JudgePlayerShootResultQueue, new PlayerShootResult(fieldStatus.getPlayer(), ShootResult.MISS));
        }
    }
}
