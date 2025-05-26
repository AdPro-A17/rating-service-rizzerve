package id.ac.ui.cs.advprog.rating_service.dto;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemDTO {
    private UUID id;
    private String name;
}
