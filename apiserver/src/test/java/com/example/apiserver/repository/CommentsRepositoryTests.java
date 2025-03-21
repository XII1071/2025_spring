package com.example.apiserver.repository;

import com.example.apiserver.entity.Comments;
import com.example.apiserver.entity.Journal;
import com.example.apiserver.entity.Members;
import com.example.apiserver.entity.Photos;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

@SpringBootTest
class CommentsRepositoryTests {
  @Autowired
  CommentsRepository commentsRepository;

  @Autowired
  MembersRepository membersRepository;

  @Autowired
  JournalRepository journalRepository;

  @Test
  public void insertJournalComments() {
    IntStream.rangeClosed(1, 200).forEach(i -> {
      Long mid = (long) (Math.random() * 100) + 1;
      Long jno = (long) (Math.random() * 100) + 1;

      Comments comments = Comments.builder()
          .journal(Journal.builder().jno(jno).build())
          .members(Members.builder().mid(mid).build())
          .likes(Long.valueOf((int)(Math.random() * 2)))
          .text("이 글에 대하여..." + i)
          .build();
      commentsRepository.save(comments);
    });
  }

  @Test
  public void testGetJournalComments() {
    List<Comments> result = commentsRepository.findByJournal(Journal.builder().jno(1L).build());
    result.forEach(c -> {
      System.out.println(c.getCno());
      System.out.println(c.getText());
      System.out.println(c.getLikes());
      System.out.println(c.getMembers().getEmail());
      System.out.println("=".repeat(30));
    });
  }

  @Transactional
  @Commit
  @Test
  public void testDeleteMembers() {
    List<Object[]> result = journalRepository.getJournalWithAll(10l);

    Journal journal = (Journal) result.get(0)[0];
    List<Photos> photosList = new ArrayList<>();
    result.forEach(new Consumer<Object[]>() {
      @Override
      public void accept(Object[] objects) {
        photosList.add((Photos) objects[1]);
      }
    });
    Members members = (Members) result.get(0)[2];

    Long likes = (Long) result.get(0)[3];
    Long commentsCnt = (Long) result.get(0)[4];
    System.out.println(journal);
    photosList.stream().forEach(photos -> {
      System.out.println(photos);
    });
    System.out.println(members);
    System.out.println(likes);
    System.out.println(commentsCnt);

//    commentsRepository.deleteByMembers(Members.builder().mid(2L).build());
//    journalRepository.deleteById();
//    membersRepository.deleteById(2L);
  }
}