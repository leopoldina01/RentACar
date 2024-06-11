package dao;

import beans.ShoppingCart;

import java.util.Collection;
import java.util.HashMap;

public class ShoppingCartDAO {
    private HashMap<Integer, ShoppingCart> shoppingCarts = new HashMap<Integer, ShoppingCart>();

    public Collection<ShoppingCart> findAll(){
        return shoppingCarts.values();
    }

    public ShoppingCart findShoppingCart(int id) {
        return shoppingCarts.containsKey(id) ? shoppingCarts.get(id) : null;
    }

    public ShoppingCart save(ShoppingCart shoppingCart) {
        Integer maxId = -1;
        for (int id : shoppingCarts.keySet()) {
            int idNum = id;
            if (idNum > maxId) {
                maxId = idNum;
            }
        }
        maxId++;
        shoppingCart.setId(maxId);
        shoppingCarts.put(shoppingCart.getId(), shoppingCart);
        return shoppingCart;
    }

    public ShoppingCart update(int id, ShoppingCart shoppingCart) {
        ShoppingCart shoppingCartToUpdate = findShoppingCart(id);
        if (shoppingCartToUpdate == null)
            return save(shoppingCart);
        shoppingCartToUpdate.setVehicles(shoppingCart.getVehicles());
        shoppingCartToUpdate.setPrice(shoppingCart.getPrice());
        delete(id);
        save(shoppingCartToUpdate);
        return shoppingCartToUpdate;
    }

    public void delete(int id) {
        shoppingCarts.remove(id);
    }
}
