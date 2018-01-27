/**
 *
 */
package model.player;

import model.option.Option;
import model.player.SimplePlayer.Move;

/**
 * Describes the SimplePlayer class, which is created as an equivalent
 * representation of an {@code Player} object.
 *
 * <p>
 * SimplePlayer is used for the view to allow only pertinent information about
 * the player be stored when needed for displaying the Player.
 * </p>
 *
 * @author Emmet Murray
 */
public interface SimplePlayerInterface {
    /**
     * The name getter method.
     *
     * @return the player's name
     */
    String getName();

    /**
     * The balance getter method.
     *
     * @return the player's balance as a formatted String
     */
    String getBalanceString();

    /**
     * The balance setter method.
     *
     * @param balance
     *            the player's new balance
     */
    void setBalance(int balance);

    /**
     * The last move getter method.
     *
     * @return the player's last move.
     */
    String getLastMoveString();

    /**
     * The last move setter method
     *
     * @param lastMove
     *            {@code Move} used to update the string representation of the
     *            player's last move
     */
    void setLastMove(Move lastMove);

    /**
     * Getter method for the {@code Option} objects the {@code Player} has.
     *
     * @return array of {@code Option}s
     */
    Option[] getOptions();

    /**
     * Option setter method, updates the entry in {@code this.options}
     * corresponding to @param option.
     *
     * @param option
     *            the option to replace in the array
     */
    void setOption(Option option);

    /**
     * Returns a {@code Move} object representing the player's last turn.
     *
     * @return {@code Move} representation of the player's last turn
     */
    Move getLastMove();
}
