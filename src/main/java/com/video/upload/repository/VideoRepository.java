package com.video.upload.repository;

import com.video.upload.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findByUserId(Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Video v WHERE v.id IN :ids ")
    void deleteByIds(@Param("ids") List<Long> ids);

    @Transactional
    @Modifying
    @Query("DELETE FROM Video v WHERE v.userId = :userId ")
    void deleteAllByUserId(@Param("userId") Long userId);
}
