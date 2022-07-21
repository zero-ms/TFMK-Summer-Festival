package ms.zero.tfmk.tfmkhotel.Objects;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Room {
    private Integer roomNumber;
    private Player owner;
    private UUID ownerUUID;
    private Location cachedLocation = null;

    public Room(Integer num, UUID u) {
        this.roomNumber = num;
        this.ownerUUID = u;
    }

    public boolean isOnwer(UUID p) {
        // Check if player is room's owner
        return false;
    }

    public boolean isLocationCached() {
        if (cachedLocation == null) {
            // Caching sign location
            return false;
        } else {
            return true;
        }
    }

    public Integer getNumber() {
        return this.roomNumber;
    }


    public UUID getUUID() {
        return this.ownerUUID;
    }
}
