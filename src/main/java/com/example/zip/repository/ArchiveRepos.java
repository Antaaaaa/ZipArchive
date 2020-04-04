package com.example.zip.repository;

import com.example.zip.entity.Archive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ArchiveRepos extends JpaRepository<Archive, Long> {
}
