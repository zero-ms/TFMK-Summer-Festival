package ms.zero.tfmk.tfmkhidenseek.hologram;

import ms.zero.tfmk.tfmkhidenseek.global.EntityIDGenerator;

public class ClickableEntity {
    private Integer slimeEntityID;
    private Integer vehicleID;

    public ClickableEntity(Integer armorStandEntityID) {
        this.vehicleID = armorStandEntityID;
        this.slimeEntityID = EntityIDGenerator.generateEntityID();
    }

    public Integer getSlimeEntityID() {
        return this.slimeEntityID;
    }

    public Integer getVehicleID() {
        return this.vehicleID;
    }
}
