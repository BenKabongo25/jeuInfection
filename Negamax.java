/**
 * Implémentation de Negamax
 */
public class Negamax extends Algorithm {

    /**
     * Negamax
     * @param player joueur qui raisonne
     * @param depth profondeur de raisonnement
     */
    public Negamax(String player, int depth) {
        super(player, depth);
    }

    /**
     * Implémentation de negamax
     * @param state état évalué
     * @param depth profondeur de raisonnement
     * @return évaluation de l'état
     */
    public float negamax(State state, int depth) {
        nbNodes++; // le nombre de noeuds visités est incrémenté à chaque appel de la méthode
        float m = Float.NEGATIVE_INFINITY;
        if (depth == 0 || state.isOver()) {
            m = state.getScore(state.getCurrentPlayer());
            return (state.getCurrentPlayer().equals(player)) ? m : -m;
        }
        for (Move move : state.getMoves(state.getCurrentPlayer())) {
            State nextState = state.play(move);
            m = Math.max(m, -negamax(nextState, depth-1));
        } return m;
    }

    /**
     * Retrouve le meilleur mouvement correspondant à
     * la meilleure évaluation negamax
     * @param state état à évaluer
     * @return Mouvement correspondant à la meilleure évaluation
     */
    @Override public Move getBestMove(State state) {
        Move bestMove = null;
        float bestValue = Float.NEGATIVE_INFINITY;
        for (Move move : state.getMoves(player)) {
            State nextState = state.play(move);
            float value = negamax(nextState, depth);
            if (value > bestValue) {
                bestValue = value;
                bestMove = move;
            }
        } return bestMove;
    }
}
