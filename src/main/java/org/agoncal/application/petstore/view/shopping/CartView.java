package org.agoncal.application.petstore.view.shopping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.agoncal.application.petstore.model.*;
import org.agoncal.application.petstore.service.PurchaseOrderService;
import org.agoncal.application.petstore.util.Loggable;
import org.agoncal.application.petstore.view.CatchException;
import org.agoncal.application.petstore.view.HttpParam;
import org.agoncal.application.petstore.view.LoggedIn;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Named
@ConversationScoped
@Loggable
@CatchException
public class CartView implements Serializable {
    @Inject @HttpParam("itemId") String itemId;
    @Inject @LoggedIn private Instance<Customer> loggedInCustomer;
    @Inject private CartService cartService;
    @Inject private PurchaseOrderService orderService;

    @Inject @Getter private Conversation conversation;
    @Getter @Setter private CreditCard creditCard = new CreditCard();
    @Getter @Setter private PurchaseOrder order;
    private List<CartItem> cartItems;

    public String addItemToCart() {
        if (conversation.isTransient()) {
            cartService.removeAll();
            conversation.begin();
        }
        cartService.add(itemId);

        return "showcart.faces";
    }

    public String removeItemFromCart() {
        cartService.remove(itemId);
        return null;
    }

    public String updateQuantity() {
        for (CartItem cartItem : cartItems) {
            cartService.put(cartItem.getItem(), cartItem.getQuantity());
        }
        return null;
    }

    public String checkout() {
        return "confirmorder.faces";
    }

    public String confirmOrder() {
        order = orderService.createOrder(getCustomer(), creditCard, cartService.takeAll());
        if (!conversation.isTransient()) {
            conversation.end();
        }

        return "orderconfirmed.faces";
    }

    public boolean shoppingCartIsEmpty() {
        return cartService.cartIsEmpty();
    }

    public Float getTotal() {
        return cartService.getTotal();
    }

    public Customer getCustomer() {
        return loggedInCustomer.get();
    }

    public CreditCardType[] getCreditCardTypes() {
        return CreditCardType.values();
    }

    public String doFindItem() {
        return "showitem.faces";
    }

    public List<CartItem> getCartItems() {
        if (cartItems == null) {
            cartItems = new ArrayList<>();
            for (Map.Entry<Item, Integer> e : cartService.getItems().entrySet()) {
                cartItems.add(new CartItem(e.getKey(), e.getValue()));
            }
        }
        return cartItems;
    }

    @Data
    @AllArgsConstructor
    public static class CartItem {
        Item item;
        int quantity;

        public float getSubTotal() {
            return item.getUnitCost() * quantity;
        }
    }
}