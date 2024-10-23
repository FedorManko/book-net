package com.manko.book.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Book extends BaseEntity {

  private String title;
  private String authorName;
  private String isbn;
  private String synopsis;
  private String bookCover;
  private boolean archived;
  private boolean shareable;

  @ManyToOne
  @JoinColumn(name = "owner_id")
  private User owner;

  @OneToMany(mappedBy = "book")
  private List<Feedback> feedbacks;

  @OneToMany(mappedBy = "book")
  private List<BookTransactionHistory> histories;

  @Transient
  public double getRate() {
    if (feedbacks == null || feedbacks.isEmpty()) {
      return 0.0;
    }
    var rate = feedbacks.stream()
        .mapToDouble(Feedback::getNote)
        .average()
        .orElse(0.0);

    // Return 4.0 if roundedRate is less than 4.5, otherwise return 4.5
    return Math.round(rate * 10.0) / 10.0;
  }

}
