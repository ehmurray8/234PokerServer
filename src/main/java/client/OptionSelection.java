package client;

public class OptionSelection {

  public enum SelectionType {
    FOLD(0), CHECK(0), CALL(1), BET(1), RAISE(2);

    int index;

    SelectionType(int index) {
      this.index = index;
    }
  }

  private int index;
  private double amount;

  public OptionSelection(SelectionType type) {
    this(type.index);
  }

  public OptionSelection(SelectionType type, double amount) {
    this(type.index, amount);
  }

  public OptionSelection(int index) {
    this(index, 0);
  }

  private OptionSelection(int index, double amount) {
    this.index = index;
    this.amount = amount;
  }

  double getAmount() {
    return amount;
  }

  int getIndex() {
    return index;
  }
}
