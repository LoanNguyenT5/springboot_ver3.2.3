package Project.Ministore.repository;

import java.util.List;

import Project.Ministore.entity.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ProductOrderRepository extends JpaRepository<ProductOrder, Integer> {

    List<ProductOrder> findByUserId(Integer userId);

    ProductOrder findByOrderId(String orderId);

}