package model.option;

import model.option.Option.OptionKind;
import model.option.Option.OptionType;

/**
 * Describes an Option that is available to the player on their turn.
 *
 * @author Emmet Murray
 */
public interface OptionInterface {

    /**
     * {@code OptionKind} getter method.
     *
     * @return the {@code OptionKind} described by this.
     */
    OptionKind getKind();

    /**
     * {@code OptionType} getter method.
     *
     * @return the {@code OptionType} described by this.
     */
    OptionType getType();

    /**
     * {@code OptionType} setter method.
     *
     * @param type
     *            the new {@code OptionType}
     * @custom.requires {@param type} is of the same {@code OptionKind} of #type
     */
    void setType(OptionType type);

    /**
     * The amount getter method.
     *
     * @return the amount this option costs to perform
     */
    int getAmount();

    /**
     * The amount setter method
     *
     * @param amount
     *            the new amount of the option
     */
    void setAmount(int amount);
}
