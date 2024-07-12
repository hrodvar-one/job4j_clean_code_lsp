package ru.job4j.ood.isp.menu;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleMenuTest {

    public static final ActionDelegate STUB_ACTION = System.out::println;

    @Test
    public void whenAddThenReturnSame() {
        Menu menu = new SimpleMenu();
        menu.add(SimpleMenu.ROOT, "Сходить в магазин", STUB_ACTION);
        menu.add(SimpleMenu.ROOT, "Покормить собаку", STUB_ACTION);
        menu.add("Сходить в магазин", "Купить продукты", STUB_ACTION);
        menu.add("Купить продукты", "Купить хлеб", STUB_ACTION);
        menu.add("Купить продукты", "Купить молоко", STUB_ACTION);
        assertThat(new Menu.MenuItemInfo("Сходить в магазин",
                List.of("Купить продукты"), STUB_ACTION, "1."))
                .isEqualTo(menu.select("Сходить в магазин").get());
        assertThat(new Menu.MenuItemInfo(
                "Купить продукты",
                List.of("Купить хлеб", "Купить молоко"), STUB_ACTION, "1.1."))
                .isEqualTo(menu.select("Купить продукты").get());
        assertThat(new Menu.MenuItemInfo(
                "Покормить собаку", List.of(), STUB_ACTION, "2."))
                .isEqualTo(menu.select("Покормить собаку").get());
        menu.forEach(i -> System.out.println(i.getNumber() + " " + i.getName()));
    }

    @Test
    public void whenSelectThenReturnCorrectItem() {
        Menu menu = new SimpleMenu();
        menu.add(SimpleMenu.ROOT, "Сходить в магазин", STUB_ACTION);
        menu.add(SimpleMenu.ROOT, "Покормить собаку", STUB_ACTION);
        menu.add("Сходить в магазин", "Купить продукты", STUB_ACTION);
        menu.add("Купить продукты", "Купить хлеб", STUB_ACTION);
        menu.add("Купить продукты", "Купить молоко", STUB_ACTION);

        Optional<Menu.MenuItemInfo> selectedItem = menu.select("Купить молоко");

        assertThat(selectedItem).isPresent();
        assertThat(selectedItem.get().getName()).isEqualTo("Купить молоко");
        assertThat(selectedItem.get().getChildren()).isEmpty();
        assertThat(selectedItem.get().getActionDelegate()).isEqualTo(STUB_ACTION);
        assertThat(selectedItem.get().getNumber()).isEqualTo("1.1.2.");
    }
}