package com.divisapp.repositories;

import com.divisapp.entities.Folder;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FolderRepository extends JpaRepository<Folder, String> {

    @Query("SELECT f FROM Folder f WHERE f.user.id LIKE :userId AND f.parent.id IS NULL ORDER BY f.name ASC")
    public List<Folder> getAllByUser(@Param("userId") String userId);

    @Query("SELECT f FROM Folder f WHERE f.parent.id LIKE :folderId ORDER BY f.name ASC")
    public List<Folder> getAllFolderByParent(@Param("folderId") String folderId);
   
    @Query(value="SELECT * FROM Folder f WHERE f.parent.id LIKE :folderId ORDER BY f.name ASC LIMIT 1", nativeQuery = true)
    public Folder getOneFolderByParent(@Param("folderId") String folderId);

}
