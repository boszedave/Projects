package search;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Map<String, List<Integer>> invertedIndexes = new HashMap<>();
    private static List<String> people = new ArrayList<>();

    public static void main(String[] args) {
        readFromFile(args);
        createInvertedIndexes();
        printMenu();
    }

    private static void readFromFile(String[] args) {
        if (args[0].equals("--data")) {
            File file = new File(args[1]);
            try (Scanner fileScanner = new Scanner(file)) {
                while (fileScanner.hasNextLine()) {
                    people.add(fileScanner.nextLine());
                }
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private static void createInvertedIndexes() {
        for (String line : people) {
            String[] wordsArr = line.split(" ");
            for (String word : wordsArr) {
                if (invertedIndexes.containsKey(word.toLowerCase())) {
                    List<Integer> indexes = invertedIndexes.get(word.toLowerCase());
                    indexes.add(Main.people.indexOf(line));
                }
                else {
                    List<Integer> indexes = new ArrayList<>();
                    indexes.add(Main.people.indexOf(line));
                    invertedIndexes.put(word.toLowerCase(), indexes);
                }
            }
        }
    }

    private static void printMenu() {
        while (true) {
            System.out.println("\n=== Menu ===");
            System.out.println("1. Find a person");
            System.out.println("2. Print all people");
            System.out.println("0. Exit");

            int menuOption = Integer.parseInt(scanner.nextLine());

            switch (menuOption) {
                case 1:
                    doActions();
                    break;
                case 2:
                    printList(people);
                    break;
                case 0:
                    System.out.println("Bye!");
                    return;
                default:
                    System.out.println("Wrong option! Try again.");
            }
        }
    }

    private static void doActions() {
        System.out.println("Select a matching strategy: ALL, ANY, NONE");
        String strategy = scanner.nextLine();

        List<String> peopleFound = new ArrayList<>();

        switch (strategy){
            case "ANY":
                System.out.println("Enter a name or email to search all suitable people.");
                String data = scanner.nextLine().toLowerCase();
                String[] searchQueryAny = data.split(" ");

                searchAny(peopleFound, searchQueryAny);
                printFoundPeople(peopleFound);
                break;
            case "ALL":
                System.out.println("Enter a name or email to search all suitable people.");
                String dataAll = scanner.nextLine();
                String[] searchQueryAll = dataAll.split(" ");

                searchAll(peopleFound, searchQueryAll);
                printFoundPeople(peopleFound);
                break;
            case "NONE":
                System.out.println("Enter a name or email to search all suitable people.");
                String dataNone = scanner.nextLine();
                String[] searchQueryNone = dataNone.split(" ");

                List<String> peopleToDelete = searchNone(peopleFound, searchQueryNone);
                printFoundPeople(peopleToDelete);
                break;
            default:
                System.out.println("Wrong option! Try again.");
                break;
        }


    }

    private static void searchAny(List<String> peopleFound, @NotNull String[] searchQueryAny) {
        for (String word : searchQueryAny) {
            if (invertedIndexes.containsKey(word.toLowerCase())) {
                List<Integer> indexes = invertedIndexes.get(word);
                for (Integer index : indexes) {
                    peopleFound.add(people.get(index));
                }
            }
        }
    }

    private static void searchAll(List<String> peopleFound, @NotNull String[] searchQueryAll) {
        List<Integer> arrayOfAllIndexes = new ArrayList<>();
        for (int i=0; i<searchQueryAll.length; i++) {
            if (invertedIndexes.containsKey(searchQueryAll[i].toLowerCase())) {
                List<Integer> indexes = invertedIndexes.get(searchQueryAll[i].toLowerCase());
                for (int j = 0; j < indexes.size(); j++) {
                    arrayOfAllIndexes.add(indexes.get(j));
                }
            }
        }

        List<Integer> indexesOfWordsInSameLine = new ArrayList<>();

        for (int i = 1; i < arrayOfAllIndexes.size(); i++) {
            if (arrayOfAllIndexes.get(0) == arrayOfAllIndexes.get(i)){
                indexesOfWordsInSameLine.add(arrayOfAllIndexes.get(i));
            }
        }

        if (indexesOfWordsInSameLine.size() != 0) {
            for (int i = 0; i < indexesOfWordsInSameLine.size(); i++) {
                peopleFound.add(people.get(arrayOfAllIndexes.get(i)));
            }
        }
        else {
            for (int i = 0; i < arrayOfAllIndexes.size(); i++) {
                peopleFound.add(people.get(arrayOfAllIndexes.get(i)));
            }
        }
    }

    private static List<String> searchNone(List<String> peopleFound, @NotNull String[] searchQueryNone) {
        for (String word : searchQueryNone) {
            if (invertedIndexes.containsKey(word.toLowerCase())) {
                List<Integer> indexes = invertedIndexes.get(word);
                for (Integer index : indexes) {
                    peopleFound.add(people.get(index));
                }
            }
        }

        List<String> peopleToDelete = new ArrayList<>(people);

        for (int i = 0; i < peopleFound.size(); i++) {
            if (people.contains(peopleFound.get(i))){
                peopleToDelete.remove(peopleFound.get(i));
            }
        }
        return peopleToDelete;
    }

    private static void printFoundPeople(@NotNull List<String> peopleFound) {
        if (peopleFound.size() != 0) {
            System.out.println(peopleFound.size() + " persons found:");
            printList(peopleFound);
        }
        else {
            System.out.println("No matching people found.");
        }
    }

    private static void printList(@NotNull List<String> list) {
        list.forEach(System.out::println);
    }

}