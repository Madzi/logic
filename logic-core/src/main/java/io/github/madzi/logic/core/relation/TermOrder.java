package io.github.madzi.logic.core.relation;

public enum TermOrder {
    XYZ(0, 1, 2),
    XZY(0, 2, 1),
    YXZ(1, 0, 2),
    YZX(2, 0, 1),
    ZXY(1, 2, 0),
    ZYX(2, 1, 0);

    private final int indexOfX;
    private final int indexOfY;
    private final int indexOfZ;

    private TermOrder(final int indexOfX, final int indexOfY, final int indexOfZ) {
        this.indexOfX = indexOfX;
        this.indexOfY = indexOfY;
        this.indexOfZ = indexOfZ;
    }

    public int indexOfX() {
        return indexOfX;
    }

    public int indexOfY() {
        return indexOfY;
    }

    public int indexOfZ() {
        return indexOfZ;
    }
}
