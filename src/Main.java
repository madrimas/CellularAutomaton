import processing.core.PApplet;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main extends PApplet {
    public static void main(String[] args) {
        PApplet.main("Main", args);
    }

    private CA ca;

    @Override
    public void settings() {
        size(1280, 720);
        noSmooth();
    }

    @Override
    public void setup() {
        int[] ruleSet = {0, 1, 0, 1, 1, 0, 1, 0};    // An initial rule system
        ca = new CA(ruleSet);                 // Initialize CA
        background(0);
    }

    @Override
    public void draw() {
        ca.render();    // Draw the CA
        ca.generate();  // Generate the next level

    }

    @Override
    public void mousePressed() {
        background(0);
        ca.setRule();
        ca.restart();
    }


    class CA {

        int[] cells;     // An array of 0s and 1s
        int generation;  // How many generations?
        int scl;         // How many pixels wide/high is each cell?

        int[] rules;     // An array to store the ruleSet, for example {0,1,1,0,1,1,0,1}

        CA(int[] r) {
            rules = r;
            scl = 1;
            cells = new int[width / scl];
            restart();
        }


        void setRule() {
            boolean isCorrect = false;
            int decimal;
            while (!isCorrect) {
                System.out.print("Podaj numer reguły <0;255>: ");
                try {
                    Scanner read = new Scanner(System.in);
                    decimal = read.nextInt();
                    String binary = Integer.toBinaryString(decimal);
                    rules = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
                    for (int i = 0; i < binary.length(); i++) {
                        rules[7 - i] = Character.getNumericValue(binary.charAt(binary.length() - 1 - i));
                    }
                    isCorrect = true;
                } catch (InputMismatchException inputMismatchException) {
                    System.out.println("Niepoprawny parametr! Nie podano liczby.");
                } catch (ArrayIndexOutOfBoundsException indexExcption){
                    System.out.println("Niepoprawny parametr! Podana liczba jest poza zakresem.");
                }
            }
        }

        // Reset to generation 0
        void restart() {
            for (int i = 0; i < cells.length; i++) {
                cells[i] = 0;
            }
            cells[cells.length / 2] = 1;    // We arbitrarily start with just the middle cell having a state of "1"
            generation = 0;
        }

        // The process of creating the new generation
        void generate() {
            // First we create an empty array for the new values
            int[] nextGen = new int[cells.length];
            // For every spot, determine new state by examine current state, and neighbor states
            // Ignore edges that only have one neighbor
            for (int i = 1; i < cells.length - 1; i++) {
                int left = cells[i - 1];   // Left neighbor state
                int me = cells[i];       // Current state
                int right = cells[i + 1];  // Right neighbor state
                nextGen[i] = executeRules(left, me, right); // Compute next generation state based on ruleset
            }
            // Copy the array into current value
            System.arraycopy(nextGen, 1, cells, 1, cells.length - 1 - 1);
            //cells = (int[]) nextGen.clone();
            generation++;
        }

        // This is the easy part, just draw the cells, fill 255 for '1', fill 0 for '0'
        void render() {
            for (int i = 0; i < cells.length; i++) {
                if (cells[i] == 1) {
                    fill(255);
                } else {
                    fill(0);
                }
                noStroke();
                rect(i * scl, generation * scl, scl, scl);
            }
        }

        // Implementing the Wolfram rules
        // Could be improved and made more concise, but here we can explicitly see what is going on for each case
        int executeRules(int a, int b, int c) {
            if (a == 1 && b == 1 && c == 1) {
                return rules[0];
            }
            if (a == 1 && b == 1 && c == 0) {
                return rules[1];
            }
            if (a == 1 && b == 0 && c == 1) {
                return rules[2];
            }
            if (a == 1 && b == 0 && c == 0) {
                return rules[3];
            }
            if (a == 0 && b == 1 && c == 1) {
                return rules[4];
            }
            if (a == 0 && b == 1 && c == 0) {
                return rules[5];
            }
            if (a == 0 && b == 0 && c == 1) {
                return rules[6];
            }
            if (a == 0 && b == 0 && c == 0) {
                return rules[7];
            }
            return 0;
        }


    }
}