package ZooSim;

import ZooSim.Parser.Lexer;

/**
 * Provides error message handling (and useful debugging info)
 *
 * @author Huw Jones
 * @since 24/10/2015
 */
public class ErrorMsg {
    public static void Fatal(String message, Lexer lexer) {
        StringBuilder errorMsg = new StringBuilder();
        errorMsg.append("FATAL: ");
        errorMsg.append(message);
        errorMsg.append("\nOn line ");
        errorMsg.append(lexer.getLineNumber());
        errorMsg.append(", position ");
        errorMsg.append(lexer.getLinePosition());
        if (!lexer.getFileName().equals("")) {
            errorMsg.append(", in ");
            errorMsg.append(lexer.getFileName());
            errorMsg.append(".");
        }
        ErrorMsg.Fatal(errorMsg.toString());
    }

    public static void Fatal(String message) {
        System.out.println("FATAL: " + message);
        System.exit(-1);
    }

    public static void Warning(String message, Lexer lexer) {
        StringBuilder errorMsg = new StringBuilder();
        errorMsg.append(message);
        errorMsg.append("\nOn line ");
        errorMsg.append(lexer.getLineNumber());
        errorMsg.append(", position ");
        errorMsg.append(lexer.getLinePosition());
        if (!lexer.getFileName().equals("")) {
            errorMsg.append(", in ");
            errorMsg.append(lexer.getFileName());
            errorMsg.append(".");
        }
        ErrorMsg.Warning(errorMsg.toString());
    }

    public static void Warning(String message) {
        System.out.println("WARN: " + message);
    }

    public static void Information(String message, Lexer lexer) {
        StringBuilder errorMsg = new StringBuilder();
        errorMsg.append(message);
        errorMsg.append("\nOn line ");
        errorMsg.append(lexer.getLineNumber());
        errorMsg.append(", position ");
        errorMsg.append(lexer.getLinePosition());
        if (!lexer.getFileName().equals("")) {
            errorMsg.append(", in ");
            errorMsg.append(lexer.getFileName());
            errorMsg.append(".");
        }
        ErrorMsg.Information(errorMsg.toString());
    }

    public static void Information(String message) {
        System.out.println("INFO: " + message);
    }

}
