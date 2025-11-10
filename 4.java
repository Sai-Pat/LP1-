import java.util.*;

public class PassTwoMacroProcessor {
    public static void main(String[] args) {
        // ---------- PASS-I TABLES (assume generated earlier) ----------
        // Macro Name Table (MNT)
        Map<String, Integer> MNT = new HashMap<>();
        MNT.put("INCR", 0); // macro starts at MDT index 0

        // Macro Definition Table (MDT)
        List<String> MDT = new ArrayList<>();
        MDT.add("MOVER AREG,(P,1)");
        MDT.add("ADD AREG,(P,2)");
        MDT.add("MOVEM AREG,(P,1)");
        MDT.add("MEND");

        // ---------- SOURCE PROGRAM ----------
        String[] program = {
            "START 100",
            "INCR NUM1,NUM2",
            "END"
        };

        // ---------- PASS-II BEGINS ----------
        System.out.println("----- EXPANDED SOURCE CODE -----");
        for (String line : program) {
            String[] parts = line.split("[ ,]+");
            String opcode = parts[0];

            // If line is a macro call
            if (MNT.containsKey(opcode)) {
                int MDTIndex = MNT.get(opcode);

                // Build ALA (Actual Argument List)
                Map<Integer, String> ALA = new HashMap<>();
                if (parts.length > 1) {
                    String[] args = Arrays.copyOfRange(parts, 1, parts.length);
                    for (int i = 0; i < args.length; i++) {
                        ALA.put(i + 1, args[i]); // (P,1) -> NUM1, (P,2) -> NUM2
                    }
                }

                // Expand macro from MDT until MEND
                while (!MDT.get(MDTIndex).equals("MEND")) {
                    String mdtLine = MDT.get(MDTIndex);

                    // Replace (P,n) with actual argument
                    for (Map.Entry<Integer, String> e : ALA.entrySet()) {
                        mdtLine = mdtLine.replace("(P," + e.getKey() + ")", e.getValue());
                    }

                    System.out.println(mdtLine);
                    MDTIndex++;
                }
            } 
            else {
                // Normal assembler line – print as is
                System.out.println(line);
            }
        }
    }
}

SAMPLE OUTPUT

  ----- EXPANDED SOURCE CODE -----
START 100
MOVER AREG,NUM1
ADD AREG,NUM2
MOVEM AREG,NUM1
END
