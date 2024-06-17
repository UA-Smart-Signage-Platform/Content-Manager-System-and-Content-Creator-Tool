package deti.uas.uasmartsignage.Repositories;

import deti.uas.uasmartsignage.Models.Widget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WidgetRepository extends JpaRepository<Widget, Long> {
    Widget findByName(String name);

    // List<Widget> findByContent(Content content);


}
