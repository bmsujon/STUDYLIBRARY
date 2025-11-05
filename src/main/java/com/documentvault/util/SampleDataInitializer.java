package com.documentvault.util;

import com.documentvault.model.*;
import com.documentvault.service.LibraryService;
import com.documentvault.service.LibraryServiceImpl;

/**
 * Utility class to initialize the library with sample data.
 * Useful for testing and demonstration purposes.
 */
public class SampleDataInitializer {

    private final LibraryService libraryService;

    public SampleDataInitializer() {
        this.libraryService = LibraryServiceImpl.getInstance();
    }

    /**
     * Creates sample data if the library is empty.
     */
    public void initializeSampleData() {
        if (libraryService.getItemCount() > 0) {
            System.out.println("Library already contains data. Skipping sample data initialization.");
            return;
        }

        System.out.println("Initializing sample data...");

        // Create categories
        Category programmingCategory = createCategory("Programming", "#3498db", "Programming and coding topics");
        Category mathCategory = createCategory("Mathematics", "#e74c3c", "Math and algorithms");
        Category researchCategory = createCategory("Research", "#27ae60", "Research papers and materials");

        // Create sample notes
        createSampleNotes(programmingCategory, mathCategory);

        // Create sample PDFs
        createSamplePdfs(researchCategory, programmingCategory);

        // Create sample media links
        createSampleMediaLinks(programmingCategory, mathCategory);

        // Create sample snippets
        createSampleSnippets(programmingCategory);

        System.out.println("Sample data initialized successfully!");
        System.out.println("Total items: " + libraryService.getItemCount());
    }

    private Category createCategory(String name, String color, String description) {
        Category category = new Category(name, color);
        category.setDescription(description);
        libraryService.addCategory(category);
        return category;
    }

    private void createSampleNotes(Category... categories) {
        // Java Basics Note
        Note javaNote = new Note();
        javaNote.setTitle("Java Fundamentals");
        javaNote.setDescription("Core concepts of Java programming");
        javaNote.setCategory(categories[0]);
        javaNote.addTag("java");
        javaNote.addTag("programming");
        javaNote.addTag("basics");
        javaNote.setContent("# Java Fundamentals\n\n" +
                "## Object-Oriented Programming\n" +
                "- Encapsulation\n" +
                "- Inheritance\n" +
                "- Polymorphism\n" +
                "- Abstraction\n\n" +
                "## Key Features\n" +
                "- Platform independent\n" +
                "- Strongly typed\n" +
                "- Automatic memory management\n" +
                "- Rich standard library");
        javaNote.setMarkdown(true);
        libraryService.addItem(javaNote);

        // Algorithm Note
        Note algoNote = new Note();
        algoNote.setTitle("Big O Notation");
        algoNote.setDescription("Understanding algorithm complexity");
        algoNote.setCategory(categories[1]);
        algoNote.addTag("algorithms");
        algoNote.addTag("complexity");
        algoNote.addTag("theory");
        algoNote.setContent("Big O Notation - Time Complexity\n\n" +
                "O(1) - Constant time\n" +
                "O(log n) - Logarithmic time\n" +
                "O(n) - Linear time\n" +
                "O(n log n) - Linearithmic time\n" +
                "O(n²) - Quadratic time\n" +
                "O(2^n) - Exponential time\n\n" +
                "Always consider:\n" +
                "- Best case\n" +
                "- Average case\n" +
                "- Worst case");
        libraryService.addItem(algoNote);

        // Data Structures Note
        Note dsNote = new Note();
        dsNote.setTitle("Common Data Structures");
        dsNote.setDescription("Overview of fundamental data structures");
        dsNote.setCategory(categories[0]);
        dsNote.addTag("data-structures");
        dsNote.addTag("programming");
        dsNote.setContent("# Data Structures Overview\n\n" +
                "## Linear Structures\n" +
                "- Arrays: Fixed size, O(1) access\n" +
                "- Linked Lists: Dynamic size, O(n) access\n" +
                "- Stacks: LIFO, O(1) push/pop\n" +
                "- Queues: FIFO, O(1) enqueue/dequeue\n\n" +
                "## Non-Linear Structures\n" +
                "- Trees: Hierarchical data\n" +
                "- Graphs: Networks and relationships\n" +
                "- Hash Tables: O(1) average access");
        dsNote.setMarkdown(true);
        libraryService.addItem(dsNote);
    }

    private void createSamplePdfs(Category... categories) {
        // Sample PDF 1
        PdfDocument pdf1 = new PdfDocument();
        pdf1.setTitle("Introduction to Algorithms");
        pdf1.setDescription("Comprehensive textbook on algorithms and data structures");
        pdf1.setCategory(categories[1]);
        pdf1.addTag("algorithms");
        pdf1.addTag("textbook");
        pdf1.addTag("reference");
        pdf1.setAuthor("Thomas H. Cormen");
        pdf1.setFilePath("/path/to/intro-to-algorithms.pdf"); // Placeholder
        pdf1.setPageCount(1312);
        pdf1.setFileSize(10485760); // 10 MB
        libraryService.addItem(pdf1);

        // Sample PDF 2
        PdfDocument pdf2 = new PdfDocument();
        pdf2.setTitle("Effective Java");
        pdf2.setDescription("Best practices for Java programming");
        pdf2.setCategory(categories[0]);
        pdf2.addTag("java");
        pdf2.addTag("best-practices");
        pdf2.addTag("reference");
        pdf2.setAuthor("Joshua Bloch");
        pdf2.setFilePath("/path/to/effective-java.pdf"); // Placeholder
        pdf2.setPageCount(416);
        pdf2.setFileSize(5242880); // 5 MB
        libraryService.addItem(pdf2);
    }

    private void createSampleMediaLinks(Category... categories) {
        // YouTube Tutorial
        MediaLink video1 = new MediaLink();
        video1.setTitle("Java Full Course for Beginners");
        video1.setDescription("Complete Java programming course");
        video1.setCategory(categories[0]);
        video1.addTag("java");
        video1.addTag("tutorial");
        video1.addTag("beginner");
        video1.setUrl("https://www.youtube.com/watch?v=example1");
        video1.setMediaType(MediaLink.MediaType.VIDEO);
        video1.setSource("YouTube");
        video1.setDurationMinutes(720); // 12 hours
        libraryService.addItem(video1);

        // Coursera Course
        MediaLink video2 = new MediaLink();
        video2.setTitle("Algorithms Part I");
        video2.setDescription("Princeton University algorithms course");
        video2.setCategory(categories[1]);
        video2.addTag("algorithms");
        video2.addTag("course");
        video2.addTag("princeton");
        video2.setUrl("https://www.coursera.org/learn/algorithms-part1");
        video2.setMediaType(MediaLink.MediaType.LECTURE);
        video2.setSource("Coursera");
        video2.setDurationMinutes(300);
        libraryService.addItem(video2);

        // Programming Podcast
        MediaLink podcast = new MediaLink();
        podcast.setTitle("Java Pub House");
        podcast.setDescription("Java programming podcast");
        podcast.setCategory(categories[0]);
        podcast.addTag("java");
        podcast.addTag("podcast");
        podcast.setUrl("https://www.javapubhouse.com/");
        podcast.setMediaType(MediaLink.MediaType.PODCAST);
        podcast.setSource("Java Pub House");
        podcast.setDurationMinutes(45);
        libraryService.addItem(podcast);
    }

    private void createSampleSnippets(Category category) {
        // Binary Search Snippet
        TextSnippet snippet1 = new TextSnippet();
        snippet1.setTitle("Binary Search Algorithm");
        snippet1.setDescription("Efficient search in sorted arrays");
        snippet1.setCategory(category);
        snippet1.addTag("algorithm");
        snippet1.addTag("search");
        snippet1.addTag("java");
        snippet1.setLanguage("java");
        snippet1.setContent("public static int binarySearch(int[] arr, int target) {\n" +
                "    int left = 0;\n" +
                "    int right = arr.length - 1;\n" +
                "    \n" +
                "    while (left <= right) {\n" +
                "        int mid = left + (right - left) / 2;\n" +
                "        \n" +
                "        if (arr[mid] == target) {\n" +
                "            return mid;\n" +
                "        } else if (arr[mid] < target) {\n" +
                "            left = mid + 1;\n" +
                "        } else {\n" +
                "            right = mid - 1;\n" +
                "        }\n" +
                "    }\n" +
                "    \n" +
                "    return -1; // Not found\n" +
                "}");
        libraryService.addItem(snippet1);

        // SQL Query Snippet
        TextSnippet snippet2 = new TextSnippet();
        snippet2.setTitle("SQL JOIN Examples");
        snippet2.setDescription("Common SQL JOIN operations");
        snippet2.setCategory(category);
        snippet2.addTag("sql");
        snippet2.addTag("database");
        snippet2.addTag("reference");
        snippet2.setLanguage("sql");
        snippet2.setContent("-- INNER JOIN\n" +
                "SELECT orders.id, customers.name\n" +
                "FROM orders\n" +
                "INNER JOIN customers ON orders.customer_id = customers.id;\n\n" +
                "-- LEFT JOIN\n" +
                "SELECT customers.name, orders.id\n" +
                "FROM customers\n" +
                "LEFT JOIN orders ON customers.id = orders.customer_id;\n\n" +
                "-- RIGHT JOIN\n" +
                "SELECT customers.name, orders.id\n" +
                "FROM customers\n" +
                "RIGHT JOIN orders ON customers.id = orders.customer_id;");
        libraryService.addItem(snippet2);

        // Git Commands Snippet
        TextSnippet snippet3 = new TextSnippet();
        snippet3.setTitle("Essential Git Commands");
        snippet3.setDescription("Frequently used Git commands");
        snippet3.setCategory(category);
        snippet3.addTag("git");
        snippet3.addTag("version-control");
        snippet3.addTag("cheatsheet");
        snippet3.setLanguage("bash");
        snippet3.setContent("# Basic Commands\n" +
                "git init                  # Initialize repository\n" +
                "git clone <url>          # Clone repository\n" +
                "git status               # Check status\n" +
                "git add <file>           # Stage file\n" +
                "git commit -m \"message\" # Commit changes\n" +
                "git push                 # Push to remote\n" +
                "git pull                 # Pull from remote\n\n" +
                "# Branching\n" +
                "git branch               # List branches\n" +
                "git branch <name>        # Create branch\n" +
                "git checkout <branch>    # Switch branch\n" +
                "git merge <branch>       # Merge branch");
        libraryService.addItem(snippet3);
    }

    /**
     * Main method to run the initializer standalone.
     */
    public static void main(String[] args) {
        SampleDataInitializer initializer = new SampleDataInitializer();
        initializer.initializeSampleData();
    }
}
