package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, String name, int price,int stockQuantity){
        // name, price, stockQuantity대신에 UpdateItemDto를 사용하는게 좀 더 깔끔함
        Item findItem = itemRepository.findOne(itemId);
//        Transactional에 의해서 자동으로 commit이 된다. 그리고 findItem은 영속 상태이므로 persist를 안해줘도 된다.
//         flush()를 날려서 변경된애를 찾아낸다.
//        findItem.setPrice(param.getPrice());
//        findItem.setName(param.getName());
//        findItem.setStockQuantity(param.getStockQuantity());
//        return findItem;
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
//                merge가 아닌 변경감지가 가장 좋은 방법
//        트랜젝션이 있는 서비스 계층에서 영속 상태의 엔티티를 조회하고, 엔티티의 데이터를 직접 변경하도록 할것
    }

    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item findOne(Long itemid){
        return itemRepository.findOne(itemid);
    }

}
