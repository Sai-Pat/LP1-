import java.util.*;

public class TinyPassTwoAssembler {
    public static void main(String[] args) {

        // Symbol Table from Pass-One
        Map<String, Integer> symbolTable = new HashMap<>();
        symbolTable.put("NUM", 102);
        symbolTable.put("ONE", 103);

        // Tiny Intermediate Code
        String IC[] = {
            "(IS,01)\tAREG, NUM",
            "(IS,02)\tBREG, ONE",
            "(DL,01)\t(C,1)"
        };

        // Register codes
        Map<String, String> registerCode = new HashMap<>();
        registerCode.put("AREG", "01");
        registerCode.put("BREG", "02");

        System.out.println("----- MACHINE CODE -----");

        int LC = 0;
        for (String line : IC) {
            line = line.trim(); // remove leading/trailing spaces

            if (line.startsWith("(DL,01)")) { 
                // DC (Define Constant)
                int start = line.indexOf("(C,") + 3;
                int end = line.indexOf(")", start);
                String value = line.substring(start, end);
                System.out.println(LC + "\t00\t0\t" + value);
                LC++;
            } 
            else if (line.startsWith("(IS")) { 
                // Split line by (, ), comma, tab, or space
                String[] parts = line.split("[(),\t ]+");

                // parts[1] = opcode, parts[2] = register, parts[3] = operand
                if (parts.length < 4) {
                    System.out.println("Error: IC line not properly formatted: " + line);
                    continue;
                }

                String opcode = parts[1];
                String reg = parts[2];
                String operand = parts[3];

                String regCode = registerCode.getOrDefault(reg, "00");
                int addr = symbolTable.getOrDefault(operand, 0);

                System.out.println(LC + "\t" + opcode + "\t" + regCode + "\t" + addr);
                LC++;
            }
        }
    }
}



----- MACHINE CODE -----
0       IS      00      0
1       IS      00      0
2       00      0       1
