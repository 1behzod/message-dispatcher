package uz.behzod.message_dispatcher.dto.payload;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserPayload {

    String old;

    String updated;
}
