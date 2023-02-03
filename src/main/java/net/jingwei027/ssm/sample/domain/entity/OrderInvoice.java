package net.jingwei027.ssm.sample.domain.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class OrderInvoice {

   @Id
   @GeneratedValue
   private Long id;
   private LocalDateTime createTime;
   private LocalDateTime updateTime;
   
   private String state;

}
