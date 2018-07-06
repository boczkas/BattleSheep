package pl.jakubowskiprzemyslaw.tajgertim.models.coordinates;


import pl.jakubowskiprzemyslaw.tajgertim.models.QueueObject;
import pl.jakubowskiprzemyslaw.tajgertim.models.player.Player;

/**
 * Class used for communication between microservices BoardHandler
 * and ShotHandler to inform ShotHandler about Field status
 * after shot has been performed
 */

public class FieldStatus implements QueueObject {
    private Coordinate coordinate;
    private FieldState fieldState;
    private Player player;

    public FieldStatus(Coordinate coordinate, FieldState fieldState, Player player) {
        this.coordinate = coordinate;
        this.fieldState = fieldState;
        this.player = player;
    }

    @Override
    public String toString() {
        return "FieldStatus{" +
                "coordinate=" + coordinate +
                ", fieldState=" + fieldState +
                ", player=" + player +
                '}';
    }
}
