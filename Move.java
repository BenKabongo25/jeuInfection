/**
 * Gestionnaire des mouvements des joueurs
 */
public class Move {

    /**
     * Mouvement de type saut
     */
    public static final int JUMP = 0;

    /**
     * Mouvement de type clonage
     */
    public static final int CLONE = 1;

    /**
     * Type de mouvement : JUMP ou CLONE
     */
    private final int type;
    public final int getType() {
        return type;
    }

    /**
     * coordonnées (ligne et colonne) d'origine du pion à déplacer
     */
    private final int startLn, startCol;

    public final int getStartLn() {
        return startLn;
    }

    public final int getStartCol() {
        return startCol;
    }

    /**
     * coordonnées (ligne et colonne) de destination du pion à déplacer
     */
    public final int endLn, endCol;

    public final int getEndLn() {
        return endLn;
    }

    public final int getEndCol() {
        return endCol;
    }

    /**
     * Mouvement
     * @param type type de mouvement: JUMP ou CLONE
     * @param startLn ligne origine du pion
     * @param startCol colonne origine du pion
     * @param endLn ligne de destination du pion
     * @param endCol colonne de destination du pion
     */
    public Move(int type, int startLn, int startCol, int endLn, int endCol) {
        if (type != JUMP && type != CLONE)
            throw new IllegalArgumentException("Type de mouvement incorrect");
        this.type = type;
        this.startLn = startLn;
        this.startCol = startCol;
        this.endLn = endLn;
        this.endCol = endCol;
    }

    @Override
    public String toString() {
        return ((type == CLONE) ? "clonage" : "saut") +
                " du pion de (" + (startLn+1) + "," + (startCol+1) + ") à (" + (endLn+1) + "," + (endCol+1) + ")";
    }

}
