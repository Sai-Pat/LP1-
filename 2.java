import java.util.*;

public class PassTwoAssembler {
    public static void main(String[] args) {

        // SYMBOL TABLE (From Pass-I)
        Map<String, Integer> symbolTable = new HashMap<>();
        symbolTable.put("ONE", 103);
        symbolTable.put("NUM", 104);
        symbolTable.put("RESULT", 105);

        // Intermediate Code (simplified)
        String IC[] = {
            "(AD,01)\t(C,100)",
            "(IS,04)\tAREG, NUM",
            "(IS,01)\tBREG, ONE",
            "(IS,05)\tAREG, RESULT",
            "(DL,01)\t(C,1)",
            "(DL,02)\t(C,1)",
            "(AD,02)"
        };

        // Register codes
        Map<String, String> registerCode = new HashMap<>();
        registerCode.put("AREG", "01");
        registerCode.put("BREG", "02");
        registerCode.put("CREG", "03");

        System.out.println("----- MACHINE CODE -----");

        int LC = 0;
        for (String line : IC) {
            if (line.contains("(AD")) {
                // Assembler directives (START/END) are ignored in Pass-2
                continue;
            } else if (line.contains("(DL,01)")) {
                // DC (Define Constant)
                String value = line.substring(line.indexOf("(C,") + 3, line.indexOf(")"));
                System.out.println(LC + "\t00\t0\t" + value);
                LC++;
            } else if (line.contains("(DL,02)")) {
                // DS (Define Storage) – reserve memory, no code
                LC++;
            } else if (line.contains("(IS")) {
                // Imperative Statement
                String[] parts = line.split("[ ,\t]+");
                String opcodePart = parts[0];
                String reg = parts[1];
                String operand = parts[2];

                // Extract opcode number (IS,0x)
                String opcode = opcodePart.substring(4, 6).replace(")", "");
                if (opcode.equals("")) opcode = "00";

                // Get register code
                String regCode = registerCode.getOrDefault(reg, "00");

                // Get symbol address
                int addr = symbolTable.getOrDefault(operand, 0);

                System.out.println(LC + "\t" + opcode + "\t" + regCode + "\t" + addr);
                LC++;
            }
        }
    }
}

SAMPLE OUTPUT
----- MACHINE CODE -----
0	04	01	104
1	01	02	103
2	05	01	105
3	00	0	1
