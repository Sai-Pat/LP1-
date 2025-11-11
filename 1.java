import java.util.*;

public class TinyPassOneAssembler {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter number of lines in program:");
        int n = sc.nextInt();
        sc.nextLine(); // consume newline

        String[] program = new String[n];
        System.out.println("Enter program lines:");
        for (int i = 0; i < n; i++) {
            program[i] = sc.nextLine();
        }

        // Very simple opcode table
        Map<String, String> MOT = new HashMap<>();
        MOT.put("START", "AD");
        MOT.put("END", "AD");
        MOT.put("MOVER", "IS");
        MOT.put("ADD", "IS");
        MOT.put("DC", "DL");
        MOT.put("DS", "DL");

        int LC = 0;
        Map<String, Integer> symbolTable = new LinkedHashMap<>();

        System.out.println("\n----- INTERMEDIATE CODE -----");
        for (String line : program) {
            String[] parts = line.split("[ ,]+");
            String opcode = parts[0];

            if (opcode.equals("START")) {
                LC = Integer.parseInt(parts[1]);
                System.out.println("(AD,01)\t(C," + LC + ")");
            } 
            else if (opcode.equals("END")) {
                System.out.println("(AD,02)");
                break;
            }
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
            else { // Imperative statements
                System.out.println("(" + MOT.get(opcode) + ")\t" + parts[1] + ", " + parts[2]);
                LC++;
            }
        }

        System.out.println("\n----- SYMBOL TABLE -----");
        System.out.println("Symbol\tAddress");
        for (Map.Entry<String, Integer> entry : symbolTable.entrySet()) {
            System.out.println(entry.getKey() + "\t" + entry.getValue());
        }
    }
}


Enter number of lines in program:
5
Enter program lines:
START 100
MOVER AREG,NUM 
ADD BREG, ONE
NUM DS 1
END

----- INTERMEDIATE CODE -----
(AD,01) (C,100)
(IS)    AREG, NUM
(IS)    BREG, ONE
(null)  DS, 1
(AD,02)

----- SYMBOL TABLE -----
Symbol  Address
