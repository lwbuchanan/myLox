package net.lwbuchanan.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox {
    static boolean hadError = false;

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
        System.out.println("Usage: jlox [script]");
        System.exit(64); 
        } else if (args.length == 1) {
        runFile(args[0]);
        } else {
        runPrompt();
        }
    }

    /**
     * Executes the code in provided lox file
     * @param path - Path to a lox source file
     * @throws IOException
     */
    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));

        if (hadError) System.exit(65);
    }

    /**
     * The REPL (read-evaluate-print loop)
     * @throws IOException
     */
    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        while (true) {
            System.out.print("(lox repl) > ");
            String line = reader.readLine();
            if (line == null) break;
            run(line);
            hadError = false;
        }
    }

    /**
     * Top-level execution method of the interpreter
     * @param source - Raw lox source code
     */
    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        for (Token token : tokens) {
            System.out.println(token);
        }
    }

    /**
     * 
     * @param line
     * @param message
     */
    static void error(int line, String message) {
        report(line, "", message);
    }

    /**
     * 
     * @param line
     * @param where
     * @param message
     */
    private static void report(int line, String where, String message) {
        System.err.println("[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }
}
