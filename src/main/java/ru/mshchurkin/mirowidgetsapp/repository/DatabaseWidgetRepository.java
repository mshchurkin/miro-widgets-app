package ru.mshchurkin.mirowidgetsapp.repository;

import java.util.Optional;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.mshchurkin.mirowidgetsapp.domain.Widget;

/**
 * Database widget storage
 * 
 * @author Mikail Shchurkin
 * @created 17.02.2022
 */
@Profile("db")
@Repository
public interface DatabaseWidgetRepository extends JpaRepository<Widget, Long> {

    Page<Widget> findAllByOrderByIndexZAsc(Pageable pageable);

    Optional<Widget> findByIndexZ(Integer zIndex);

    @Query("SELECT MAX(indexZ) FROM Widget")
    Optional<Integer> getMaximumIndexZ();
}
