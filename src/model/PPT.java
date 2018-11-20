package model;

import util.Vts;

import java.util.*;

public class PPT {
    private Map<String, Integer[]> PPT;
    private List<Production> productionList;

    public PPT() {

        // 构造PPT
        PPT = new HashMap<>();
        PPT.put("S", new Integer[]{2, 3, -1, -1, -1, -1, 4, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, 4});
        PPT.put("E", new Integer[]{-1, -1, -1, 5, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 5, 5, -1});
        PPT.put("E'", new Integer[]{-1, -1, -1, -1, -1, -1, -1, -1, 8, 9, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1});
        PPT.put("E''", new Integer[]{-1, -1, -1, -1, 7, -1, -1, 7, 6, 6, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1});
        PPT.put("T", new Integer[]{-1, -1, -1, 10, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 10, 10, -1});
        PPT.put("T'", new Integer[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 13, 14, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1});
        PPT.put("T''", new Integer[]{-1, -1, -1, -1, 12, -1, -1, 12, 12, 12, 11, 11, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1});
        PPT.put("F", new Integer[]{-1, -1, -1, 17, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 15, 16, -1});
        PPT.put("C", new Integer[]{-1, -1, -1, 18, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 18, 18, -1});
        PPT.put("C'", new Integer[]{-1, -1, -1, -1, 20, -1, -1, -1, -1, -1, -1, -1, 19, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1});
        PPT.put("D", new Integer[]{-1, -1, -1, 21, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 21, 21, -1});
        PPT.put("D'", new Integer[]{-1, -1, -1, -1, 23, -1, -1, -1, -1, -1, -1, -1, 23, 22, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1});
        PPT.put("H", new Integer[]{-1, -1, -1, 24, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 25, 25, -1});
        PPT.put("K", new Integer[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 26, 27, -1});
        PPT.put("COP", new Integer[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 28, 29, 30, 31, 32, 33, -1, -1, -1, -1});

        // 构造productionList
        productionList = new ArrayList<>();
        productionList.add(new Production("S", new String[]{"id", "=", "E", ";", "S"})); // S -> id=E;S
        productionList.add(new Production("S", new String[]{"if", "(", "C", ")", "{", "S", "}", "else", "{", "S", "}"})); // S -> if (C) {S} else {S}
        productionList.add(new Production("S", new String[]{"while", "(", "C", ")", "{", "S", "}", "S"})); // S -> while (C) {S} S
        productionList.add(new Production("S", null)); // S -> ε
        productionList.add(new Production("E", new String[]{"T", "E''"})); // E -> TE’’
        productionList.add(new Production("E''", new String[]{"E'", "E''"})); // E’’ -> E’E’’
        productionList.add(new Production("E''", null)); // E’’ -> ε
        productionList.add(new Production("E'", new String[]{"+", "T"})); // E’ -> +T
        productionList.add(new Production("E'", new String[]{"-", "T"})); // E’ -> -T
        productionList.add(new Production("T", new String[]{"F", "T''"})); // T -> FT’’
        productionList.add(new Production("T''", new String[]{"T'", "T''"})); // T’’ -> T’T’’
        productionList.add(new Production("T''", null)); // T’’ -> ε
        productionList.add(new Production("T'", new String[]{"*", "F"})); // T’ -> *F
        productionList.add(new Production("T'", new String[]{"/", "F"})); // T’ -> /F
        productionList.add(new Production("F", new String[]{"num"})); // F -> num
        productionList.add(new Production("F", new String[]{"id"})); // F -> id
        productionList.add(new Production("F", new String[]{"(", "E", ")"})); // F -> (E)
        productionList.add(new Production("C", new String[]{"D", "C'"})); // C -> DC’
        productionList.add(new Production("C'", new String[]{"||", "D", "C'"})); // C’ -> ||DC’
        productionList.add(new Production("C''", null)); // C’ -> ε
        productionList.add(new Production("D", new String[]{"H", "D'"})); // D -> HD’
        productionList.add(new Production("D'", new String[]{"&&", "H", "D'"})); // D’ -> &&HD’
        productionList.add(new Production("D'", null)); // D’ -> ε
        productionList.add(new Production("H", new String[]{"(", "C", ")"})); // H -> (C)
        productionList.add(new Production("H", new String[]{"K", "COP", "K"})); //  H -> K COP K
        productionList.add(new Production("K", new String[]{"num"})); // K -> num
        productionList.add(new Production("K", new String[]{"id"})); // K -> id
        productionList.add(new Production("COP", new String[]{">"})); // COP -> >
        productionList.add(new Production("COP", new String[]{">="})); // COP -> >=
        productionList.add(new Production("COP", new String[]{"<"})); // COP -> <
        productionList.add(new Production("COP", new String[]{"<="})); // COP -> <=
        productionList.add(new Production("COP", new String[]{"=="})); // COP -> ==
        productionList.add(new Production("COP", new String[]{"!="})); // COP -> !=
    }

    /**
     * @param Vt
     * @param State
     * @return
     */
    public Production findProduction(String Vt, String State) {
        Integer[] row = PPT.get(State);
        int index = Arrays.asList(Vts.Vts).indexOf(Vt);

        if (row != null && row[index] > 0) {
            return productionList.get(row[index] - 1);
        } else {
            return null;
        }
    }

}
