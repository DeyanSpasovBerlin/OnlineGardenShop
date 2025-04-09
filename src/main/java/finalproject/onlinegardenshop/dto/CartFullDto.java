package finalproject.onlinegardenshop.dto;

import finalproject.onlinegardenshop.entity.CartItems;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartFullDto {

    private Integer id;

    private Integer usersId;

    private String firstName;

    private String lastName;

    private List<CartItemsDto> cartItems;
}
