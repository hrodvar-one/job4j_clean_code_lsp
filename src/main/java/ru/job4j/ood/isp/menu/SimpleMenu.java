package ru.job4j.ood.isp.menu;

import java.util.*;

public class SimpleMenu implements Menu {

    public static final String ROOT = "root";
    private final List<MenuItem> rootElements = new ArrayList<>();

    @Override
    public boolean add(String parentName, String childName, ActionDelegate actionDelegate) {
        MenuItem newItem = new SimpleMenuItem(childName, actionDelegate);
        if (ROOT.equals(parentName)) {
            rootElements.add(newItem);
            return true;
        } else {
            Optional<ItemInfo> parentItem = findItem(parentName);
            if (parentItem.isPresent()) {
                parentItem.get().menuItem.getChildren().add(newItem);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public Optional<MenuItemInfo> select(String itemName) {
        Optional<ItemInfo> itemInfo = findItem(itemName);
        return itemInfo.map(item -> new MenuItemInfo(
                item.menuItem.getName(),
                item.menuItem.getChildren().stream().map(MenuItem::getName).toList(),
                item.menuItem.getActionDelegate(),
                item.number
        ));
    }

    @Override
    public Iterator<MenuItemInfo> iterator() {
        List<MenuItemInfo> allItems = new ArrayList<>();
        DFSIterator iterator = new DFSIterator();
        while (iterator.hasNext()) {
            ItemInfo item = iterator.next();
            allItems.add(new MenuItemInfo(
                    item.menuItem.getName(),
                    item.menuItem.getChildren().stream().map(MenuItem::getName).toList(),
                    item.menuItem.getActionDelegate(),
                    item.number
            ));
        }
        return allItems.iterator();
    }

    private Optional<ItemInfo> findItem(String name) {
        DFSIterator iter = new DFSIterator();
        while (iter.hasNext()) {
            ItemInfo item = iter.next();
            if (item.menuItem.getName().equals(name)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    private static class SimpleMenuItem implements MenuItem {

        private final String name;
        private final List<MenuItem> children = new ArrayList<>();
        private final ActionDelegate actionDelegate;

        public SimpleMenuItem(String name, ActionDelegate actionDelegate) {
            this.name = name;
            this.actionDelegate = actionDelegate;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public List<MenuItem> getChildren() {
            return children;
        }

        @Override
        public ActionDelegate getActionDelegate() {
            return actionDelegate;
        }
    }

    private class DFSIterator implements Iterator<ItemInfo> {

        private final Deque<MenuItem> stack = new LinkedList<>();
        private final Deque<String> numbers = new LinkedList<>();

        DFSIterator() {
            int number = 1;
            for (MenuItem item : rootElements) {
                stack.addLast(item);
                numbers.addLast(String.valueOf(number++).concat("."));
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public ItemInfo next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            MenuItem current = stack.removeFirst();
            String lastNumber = numbers.removeFirst();
            List<MenuItem> children = current.getChildren();
            int currentNumber = children.size();
            for (var i = children.listIterator(children.size()); i.hasPrevious();) {
                stack.addFirst(i.previous());
                numbers.addFirst(lastNumber.concat(String.valueOf(currentNumber--)).concat("."));
            }
            return new ItemInfo(current, lastNumber);
        }
    }

    private class ItemInfo {

        private final MenuItem menuItem;
        private final String number;

        public ItemInfo(MenuItem menuItem, String number) {
            this.menuItem = menuItem;
            this.number = number;
        }
    }
}