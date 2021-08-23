/**
 * Classe de base des algorithmes de raisonnement
 */
public abstract class Algorithm {

    /**
     * joueur qui raisonne
     */
    protected final String player;

    /**
     * profondeur de raisonnement du joueur
     */
    protected final int depth;

    /**
     * Nombre de noeuds visités par l'algorithme
     */
    protected int nbNodes;

    public final int getNbNodes() {
        return nbNodes;
    }

    /**
     * @param player joueur courant
     * @param depth profondeur de raisonnement
     */
    public Algorithm(String player, int depth) {
        this.player = player;
        this.depth = depth;
        nbNodes = 0;
    }

    /**
     * trouve le meilleur coup pour le joueur qui raisonne
     * @param state état à évaluer
     * @return le meilleur coup pour le joueur
     */
    public abstract Move getBestMove(State state);

}
