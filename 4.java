import java.util.*;

public class TinyPassTwoMacroProcessor {
    public static void main(String[] args) {

        // ---------- PASS-I TABLES ----------
        // Macro Name Table (MNT)
        Map<String, Integer> MNT = new HashMap<>();
        MNT.put("INCR", 0); // Macro INCR starts at MDT index 0

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

        // ---------- PASS-II: EXPAND MACROS ----------
        System.out.println("----- EXPANDED SOURCE CODE -----");
        for (String line : program) {
            line = line.trim();
            String[] parts = line.split("[ ,]+");
            String opcode = parts[0];

            // If the line is a macro call
            if (MNT.containsKey(opcode)) {
                int MDTIndex = MNT.get(opcode);

                // Build Actual Argument List (ALA)
                Map<Integer, String> ALA = new HashMap<>();
                for (int i = 1; i < parts.length; i++) {
                    ALA.put(i, parts[i]); // (P,1) -> NUM1, (P,2) -> NUM2
                }

                // Expand macro instructions until MEND
                while (!MDT.get(MDTIndex).equals("MEND")) {
                    String mdtLine = MDT.get(MDTIndex);

                    // Replace formal parameters with actual arguments
                    for (Map.Entry<Integer, String> e : ALA.entrySet()) {
                        mdtLine = mdtLine.replace("(P," + e.getKey() + ")", e.getValue());
                    }

                    System.out.println(mdtLine);
                    MDTIndex++;
                }
            } else {
                // Non-macro line – print as is
                System.out.println(line);
            }
        }
    }
}



----- EXPANDED SOURCE CODE -----
START 100
MOVER AREG,NUM1
ADD AREG,NUM2
MOVEM AREG,NUM1
END
