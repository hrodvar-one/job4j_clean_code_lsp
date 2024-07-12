package ru.job4j.ood.isp.menu;

import java.util.Optional;
import java.util.Scanner;

public class TodoApp {

    private static final ActionDelegate DEFAULT_ACTION = () -> System.out.println("Some action");

    public static void main(String[] args) {
        Menu menu = new SimpleMenu();
        MenuPrinter printer = new Printer();
        Scanner scanner = new Scanner(System.in);
        boolean run = true;

        while (run) {
            showMenu();
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1 -> addRootItem(menu, scanner);
                case 2 -> addChildItem(menu, scanner);
                case 3 -> callAction(menu, scanner);
                case 4 -> printer.print(menu);
                case 5 -> run = false;
                default -> System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private static void showMenu() {
        System.out.println("Выберите опцию:");
        System.out.println("1. Добавить элемент в корень меню");
        System.out.println("2. Добавить элемент к родительскому элементу");
        System.out.println("3. Вызвать действие, привязанное к пункту меню");
        System.out.println("4. Вывести меню в консоль");
        System.out.println("5. Выйти");
        System.out.print("Ваш выбор: ");
    }

    private static void addRootItem(Menu menu, Scanner scanner) {
        System.out.print("Введите название элемента: ");
        String name = scanner.nextLine();
        menu.add(SimpleMenu.ROOT, name, DEFAULT_ACTION);
        System.out.println("Элемент добавлен в корень меню.");
    }

    private static void addChildItem(Menu menu, Scanner scanner) {
        System.out.print("Введите название родительского элемента: ");
        String parentName = scanner.nextLine();
        System.out.print("Введите название дочернего элемента: ");
        String childName = scanner.nextLine();
        if (menu.add(parentName, childName, DEFAULT_ACTION)) {
            System.out.println("Дочерний элемент добавлен.");
        } else {
            System.out.println("Ошибка: Родительский элемент не найден.");
        }
    }

    private static void callAction(Menu menu, Scanner scanner) {
        System.out.print("Введите название элемента для вызова действия: ");
        String itemName = scanner.nextLine();
        Optional<Menu.MenuItemInfo> itemInfo = menu.select(itemName);
        if (itemInfo.isPresent()) {
            itemInfo.get().getActionDelegate().delegate();
        } else {
            System.out.println("Ошибка: Элемент не найден.");
        }
    }
}
