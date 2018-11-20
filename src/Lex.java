import util.ReservedWords;
import model.Token;

import java.io.IOException;
import java.util.ArrayList;

public class Lex {
    private int index;
    private char input[] = new char[500];
    private int code;            //记录当前Token的code
    private String string;       //记录当前Token的string
    private int row;             //记录当前的行
    private ArrayList<Token> output;

    public Lex(String filename){
        String inputFileName = filename;

        //输入
        try{
            input = IO.getInput(inputFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        output = new ArrayList<>();

        index = 0;
        row = 1;
        while(input[index] != '\0'){
            //每次循环识别一个词
            scan();     //识别
            recognizeToken();
        }
    }

    private void recognizeToken(){
        switch (code){
            case -1:{       //换行
                row++;
                break;
            }
            case -2:{       //数字太大
                output.add(new Token("Error: Integer overflow at row "+row));
                break;
            }
            case -3:{       //非法字符
                output.add(new Token("Error: Illegal character at row "+row));
                break;
            }
            case -4:{       //非法值
                output.add(new Token("Error: Illegal value at row "+row));
                break;
            }
            case -5:{       //要被忽略的
                break;
            }
            default:{       //正常情况
                output.add(new Token(code, string));
            }
        }
    }

    private void scan(){
        char[] chars = new char[30];
        int index2 = 0;     //当前词的计数

        char curCh = input[index++];

        //判断是保留字还是变量名，字母开头或者_$
        if((curCh >= 'a' && curCh <= 'z') || (curCh >= 'A' && curCh <= 'Z') || curCh == '_' || curCh == '$'){
            //获取这个词
            while((curCh >= 'a' && curCh <= 'z') || (curCh >= 'A' && curCh <= 'Z') || (curCh >= '0' && curCh <= '9')
                    || curCh == '_' || curCh == '$'){
                chars[index2] = curCh;
                index2++;
                curCh = input[index++];
            }

            //判断是否是保留字
            String[] reservedWords = ReservedWords.reservedWords;
            String curStr = String.valueOf(chars);
            curStr = curStr.substring(0, curStr.indexOf(0));
            for(int i = 0; i < reservedWords.length; i++){
                if(curStr.equals(reservedWords[i])){
                    code = i+1;     //记录为保留字
                    string = curStr;
                    index--;
                    return;
                }
            }

            //记录为变量名
            code = 59;
            string = curStr;
            index--;
        }
        else if(curCh >= '0' && curCh <= '9'){        //数字开头的
//        if(curCh >= '0' && curCh <= '9'){        //数字开头的
            int number = 0;
            boolean isIllegalValue = false;
            boolean isOverflow = false;
            index--;
            for( ; input[index] != 0; index++){
                curCh = input[index];
                if(curCh >= '0' && curCh <= '9'){
                    if(!isIllegalValue && !isOverflow){
                        number = number * 10 + curCh - '0';
                        if(number < 0){
                            isOverflow = true;
                        }
                    }
                }
                else if((curCh >= 'a' && curCh <= 'z') || (curCh >= 'A' && curCh <= 'Z') || curCh == '_' || curCh == '$'){
                    //数字开头的变量名，非法
                    isIllegalValue = true;
                }
                else{       //已读取完当前
                    break;
                }
            }
            if(isIllegalValue){      //非法变量名
                code = -4;
            }
            else{           //数字
                //数字太大
                if(isOverflow){
                    code = -2;
                }
                else{
                    code = 60;
                    string = String.valueOf(number);
                }
            }
        }
        else{       //其他情况
            chars[index2] = curCh;
            index2++;
            switch (curCh){
                case '+':{
                    curCh = input[index++];
                    if(curCh == '='){       // +=
                        chars[index2++] = curCh;
                        code = 26;
                    }
                    else{       //+
                        code = 25;
                        index--;
                    }
                    break;
                }
                case '-':{
                    curCh = input[index++];
                    if (curCh >= '0' && curCh <= '9') { //负数
                        int number = 0;
                        boolean isIllegalValue = false;
                        boolean isOverflow = false;
                        index--;
                        for( ; input[index] != 0; index++){
                            curCh = input[index];
                            if(curCh <= '9' && curCh >= '0'){
                                if(!isIllegalValue && !isOverflow){
                                    number = number * 10 + curCh - '0';
                                    if(number < 0){
                                        isOverflow = true;      //溢出了
                                    }
                                }
                            }
                            else if((curCh >= 'a' && curCh <= 'z') || (curCh >= 'A' && curCh <= 'Z') || curCh == '_' || curCh == '$'){
                                //数字开头的变量名，非法
                                isIllegalValue = true;
                            }
                            else{       //已读取完当前
                                break;
                            }
                        }
                        if(isIllegalValue){      //非法变量名
                            code = -4;
                        }
                        else{           //数字
                            //数字太大
                            if(isOverflow){
                                code = -2;
                            }
                            else{
                                code = 60;
                                string = String.valueOf(number*-1);
                            }
                        }
                    } else if (curCh == '=') { // -=
                        chars[index2++] = curCh;
                        code = 28;
                    } else { // -
                        code = 27;
                        index--;
                    }
                    break;
                }
                case '*':{
                    curCh = input[index++];
                    if(curCh == '='){       //  *=
                        chars[index2++] = curCh;
                        code = 30;
                    }
                    else if(curCh == '/') {      //  */
                        chars[index2++] = curCh;
                        code = 47;
                    }
                    else{           //  *
                        code = 29;
                        index--;
                    }
                    break;
                }
                case '/':{
                    curCh = input[index++];
                    if(curCh == '='){       //  /=
                        chars[index2++] = curCh;
                        code = 32;
                    }
                    else if(curCh == '/'){  //  //
                        chars[index2++] = curCh;
                        code = 45;
                    }
                    else if(curCh == '*'){  //  /*
                        chars[index2++] = curCh;
                        code = 46;
                    }
                    else{           //  /
                        code = 31;
                        index--;
                    }
                    break;
                }
                case '=':{
                    curCh = input[index++];
                    if(curCh == '='){       //  ==
                        chars[index2++] = curCh;
                        code = 34;
                    }
                    else{       //  =
                        code = 33;
                        index--;
                    }
                    break;
                }
                case '&':{
                    curCh = input[index++];
                    if(curCh == '&') {       //  &&
                        chars[index2++] = curCh;
                        code = 36;
                    }
                    else{       //  &
                        code = 35;
                        index--;
                    }
                    break;
                }
                case '|':{
                    curCh = input[index++];
                    if(curCh == '|') {       //  ||
                        chars[index2++] = curCh;
                        code = 38;
                    }
                    else{       //  |
                        code = 37;
                        index--;
                    }
                    break;
                }
                case '!':{
                    curCh = input[index++];
                    if(curCh == '=') {       // !=
                        chars[index2++] = curCh;
                        code = 40;
                    }
                    else{       //  !
                        code = 39;
                        index--;
                    }
                    break;
                }
                case '<':{
                    curCh = input[index++];
                    if(curCh == '=') {       // <=
                        chars[index2++] = curCh;
                        code = 42;
                    }
                    else{       //  <
                        code = 41;
                        index--;
                    }
                    break;
                }
                case '>':{
                    curCh = input[index++];
                    if(curCh == '=') {       // >=
                        chars[index2++] = curCh;
                        code = 44;
                    }
                    else{       //  >
                        code = 43;
                        index--;
                    }
                    break;
                }
                case '(':{
                    code = 48;
                    break;
                }
                case ')':{
                    code = 49;
                    break;
                }
                case '[':{
                    code = 50;
                    break;
                }
                case ']':{
                    code = 51;
                    break;
                }
                case '{':{
                    code = 52;
                    break;
                }
                case '}':{
                    code = 53;
                    break;
                }
                case ',':{
                    code = 54;
                    break;
                }
                case ':':{
                    code = 55;
                    break;
                }
                case ';':{
                    code = 56;
                    break;
                }
                case '\'':{
                    code = 57;
                    break;
                }
                case '"':{
                    code = 58;
                    break;
                }
                case '\n':{
                    code = -1;  //换行
                    break;
                }
                case ' ':{      //空格忽略
                    code = -5;
                    break;
                }
                default:{
                    code = -3;     //其他字符
                    break;
                }
            }
            if(code == 60){     //数字已经转换过string
                return;
            }
            //转换为string
            String curStr = String.valueOf(chars);
            curStr = curStr.substring(0, curStr.indexOf(0));
            string = curStr;
        }

    }

    public ArrayList<Token> getOutput(){
        return output;
    }
}
