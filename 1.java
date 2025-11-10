import java.util.*;

public class PassOneAssembler {
    public static void main(String[] args) {
        // Sample assembly program (can also be taken from file)
        String program[] = {
            "START 100",
            "MOVER AREG, NUM",
            "ADD BREG, ONE",
            "MOVEM AREG, RESULT",
            "ONE DC 1",
            "NUM DS 1",
            "RESULT DS 1",
            "END"
        };

        // Opcode table (MOT)
        Map<String, String> MOT = new HashMap<>();
        MOT.put("START", "AD");
        MOT.put("END", "AD");
        MOT.put("MOVER", "IS");
        MOT.put("MOVEM", "IS");
        MOT.put("ADD", "IS");
        MOT.put("DC", "DL");
        MOT.put("DS", "DL");

        int LC = 0; // Location counter
        Map<String, Integer> symbolTable = new LinkedHashMap<>();

        System.out.println("----- INTERMEDIATE CODE -----");
        for (String line : program) {
            String parts[] = line.split("[ ,]+"); // split by space or comma
            String opcode = parts[0];

            // START directive
            if (opcode.equals("START")) {
                LC = Integer.parseInt(parts[1]);
                System.out.println("(AD,01)\t(C," + LC + ")");
            }
            // END directive
            else if (opcode.equals("END")) {
                System.out.println("(AD,02)");
                break;
            }
            // Declarative statement DC or DS
            else if (opcode.equals("DC")) {
                symbolTable.put(parts[0], LC);
                System.out.println("(DL,01)\t(C," + parts[1] + ")");
                LC++;
            } 
            else if (opcode.equals("DS")) {
                symbolTable.put(parts[0], LC);
                System.out.println("(DL,02)\t(C," + parts[1] + ")");
                LC += Integer.parseInt(parts[1]);
            }
            // Imperative statements
            else {
                // Check if label present
                int index = 0;
                if (!MOT.containsKey(parts[0])) {
                    // It's a label
                    symbolTable.put(parts[0], LC);
                    index = 1;
                    opcode = parts[1];
                }
                System.out.println("(" + MOT.get(opcode) + ")\t" + parts[index + 1] + ", " + parts[index + 2]);
                LC++;
            }
        }

        // Display Symbol Table
        System.out.println("\n----- SYMBOL TABLE -----");
        System.out.println("Symbol\tAddress");
        for (Map.Entry<String, Integer> entry : symbolTable.entrySet()) {
            System.out.println(entry.getKey() + "\t" + entry.getValue());
        }
    }
}


Sample Input

START 100
MOVER AREG, NUM
ADD BREG, ONE
MOVEM AREG, RESULT
ONE DC 1
NUM DS 1
RESULT DS 1
END


Sample Output

----- INTERMEDIATE CODE -----
(AD,01)	(C,100)
(IS)	AREG, NUM
(IS)	BREG, ONE
(IS)	AREG, RESULT
(DL,01)	(C,1)
(DL,02)	(C,1)
(DL,02)	(C,1)
(AD,02)

----- SYMBOL TABLE -----
ONE	103
NUM	104
RESULT	105
