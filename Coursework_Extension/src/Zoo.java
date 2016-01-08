import ZooSim.ErrorMsg;
import ZooSim.Simulator;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Container for Zoo Simulator. Provides back end for CLI interaction. (And it needs to be called Zoo to conform to the
 * spec.)
 *
 * @author Huw Jones
 * @since 28/10/2015
 */
class Zoo {

    public static void main(String args[]) {
        ArrayList<String> argList = new ArrayList<>(Arrays.asList(args));

        // Set batch mode
        Boolean batchMode = _getBatchMode(argList);

        // Get number of iterations
        int iterations = Zoo._getNumberOfIterations(argList, batchMode);

        // Get output mode
        String outputTo = Zoo._getOutput(argList, batchMode);


        if (argList.contains("-h") || argList.contains("--help")) {
            Zoo.help();
        } else if (argList.contains("-f") || argList.contains("--file")) {
            // therefore we know if it's not -f, then it must be --file
            int index = (argList.contains("-f")) ? argList.indexOf("-f") : argList.indexOf("--file");

            try {
                Zoo.fromFile(argList.get(index + 1), iterations, outputTo);
            } catch (Exception ex) {

            }
        } else if (argList.contains("-i") || argList.contains("--interactive")) {
            //Zoo.run(verbosity);
        } else if (argList.contains("-v") || argList.contains("--version")) {
            Zoo.version();
        } else {
            Zoo.help();
        }
    }

    /**
     * Loads the simulator from a config file
     *
     * @param file               File to load
     * @param numberOfIterations Number of months to "run"
     * @param outputTo           Destination of zoo state after simulation run.
     */
    private static void fromFile(String file, int numberOfIterations, String outputTo) {
        // Check file isn't a blank string
        if (file.equals("")) {
            Zoo.header();
            System.out.println("Could not open file. No file specified.");
            return;
        }

        // Check file exists
        File f = new File(file);
        if (!f.exists()) {
            Zoo.header();
            System.out.println("Could not open file. File, \"" + file + "\", was not found.");
            return;
        }

        // Check file is actually a file
        if (!f.isFile()) {
            Zoo.header();
            System.out.println("Could not open file. File, \"" + file + "\", was invalid.");
            System.out.println("File was not a file (probably a directory)");

            return;
        }
        try {
            // 1) Get file as byte encoded string
            // 2) Decode the file into ASCII
            // 3) Normalise line endings to Unix-like \n (I prefer it to Windows \r\n & Mac \r)
            //    Also, the lexer will throw a token not found error if it's fed \r
            File fileInf = new File(file);
            byte[] encodedFile = Files.readAllBytes(Paths.get(file));
            Charset encoding = StandardCharsets.US_ASCII;
            String fileContents = new String(encodedFile, encoding);
            fileContents = fileContents.replace("\r\n", "\n");
            fileContents = fileContents.replace('\r', '\n');

            Zoo.version();
            System.out.println("Loading config from " + file + "...");
            Simulator zoo = new Simulator();
            zoo.load(fileContents, fileInf.getName());
            System.out.println("Config loaded!");

            // Run x months
            for (int i = 0; i < numberOfIterations; i++) {
                zoo.aMonthPasses();
            }

            // Output state after simulation
            switch (outputTo) {
                case "none":
                    return;
                case "stdout":
                    System.out.println(zoo.save());
                    break;
                default:
                    saveFile(outputTo, zoo.save());
                    break;
            }

        } catch (Exception ex) {
            ErrorMsg.Fatal(ex.toString());
        }
    }

    /**
     * Prints out programme header info
     */
    private static void header() {
        System.out.println("Usage: Zoo [OPTION]...");
        System.out.println("COMP1202 Zoo Simulator that takes simulator setup from FILE.\n");
    }

    /**
     * Prints out ZooSim help
     */
    private static void help() {
        Zoo.header();
        System.out.println("Arguments:");
        System.out.println("  -b, --batch\t\tDo not enter interactive mode after config is loaded. Requires -n.");
        System.out.println("  -f, --file\t\tSpecifies file to load simulation config.");
        System.out.println("  -h, --help\t\tPrints this help message.");
        //System.out.println("  -i, --interactive\tRuns in interactive mode.");
        System.out.println("  -n, --number [INT]\tProcesses [INT] number of months.");
        System.out.println("  -o, --output [FILE]\tWhen simulation is complete, saves zoo state to [FILE]. Will overwrite existing files. \n\t\t\tIf no file is specified, will output to stdout. Implies -b, requires -n.");
        System.out.println("  -v, --version\t\tPrints version.");
    }

    /**
     * Prints out ZooSim version
     */
    private static void version() {
        System.out.println("ZooSim 0.9e");
        System.out.println("Written by Huw Jones for COMP1202 Coursework");
        Zoo.header();
    }

    /**
     * Gets debug/output level
     *
     * @param argList CLI arg list
     * @return int representing value. 0 = none, 1 = fatal, 2 = warn, 3 = all
     */
    private static int _getDebugMode(ArrayList<String> argList) {
        int debugLevel = 0;
        if (argList.contains("-d") || argList.contains("--debug")) {
            // Can use ternary operator here as we know either -d, or --debug exist
            // therefore we know if it's not -d, then it must be --debug
            int index = (argList.contains("-d")) ? argList.indexOf("-d") : argList.indexOf("--debug");
            String intStr = argList.get(index + 1);
            if (!intStr.matches("\\d")) {
                ErrorMsg.Fatal("Invalid argument passed to -d");
            }
            debugLevel = Integer.parseInt(intStr);
            if (debugLevel < 0) debugLevel = 0;
            if (debugLevel > 3) debugLevel = 3;
        }
        return debugLevel;
    }

    /**
     * Gets a boolean representing whether the simulator is running batch job or not
     *
     * @param argList CLI args list
     * @return boolean, true if batch mode, false if not
     */
    private static boolean _getBatchMode(ArrayList<String> argList) {
        Boolean batchMode = false;
        if (argList.contains("-b") || argList.contains("--batch")) {
            batchMode = true;
        }
        return batchMode;
    }

    /**
     * Gets number of iterations form the CLI args
     *
     * @param argList   List of CLI args
     * @param batchMode Used to check to see if the parameter is required
     * @return Number of iterations to run the simulator
     */
    private static int _getNumberOfIterations(ArrayList<String> argList, boolean batchMode) {
        int iterations = 0;
        if (argList.contains("-n") || argList.contains("--number")) {
            // Can use ternary operator here as we know either -d, or --number exist
            // therefore we know if it's not -n, then it must be --number
            int index = (argList.contains("-n")) ? argList.indexOf("-n") : argList.indexOf("--number");
            try {
                String intStr = argList.get(index + 1);
                if (!intStr.matches("[\\d]+")) {
                    Zoo.header();
                    ErrorMsg.Fatal("Invalid argument passed to -n.");
                }
                iterations = Integer.parseInt(intStr);
            } catch (Exception ex) {
                Zoo.header();
                ErrorMsg.Fatal("Invalid argument passed to -n.");
            }
        } else {
            if (batchMode) {
                ErrorMsg.Fatal("-n is required when -b flag is used.");
            }
        }
        return iterations;
    }

    /**
     * Get's the output source (if any) from the CLI args
     *
     * @param argList   List of CLI arguments
     * @param batchMode Required to see if output destination is required
     * @return String representing output source. Either "none", "stdout", or {filename}
     */
    private static String _getOutput(ArrayList<String> argList, boolean batchMode) {
        String outputTo = "none";

        if (argList.contains("-o") || argList.contains("--output")) {
            // Can use ternary operator here as we know either -o, or --output exist
            // therefore we know if it's not -o, then it must be --output
            int index = (argList.contains("-o")) ? argList.indexOf("-o") : argList.indexOf("--output");
            try {
                outputTo = argList.get(index + 1);
                if (outputTo.substring(0, 1).equals("-")) {
                    outputTo = "stdout";
                }
            } catch (Exception ex) {
                outputTo = "stdout";
            }
        } else {
            if (batchMode) {
                ErrorMsg.Fatal("-n is required when -b flag is used.");
            }
        }

        return outputTo;
    }

    /**
     * Saves a string to a file
     *
     * @param filename    Filename to save as
     * @param fileContent Content to write
     */
    private static void saveFile(String filename, String fileContent) {
        try {
            // Create a PrintWriter and set the charset to UTF-8
            // Why UTF-8? Well, I know we only need ASCII, but UTF-8 is nice
            PrintWriter writer = new PrintWriter(filename, "UTF-8");

            // Write to the file
            writer.write(fileContent);

            // Close the file handle
            writer.close();
        } catch (Exception e) {
            System.out.println("Failed to save to: " + filename);
        }
    }

}
