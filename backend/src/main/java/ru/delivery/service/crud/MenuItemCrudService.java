package ru.delivery.service.crud;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.delivery.entity.MenuItem;
import ru.delivery.repository.MenuItemRepository;


@RequiredArgsConstructor
@Service
public class MenuItemCrudService {

  private final MenuItemRepository menuItemRepository;

  public MenuItem getById(Long id) {
    return menuItemRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(
            "Элемента меню с id = %s не существует".formatted(id)));
  }

}
