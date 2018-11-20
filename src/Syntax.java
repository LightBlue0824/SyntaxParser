import model.PPT;
import model.Production;
import model.Token;

import java.util.ArrayList;
import java.util.Stack;

public class Syntax {
    private PPT ppt = new PPT();
    private ArrayList<String> output = new ArrayList<>();

    public Syntax(ArrayList<Token> tokens){
        analyse(tokens);
    }

    private void analyse(ArrayList<Token> tokens){
        Stack<String> stack = new Stack<>();
        stack.push("$");
        stack.push("S");

        while(tokens.size() > 0 && stack.size() > 0){
            Token curToken = tokens.get(0);
            String curTokenStr = null;
            if(curToken.getCode() == 59){
                curTokenStr = "id";
            }else if(curToken.getCode() == 60){
                curTokenStr = "num";
            }else{
                curTokenStr = curToken.getString();
            }
            String v = stack.pop();

            if(curTokenStr.equals(v)){      //匹配
                tokens.remove(0);
            }
            else{       //不匹配，v为终结符
                Production production = ppt.findProduction(curTokenStr, v);
                if(production != null){     //有符合的产生式
                    output.add(production.toString());
                    String[] right = production.getRight();
                    if(right != null){
                        //压入栈中，倒序
                        for(int i = right.length-1; i >= 0; i--){
                            stack.push(right[i]);
                        }
                    }
                }
                else{       //没有符合的产生式
                    output.add("ERROR：Invalid Input \"" + curTokenStr + "\"");
                    break;
                }
            }
        }
        if(tokens.size() == 0 && stack.size() == 0){
            System.out.println("SUCCESS");
        }
        else{
            System.out.println("ERROR");
        }
    }

    public ArrayList<String> getOutput(){
        return output;
    }
}
