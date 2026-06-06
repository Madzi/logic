package io.github.madzi.logic.core;

public record LogicPresenter(String neg, String unk, String pos) {

    public static final LogicPresenter FUT = new LogicPresenter("F", "U", "T");
    public static final LogicPresenter NOP = new LogicPresenter("N", "0", "P");
    public static final LogicPresenter SYM = new LogicPresenter("-", "0", "+");

    public String write(final Logic logic) {
        return switch (logic) {
            case FALSE -> neg;
            case UNKNOWN -> unk;
            case TRUE -> pos;
        };
    }

    public Logic parse(final String text) {
        if (neg.equals(text)) {
            return Logic.FALSE;
        } else if (pos.equals(text)) {
            return Logic.TRUE;
        } else if (unk.equals(text)) {
            return Logic.UNKNOWN;
        }
        throw new IllegalArgumentException("Unsupported string '" + text +"' known only[" + neg + "," + unk + "," + pos + "]");
    }
}
