import model.Production;

import java.io.*;
import java.util.ArrayList;

public class IO {
    public static char[] getInput(String inputFileName) throws IOException{
        File file = new File(inputFileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String perLine;
        int index = 0;
        char input[] = new char[500];

        while ((perLine = br.readLine()) != null) {
            char[] tempChars = perLine.toCharArray();
            for (char c : tempChars) {
                if (c == '\t') {
//                    System.out.println("t");
                    continue;
                }
                input[index++] = c;
            }
            input[index++] = '\n';
        }
        input[index] = 0;
        br.close();

        return input;
    }

    public static void output(ArrayList<String> output, String inputFileName) throws IOException {
        String outputFileName = inputFileName.split("\\.")[0]+"_result.txt";
        File outputFile = new File(outputFileName);
        BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile, false));

        for(int i = 0; i < output.size(); i++){
            bw.write("（"+i+"）"+output.get(i));
            bw.newLine();
        }
        bw.flush();
        bw.close();
    }
}
