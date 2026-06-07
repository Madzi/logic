package io.github.madzi.logic.core.relation;

import io.github.madzi.logic.core.Logic;

public record Transform() {

    private static final int REL1_LEN = 2; // x, x'
    private static final int REL2_LEN = REL1_LEN * 2; // xy, xy', x'y, x'y'
    private static final int REL3_LEN = REL2_LEN * 2; // xyz, xyz', xy'z, xy'z', x'yz, x'yz', x'y'z, x'y'z'

    private static final int[][] IDX4 = {
        { 3, 2 },
        { 1, 0 }
    };

    private static final int[][] TRIPLES = {
        { 1, 1, 1 },
        { 1, 1, 0 },
        { 1, 0, 1 },
        { 1, 0, 0 },
        { 0, 1, 1 },
        { 0, 1, 0 },
        { 0, 0, 1 },
        { 0, 0, 0 }
    };

    public Relation3 lift(final Relation2 relation, final TermOrder order) {
        final Logic[] dk4 = relation.dkscale();
        final Logic[] dk8 = new Logic[REL3_LEN];
        for (int i = 0; i < REL3_LEN; ++i) {
            int[] triple = TRIPLES[i];
            int xVal = triple[order.indexOfX()];
            int yVal = triple[order.indexOfY()];
            dk8[i] = dk4[IDX4[xVal][yVal]];
        }
        return new Relation3(dk8);
    }

    public Relation2 eliminate(final Relation3 relation, final TermOrder order) {
        final Logic[] dk4 = new Logic[REL2_LEN];
        final Logic[] dk8 = relation.dkscale();
        Logic xy = Logic.FALSE;
        Logic xy_ = Logic.FALSE;
        Logic x_y = Logic.FALSE;
        Logic x_y_ = Logic.FALSE;
        for (int i = 0; i < REL3_LEN; ++i) {
            int[] triple = TRIPLES[i];
            int xVal = triple[order.indexOfX()];
            int yVal = triple[order.indexOfY()];
            Logic logic = dk8[i];
            if (xVal == 1 && yVal == 1) {
                xy = xy.or(logic);
            }
            if (xVal == 1 && yVal == 0) {
                xy_ = xy_.or(logic);
            }
            if (xVal == 0 && yVal == 1) {
                x_y = x_y.or(logic);
            }
            if (xVal == 0 && yVal == 0) {
                x_y_ = x_y_.or(logic);
            }
        }
        dk4[0] = xy;
        dk4[1] = xy_;
        dk4[2] = x_y;
        dk4[3] = x_y_;
        return new Relation2(dk4);
    }

    public Relation2 conclude(Relation2 xy, Relation2 yz) {
        var xyz1 = lift(xy, TermOrder.XYZ);
        var xyz2 = lift(yz, TermOrder.ZXY);
        var res = xyz1.intersect(xyz2);
        return eliminate(res, TermOrder.XZY);
    }
}
