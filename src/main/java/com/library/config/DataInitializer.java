package com.library.config;

import com.library.entity.Book;
import com.library.entity.BorrowRecord;
import com.library.entity.Fine;
import com.library.entity.Role;
import com.library.entity.User;
import com.library.repository.BookRepository;
import com.library.repository.BorrowRecordRepository;
import com.library.repository.FineRepository;
import com.library.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BorrowRecordRepository borrowRecordRepository;
    private final FineRepository fineRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           BookRepository bookRepository,
                           BorrowRecordRepository borrowRecordRepository,
                           FineRepository fineRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.borrowRecordRepository = borrowRecordRepository;
        this.fineRepository = fineRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            seedUsers();
        }
        if (bookRepository.count() == 0) {
            seedBooks();
        }
        if (borrowRecordRepository.count() == 0) {
            seedBorrowRecordsAndFines();
        }
    }

    private void seedUsers() {
        User admin = new User("admin", "admin@library.com", passwordEncoder.encode("admin123"),
                "Dr. Robert Vance", "+1 (555) 019-2834", "742 Evergreen Terrace, Suite 100", Role.ROLE_ADMIN);

        User librarian = new User("librarian", "librarian@library.com", passwordEncoder.encode("librarian123"),
                "Sarah Jenkins", "+1 (555) 018-9271", "124 Conch Street, Floor 2", Role.ROLE_LIBRARIAN);

        User member1 = new User("member", "john.doe@email.com", passwordEncoder.encode("member123"),
                "John Doe", "+1 (555) 014-5582", "42 Wallaby Way, Sydney", Role.ROLE_MEMBER);

        User member2 = new User("emily_w", "emily.watson@email.com", passwordEncoder.encode("member123"),
                "Emily Watson", "+1 (555) 012-7731", "221B Baker Street", Role.ROLE_MEMBER);

        User member3 = new User("michael_c", "michael.chen@email.com", passwordEncoder.encode("member123"),
                "Michael Chen", "+1 (555) 016-9924", "350 5th Avenue, Apt 4B", Role.ROLE_MEMBER);

        userRepository.saveAll(Arrays.asList(admin, librarian, member1, member2, member3));
    }

    private void seedBooks() {
        List<Book> books = Arrays.asList(
                new Book("978-0132350884", "Clean Code: A Handbook of Agile Software Craftsmanship",
                        "Robert C. Martin", "Prentice Hall", 2008, "Computer Science", 6, 5,
                        "Section CS-01", "https://images.unsplash.com/photo-1532012164546-f432f2e3edd4?w=600&auto=format&fit=crop&q=80",
                        "Even bad code can function. But if code isn't clean, it can bring a development organization to its knees."),

                new Book("978-1449373320", "Designing Data-Intensive Applications",
                        "Martin Kleppmann", "O'Reilly Media", 2017, "Computer Science", 4, 3,
                        "Section CS-02", "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=600&auto=format&fit=crop&q=80",
                        "The definitive guide to the system architectures behind modern distributed databases and data processing pipelines."),

                new Book("978-0201616224", "The Pragmatic Programmer: Your Journey to Mastery",
                        "David Thomas & Andrew Hunt", "Addison-Wesley", 2019, "Computer Science", 5, 4,
                        "Section CS-03", "https://images.unsplash.com/photo-1512820790803-83ca734da794?w=600&auto=format&fit=crop&q=80",
                        "Straightforward advice and best practices for creating working, maintainable software systems."),

                new Book("978-0262033848", "Introduction to Algorithms (4th Edition)",
                        "Thomas H. Cormen & Charles E. Leiserson", "MIT Press", 2022, "Computer Science", 3, 2,
                        "Section CS-04", "https://images.unsplash.com/photo-1497633762265-9d179a990aa6?w=600&auto=format&fit=crop&q=80",
                        "Comprehensive textbook covering the breadth of modern algorithms and theoretical foundations."),

                new Book("978-0061120084", "To Kill a Mockingbird",
                        "Harper Lee", "J. B. Lippincott & Co.", 1960, "Fiction", 5, 5,
                        "Section FIC-01", "https://images.unsplash.com/photo-1543002588-bfa74002ed7e?w=600&auto=format&fit=crop&q=80",
                        "The unforgettable novel of a childhood in a sleepy Southern town and the crisis of conscience that rocked it."),

                new Book("978-0451524935", "1984",
                        "George Orwell", "Secker & Warburg", 1949, "Fiction", 6, 4,
                        "Section FIC-02", "https://images.unsplash.com/photo-1516979187457-637abb4f9353?w=600&auto=format&fit=crop&q=80",
                        "A startling and haunting novel that creates an imaginary world that is completely convincing from start to finish."),

                new Book("978-0743273565", "The Great Gatsby",
                        "F. Scott Fitzgerald", "Charles Scribner's Sons", 1925, "Fiction", 4, 3,
                        "Section FIC-03", "https://images.unsplash.com/photo-1495446815901-a7297e633e8d?w=600&auto=format&fit=crop&q=80",
                        "The exemplary novel of the Jazz Age, capturing the American spirit and disillusionment."),

                new Book("978-0441013593", "Dune (Chronicles Book 1)",
                        "Frank Herbert", "Chilton Books", 1965, "Science Fiction", 4, 3,
                        "Section SF-01", "https://images.unsplash.com/photo-1506880018603-83d5b814b5a6?w=600&auto=format&fit=crop&q=80",
                        "Set on the desert planet Arrakis, Dune is the story of the boy Paul Atreides, who would become the mysterious man known as Muad'Dib."),

                new Book("978-0593135204", "Project Hail Mary",
                        "Andy Weir", "Ballantine Books", 2021, "Science Fiction", 5, 4,
                        "Section SF-02", "https://images.unsplash.com/photo-1457369804613-52c61a468e7d?w=600&auto=format&fit=crop&q=80",
                        "A lone astronaut must save the earth from disaster in this incredible new science-based adventure."),

                new Book("978-0062316097", "Sapiens: A Brief History of Humankind",
                        "Yuval Noah Harari", "Harper", 2015, "History", 5, 5,
                        "Section HIS-01", "https://images.unsplash.com/photo-1463320726281-696a485928c7?w=600&auto=format&fit=crop&q=80",
                        "From a renowned historian comes a groundbreaking narrative of humanity’s creation and evolution."),

                new Book("978-0374533557", "Thinking, Fast and Slow",
                        "Daniel Kahneman", "Farrar, Straus and Giroux", 2011, "Psychology", 4, 3,
                        "Section PSY-01", "https://images.unsplash.com/photo-1509021436665-8f07dbf76c87?w=600&auto=format&fit=crop&q=80",
                        "An exploration of the two systems that drive the way we think: fast, intuitive, and emotional vs slow, deliberative, and logical."),

                new Book("978-0735211292", "Atomic Habits: An Easy & Proven Way to Build Good Habits",
                        "James Clear", "Avery", 2018, "Self-Help", 8, 6,
                        "Section SH-01", "https://images.unsplash.com/photo-1589829085413-56de8ae18c73?w=600&auto=format&fit=crop&q=80",
                        "No matter your goals, Atomic Habits offers a proven framework for improving every day.")
        );

        bookRepository.saveAll(books);
    }

    private void seedBorrowRecordsAndFines() {
        User member1 = userRepository.findByUsername("member").orElse(null);
        User member2 = userRepository.findByUsername("emily_w").orElse(null);
        User member3 = userRepository.findByUsername("michael_c").orElse(null);

        Book cleanCode = bookRepository.findByIsbn("978-0132350884").orElse(null);
        Book dataIntensive = bookRepository.findByIsbn("978-1449373320").orElse(null);
        Book dune = bookRepository.findByIsbn("978-0441013593").orElse(null);
        Book atomicHabits = bookRepository.findByIsbn("978-0735211292").orElse(null);

        if (member1 != null && cleanCode != null) {
            BorrowRecord br1 = new BorrowRecord();
            br1.setMember(member1);
            br1.setBook(cleanCode);
            br1.setIssueDate(LocalDate.now().minusDays(5));
            br1.setDueDate(LocalDate.now().plusDays(9));
            br1.setStatus("ISSUED");
            br1.setIssuedBy("librarian");
            br1.setRemarks("Academic study loan");
            borrowRecordRepository.save(br1);
        }

        if (member2 != null && dune != null) {
            BorrowRecord br2 = new BorrowRecord();
            br2.setMember(member2);
            br2.setBook(dune);
            br2.setIssueDate(LocalDate.now().minusDays(8));
            br2.setDueDate(LocalDate.now().plusDays(6));
            br2.setStatus("ISSUED");
            br2.setIssuedBy("librarian");
            br2.setRemarks("Fiction reading club");
            borrowRecordRepository.save(br2);
        }

        if (member3 != null && dataIntensive != null) {
            // Overdue loan (issued 25 days ago, due 11 days ago)
            BorrowRecord br3 = new BorrowRecord();
            br3.setMember(member3);
            br3.setBook(dataIntensive);
            br3.setIssueDate(LocalDate.now().minusDays(25));
            br3.setDueDate(LocalDate.now().minusDays(11));
            br3.setStatus("ISSUED");
            br3.setIssuedBy("admin");
            br3.setRemarks("Research project loan - currently overdue");
            BorrowRecord savedOverdue = borrowRecordRepository.save(br3);

            // Seed an active pending fine for this overdue loan (₹10/day)
            Fine fine1 = new Fine(savedOverdue, member3, 110.00, 10.00, 11);
            fine1.setStatus("PENDING");
            fineRepository.save(fine1);
        }

        if (member1 != null && atomicHabits != null) {
            // Returned loan with paid fine
            BorrowRecord br4 = new BorrowRecord();
            br4.setMember(member1);
            br4.setBook(atomicHabits);
            br4.setIssueDate(LocalDate.now().minusDays(30));
            br4.setDueDate(LocalDate.now().minusDays(16));
            br4.setReturnDate(LocalDate.now().minusDays(12));
            br4.setStatus("RETURNED");
            br4.setIssuedBy("librarian");
            br4.setRemarks("Returned with 4 days delay");
            BorrowRecord savedReturned = borrowRecordRepository.save(br4);

            Fine fine2 = new Fine(savedReturned, member1, 40.00, 10.00, 4);
            fine2.setStatus("PAID");
            fine2.setPaidDate(LocalDate.now().minusDays(12));
            fine2.setPaymentMethod("CARD");
            fine2.setTransactionId("TXN-PAID-90821");
            fineRepository.save(fine2);
        }
    }
}
