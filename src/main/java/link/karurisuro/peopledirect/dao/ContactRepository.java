package link.karurisuro.peopledirect.dao;

import link.karurisuro.peopledirect.entities.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    @Query("select c from Contact c where c.user.id=:userId")
    public Page<Contact> findByUser(@Param("userId") Long userId, Pageable firstFiveEle);

    @Query("select c from Contact c where c.id=:id and c.user.id=:userId")
    public Optional<Contact> findByIdAndUser(@Param("id")Long id, @Param("userId")Long userId);

    @Query(value="select * from Contact c where c.name like %?1% or c.user_name like %?1%", nativeQuery=true)
    public List<Contact> searchContact(String searchStr);

    /* @Query("delete from Contact c where c.id=:id and c.user.id=:userId")
    public void deleteByIdAndUser(@Param("id")Long id, @Param("userId")Long userId); */
}
