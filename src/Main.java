import model.Token;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String args[]){
        String inputFile = "test.txt";

        Lex lex = new Lex(inputFile);
        ArrayList<Token> tokens = lex.getOutput();
        tokens.add(new Token(0, "$"));

        Syntax syntax = new Syntax(tokens);
        ArrayList<String> output = syntax.getOutput();

        try{
            IO.output(output, inputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
