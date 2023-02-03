package net.jingwei027.ssm.sample.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.jingwei027.ssm.sample.domain.entity.OrderInvoice;

@Repository
public interface OrderRepository extends JpaRepository<OrderInvoice, Long> {
    
    @Modifying
    @Query("UPDATE OrderInvoice SET state = :state WHERE id = :id")
    public int updateStateById(String state, Long id);

}
