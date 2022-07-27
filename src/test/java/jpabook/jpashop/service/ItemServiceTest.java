package jpabook.jpashop.service;

import jpabook.jpashop.domain.Item.Album;
import jpabook.jpashop.domain.Item.Book;
import jpabook.jpashop.domain.Item.Item;
import jpabook.jpashop.domain.Item.Movie;
import jpabook.jpashop.repository.ItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired private ItemService itemService;
    @Autowired private ItemRepository itemRepository;

    @Test
    public void itemSaveTest() {
        //given
        Item book = new Book();

        //when
        Long savedId = itemService.saveItem(book);

        //then
        assertThat(book).isEqualTo(itemRepository.findById(savedId));
    }

    @Test
    public void itemViewTest() {
        //given

        Item book = new Book();
        Item movie = new Movie();
        Item album = new Album();

        //when
        itemService.saveItem(book);
        itemService.saveItem(movie);
        itemService.saveItem(album);

        List<Item> items = itemService.viewAllItem();

        //then
        assertThat(items.size()).isEqualTo(3);
    }

}