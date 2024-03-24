package bullscows;

import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;

public class Main {
    private final char[] code;
    private final Random random = new Random();
    private final char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    private final char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private char[] symbols;
    private char[] symbolsLeft;
    private int turns;
    private int range;

    public Main(int length, int range) {
        this.range = range;
        this.turns = 1;
        this.symbols = new char[range];

        for (int i = 0; i < symbols.length; i++) {
            if (i <= 9) {
                symbols[i] = digits[i];
            } else {
                symbols[i] = alphabet[i-10];
            }
        }
        this.symbolsLeft = this.symbols;
        this.code = generateCode(length);
//        System.out.println(code);
    }
    public char[] getCode() {
        return this.code;
    }

    public void start(Scanner scanner) {
        System.out.println("Okay, let's start a game!");
        boolean guessed = false;
        while (!guessed) {
            System.out.printf("Turn %d: ", this.turns);
            String input = scanner.next();
            this.turns++;
            guessed = gradeInput(input);
            if (guessed) {
                System.out.println("Congratulations! You guessed the secret code.");
            }
        }
    }

    private char[] generateCode(int length) {
        char[] code = new char[length];
        for (int i = 0; i < code.length; i++) {
            code[i] = generateUniqueSymbol();
        }
        return code;
    }

    private char generateUniqueSymbol() {
        int index = random.nextInt(0, this.range);
        char symbol = this.symbolsLeft[index];
        this.symbolsLeft = removeElement(symbol);
        this.range--;
        return symbol;
    }

    private char[] removeElement(char element) {
        char[] arr = new char[symbolsLeft.length-1];
        int index = 0;
        for (char c : symbolsLeft) {
            if (c != element) {
                arr[index] = c;
                index++;
            }
        }
        return arr;
    }

    private boolean gradeInput(String input) {
        char[] inputDigits = input.toCharArray();

        if (inputDigits.length > this.code.length) {
            System.out.println("Error: Guess is longer than code length.");
            return false;
        }

        int cows = 0;
        int bulls = 0;
        
        for (int i = 0; i < inputDigits.length; i++) {
            if (this.code[i] == inputDigits[i]) bulls++;
        }
        if (bulls < this.code.length) {
            boolean found = false;
            for (int i = 0; i < inputDigits.length; i++) {
                for (int j = 0; j < inputDigits.length; j++) {
                    if (this.code[j] == inputDigits[i] && i != j) {
                        found = true;
                        break;
                    }
                }
                if (found) cows++;
                found = false;
            }
        }
        if (bulls == this.code.length) {
            System.out.printf("Grade: %d bull(s).\n", bulls);
            return true;
        }
        if (bulls == 0 && cows > 0) {
            System.out.printf("Grade: %d cow(s).\n", cows);
        } else if (bulls > 0 && cows > 0) {
            System.out.printf("Grade: %d bull(s) and %d cow(s).\n", bulls, cows);
        } else if (bulls > 0 && cows == 0) {
            System.out.printf("Grade: %d bull(s).\n", bulls);
        } else {
            System.out.println("Grade: None.");
        }
        return false;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Main game;
        while (true) {
            System.out.println("Input the length of the secret code:");

            int length = 0;
            if (scanner.hasNextInt()) {
                length = scanner.nextInt();
            } else if (scanner.hasNext()) {
                System.out.println("Error: " +scanner.nextLine() + " isn't a valid number.");
                continue;
            }

            if (length <= 0) {
                System.out.println("Error: length must be greater than 0!");
                continue;
            }

            if (length == 1) {
                System.out.println("Error: length must be greater than 1!");
                continue;
            }

            if (length > 36) {
                System.out.println("Error: maximum length of code is 36.");
                continue;
            }

            System.out.println("Input the number of possible symbols in the code:");
            
            int range = scanner.nextInt();

            if (range > 36) {
                System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
                continue;
            }

            if (range < length) {
                System.out.println("Error: it's not possible to generate a code with a length of " + length + " with " + range + " unique symbols.");
                continue;
            }

            game = new Main(length, range);
            String charsUsed;
            if (range == 11) {
                charsUsed = "(0-9, a)";
            } else if (range > 9) {
                charsUsed = String.format("(0-9, a-%s)", game.symbols[range-1]);
            } else {
                charsUsed = String.format("(0-%s)", game.symbols[range-1]);
            }
            char[] hiddenCode = new char[length];
            Arrays.fill(hiddenCode, '*');
            System.out.printf("The secret is prepared: %s %s.\n", String.valueOf(hiddenCode), charsUsed);
            break;
        }

        game.start(scanner);

    }
}
