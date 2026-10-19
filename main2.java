
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class main2 extends JFrame {

    private final HashMap<String, String> accounts = new HashMap<>();

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    // MAIN BOOK CLASS
    private static class Main {
        private final String id;
        private final String title;
        private final String author;
        private final int totalCopies;
        private int availableCopies;

        public Main(String id, String title, String author, int totalCopies) {
            if (id == null || id.trim().isEmpty()
                    || title == null || title.trim().isEmpty()
                    || author == null || author.trim().isEmpty()
                    || totalCopies < 1) {
                throw new IllegalArgumentException("Invalid book details.");
            }

            this.id = id.trim();
            this.title = title.trim();
            this.author = author.trim();
            this.totalCopies = totalCopies;
            this.availableCopies = totalCopies;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public int getTotalCopies() {
            return totalCopies;
        }

        public int getAvailableCopies() {
            return availableCopies;
        }

        public int getBorrowedCopies() {
            return totalCopies - availableCopies;
        }

        public boolean isAvailable() {
            return availableCopies > 0;
        }

        public void borrowCopy() {
            if (!isAvailable()) {
                throw new IllegalStateException(
                        "No copies of this book are available."
                );
            }
            availableCopies--;
        }

        public void returnCopy() {
            if (availableCopies >= totalCopies) {
                throw new IllegalStateException(
                        "All copies have already been returned."
                );
            }
            availableCopies++;
        }

        public void borrowCopies(int quantity) {
            if (quantity <= 0 || quantity > availableCopies) {
                throw new IllegalArgumentException(
                        "Not enough copies available."
                );
            }

            availableCopies -= quantity;
        }

        public void returnCopies(int quantity) {
            if (quantity <= 0 || quantity > getBorrowedCopies()) {
                throw new IllegalArgumentException(
                        "Invalid return quantity."
                );
            }

            availableCopies += quantity;
        }
    }

    // SIGN-UP WINDOW
    public main2() {
        setTitle("Library Management System - Sign Up");
        setSize(450, 470);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel(
                "LIBRARY MANAGEMENT SYSTEM",
                SwingConstants.CENTER
        );
        title.setFont(new Font("Arial", Font.BOLD, 19));
        title.setForeground(Color.WHITE);
        title.setOpaque(true);
        title.setBackground(new Color(35, 70, 110));
        title.setBorder(BorderFactory.createEmptyBorder(22, 10, 22, 10));
        mainPanel.add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(0, 1, 8, 8));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 45, 15, 45)
        );

        JLabel signupLabel = new JLabel("Create an Account");
        signupLabel.setFont(new Font("Arial", Font.BOLD, 18));

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();

        JButton signupButton = new JButton("SIGN UP");
        signupButton.setBackground(new Color(35, 70, 110));
        signupButton.setForeground(Color.WHITE);

        JButton loginButton = new JButton("Already have an account? Login");

        formPanel.add(signupLabel);
        formPanel.add(new JLabel("Username:"));
        formPanel.add(usernameField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);
        formPanel.add(new JLabel("Confirm Password:"));
        formPanel.add(confirmPasswordField);
        formPanel.add(signupButton);
        formPanel.add(loginButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);

        signupButton.addActionListener(e -> signUp());
        loginButton.addActionListener(e -> openLogin());
    }

    private void signUp() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirm = new String(confirmPasswordField.getPassword());

        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(
                    this, "Password must be at least 6 characters."
            );
            return;
        }

        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!");
            return;
        }

        if (accounts.containsKey(username)) {
            JOptionPane.showMessageDialog(this, "Username already exists!");
            return;
        }

        accounts.put(username, password);

        JOptionPane.showMessageDialog(
                this, "Account created successfully! You can now log in."
        );

        usernameField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        openLogin();
    }

    // LOGIN WINDOW
    private void openLogin() {
        JFrame loginFrame = new JFrame("Library Management System - Login");
        loginFrame.setSize(400, 300);
        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        loginFrame.setLocationRelativeTo(this);
        loginFrame.setResizable(false);

        JPanel panel = new JPanel(new GridLayout(0, 1, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));
        panel.setBackground(Color.WHITE);

        JTextField loginUsername = new JTextField();
        JPasswordField loginPassword = new JPasswordField();

        JButton loginButton = new JButton("LOGIN");
        loginButton.setBackground(new Color(35, 70, 110));
        loginButton.setForeground(Color.WHITE);

        panel.add(new JLabel("Account Login"));
        panel.add(new JLabel("Username:"));
        panel.add(loginUsername);
        panel.add(new JLabel("Password:"));
        panel.add(loginPassword);
        panel.add(loginButton);

        loginFrame.add(panel);

        Runnable authenticate = () -> {
            String username = loginUsername.getText().trim();
            String password = new String(loginPassword.getPassword());

            if (accounts.containsKey(username)
                    && accounts.get(username).equals(password)) {

                JOptionPane.showMessageDialog(
                        loginFrame, "Login successful! Welcome, " + username + "."
                );

                loginFrame.dispose();
                dispose();

                new LibraryWindow().setVisible(true);

            } else {
                JOptionPane.showMessageDialog(
                        loginFrame,
                        "Invalid username or password!",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );
                loginPassword.setText("");
            }
        };

        loginButton.addActionListener(e -> authenticate.run());
        loginPassword.addActionListener(e -> authenticate.run());

        loginFrame.setVisible(true);
    }

    // LIBRARY WINDOW
    private class LibraryWindow extends JFrame {

        private final ArrayList<Main> books = new ArrayList<>();

        private JTextField nameField, idField, bookIdField;
        private JTextField quantityField, searchField;
        private DefaultTableModel tableModel;
        private JTable bookTable;
        private JTextArea receiptArea;

        private JLabel totalBooksLabel;
        private JLabel totalCopiesLabel;
        private JLabel availableCopiesLabel;

        LibraryWindow() {
            initializeBooks();
            initializeGUI();
            refreshTable();
        }

        private void initializeBooks() {
            String[][] data = {
                    {"B001", "Java Programming", "John Smith"},
                    {"B002", "Python Basics", "Mary Jane"},
                    {"B003", "Database Systems", "David Cruz"},
                    {"B004", "English Grammar", "Ana Santos"},
                    {"B005", "Computer Networks", "Robert Lee"},
                    {"B006", "Data Structures", "Michael Brown"},
                    {"B007", "Web Development", "Sarah Wilson"},
                    {"B008", "Operating Systems", "James Miller"},
                    {"B009", "Object-Oriented Programming", "Peter Garcia"},
                    {"B010", "Information Technology", "Linda Davis"},
                    {"B011", "Discrete Mathematics", "Kevin Thomas"},
                    {"B012", "Software Engineering", "Emily Johnson"},
                    {"B013", "Computer Security", "Daniel White"},
                    {"B014", "Artificial Intelligence", "Sophia Martin"},
                    {"B015", "Mobile App Development", "Mark Taylor"},
                    {"B016", "Computer Fundamentals", "William Anderson"},
                    {"B017", "C Programming", "Richard Thomas"},
                    {"B018", "C++ Programming", "Joseph Jackson"},
                    {"B019", "JavaScript Basics", "Thomas Harris"},
                    {"B020", "HTML and CSS", "Charles Martin"},
                    {"B021", "Data Analytics", "Daniel Thompson"},
                    {"B022", "Cloud Computing", "Matthew Garcia"},
                    {"B023", "Cybersecurity Essentials", "Anthony Martinez"},
                    {"B024", "Computer Architecture", "Donald Robinson"},
                    {"B025", "Algorithms", "Steven Clark"},
                    {"B026", "Machine Learning", "Paul Rodriguez"},
                    {"B027", "Deep Learning", "Andrew Lewis"},
                    {"B028", "Robotics", "Joshua Lee"},
                    {"B029", "Internet of Things", "Kenneth Walker"},
                    {"B030", "Computer Graphics", "George Hall"},
                    {"B031", "Game Development", "Brian Allen"},
                    {"B032", "Android Development", "Edward Young"},
                    {"B033", "iOS Development", "Ronald King"},
                    {"B034", "UI and UX Design", "Timothy Wright"},
                    {"B035", "Digital Marketing", "Jason Scott"},
                    {"B036", "Business Management", "Jeffrey Green"},
                    {"B037", "Entrepreneurship", "Ryan Baker"},
                    {"B038", "Accounting Principles", "Jacob Adams"},
                    {"B039", "Financial Management", "Gary Nelson"},
                    {"B040", "Economics", "Nicholas Hill"},
                    {"B041", "Marketing Principles", "Eric Campbell"},
                    {"B042", "Human Resource Management", "Jonathan Mitchell"},
                    {"B043", "Project Management", "Stephen Carter"},
                    {"B044", "Business Communication", "Larry Roberts"},
                    {"B045", "Leadership Skills", "Justin Phillips"},
                    {"B046", "Public Speaking", "Scott Evans"},
                    {"B047", "Research Methods", "Brandon Turner"},
                    {"B048", "Statistics", "Frank Parker"},
                    {"B049", "College Algebra", "Raymond Collins"},
                    {"B050", "Calculus", "Gregory Edwards"},
                    {"B051", "Physics", "Samuel Stewart"},
                    {"B052", "Chemistry", "Patrick Morris"},
                    {"B053", "Biology", "Jack Rogers"},
                    {"B054", "Environmental Science", "Dennis Reed"},
                    {"B055", "Earth Science", "Jerry Cook"},
                    {"B056", "Astronomy", "Tyler Morgan"},
                    {"B057", "World History", "Aaron Bell"},
                    {"B058", "Philippine History", "Jose Reyes"},
                    {"B059", "Geography", "Nathan Cooper"},
                    {"B060", "Political Science", "Peter Richardson"},
                    {"B061", "Psychology", "Harold Cox"},
                    {"B062", "Sociology", "Carl Howard"},
                    {"B063", "Philosophy", "Arthur Ward"},
                    {"B064", "Ethics", "Henry Torres"},
                    {"B065", "Communication Skills", "Dylan Peterson"},
                    {"B066", "Creative Writing", "Ethan Gray"},
                    {"B067", "World Literature", "Noah Ramirez"},
                    {"B068", "Poetry and Fiction", "Liam James"},
                    {"B069", "Grammar and Composition", "Mason Watson"},
                    {"B070", "Reading Comprehension", "Logan Brooks"},
                    {"B071", "Teaching Strategies", "Oliver Kelly"},
                    {"B072", "Educational Psychology", "Elijah Sanders"},
                    {"B073", "Classroom Management", "Lucas Price"},
                    {"B074", "Child Development", "Benjamin Bennett"},
                    {"B075", "Nursing Fundamentals", "Amelia Wood"},
                    {"B076", "Human Anatomy", "Isabella Barnes"},
                    {"B077", "Human Physiology", "Charlotte Ross"},
                    {"B078", "First Aid and Safety", "Mia Henderson"},
                    {"B079", "Nutrition and Dietetics", "Harper Coleman"},
                    {"B080", "Public Health", "Evelyn Jenkins"},
                    {"B081", "Civil Engineering", "Abigail Perry"},
                    {"B082", "Mechanical Engineering", "Ella Powell"},
                    {"B083", "Electrical Engineering", "Grace Long"},
                    {"B084", "Electronics", "Chloe Patterson"},
                    {"B085", "Automotive Technology", "Lily Hughes"},
                    {"B086", "Industrial Technology", "Zoe Flores"},
                    {"B087", "Architecture Basics", "Hannah Washington"},
                    {"B088", "Interior Design", "Natalie Butler"},
                    {"B089", "Graphic Design", "Victoria Simmons"},
                    {"B090", "Photography", "Sofia Foster"},
                    {"B091", "Cooking Fundamentals", "Ava Gonzales"},
                    {"B092", "Food Safety", "Emily Bryant"},
                    {"B093", "Hotel Management", "Madison Alexander"},
                    {"B094", "Tourism Management", "Scarlett Russell"},
                    {"B095", "Customer Service", "Aria Griffin"},
                    {"B096", "Law and Society", "Penelope Diaz"},
                    {"B097", "Criminal Justice", "Layla Hayes"},
                    {"B098", "Personal Finance", "Nora Myers"},
                    {"B099", "Career Development", "Riley Ford"},
                    {"B100", "Research and Statistics", "Camila Hamilton"}
            };

            for (String[] item : data) {
                books.add(new Main(item[0], item[1], item[2], 5));
            }
        }

        private void initializeGUI() {
            setTitle("Library Management System");
            setSize(1100, 720);
            setMinimumSize(new Dimension(900, 600));
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout(10, 10));

            JLabel header = new JLabel(
                    "LIBRARY MANAGEMENT SYSTEM", SwingConstants.CENTER
            );
            header.setFont(new Font("Arial", Font.BOLD, 26));
            header.setForeground(Color.WHITE);
            header.setBackground(new Color(35, 70, 110));
            header.setOpaque(true);
            header.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
            add(header, BorderLayout.NORTH);

            JPanel content = new JPanel(new GridLayout(1, 2, 15, 10));
            content.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            add(content, BorderLayout.CENTER);

            JPanel leftPanel = new JPanel(new BorderLayout(8, 8));
            leftPanel.setBorder(BorderFactory.createTitledBorder("Book Inventory"));

            searchField = new JTextField();
            JButton searchButton = new JButton("Search");
            JButton showAllButton = new JButton("Show All");

            JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
            searchPanel.add(new JLabel("Search: "), BorderLayout.WEST);
            searchPanel.add(searchField, BorderLayout.CENTER);

            JPanel searchButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
            searchButtons.add(searchButton);
            searchButtons.add(showAllButton);

            JPanel searchContainer = new JPanel(new BorderLayout());
            searchContainer.add(searchPanel, BorderLayout.NORTH);
            searchContainer.add(searchButtons, BorderLayout.SOUTH);
            leftPanel.add(searchContainer, BorderLayout.NORTH);

            String[] columns = {"Book ID", "Title", "Author", "Total", "Available"};

            tableModel = new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            bookTable = new JTable(tableModel);
            bookTable.setRowHeight(27);
            bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            bookTable.setAutoCreateRowSorter(true);
            leftPanel.add(new JScrollPane(bookTable), BorderLayout.CENTER);

            JPanel inventoryBottom = new JPanel(new BorderLayout(5, 5));
            JPanel summaryPanel = new JPanel(new GridLayout(0, 1, 3, 3));

            totalBooksLabel = new JLabel();
            totalCopiesLabel = new JLabel();
            availableCopiesLabel = new JLabel();

            summaryPanel.add(totalBooksLabel);
            summaryPanel.add(totalCopiesLabel);
            summaryPanel.add(availableCopiesLabel);

            JButton selectButton = new JButton("Select Book");
            selectButton.addActionListener(e -> selectBook());

            inventoryBottom.add(summaryPanel, BorderLayout.CENTER);
            inventoryBottom.add(selectButton, BorderLayout.SOUTH);
            leftPanel.add(inventoryBottom, BorderLayout.SOUTH);
            content.add(leftPanel);

            JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
            JPanel form = new JPanel(new GridLayout(0, 2, 8, 12));
            form.setBorder(BorderFactory.createTitledBorder("Borrow / Return Books"));

            nameField = new JTextField();
            idField = new JTextField();
            bookIdField = new JTextField();
            quantityField = new JTextField();

            form.add(new JLabel("Borrower's Name:"));
            form.add(nameField);
            form.add(new JLabel("ID Number:"));
            form.add(idField);
            form.add(new JLabel("Book ID:"));
            form.add(bookIdField);
            form.add(new JLabel("Number of Books:"));
            form.add(quantityField);

            JButton borrowButton = new JButton("Borrow Book");
            JButton returnButton = new JButton("Return Book");
            JButton addBookButton = new JButton("Add New Book");
            JButton clearButton = new JButton("Clear Fields");

            form.add(borrowButton);
            form.add(returnButton);
            form.add(addBookButton);
            form.add(clearButton);
            rightPanel.add(form, BorderLayout.NORTH);

            receiptArea = new JTextArea();
            receiptArea.setEditable(false);
            receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
            receiptArea.setMargin(new Insets(10, 10, 10, 10));
            receiptArea.setText(
                    "Welcome to the Library System!\n\n"
                            + "Select a book from the inventory or enter a Book ID.\n"
                            + "Enter the borrower details to begin."
            );

            JScrollPane receiptScroll = new JScrollPane(receiptArea);
            receiptScroll.setBorder(
                    BorderFactory.createTitledBorder("Transaction Receipt")
            );
            rightPanel.add(receiptScroll, BorderLayout.CENTER);
            content.add(rightPanel);

            searchButton.addActionListener(e -> searchBooks());
            searchField.addActionListener(e -> searchBooks());

            showAllButton.addActionListener(e -> {
                searchField.setText("");
                refreshTable();
            });

            borrowButton.addActionListener(e -> borrowBook());
            returnButton.addActionListener(e -> returnBook());
            addBookButton.addActionListener(e -> addNewBook());
            clearButton.addActionListener(e -> clearFields());

            JButton exitButton = new JButton("Exit");
            exitButton.addActionListener(e -> {
                int answer = JOptionPane.showConfirmDialog(
                        this, "Are you sure you want to exit?",
                        "Exit Library System", JOptionPane.YES_NO_OPTION
                );

                if (answer == JOptionPane.YES_OPTION) {
                    dispose();
                }
            });

            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            bottomPanel.add(exitButton);
            add(bottomPanel, BorderLayout.SOUTH);
        }

        private void refreshTable() {
            tableModel.setRowCount(0);

            int totalCopies = 0;
            int availableCopies = 0;

            for (Main book : books) {
                tableModel.addRow(new Object[]{
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getTotalCopies(),
                        book.getAvailableCopies()
                });

                totalCopies += book.getTotalCopies();
                availableCopies += book.getAvailableCopies();
            }

            totalBooksLabel.setText("Total book titles: " + books.size());
            totalCopiesLabel.setText("Total copies: " + totalCopies);
            availableCopiesLabel.setText("Available copies: " + availableCopies);
        }

        private Main findBook(String id) {
            for (Main book : books) {
                if (book.getId().equalsIgnoreCase(id.trim())) {
                    return book;
                }
            }
            return null;
        }

        private void selectBook() {
            int row = bookTable.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a book.");
                return;
            }

            int modelRow = bookTable.convertRowIndexToModel(row);
            bookIdField.setText(
                    tableModel.getValueAt(modelRow, 0).toString()
            );
        }

        private void searchBooks() {
            String keyword = searchField.getText().trim().toLowerCase();
            tableModel.setRowCount(0);

            for (Main book : books) {
                if (book.getId().toLowerCase().contains(keyword)
                        || book.getTitle().toLowerCase().contains(keyword)
                        || book.getAuthor().toLowerCase().contains(keyword)) {
                    tableModel.addRow(new Object[]{
                            book.getId(),
                            book.getTitle(),
                            book.getAuthor(),
                            book.getTotalCopies(),
                            book.getAvailableCopies()
                    });
                }
            }

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No books found.");
            }
        }

        private int getQuantity() {
            try {
                int quantity = Integer.parseInt(quantityField.getText().trim());
                if (quantity <= 0) {
                    throw new NumberFormatException();
                }
                return quantity;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this, "Enter a valid quantity greater than zero."
                );
                return -1;
            }
        }

        private boolean validateBorrower() {
            if (nameField.getText().trim().isEmpty()
                    || idField.getText().trim().isEmpty()
                    || bookIdField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter the borrower's name, ID number, and Book ID."
                );
                return false;
            }
            return true;
        }

        private void borrowBook() {
            if (!validateBorrower()) return;

            int quantity = getQuantity();
            if (quantity == -1) return;

            Main book = findBook(bookIdField.getText());

            if (book == null) {
                JOptionPane.showMessageDialog(this, "Book ID not found!");
                return;
            }

            if (quantity > book.getAvailableCopies()) {
                JOptionPane.showMessageDialog(
                        this, "Not enough copies available.\nAvailable: "
                                + book.getAvailableCopies()
                );
                return;
            }

            book.borrowCopies(quantity);
            refreshTable();

            receiptArea.setText(
                    "========== BORROWING RECEIPT ==========\n\n"
                            + "Borrower's Name: " + nameField.getText().trim() + "\n"
                            + "ID Number: " + idField.getText().trim() + "\n"
                            + "Book ID: " + book.getId() + "\n"
                            + "Book Title: " + book.getTitle() + "\n"
                            + "Author: " + book.getAuthor() + "\n"
                            + "Number of Books: " + quantity + "\n"
                            + "Status: BORROWED SUCCESSFULLY\n"
                            + "Remaining Copies: " + book.getAvailableCopies() + "\n"
                            + "\n========================================"
            );

            JOptionPane.showMessageDialog(this, "Book borrowed successfully!");
        }

        private void returnBook() {
            if (!validateBorrower()) return;

            int quantity = getQuantity();
            if (quantity == -1) return;

            Main book = findBook(bookIdField.getText());

            if (book == null) {
                JOptionPane.showMessageDialog(this, "Book ID not found!");
                return;
            }

            if (quantity > book.getBorrowedCopies()) {
                JOptionPane.showMessageDialog(
                        this, "Invalid quantity.\nBorrowed copies in inventory: "
                                + book.getBorrowedCopies()
                );
                return;
            }

            book.returnCopies(quantity);
            refreshTable();

            receiptArea.setText(
                    "============ RETURN RECEIPT ============\n\n"
                            + "Borrower's Name: " + nameField.getText().trim() + "\n"
                            + "ID Number: " + idField.getText().trim() + "\n"
                            + "Book ID: " + book.getId() + "\n"
                            + "Book Title: " + book.getTitle() + "\n"
                            + "Number of Books Returned: " + quantity + "\n"
                            + "Status: RETURNED SUCCESSFULLY\n"
                            + "Available Copies: " + book.getAvailableCopies() + "\n"
                            + "\n========================================"
            );

            JOptionPane.showMessageDialog(this, "Book returned successfully!");
        }

        private void addNewBook() {
            JTextField newId = new JTextField();
            JTextField newTitle = new JTextField();
            JTextField newAuthor = new JTextField();
            JTextField newCopies = new JTextField();

            Object[] fields = {
                    "Book ID:", newId,
                    "Book Title:", newTitle,
                    "Author:", newAuthor,
                    "Number of Copies:", newCopies
            };

            int result = JOptionPane.showConfirmDialog(
                    this, fields, "Add New Book",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
            );

            if (result != JOptionPane.OK_OPTION) return;

            String id = newId.getText().trim();
            String title = newTitle.getText().trim();
            String author = newAuthor.getText().trim();

            if (id.isEmpty() || title.isEmpty() || author.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.");
                return;
            }

            int copies;

            try {
                copies = Integer.parseInt(newCopies.getText().trim());
                if (copies <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this, "Enter a valid number of copies greater than zero."
                );
                return;
            }

            if (findBook(id) != null) {
                JOptionPane.showMessageDialog(this, "That Book ID already exists.");
                return;
            }

            books.add(new Main(id, title, author, copies));
            refreshTable();

            JOptionPane.showMessageDialog(this, "New book added successfully!");
        }

        private void clearFields() {
            nameField.setText("");
            idField.setText("");
            bookIdField.setText("");
            quantityField.setText("");
            searchField.setText("");
            bookTable.clearSelection();

            receiptArea.setText(
                    "Transaction form cleared.\n\n"
                            + "Select a book or enter a Book ID to begin."
            );

            refreshTable();
        }
    }

    // START PROGRAM
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            main2 signupPage = new main2();
            signupPage.setVisible(true);
        });
    }
}
