package model;

public class Production {
    private String left;
    private String[] right;

    public Production(String left, String[] right) {
        this.left = left;
        this.right = right;
    }

    public String[] getRight() {
        return right;
    }

    public String toString() {
        String result = left + " -> ";
        if (right != null) {
            for (String tempStr : right) {
                result += (tempStr + " ");
            }
        } else {
            result += "ε";
        }
        return result;
    }
}