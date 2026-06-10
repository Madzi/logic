
package io.github.madzi.logic.core.scale;

import io.github.madzi.logic.core.Logic;


public record DKScale4(
        Logic xyzw, Logic xyzw_, Logic xyz_w, Logic xyz_w_,
        Logic xy_zw, Logic xy_zw_, Logic xy_z_w, Logic xy_z_w_,
        Logic x_yzw, Logic x_yzw_, Logic x_yz_w, Logic x_yz_w_,
        Logic x_y_zw, Logic x_y_zw_, Logic x_y_z_w, Logic x_y_z_w_
) implements DKScale<DKScale4> {
    private static final int LENGTH = 16;
    public DKScale4(final Logic[] logics) {
        if (null == logics || logics.length != LENGTH) { throw new IllegalArgumentException("unsupported array size"); }
        this(logics[0], logics[1], logics[2], logics[3], logics[4], logics[5], logics[6], logics[7], logics[8], logics[9], logics[10], logics[11], logics[12], logics[13], logics[14], logics[15]);
    }

    public DKScale3 eliminateX() { return new DKScale3(xyzw.or(x_yzw), xyzw_.or(x_yzw_), xyz_w.or(x_yz_w), xyz_w_.or(x_yz_w_),
                xy_zw.or(x_y_zw), xy_zw_.or(x_y_zw_), xy_z_w.or(x_y_z_w), xy_z_w_.or(x_y_z_w_)); }
    public DKScale3 eliminateY() { return new DKScale3(xyzw.or(xy_zw), xyzw_.or(xy_zw_), xyz_w.or(xy_z_w), xyz_w_.or(xy_z_w_),
                x_yzw.or(x_y_zw), x_yzw_.or(x_y_zw_), x_yz_w.or(x_y_z_w), x_yz_w_.or(x_y_z_w_)); }
    public DKScale3 eliminateZ() {return new DKScale3(xyzw.or(xyz_w), xyzw_.or(xyz_w_), xy_zw.or(xy_z_w), xy_zw_.or(xy_z_w_),
                x_yzw.or(x_yz_w), x_yzw_.or(x_yz_w_), x_y_zw.or(x_y_z_w), x_y_zw_.or(x_y_z_w_)); }
    public DKScale3 eliminateW() { return new DKScale3(xyzw.or(xyzw_), xyz_w.or(xyz_w_), xy_zw.or(xy_zw_), xy_z_w.or(xy_z_w_),
                x_yzw.or(x_yzw_), x_yz_w.or(x_yz_w_), x_y_zw.or(x_y_zw_), x_y_z_w.or(x_y_z_w_)); }
        public DKScale4 swapXY() {
        return new DKScale4(
            xyzw,    xyzw_,              // 0,1:  x=1,y=1 → unchanged
            xyz_w,   xyz_w_,             // 2,3:  x=1,y=1,z=0 → unchanged
            x_yzw,   x_yzw_,            // 4,5:  xy'z* → x'yz*
            x_yz_w,  x_yz_w_,           // 6,7:  xy'z'* → x'yz'*
            xy_zw,   xy_zw_,            // 8,9:  x'yz* → xy'z*
            xy_z_w,  xy_z_w_,           // 10,11: x'yz'* → xy'z'*
            x_y_zw,  x_y_zw_,           // 12,13: x=0,y=0 → unchanged
            x_y_z_w, x_y_z_w_           // 14,15: x=0,y=0,z=0 → unchanged
        );
    }
    public DKScale4 swapXZ() {
        return new DKScale4(
            xyzw,    xyzw_,              // 0,1:  x=1,z=1 → unchanged
            x_yzw,   x_yzw_,            // 2,3:  xyz'* → x'yz*
            xy_zw,   xy_zw_,            // 4,5:  x=1,y=0,z=1 → unchanged
            x_y_zw,  x_y_zw_,           // 6,7:  xy'z'* → x'y'z*
            xyz_w,   xyz_w_,            // 8,9:  x'yz* → xyz'*
            x_yz_w,  x_yz_w_,           // 10,11: x=0,z=0 → unchanged
            xy_z_w,  xy_z_w_,           // 12,13: x'y'z* → xy'z'*
            x_y_z_w, x_y_z_w_           // 14,15: x=0,z=0,y=0 → unchanged
        );
    }
    public DKScale4 swapXW() {
        return new DKScale4(
            xyzw,    x_yzw,              // 0: unchanged,   1: xyzw' → x'yzw
            xyz_w,   x_yz_w,             // 2: unchanged,   3: xyz'w' → x'yz'w
            xy_zw,   x_y_zw,             // 4: unchanged,   5: xy'zw' → x'y'zw
            xy_z_w,  x_y_z_w,            // 6: unchanged,   7: xy'z'w' → x'y'z'w
            xyzw_,   x_yzw_,             // 8: x'yzw → xyzw', 9: unchanged
            xyz_w_,  x_yz_w_,            // 10: x'yz'w → xyz'w', 11: unchanged
            xy_zw_,  x_y_zw_,            // 12: x'y'zw → xy'zw', 13: unchanged
            xy_z_w_, x_y_z_w_            // 14: x'y'z'w → xy'z'w', 15: unchanged
        );
    }
    public DKScale4 swapYZ() {
        return new DKScale4(
            xyzw,    xyzw_,              // 0,1:  y=1,z=1 → unchanged
            xy_zw,   xy_zw_,            // 2,3:  xyz'* → xy'z*
            xyz_w,   xyz_w_,            // 4,5:  xy'z* → xyz'*
            xy_z_w,  xy_z_w_,           // 6,7:  y=0,z=0 → unchanged
            x_yzw,   x_yzw_,            // 8,9:  y=1,z=1 (x=0) → unchanged
            x_y_zw,  x_y_zw_,           // 10,11: x'yz'* → x'y'z*
            x_yz_w,  x_yz_w_,           // 12,13: x'y'z* → x'yz'*
            x_y_z_w, x_y_z_w_           // 14,15: y=0,z=0 (x=0) → unchanged
        );
    }
    public DKScale4 swapYW() {
        return new DKScale4(
            xyzw,    xy_zw,              // 0: unchanged,   1: xyzw' → xy'zw
            xyz_w,   xy_z_w,             // 2: unchanged,   3: xyz'w' → xy'z'w
            xyzw_,   xy_zw_,             // 4: xy'zw → xyzw', 5: unchanged
            xyz_w_,  xy_z_w_,            // 6: xy'z'w → xyz'w', 7: unchanged
            x_yzw,   x_y_zw,             // 8: unchanged,   9: x'yzw' → x'y'zw
            x_yz_w,  x_y_z_w,            // 10: unchanged,  11: x'yz'w' → x'y'z'w
            x_yzw_,  x_y_zw_,            // 12: x'y'zw → x'yzw', 13: unchanged
            x_yz_w_, x_y_z_w_            // 14: x'y'z'w → x'yz'w', 15: unchanged
        );
    }
    public DKScale4 swapZW() {
        return new DKScale4(
            xyzw,    xyz_w,              // 0: unchanged,   1: xyzw' → xyz'w
            xyzw_,   xyz_w_,             // 2: xyz'w → xyzw', 3: unchanged
            xy_zw,   xy_z_w,             // 4: unchanged,   5: xy'zw' → xy'z'w
            xy_zw_,  xy_z_w_,            // 6: xy'z'w → xy'zw', 7: unchanged
            x_yzw,   x_yz_w,             // 8: unchanged,   9: x'yzw' → x'yz'w
            x_yzw_,  x_yz_w_,            // 10: x'yz'w → x'yzw', 11: unchanged
            x_y_zw,  x_y_z_w,            // 12: unchanged,  13: x'y'zw' → x'y'z'w
            x_y_zw_, x_y_z_w_            // 14: x'y'z'w → x'y'zw', 15: unchanged
        );
    }
    @Override public Logic[] scale() { return new Logic[] {
            xyzw, xyzw_, xyz_w, xyz_w_, xy_zw, xy_zw_, xy_z_w, xy_z_w_,
            x_yzw, x_yzw_, x_yz_w, x_yz_w_, x_y_zw, x_y_zw_, x_y_z_w, x_y_z_w_
        }; }
    @Override public int size() { return LENGTH; }
    @Override public DKScale4 not() { return new DKScale4(
            xyzw.not(), xyzw_.not(), xyz_w.not(), xyz_w_.not(),
            xy_zw.not(), xy_zw_.not(), xy_z_w.not(), xy_z_w_.not(),
            x_yzw.not(), x_yzw_.not(), x_yz_w.not(), x_yz_w_.not(),
            x_y_zw.not(), x_y_zw_.not(), x_y_z_w.not(), x_y_z_w_.not()
        ); }
    @Override public DKScale4 and(final DKScale4 other) { return new DKScale4(
            xyzw.and(other.xyzw), xyzw_.and(other.xyzw_), xyz_w.and(other.xyz_w), xyz_w_.and(other.xyz_w_),
            xy_zw.and(other.xy_zw), xy_zw_.and(other.xy_zw_), xy_z_w.and(other.xy_z_w), xy_z_w_.and(other.xy_z_w_),
            x_yzw.and(other.x_yzw), x_yzw_.and(other.x_yzw_), x_yz_w.and(other.x_yz_w), x_yz_w_.and(other.x_yz_w_),
            x_y_zw.and(other.x_y_zw), x_y_zw_.and(other.x_y_zw_), x_y_z_w.and(other.x_y_z_w), x_y_z_w_.and(other.x_y_z_w_)
        ); }
    @Override public DKScale4 or(final DKScale4 other) { return new DKScale4(
            xyzw.or(other.xyzw), xyzw_.or(other.xyzw_), xyz_w.or(other.xyz_w), xyz_w_.or(other.xyz_w_),
            xy_zw.or(other.xy_zw), xy_zw_.or(other.xy_zw_), xy_z_w.or(other.xy_z_w), xy_z_w_.or(other.xy_z_w_),
            x_yzw.or(other.x_yzw), x_yzw_.or(other.x_yzw_), x_yz_w.or(other.x_yz_w), x_yz_w_.or(other.x_yz_w_),
            x_y_zw.or(other.x_y_zw), x_y_zw_.or(other.x_y_zw_), x_y_z_w.or(other.x_y_z_w), x_y_z_w_.or(other.x_y_z_w_)
        ); }
}
