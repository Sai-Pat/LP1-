import java.util.*;

public class TinyPassOneMacroProcessor {
    public static void main(String[] args) {

        String[] program = {
            "MACRO",
            "INCR &X,&Y",
            "MOVER AREG,&X",
            "ADD AREG,&Y",
            "MOVEM AREG,&X",
            "MEND",
            "START 100",
            "INCR NUM1,NUM2",
            "END"
        };

        List<String> MDT = new ArrayList<>(); // Macro Definition Table
        Map<String, Integer> MNT = new LinkedHashMap<>(); // Macro Name Table
        List<String> ALA = new ArrayList<>(); // Argument List Array for current macro

        boolean inMacro = false;
        int MDTIndex = 0;

        for (String line : program) {
            line = line.trim();

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

                // Macro header line: MacroName &Arg1,&Arg2,...
                if (!line.startsWith("MOVER") && !line.startsWith("ADD") && !line.startsWith("MOVEM")) {
                    String[] parts = line.split("\\s+", 2); // Split into macro name and arguments
                    String macroName = parts[0];
                    MNT.put(macroName, MDTIndex);

                    // Clear previous arguments and store new ones
                    ALA.clear();
                    if (parts.length > 1) {
                        String[] argsArr = parts[1].split(",");
                        for (String arg : argsArr) {
                            ALA.add(arg.trim());
                        }
                    }
                } else {
                    // Replace arguments in macro body with positional notation (P,1), (P,2), ...
                    String newLine = line;
                    for (int i = 0; i < ALA.size(); i++) {
                        newLine = newLine.replace(ALA.get(i), "(P," + (i + 1) + ")");
                    }
                    MDT.add(newLine);
                    MDTIndex++;
                }
            }
        }

        // Display Macro Name Table
        System.out.println("----- MACRO NAME TABLE (MNT) -----");
        System.out.println("MacroName\tMDT Index");
        for (Map.Entry<String, Integer> e : MNT.entrySet())
            System.out.println(e.getKey() + "\t\t" + e.getValue());

        // Display Macro Definition Table
        System.out.println("\n----- MACRO DEFINITION TABLE (MDT) -----");
        for (int i = 0; i < MDT.size(); i++)
            System.out.println(i + "\t" + MDT.get(i));

        // Display Argument List Array
        System.out.println("\n----- ARGUMENT LIST ARRAY (ALA) -----");
        for (int i = 0; i < ALA.size(); i++)
            System.out.println((i + 1) + "\t" + ALA.get(i));
    }
}


----- MACRO NAME TABLE (MNT) -----
MacroName       MDT Index
INCR            0

----- MACRO DEFINITION TABLE (MDT) -----
0       MOVER AREG,(P,1)
1       ADD AREG,(P,2)
2       MOVEM AREG,(P,1)
3       MEND

----- ARGUMENT LIST ARRAY (ALA) -----  
1       &X
2       &Y
