package dev.local.olympia.domain;

public enum TrainingType {
    STRENGTH("Strength Training"),
    ENDURANCE("Endurance Training"),
    FLEXIBILITY("Flexibility Training"),
    BALANCE("Balance Training"),
    SPEED("Speed Training"),
    AGILITY("Agility Training"),
    POWER("Power Training"),
    RECOVERY("Recovery"),
    OTHER("Other"),;

    private final String displayName;

    TrainingType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
