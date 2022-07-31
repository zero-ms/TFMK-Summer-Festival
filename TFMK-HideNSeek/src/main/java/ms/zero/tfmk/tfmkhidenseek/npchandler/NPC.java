package ms.zero.tfmk.tfmkhidenseek.npchandler;

public class NPC {
    private String npcName;
    private Integer entityID;

    public NPC(String name, Integer id) {
        this.npcName = name;
        this.entityID = id;
    }

    public String getNpcName() {
        return npcName;
    }

    public Integer getEntityID() {
        return entityID;
    }
}
