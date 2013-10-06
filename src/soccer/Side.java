package soccer;

/**
 * @author Kotuc
 */
public enum Side {
    LEFT(-1, 0),
    RIGHT(1, 1);

    private final int sign;
    private final int pascalSign;

    Side(int sign, int pascalSign) {

        this.sign = sign;
        this.pascalSign = pascalSign;
    }

    /**
     * @return -1 for left team, 1 for right team
     */
    public int getSign() {
        return sign;
    }

    /**
     * @return 0 for left team, 1 for right team
     */
    public int getPascalConst() {
        return pascalSign;
    }


    public Side opponent() {
        switch (this) {
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            default:
                throw new IllegalStateException();
        }
    }

}
