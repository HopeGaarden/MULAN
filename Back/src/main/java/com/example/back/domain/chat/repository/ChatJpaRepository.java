package com.example.back.domain.chat.repository;

import com.example.back.domain.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatJpaRepository extends JpaRepository<Chat, Long> {

    Chat save(Chat chat);
}
