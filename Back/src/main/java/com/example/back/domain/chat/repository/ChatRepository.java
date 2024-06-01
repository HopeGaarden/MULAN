package com.example.back.domain.chat.repository;

import com.example.back.domain.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long>, ChatRepositoryCustom {

    Chat save(Chat chat);

}
