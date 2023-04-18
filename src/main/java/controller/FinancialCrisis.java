package controller;

/**
 * This class represents a financial crisis.
 */
public class FinancialCrisis extends Catastrophy{
    private static FinancialCrisis instance = null;

    /**
     * Constructor of the financial crisis.
     */
    private FinancialCrisis() {}

    /**
     * Get the instance of the financial crisis.
     * @return the instance of the financial crisis
     */
    public static FinancialCrisis getInstance() {
        if (instance == null) {
            instance = new FinancialCrisis();
        }
        return instance;
    }

    @Override
    protected void effect() {

    }
}
