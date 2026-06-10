package io.github.madzi.logic.core.scale;

import io.github.madzi.logic.core.Logic;

public record DKScale3(Logic xyz, Logic xyz_, Logic xy_z, Logic xy_z_, Logic x_yz, Logic x_yz_, Logic x_y_z, Logic x_y_z_) implements DKScale<DKScale3> {
    private static final int LENGTH = 8;
    public DKScale3(final Logic[] logics) {
        if (null == logics || logics.length != LENGTH) { throw new IllegalArgumentException("unsupported array size"); }
        this(logics[0], logics[1], logics[2], logics[3], logics[4], logics[5], logics[6], logics[7]);
    }
    public DKScale3(final Logic x, final Logic y, final Logic z) { this(x.and(y).and(z), x.and(y).and(z.not()), x.and(y.not()).and(z), x.and(y.not()).and(z.not()),
                x.not().and(y).and(z), x.not().and(y).and(z.not()), x.not().and(y.not()).and(z), x.not().and(y.not()).and(z.not())); }
    public DKScale3 swapXY() { return new DKScale3(xyz, xyz_, x_yz, x_yz_, xy_z, xy_z_, x_y_z, x_y_z_); }
    public DKScale3 swapXZ() { return new DKScale3(xyz, x_yz, xy_z, x_y_z, xyz_, x_yz_, xy_z_, x_y_z_); }
    public DKScale3 swapYZ() { return new DKScale3(xyz, xy_z, xyz_, xy_z_, x_yz, x_y_z, x_yz_, x_y_z_); }
    public DKScale2 eliminateZ() { return new DKScale2(xyz.or(xyz_), xy_z.or(xy_z_), x_yz.or(x_yz_), x_y_z.or(x_y_z_)); }
    public DKScale2 eliminateY() { return new DKScale2(xyz.or(xy_z), xyz_.or(xy_z_), x_yz.or(x_y_z), x_yz_.or(x_y_z_)); }
    public DKScale2 eliminateX() { return new DKScale2(xyz.or(x_yz), xyz_.or(x_yz_), xy_z.or(x_y_z), xy_z_.or(x_y_z_)); }
    public DKScale4 liftAsXYZ() { return new DKScale4(xyz, xyz, xyz_, xyz_, xy_z, xy_z, xy_z_, xy_z_, x_yz, x_yz, x_yz_, x_yz_, x_y_z, x_y_z, x_y_z_, x_y_z_); }
    public DKScale4 liftAsXYW() { return new DKScale4(xyz, xyz_, xyz, xyz_, xy_z, xy_z_, xy_z, xy_z_, x_yz, x_yz_, x_yz, x_yz_, x_y_z, x_y_z_, x_y_z, x_y_z_); }
    public DKScale4 liftAsXZW() { return new DKScale4( xyz, xyz_, xy_z, xy_z_, xyz, xyz_, xy_z, xy_z_, x_yz, x_yz_, x_y_z, x_y_z_, x_yz, x_yz_, x_y_z, x_y_z_); }
    public DKScale4 liftAsYZW() { return new DKScale4(xyz, xyz_, xy_z, xy_z_, x_yz, x_yz_, x_y_z, x_y_z_, xyz, xyz_, xy_z, xy_z_, x_yz, x_yz_, x_y_z, x_y_z_); }
    @Override public Logic[] scale() { return new Logic[] { xyz, xyz_, xy_z, xy_z_, x_yz, x_yz_, x_y_z, x_y_z_ }; }
    @Override public int size() { return LENGTH; }
    @Override public DKScale3 not() { return new DKScale3( xyz.not(), xyz_.not(), xy_z.not(), xy_z_.not(), x_yz.not(), x_yz_.not(), x_y_z.not(), x_y_z_.not()); }
    @Override public DKScale3 and(final DKScale3 other) { return new DKScale3(xyz.and(other.xyz), xyz_.and(other.xyz_), xy_z.and(other.xy_z), xy_z_.and(other.xy_z_),
                x_yz.and(other.x_yz), x_yz_.and(other.x_yz_), x_y_z.and(other.x_y_z), x_y_z_.and(other.x_y_z_)); }
    @Override public DKScale3 or(final DKScale3 other) { return new DKScale3(xyz.or(other.xyz), xyz_.or(other.xyz_), xy_z.or(other.xy_z), xy_z_.or(other.xy_z_),
                x_yz.or(other.x_yz), x_yz_.or(other.x_yz_), x_y_z.or(other.x_y_z), x_y_z_.or(other.x_y_z_)); }
}
