package ms.zero.tfmk.tfmkhidenseek.hologramhandler;

import org.bukkit.Location;

public class Hologram {

    private String hologramText;
    private Location hologramLocation;
    private Boolean clickable;

    public Hologram(String text, Location location, Boolean clickable) {
        this.hologramText = text;
        this.hologramLocation = location;
        this.clickable = clickable;
    }

    public String getHologramText() {
        return this.hologramText;
    }

    public Location getHologramLocation() {
        return this.hologramLocation;
    }

    public Boolean getClickable() {
        return this.clickable;
    }

    public void setHologramText(String hologramText) {
        this.hologramText = hologramText;
    }
}
