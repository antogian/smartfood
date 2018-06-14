package com.athena.controllers;

import com.athena.entities.Menu;
import com.athena.entities.Product;
import com.athena.entities.User;
import com.athena.model.ShoppingCart;
import com.athena.services.MenuService;
import com.athena.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class MenuController
{
    private List<Menu> allMenus;
    private ShoppingCart cart;
    private User currentUser;

    @Autowired
    MenuService menuService;
    @Autowired
    UserService userService;

    private void initialize()
    {
        allMenus = new ArrayList<Menu>();
        allMenus = menuService.getAllMenus();
        cart = new ShoppingCart();
        currentUser = userService.getCurrentUser();
    }

    private Product getProductById(String id)
    {
        for(int i = 0; i<allMenus.size(); i++)
        {
            for(int j=0; j<allMenus.get(i).getProductList().size(); j++)
            {
                if (allMenus.get(i).getProductList().get(j).getId().equals(id))
                {
                    return allMenus.get(i).getProductList().get(j);
                }
            }
        }
        return new Product();
    }

    private void removeItemFromBucket(String id)
    {
        cart.removeProduct(id);
    }

    @RequestMapping("/menu")
    public String menu(Model model)
    {
        if(allMenus == null && cart == null)
        {
            initialize();
            //populateFeed();
        }
        model.addAttribute("allMenus", allMenus);
        model.addAttribute("shoppingCart", cart);
        model.addAttribute("totalItems", cart.getSelectedProducts().size());
        //model.addAttribute("selectedProduct", new Product());

        return "menu";
    }

    @RequestMapping(value="/addItem", method=RequestMethod.POST)
    public String addItemToCart(@RequestParam(value="productID") String id,
                                @RequestParam(value="checkedToppings", required=false) List<String> toppings,
                                @RequestParam(value="productQuantity") int quantity)
    {
        Product newItem = new Product();
        Product menuItem = getProductById(id);

        newItem.setId(UUID.randomUUID().toString());
        newItem.setName(menuItem.getName());
        newItem.setDescription(menuItem.getDescription());
        newItem.setPrice(menuItem.getPrice());
        newItem.setToppings(menuItem.getToppings());
        newItem.setSelectedToppings(toppings);
        newItem.setQuantity(quantity);

        cart.addProduct(newItem);

        return "redirect:/menu";
    }

    @RequestMapping(value="/editItem", method=RequestMethod.POST)
    public String editItem(@RequestParam(value="productID") String id,
                                @RequestParam(value="checkedToppings", required=false) List<String> toppings,
                                @RequestParam(value="productQuantity") int quantity)
    {
        Product currentItem = cart.getProductById(id);

        currentItem.setSelectedToppings(toppings);
        currentItem.setQuantity(quantity);

        return "redirect:/menu";
    }

    @RequestMapping({"/removeItem"})
    public String removeItem(@RequestParam(value = "code") String code)
    {
        removeItemFromBucket(code);

        return "redirect:/menu";
    }

    @RequestMapping("/experiments")
    public String experiments(Model model)
    {
        if(allMenus == null && cart == null)
        {
            initialize();
            //populateFeed();
        }
        model.addAttribute("allMenus", allMenus);
        model.addAttribute("totalItems", cart.getSelectedProducts().size());

        return "experiments";
    }

    @RequestMapping(value="/checkout")
    public String checkout(Model model, HttpServletRequest request)
    {
        model.addAttribute("shoppingCart", cart);
        model.addAttribute("user", new User());

        return "checkout";
    }

    @RequestMapping(value="/checkout-bootstrap")
    public String checkoutBootstrap(Model model)
    {
        model.addAttribute("shoppingCart", cart);

        return "checkout-bootstrap";
    }

    @RequestMapping(value="/processOrder", method=RequestMethod.POST)
    public String processOrder(@RequestParam(value="firstName") String firstName,
                               @RequestParam(value="lastName") String lastName, @RequestParam(value="email") String email,
                               @RequestParam(value="phone") String phoneNumber, @RequestParam(value="city") String city,
                               @RequestParam(value="state") String state, @RequestParam(value="zip") String zip,
                               @RequestParam(value="address") String address, @RequestParam(value="floor") int floor)
    {
        currentUser.setFirstName(firstName);
        currentUser.setLastName(lastName);
        currentUser.setEmail(email);
        currentUser.setPhoneNumber(phoneNumber);
        currentUser.setCity(city);
        currentUser.setState(state);
        currentUser.setZip(zip);
        currentUser.setAddress(address);
        currentUser.setFloor(floor);

        return "redirect:/checkout";
    }
}
