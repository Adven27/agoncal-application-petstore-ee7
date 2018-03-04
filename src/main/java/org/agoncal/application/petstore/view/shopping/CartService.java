package org.agoncal.application.petstore.view.shopping;

import lombok.Getter;
import org.agoncal.application.petstore.model.Item;
import org.agoncal.application.petstore.service.CatalogService;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CartService implements Serializable {
    @Inject private CatalogService catalogService;
    @Getter private Map<Item, Integer> items = new HashMap<>();

    public void add(String itemId) {
        Item item = catalogService.findItem(Long.valueOf(itemId));
        if (!changeQuantityIfAlreadyHave(item)) {
            items.put(item, 1);
        }
    }

    public void put(Item item, int quantity) {
        items.put(item, quantity);
    }

    private boolean changeQuantityIfAlreadyHave(Item item) {
        if (items.containsKey(item)) {
            items.put(item, items.get(item) + 1);
            return true;
        }
        return false;
    }

    public void remove(String itemId) {
        items.remove(catalogService.findItem(Long.valueOf(itemId)));
    }

    public boolean cartIsEmpty() {
        return items == null || items.size() == 0;
    }

    public void removeAll() {
        items = new HashMap<>();
    }

    public Float getTotal() {
        if (items == null || items.isEmpty()) {
            return 0f;
        }

        Float total = 0f;

        for (Map.Entry<Item, Integer> it : items.entrySet()) {
            total += it.getKey().getUnitCost() * it.getValue();
        }
        return total;
    }

    public Map<Item, Integer> takeAll() {
        Map<Item, Integer> result = new HashMap<>(items);
        items.clear();
        return result;
    }
}