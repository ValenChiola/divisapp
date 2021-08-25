package com.divisapp.repositories;

import com.divisapp.entities.File;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, String> {

    @Query("SELECT f FROM File f WHERE f.user.id LIKE :userId AND f.folder.id IS NULL")
    public List<File> getAllByUser(@Param("userId") String userId);

    @Query("SELECT f FROM File f WHERE f.folder.id LIKE :folderId")
    public List<File> getAllByFolder(@Param("folderId") String folderId);
    
}
