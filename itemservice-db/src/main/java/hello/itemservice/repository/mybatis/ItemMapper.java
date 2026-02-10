package hello.itemservice.repository.mybatis;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

//마이바티스 매핑 xml을 호출해주는 매퍼 인터페이스
@Mapper
public interface ItemMapper {

    void save(Item item);

    //파라미터가 넘어가는 경우 @Param 필수
    void update(@Param("id") Long id, @Param("updateParam")ItemUpdateDto updateParam);

    Optional<Item> findById(Long id);

    List<Item> findAll(ItemSearchCond itemSearch);

}
