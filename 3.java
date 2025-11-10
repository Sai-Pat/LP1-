import java.util.*;

public class PassOneMacroProcessor {
    public static void main(String[] args) {
        String[] program = {
            "MACRO",
            "INCR &A,&B",
            "MOVER AREG,&A",
            "ADD AREG,&B",
            "MOVEM AREG,&A",
            "MEND",
            "START 100",
            "INCR NUM1,NUM2",
            "END"
        };

        List<String> MDT = new ArrayList<>(); // Macro Definition Table
        Map<String, Integer> MNT = new LinkedHashMap<>(); // Macro Name Table
        List<String> ALA = new ArrayList<>(); // Argument List Array

        boolean inMacro = false;
        int MDTIndex = 0;

        for (int i = 0; i < program.length; i++) {
            String line = program[i].trim();

            if (line.equalsIgnoreCase("MACRO")) {
                inMacro = true;
                continue;
            }

            if (inMacro) {
                if (line.equalsIgnoreCase("MEND")) {
                    MDT.add("MEND");
                    inMacro = false;
                    continue;
                }

                // First line after MACRO = macro name and parameters
                if (!line.startsWith("MOVER") && !line.startsWith("ADD") && !line.startsWith("MOVEM")) {
                    String[] parts = line.split("[ ,]+");
                    String macroName = parts[0];
                    MNT.put(macroName, MDTIndex); // store macro name with starting index of MDT

                    // Store arguments in ALA
                    if (parts.length > 1) {
                        String[] args = parts[1].split(",");
                        for (String arg : args) {
                            if (!ALA.contains(arg))
                                ALA.add(arg);
                        }
                    }
                } else {
                    // Replace arguments with positional notation like (P,1)
                    String newLine = line;
                    for (int k = 0; k < ALA.size(); k++) {
                        newLine = newLine.replace(ALA.get(k), "(P," + (k + 1) + ")");
                    }
                    MDT.add(newLine);
                    MDTIndex++;
                }
            }
        }

        // Display Tables
        System.out.println("----- MACRO NAME TABLE (MNT) -----");
        System.out.println("MacroName\tMDT Index");
        for (Map.Entry<String, Integer> e : MNT.entrySet()) {
            System.out.println(e.getKey() + "\t\t" + e.getValue());
        }

        System.out.println("\n----- MACRO DEFINITION TABLE (MDT) -----");
        int i = 0;
        for (String s : MDT) {
            System.out.println(i + "\t" + s);
            i++;
        }

        System.out.println("\n----- ARGUMENT LIST ARRAY (ALA) -----");
        for (int j = 0; j < ALA.size(); j++) {
            System.out.println((j + 1) + "\t" + ALA.get(j));
        }
    }
}


SAMPLE OUTPUT
----- MACRO NAME TABLE (MNT) -----
MacroName	MDT Index
INCR		0

----- MACRO DEFINITION TABLE (MDT) -----
0	MOVER AREG,(P,1)
1	ADD AREG,(P,2)
2	MOVEM AREG,(P,1)
3	MEND

----- ARGUMENT LIST ARRAY (ALA) -----
1	&A
2	&B
