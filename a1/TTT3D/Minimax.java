
import java.util.*;

public class Minimax {

    private static int[][] HEURISTIC_SCORES = { { 1, -10, -100, -1000 }, { 10, 0, 0, 0 }, { 100, 0, 0, 0 },
            { 1000, 0, 0, 0 } };

    // private HashMap<GameState, Integer> histMap=new HashMap<GameState,
    // Integer>();
    public BestAction<GameState, Integer> alphaBeta(GameState gameState, int player, int depth, int alpha, int beta) {

        // int v=0,key=0;
        if (gameState.isOWin()) { // check if terminal
            // map.put(gameState, Integer.MIN_VALUE);
            return new BestAction<GameState, Integer>(gameState, Integer.MIN_VALUE);
        } else if (gameState.isXWin()) {
            // map.put(gameState, Integer.MAX_VALUE);
            return new BestAction<GameState, Integer>(gameState, Integer.MAX_VALUE);
        } else if (gameState.isEOG()) {
            // map.put(gameState, 0);
            return new BestAction<GameState, Integer>(gameState, 0);
        } else if (depth == 0) { // evaluate
            // key=gameState.hashCode();
            /*
             * if (map.containsKey(gameState)) return new BestAction<GameState,
             * Integer>(gameState, map.get(gameState)); else { v = evaluate3d(gameState,
             * player); map.put(gameState, v); return new BestAction<GameState,
             * Integer>(gameState, v); }
             */
            return new BestAction<GameState, Integer>(gameState, evaluate3d(gameState, player));
        }

        Vector<GameState> nextStates = new Vector<GameState>();
        gameState.findPossibleMoves(nextStates);

        Collections.shuffle(nextStates);
        // Recursive calls for min max
        if (player == Constants.CELL_X) {
            return maxValue(nextStates, Constants.CELL_O, depth, alpha, beta);// max for other player
        } else { // min
            return minValue(nextStates, Constants.CELL_X, depth, alpha, beta); // min for other player
        }

    }

    /**
     * REcursion for min player
     * 
     * @param nextStates
     * @param player
     * @param depth
     * @param alpha
     * @param beta
     * @return
     */
    public BestAction<GameState, Integer> minValue(Vector<GameState> nextStates, int player, int depth, int alpha,
            int beta) {

        Integer bestUtil = Integer.MAX_VALUE;
        GameState bestState = null;
        for (int i = 0; i < nextStates.size(); i++) {

            BestAction<GameState, Integer> state = alphaBeta(nextStates.elementAt(i), player, depth - 1, alpha, beta);
            if (state.value < bestUtil) { // min
                // bestState = i;
                bestState = nextStates.elementAt(i);
                bestUtil = state.value;
            }

            beta = Math.min(beta, bestUtil); // min
            if (beta <= alpha) // prune
                break;
        }
        return new BestAction<GameState, Integer>(bestState, bestUtil);
    }

    /**
     * Recursion call for max player
     * 
     * @param nextStates
     * @param player
     * @param depth
     * @param alpha
     * @param beta
     * @return
     */
    public BestAction<GameState, Integer> maxValue(Vector<GameState> nextStates, int player, int depth, int alpha,
            int beta) {

        Integer bestUtil = Integer.MIN_VALUE;
        GameState bestState = null;
        for (int i = 0; i < nextStates.size(); i++) {

            BestAction<GameState, Integer> state = alphaBeta(nextStates.elementAt(i), player, depth - 1, alpha, beta);
            if (state.value > bestUtil) { // max
                // bestState = i;
                bestState = nextStates.elementAt(i);
                bestUtil = state.value;
            }
            alpha = Math.max(alpha, bestUtil);// max
            if (beta <= alpha) // prune
                break;
        }
        return new BestAction<GameState, Integer>(bestState, bestUtil);

    }

    /**
     * Evaluation of heuristic utility of state.GameState is iterated for 3D axis
     * and split into 2D layers which are evaluated for heuristic value.
     * 
     * @param state
     * @param player
     * @return
     */
    public int evaluate3d(GameState state, int player) {

        int result = 0;
        int z = 0;
        // int[][] layer;
        while (z < 3) { // Iterate for 3D axis 0,1,2
            for (int l = 0; l < GameState.BOARD_SIZE; l++) {
                // layer = getLayers(state, player, z, l);
                result += evalLayer(getLayers(state, player, z, l), player);
                // result += evalDiag1(layer);
                // result += evalDiag2(layer);
            }
            z++;
        }




        return result;// utility
    }

    /**
     * Split into 2D layers for given axis
     * 
     * @param state
     * @param player
     * @param axis
     * @param l
     * @return
     */
    private int[][] getLayers(GameState state, int player, int axis, int l) {

        int[][] layer = new int[GameState.BOARD_SIZE][GameState.BOARD_SIZE];
        for (int i = 0; i < GameState.BOARD_SIZE; i++) {
            for (int j = 0; j < GameState.BOARD_SIZE; j++) {
                if (axis == 0) { // for X axis
                    layer[i][j] = state.at(l, i, j);
                } else if (axis == 1) { // for Y axis
                    layer[i][j] = state.at(i, l, j);
                } else if (axis == 2) { // for Z axis
                    layer[i][j] = state.at(i, j, l);
                }
            }
        }

        return layer;
    }

    /**
     * Calcuate heuristic value for each layer
     * 
     * @param layer
     * @param player
     * @return
     */
    public int evalLayer(int[][] layer, int player) {
        int value = 0;
        int numX = 0;
        int numO = 0;
        int numOCol = 0;
        int numXCol = 0;

        for (int i = 0; i < GameState.BOARD_SIZE; i++) {
            numX = 0;
            numO = 0;
            numOCol = 0;
            numXCol = 0;
            for (int j = 0; j < GameState.BOARD_SIZE; j++) {
                // for row
                if (layer[i][j] == Constants.CELL_X) {
                    numX++;
                } else if (layer[i][j] == Constants.CELL_O) {
                    numO++;
                }
                // for column
                if (layer[j][i] == Constants.CELL_X) {
                    numXCol++;
                } else if (layer[j][i] == Constants.CELL_O) {
                    numOCol++;
                }
            }
            value += HEURISTIC_SCORES[numX][numO]; // value for row
            value += HEURISTIC_SCORES[numXCol][numOCol]; // value for column
        }

        numX = 0;
        numO = 0;

        for (int i = 0; i < GameState.BOARD_SIZE; i++) {
            if (layer[i][i] == Constants.CELL_X) {
                numX++;
            } else if (layer[i][i] == Constants.CELL_O) {
                numO++;
            }
        }
        value += HEURISTIC_SCORES[numX][numO]; // value

        numX = 0;
        numO = 0;
        for (int i = 0; i < GameState.BOARD_SIZE; i++) {
            if (layer[i][(GameState.BOARD_SIZE - 1) - i] == Constants.CELL_X) {
                numX++;
            } else if (layer[i][(GameState.BOARD_SIZE - 1) - i] == Constants.CELL_O) {
                numO++;
            }
        }
        value += HEURISTIC_SCORES[numX][numO]; // value

        return value; // value
    }

    /**
     * evaluate diagonal1
     * 
     * @param layer
     * @return
     */
    private int evalDiag1(int[][] layer) {

        int numX = 0;
        int numO = 0;

        for (int i = 0; i < GameState.BOARD_SIZE; i++) {
            if (layer[i][i] == Constants.CELL_X) {
                numX++;
            } else if (layer[i][i] == Constants.CELL_O) {
                numO++;
            }
        }
        return HEURISTIC_SCORES[numX][numO]; // value

    }

    /**
     * evaluate diagonal 2
     * 
     * @param layer
     * @return
     */
    private int evalDiag2(int[][] layer) {

        int numX = 0;
        int numO = 0;
        for (int i = 0; i < GameState.BOARD_SIZE; i++) {
            if (layer[i][(GameState.BOARD_SIZE - 1) - i] == Constants.CELL_X) {
                numX++;
            } else if (layer[i][(GameState.BOARD_SIZE - 1) - i] == Constants.CELL_O) {
                numO++;
            }
        }
        return HEURISTIC_SCORES[numX][numO]; // value
    }

}