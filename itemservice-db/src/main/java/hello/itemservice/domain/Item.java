package hello.itemservice.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
//@Table(name = "item") //객체명과 테이블명이 동일하면 생략가능.
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) // db에서 증가해 주는 값을 넣는다.
    private Long id;

    // 컬럼명과 필드명이 동일하면 생략 가능.
    // 객체 필드의 카멜 케이스를 테이블의 언더스코어로 자동 변환해준다.
    @Column(name = "item_name", length = 10)
    private String itemName;
    private Integer price;
    private Integer quantity;

    //jpa는 public 또는 protected의 기본 생성자가 필수
    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
