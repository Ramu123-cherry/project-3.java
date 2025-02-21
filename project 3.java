import java.util.*;
import java.io.*;

public class ToDoListApp {
    private static List<Task> tasks = new ArrayList<>();
    private static final String FILE_NAME = "tasks.txt";

    public static void main(String[] args) {
        loadTasksFromFile();
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline left-over
            switch (choice) {
                case 1:
                    addTask(scanner);
                    break;
                case 2:
                    markTaskCompleted(scanner);
                    break;
                case 3:
                    viewTasks(false); // View pending tasks
                    break;
                case 4:
                    viewTasks(true); // View completed tasks
                    break;
                case 5:
                    saveTasksToFile();
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    // Display the menu options
    private static void displayMenu() {
        System.out.println("\nTo-Do List Application");
        System.out.println("1. Add Task");
        System.out.println("2. Mark Task as Completed");
        System.out.println("3. View Pending Tasks");
        System.out.println("4. View Completed Tasks");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }

    // Add a task
    private static void addTask(Scanner scanner) {
        System.out.print("Enter task title: ");
        String title = scanner.nextLine();
        System.out.print("Enter due date (Optional, leave blank if not applicable): ");
        String dueDate = scanner.nextLine();
        
        Task newTask = new Task(title, dueDate.isEmpty() ? null : dueDate);
        tasks.add(newTask);
        System.out.println("Task added successfully.");
    }

    // Mark task as completed
    private static void markTaskCompleted(Scanner scanner) {
        viewTasks(false); // Show pending tasks first
        System.out.print("Enter the task number to mark as completed: ");
        int taskNumber = scanner.nextInt();
        if (taskNumber >= 1 && taskNumber <= tasks.size() && !tasks.get(taskNumber - 1).isCompleted()) {
            tasks.get(taskNumber - 1).markAsCompleted();
            System.out.println("Task marked as completed.");
        } else {
            System.out.println("Invalid task number or task already completed.");
        }
    }

    // View tasks based on their status
    private static void viewTasks(boolean completed) {
        System.out.println("\n" + (completed ? "Completed Tasks" : "Pending Tasks"));
        int count = 1;
        for (Task task : tasks) {
            if (task.isCompleted() == completed) {
                System.out.println(count++ + ". " + task);
            }
        }
        if (count == 1) {
            System.out.println("No tasks to display.");
        }
    }

    // Load tasks from file
    private static void loadTasksFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            tasks = (List<Task>)ois.readObject();
        } catch (FileNotFoundException e) {
            // No previous tasks file found, starting fresh
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading tasks from file.");
        }
    }

    // Save tasks to file
    private static void saveTasksToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(tasks);
        } catch (IOException e) {
            System.out.println("Error saving tasks to file.");
        }
    }

    // Task class to represent individual tasks
    static class Task implements Serializable {
        private String title;
        private String dueDate;
        private boolean completed;

        public Task(String title, String dueDate) {
            this.title = title;
            this.dueDate = dueDate;
            this.completed = false;
        }

        public void markAsCompleted() {
            this.completed = true;
        }

        public boolean isCompleted() {
            return completed;
        }

        @Override
        public String toString() {
            return title + (dueDate != null ? " (Due: " + dueDate + ")" : "") + (completed ? " - Completed" : " - Pending");
        }
    }
}
