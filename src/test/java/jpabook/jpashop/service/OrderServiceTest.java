package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Item.Book;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.order.Order;
import jpabook.jpashop.domain.order.OrderStatus;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void orderTest() {

        //given
        Member member = createMember("memberA");

        Book book = createBook("시골 JPA", 10000,10);

        //when
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findById(orderId);

        assertThat(getOrder.getStatus()).isEqualTo(OrderStatus.ORDER);
        assertThat(getOrder.getOrderItems().size()).isEqualTo(1);
        assertThat(book.getStockQuantity()).isEqualTo(8);
    }

    @Test
    @DisplayName("재고 수량보다 주문 수량이 더 많으면 예외 발생")
    public void overStockQuantity() throws Exception{
        //given
        Member member = createMember("memberA");
        Book book = createBook("JPA", 10000, 10);

        //when
        int orderCount = 11;

        //then
        assertThrows(NotEnoughStockException.class, () -> {
            orderService.order(member.getId(), book.getId(), orderCount);
        });
    }

    @Test
    public void orderCancel() {
        //given
        Member member = createMember("memberB");
        Book book = createBook("JPA2", 10000, 20);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //when
        orderService.orderCancel(orderId);

        //then
        Order getOrder = orderRepository.findById(orderId);

        assertThat(getOrder.getStatus()).isEqualTo(OrderStatus.CANCEL);
        assertThat(book.getStockQuantity()).isEqualTo(20);
    }

    @Test
    public void totalPriceTest() {
        //given
        Member member = createMember("memberC");
        Book item1 = createBook("JPA3", 15000, 20);
        Book item2 = createBook("JPA4", 10000, 20);

        //when
        int orderCount = 5;
        Long orderId1 = orderService.order(member.getId(), item1.getId(), orderCount);
        Long orderId2 = orderService.order(member.getId(), item2.getId(), orderCount);

        //then
        int totalPrice1 = orderService.totalPriceView(orderId1);
        assertThat(totalPrice1).isEqualTo(75000);

        int totalPrice2 = orderService.totalPriceView(orderId2);
        assertThat(totalPrice2).isEqualTo(50000);
    }

    private Member createMember(String name) {
        Member member = new Member();
        member.setName(name);
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }
}