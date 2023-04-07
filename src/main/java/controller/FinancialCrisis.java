package controller;

public class FinancialCrisis extends Catastrophy{
    private static FinancialCrisis instance = null;

    private FinancialCrisis() {}

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
