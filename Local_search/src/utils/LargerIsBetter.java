package utils;

class LargerIsBetter implements ComparisonFunction {
    @Override
    public boolean compare(int a, int b) {
        return a > b;
    }
}
