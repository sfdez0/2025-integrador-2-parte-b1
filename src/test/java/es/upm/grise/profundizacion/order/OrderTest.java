package es.upm.grise.profundizacion.order;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.upm.grise.exceptions.IncorrectItemException;

public class OrderTest {

    private Order order;
    private Item itemMock;
    private Product productMock;

    @BeforeEach
    void setUp() {
        order = new Order();
        itemMock = mock(Item.class);
        productMock = mock(Product.class);
        when(itemMock.getProduct()).thenReturn(productMock);
    }

    @Test
    void testInitialOrderIsEmpty() {
        assertTrue(order.getItems().isEmpty(), "Order should start empty");
    }

    @Test
    void testAddValidItem() throws IncorrectItemException {
        when(itemMock.getPrice()).thenReturn(10.0);
        when(itemMock.getQuantity()).thenReturn(2);

        order.addItem(itemMock);

        assertEquals(1, order.getItems().size());
        assertTrue(order.getItems().contains(itemMock));
    }

    @Test
    void testAddItemWithNegativePriceThrowsException() {
        when(itemMock.getPrice()).thenReturn(-5.0);
        when(itemMock.getQuantity()).thenReturn(2);

        assertThrows(IncorrectItemException.class, () -> order.addItem(itemMock));
    }

    @Test
    void testAddItemWithZeroQuantityThrowsException() {
        when(itemMock.getPrice()).thenReturn(10.0);
        when(itemMock.getQuantity()).thenReturn(0);

        assertThrows(IncorrectItemException.class, () -> order.addItem(itemMock));
    }

    @Test
    void testAddItemSameProductSamePriceAccumulatesQuantity() throws IncorrectItemException {
        when(itemMock.getPrice()).thenReturn(10.0);
        when(itemMock.getQuantity()).thenReturn(2);

        order.addItem(itemMock);

        Item secondItemMock = mock(Item.class);
        when(secondItemMock.getProduct()).thenReturn(productMock);
        when(secondItemMock.getPrice()).thenReturn(10.0);
        when(secondItemMock.getQuantity()).thenReturn(3);

        order.addItem(secondItemMock);

        Collection<Item> items = order.getItems();
        assertEquals(1, items.size());
        verify(itemMock).setQuantity(5); // 2 + 3
    }

    @Test
    void testAddItemSameProductDifferentPriceAddsNewItem() throws IncorrectItemException {
        when(itemMock.getPrice()).thenReturn(10.0);
        when(itemMock.getQuantity()).thenReturn(2);

        order.addItem(itemMock);

        Item secondItemMock = mock(Item.class);
        when(secondItemMock.getProduct()).thenReturn(productMock);
        when(secondItemMock.getPrice()).thenReturn(15.0);
        when(secondItemMock.getQuantity()).thenReturn(1);

        order.addItem(secondItemMock);

        assertEquals(2, order.getItems().size());
    }

    @Test
    void testAddItemDifferentProductAddsNewItem() throws IncorrectItemException {
        when(itemMock.getPrice()).thenReturn(10.0);
        when(itemMock.getQuantity()).thenReturn(2);

        order.addItem(itemMock);

        Item anotherItemMock = mock(Item.class);
        Product anotherProductMock = mock(Product.class);
        when(anotherItemMock.getProduct()).thenReturn(anotherProductMock);
        when(anotherItemMock.getPrice()).thenReturn(20.0);
        when(anotherItemMock.getQuantity()).thenReturn(1);

        order.addItem(anotherItemMock);

        assertEquals(2, order.getItems().size());
    }
}
