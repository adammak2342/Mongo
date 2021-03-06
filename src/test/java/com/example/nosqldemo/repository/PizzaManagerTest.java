package com.example.nosqldemo.repository;

import com.example.nosqldemo.domain.Addon;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.nosqldemo.domain.Pizza;
import com.example.nosqldemo.service.AddonManager;
import com.example.nosqldemo.service.PizzaManager;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/beans.xml" })
public class PizzaManagerTest {
	
	@Autowired
	PizzaManager pizzaManager;
	@Autowired
	AddonManager addonManager;
        
        @After
        @Before
	public void clear() {
		pizzaManager.clear();
                addonManager.clear();
	}
        
        @Before
        public void add() {
                Pizza pizza = new Pizza();
		pizza.setName("Carbonara");
                pizza.setPrice(22);
                pizza.setDiameter(20);
                pizza.setSold(false);
		pizzaManager.persist(pizza);
		pizza=null;
        }
        
	@Test
	public void checkAdding(){
                this.clear();
		Pizza pizza = new Pizza();
		pizza.setName("Margerita");
                pizza.setPrice(22);
                pizza.setDiameter(20);
                pizza.setSold(false);
		pizzaManager.persist(pizza);
		pizza=null;
		pizza = new Pizza();
		pizza.setName("Carbonara");
                pizza.setPrice(222);
                pizza.setDiameter(20);
                pizza.setSold(false);
		pizzaManager.persist(pizza);
		pizza=null;
		pizza = new Pizza();
		pizza.setName("Hawajska");
                pizza.setPrice(2222);
                pizza.setDiameter(15);
                pizza.setSold(false);
		pizzaManager.persist(pizza);
		List<Pizza> cars = pizzaManager.getpizzas("Margerita");
		
		assertTrue(cars.size() >= 1);
	}
        
        @Test 
        public void checkingEdit() {
            Pizza pizza = pizzaManager.getpizzas("Carbonara").get(0);
            pizza.setName("Hawajska");
            pizza.setSold(true);
            pizzaManager.persist(pizza);
            pizza = null;
            
            pizza = pizzaManager.getpizzas("Hawajska").get(0);
            assertEquals(pizza.getName(), "Hawajska");
            assertEquals(pizza.getPrice(), 22.0, 0.01);
            assertEquals(pizza.getDiameter(), 20);
            assertEquals(pizza.getSold(), true);
        }
        
        @Test
        public void checkingRemove() {
                this.clear();
		Pizza pizza = new Pizza();
		pizza.setName("Margerita");
                pizza.setPrice(22);
                pizza.setDiameter(20);
                pizza.setSold(false);
		pizzaManager.persist(pizza);
		pizza=null;
		pizza = new Pizza();
		pizza.setName("Carbonara");
                pizza.setPrice(222);
                pizza.setDiameter(20);
                pizza.setSold(false);
		pizzaManager.persist(pizza);
                
                pizza=null;
                pizza = pizzaManager.getpizzas("Margerita").get(0);
                pizzaManager.remove(pizza);
                
                List<Pizza> list = (List<Pizza>) pizzaManager.getAll();
                pizza = list.get(0);
                
                assertEquals(pizza.getName(), "Carbonara");
                assertEquals(pizza.getPrice(), 222.0, 0.01);
                assertEquals(pizza.getDiameter(), 20);
                assertEquals(pizza.getSold(), false);
        }
        
        
        @Test
        public void checkingFind() {
                Pizza pizza = pizzaManager.getpizzas("Carbonara").get(0);

                assertEquals(pizza.getName(), "Carbonara");
                assertEquals(pizza.getPrice(), 22.0, 0.01);
                assertEquals(pizza.getDiameter(), 20);
                assertEquals(pizza.getSold(), false);
                
        }
        
        @Test
        public void addAddon() {
            Addon addon = new Addon();
            addon.setName("Bag");
            addon.setPrice(22);
            addonManager.add(addon);
            Pizza pizza = pizzaManager.getpizzas("Carbonara").get(0);
            pizza.addAddon(addon);
            pizzaManager.persist(pizza);
            
            pizza = null;
            pizza = pizzaManager.getpizzas("Carbonara").get(0);
            
            assertTrue(pizza.getAddon().size() >= 1);
        }
        
        @Test
        public void removeAddonFrompizza() {
            Pizza pizza2 = pizzaManager.getpizzas("Carbonara").get(0);
            Addon addonOther = new Addon();
            addonOther.setName("Battery");
            addonOther.setPrice(22);
            addonManager.add(addonOther);
            pizza2.addAddon(addonOther);
            pizzaManager.persist(pizza2);
            
                Pizza pizza = new Pizza();
		pizza.setName("Romana");
                pizza.setPrice(22);
                pizza.setDiameter(20);
                pizza.setSold(false);
		pizzaManager.persist(pizza);
		pizza=null;
            
            Addon addon = new Addon();
            addon.setName("Baguuniqe");
            addon.setPrice(22);
            addonManager.add(addon);
            Addon addon2 = new Addon();
            addon2.setName("Filteruuuuniqe");
            addon2.setPrice(102);
            addonManager.add(addon2);
            pizza = pizzaManager.getpizzas("Romana").get(0);
            pizza.addAddon(addon);
            pizza.addAddon(addon2);
            pizzaManager.persist(pizza);
            
            pizza = null;
            pizza = pizzaManager.getpizzas("Romana").get(0);
            List<Addon> list = (List<Addon>) addonManager.getAll();
            
            assertEquals(list.size(),3);
            assertEquals(pizza.getAddon().size(),2);
            
            for(Addon item : pizza.getAddon()) {
                addonManager.remove(item);
            }
            pizza.setAddom(null);
            pizzaManager.persist(pizza);
            
            
            list = (List<Addon>) addonManager.getAll();
            Addon last = list.get(0);
            assertEquals(last.getName(), "Battery");
            assertEquals(last.getPrice(), 22, 0.01);
            
            pizza = pizzaManager.getpizzas("Romana").get(0);
            assertTrue(pizza.getAddon().size() == 0);
        }
        
        @Test
        public void regexTest() {

                Pizza pizza = new Pizza();
		pizza.setName("Capricciosa");
                pizza.setPrice(22);
                pizza.setDiameter(20);
                pizza.setSold(false);
		pizzaManager.persist(pizza);
		pizza=null;
                pizza = new Pizza();
		pizza.setName("Najlepsza");
                pizza.setPrice(22);
                pizza.setDiameter(20);
                pizza.setSold(false);
		pizzaManager.persist(pizza);
		pizza=null;
                
            List<Pizza> list = pizzaManager.findByNameRegex("[A-Za-z]{2,}");
            List<Pizza> listAll = (List<Pizza>) pizzaManager.getAll();
            System.out.println(list.size());
            assertTrue(list.size()==1);
            assertTrue(listAll.size()==3);
        }

}
