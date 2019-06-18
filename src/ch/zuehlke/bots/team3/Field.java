package ch.zuehlke.bots.team3;

public class Field {

    private final double fieldHeight;
    private final double fieldWidth;
    private final double sentryBorderSize;

    public Field(double battleFieldHeight, double battleFieldWidth, int sentryBorderSize) {
        this.fieldHeight = battleFieldHeight;
        this.fieldWidth = battleFieldWidth;
        this.sentryBorderSize = sentryBorderSize * 1.3;
    }

    public boolean isInBattlefield(double x, double y) {
        return x > sentryBorderSize && x < fieldWidth - sentryBorderSize && y > sentryBorderSize && y < fieldHeight - sentryBorderSize;
    }
}
