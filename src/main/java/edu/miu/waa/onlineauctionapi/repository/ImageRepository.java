package edu.miu.waa.onlineauctionapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.miu.waa.onlineauctionapi.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
